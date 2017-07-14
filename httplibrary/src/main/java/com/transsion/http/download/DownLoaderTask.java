package com.transsion.http.download;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.transsion.http.HttpURL;
import com.transsion.http.RequestCall;
import com.transsion.http.cache.DiskCacheWriteLocker;
import com.transsion.http.impl.DownloadCallback;
import com.transsion.http.log.Console;
import com.transsion.http.request.UriRequest;
import com.transsion.http.util.FileUtil;
import com.transsion.http.util.IOUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by wenshuai.liu on 2017/6/28.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class DownLoaderTask implements Runnable, Comparable<DownLoaderTask> {
    private static final int CHECK_SIZE = 512;
    final RequestCall requestCall;
    final DownloadCallback callback;
    //private final IDiskCache diskCache;
    private String saveFilepath;
    private String tempFilepath;
    private final AtomicBoolean isCancelled = new AtomicBoolean();
    private final DiskCacheWriteLocker writerLocker = new DiskCacheWriteLocker();
    private final WeakReference<Object> tag;
    private String downUrl;
    private long contentLength;
    private boolean isAutoResume;
    private UriRequest mUriRequest;
    private volatile boolean isFinished;
    private final HttpURL httpUrl;


    public DownLoaderTask(RequestCall requestCall, DownloadCallback callback) {
        this.requestCall = requestCall;
        this.callback = callback;
        this.tag = new WeakReference<>(requestCall.getRequest().getTag());
        this.saveFilepath = requestCall.getRequest().getPathname();
        //this.diskCache = new DownloadCacheProvider(saveFilepath).getDiskCache();
        this.downUrl = requestCall.getRequest().getUrl();
        this.httpUrl = new HttpURL(downUrl);
    }

    @Override
    public void run() {
        startLoad();
    }


    public File autoRename(File loadedFile) {
        if (!saveFilepath.equals(tempFilepath)) {
            File newFile = new File(saveFilepath);
            return loadedFile.renameTo(newFile) ? newFile : loadedFile;
        } else {
            return loadedFile;
        }
    }

    public void startLoad() {
        isCancelled.set(false);
        if (callback != null) {
            callback.sendStartMessage();
        }
        isAutoResume = true;
        makeRequest();
        if (isCancelled()) {
            return;
        }
        if (callback != null) {
            callback.sendFinishMessage();
        }
        isFinished = true;
    }

    private void makeRequest() {
        UriRequest uriRequest = requestCall.getUriRequest();
        mUriRequest = uriRequest;
        try {
            File file = this.loadFromUri(uriRequest);
            if (file != null && file.exists() && file.length() > 0) {
                if (isCancelled()) {
                    return;
                }
                if (callback != null) {
                    callback.onSuccess(uriRequest.getResponseCode(),downUrl, file);
                }
            } else {
                if (isCancelled()) {
                    return;
                }
                if (callback != null) {
                    callback.onFailure(uriRequest.getResponseCode(), downUrl, "file null");
                }
            }
        } catch (Throwable throwable) {
            File file = new File(saveFilepath);
            if (file != null && file.exists() && file.length() > 0) {
                if (isCancelled()) {
                    return;
                }
                if (callback != null) {
                    callback.onSuccess(uriRequest.getResponseCode(),downUrl, file);
                }
            } else {
                if (isCancelled()) {
                    return;
                }
                if (callback != null) {
                    callback.onFailure(uriRequest.getResponseCode(), downUrl, "file null");
                }
            }
        } finally {
            uriRequest.close();
        }
    }

    private File loadFromUri(final UriRequest uriRequest) throws Throwable {
        writerLocker.acquire(httpUrl);
        File resultFile;
        if (TextUtils.isEmpty(saveFilepath)) {
            ///default cache
        } else {
            tempFilepath = saveFilepath + ".tmp";
        }
        if (isCancelled()) {
            return null;
        }
        if (callback != null) {
            callback.onLoading(uriRequest.getResponseCode(),downUrl, 0, 1);
        }
        long range = 0;
        if (isAutoResume) {
            File tempFile = new File(tempFilepath);
            long fileLen = tempFile.length();
            if (fileLen <= CHECK_SIZE) {
                FileUtil.deleteFileOrDir(tempFile);
                range = 0;
            } else {
                range = fileLen - CHECK_SIZE;
            }
        }
        requestCall.getRequest().getHeaders().put("RANGE", "bytes=" + range + "-");
        if (isCancelled()) {
            return null;
        }
        if (callback != null) {
            callback.onLoading(uriRequest.getResponseCode(),downUrl, 0, 1);
        }
        uriRequest.setRequest();
        contentLength = uriRequest.getContentLength();
        if (isAutoResume) {
            isAutoResume = isSupportRange(uriRequest);
        }
        resultFile = this.loadFromInputStream(uriRequest.getInputStream());
        return resultFile;
    }

    private File loadFromInputStream(InputStream in) throws Throwable {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        File targetFile = null;
        try {
            targetFile = new File(tempFilepath);
            if (targetFile.isDirectory()) {
                // 防止文件正在写入时, 父文件夹被删除, 继续写入时造成偶现文件节点异常问题.
                FileUtil.deleteFileOrDir(targetFile);
            }
            if (!targetFile.exists()) {
                File dir = targetFile.getParentFile();
                if (!dir.exists() && !dir.mkdirs()) {
                    throw new IOException("can not create dir: " + dir.getAbsolutePath());
                }
            }
            // 若文件存在且大于 CHECK_SIZE, range = fileLen - CHECK_SIZE , 校验check_buffer
            // 相同: 继续下载,不相同: 删掉目标文件,下载重新开始
            long targetFileLen = targetFile.length();
            if (isAutoResume && targetFileLen > 0) {
                FileInputStream fis = null;
                try {
                    long filePos = targetFileLen - CHECK_SIZE;
                    if (filePos > 0) {
                        fis = new FileInputStream(targetFile);
                        byte[] fileCheckBuffer = IOUtil.readBytes(fis, filePos, CHECK_SIZE);
                        byte[] checkBuffer = IOUtil.readBytes(in, 0, CHECK_SIZE);
                        if (!Arrays.equals(checkBuffer, fileCheckBuffer)) {
                            IOUtil.closeQuietly(fis); // 先关闭文件流, 否则文件删除会失败.
                            FileUtil.deleteFileOrDir(targetFile);
                            throw new RuntimeException("need retry");
                        } else {
                            contentLength -= CHECK_SIZE;
                        }
                    } else {
                        FileUtil.deleteFileOrDir(targetFile);
                        throw new RuntimeException("need retry");
                    }
                } finally {
                    IOUtil.closeQuietly(fis);
                }
            }

            // 开始下载
            long current = 0;
            FileOutputStream fileOutputStream = null;
            if (isAutoResume) {
                current = targetFileLen;
                fileOutputStream = new FileOutputStream(targetFile, true);
            } else {
                fileOutputStream = new FileOutputStream(targetFile);
            }

            long total = contentLength + current;
            bis = new BufferedInputStream(in);
            bos = new BufferedOutputStream(fileOutputStream);
            if (isCancelled()) {
                return null;
            }
            if (callback != null) {
                callback.onLoading(mUriRequest.getResponseCode(),downUrl, current, total);
            }
            byte[] tmp = new byte[4096];
            int len;
            while ((len = bis.read(tmp)) != -1) {
                // 防止父文件夹被其他进程删除, 继续写入时造成父文件夹变为0字节文件的问题.
                if (!targetFile.getParentFile().exists()) {
                    targetFile.getParentFile().mkdirs();
                    throw new IOException("parent be deleted!");
                }
                bos.write(tmp, 0, len);
                current += len;
                if (isCancelled()) {
                    return null;
                }
                if (callback != null) {
                    callback.onLoading(mUriRequest.getResponseCode(),downUrl, current, total);
                }
            }
            bos.flush();
        } finally {
            IOUtil.closeQuietly(bis);
            IOUtil.closeQuietly(bos);
            writerLocker.release(httpUrl);
        }
        return autoRename(targetFile);
    }

    private boolean isSupportRange(UriRequest uriRequest) {
        String ranges = uriRequest.getResponseHeader("Accept-Ranges");
        if (ranges != null) {
            return ranges.contains("bytes");
        }
        ranges = uriRequest.getResponseHeader("Content-Range");
        return ranges != null && ranges.contains("bytes");
    }


    private synchronized void sendCancelNotification() {
        if (!isFinished && isCancelled.get()) {
            if (callback != null) {
                callback.sendCancelMessage();
            }
        }
    }

    public boolean isCancelled() {
        return isCancelled.get();
    }

    public boolean isPaused() {
        boolean cancelled = isCancelled.get();
        if (cancelled) {
            sendPauseNotification();
        }
        return cancelled;
    }


    private synchronized void sendPauseNotification() {
        if (!isFinished && isCancelled.get()) {
            if (callback != null) {
                callback.sendPauseMessage();
            }
        }
    }

    public boolean isDone() {
        return isCancelled() || isFinished;
    }

    public boolean cancel() {
        Console.log.d("book", "cancel by tag");
        isCancelled.set(true);
        sendCancelNotification();
        if (mUriRequest != null) {
            mUriRequest.close();
        }
        if (!isFinished) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    FileUtil.deleteFileOrDir(new File(tempFilepath));
                }
            }).start();
        }
        return isCancelled();
    }

    public boolean pause() {
        isCancelled.set(true);
        if (mUriRequest != null) {
            mUriRequest.close();
        }
        return isPaused();
    }

    public Object getTag() {
        return this.tag.get();
    }


    @Override
    public int compareTo(@NonNull DownLoaderTask o) {
        return 0;
    }
}
