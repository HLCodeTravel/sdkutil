package com.transsion.http.cache;

import com.transsion.http.impl.ProgressHandler;
import com.transsion.http.util.FileUtil;
import com.transsion.http.util.IOUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static com.transsion.http.HttpCode.DOWNLOAD_INTERNAL_FAIL;
import static com.transsion.http.HttpCode.DOWNLOAD_INTERNAL_RETRY;

/**
 * Created by wenshuai.liu on 2017/7/7.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class StreamEncoder implements Encoder<InputStream> {


    private static final int CHECK_SIZE = 512;
    private final ProgressHandler httpCallback;

    public StreamEncoder(ProgressHandler httpCallback) {
        this.httpCallback = httpCallback;
    }

    @Override
    public boolean encode(InputStream inputStream, File file, long contentLength) {
        boolean success = false;
        long tempFileLen = file.length();
        if (tempFileLen > 0) {
            FileInputStream fis = null;
            try {
                long filePos = tempFileLen - CHECK_SIZE;
                if (filePos > 0) {
                    fis = new FileInputStream(file);
                    byte[] fileCheckBuffer = IOUtil.readBytes(fis, filePos, CHECK_SIZE);
                    byte[] checkBuffer = IOUtil.readBytes(inputStream, 0, CHECK_SIZE);
                    if (!Arrays.equals(checkBuffer, fileCheckBuffer)) {
                        IOUtil.closeQuietly(fis); // 先关闭文件流, 否则文件删除会失败.
                        FileUtil.deleteFileOrDir(file);
                        if (httpCallback != null) {
                            httpCallback.sendRetryMessage(DOWNLOAD_INTERNAL_RETRY);
                        }
                    } else {
                        contentLength -= CHECK_SIZE;
                    }
                } else {
                    FileUtil.deleteFileOrDir(file);
                    if (httpCallback != null) {
                        httpCallback.sendRetryMessage(DOWNLOAD_INTERNAL_RETRY);
                    }
                }
            } catch (IOException ie) {
                ie.printStackTrace();
            } finally {
                IOUtil.closeQuietly(fis);
            }
        }
        long current = file.length();
        FileOutputStream fileOutputStream;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            fileOutputStream = new FileOutputStream(file, true);
            long total = contentLength + current;
            if (httpCallback != null) {
                if (total > 0) {
                    httpCallback.updateProgress(current, total);
                }
            }
            bis = new BufferedInputStream(inputStream);
            bos = new BufferedOutputStream(fileOutputStream);
            byte[] temp = new byte[4096];
            int len;
            while ((len = bis.read(temp)) != -1) {
                bos.write(temp, 0, len);
                current += len;
                if (httpCallback != null) {
                    if (total > 0) {
                        httpCallback.updateProgress(current, total);
                    }
                }
            }
            bos.flush();
            bos.close();
            success = true;
        } catch (IOException e) {
            if (httpCallback != null) {
                httpCallback.sendFailMessage(DOWNLOAD_INTERNAL_FAIL, e.getMessage());
            }
        } finally {
            IOUtil.closeQuietly(bis);
            IOUtil.closeQuietly(bos);
        }
        return success;
    }
}
