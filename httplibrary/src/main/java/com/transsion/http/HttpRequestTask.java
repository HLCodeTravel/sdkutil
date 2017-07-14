package com.transsion.http;

import android.os.Looper;
import android.support.annotation.NonNull;

import com.transsion.http.cache.ByteBufferEncoder;
import com.transsion.http.cache.IDiskCache;
import com.transsion.http.cache.SaveImpl;
import com.transsion.http.impl.IHttpCallback;
import com.transsion.http.log.Console;
import com.transsion.http.request.UriRequest;
import com.transsion.http.util.ByteBufferUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by wenshuai.liu on 2017/5/8.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class HttpRequestTask implements Runnable, Comparable<HttpRequestTask> {
    private final IHttpCallback responseHandler;
    private final UriRequest uriRequest;
    private final AtomicBoolean isCancelled = new AtomicBoolean();
    private final IDiskCache diskCache;
    private volatile boolean isFinished;
    private final WeakReference<Object> tag;

    public HttpRequestTask(RequestCall requestCall, IHttpCallback responseHandler) {
        this.responseHandler = responseHandler;
        this.uriRequest = requestCall.getUriRequest();
        this.diskCache = requestCall.getDiskCache();
        this.tag = new WeakReference<>(uriRequest.getRequest().getTag());
    }

    @Override
    public void run() {
        Console.log.d("book", "run Current Looper:" + (Looper.myLooper() == Looper.getMainLooper() ? "Main Looper" : "thread Looper"));
        if (isCancelled()) {
            return;
        }
        if (responseHandler != null) {
            responseHandler.sendStartMessage();
        }
        try {
            if (uriRequest.getRequest().isImageCache()) {
                if (diskCache != null) {
                    File imageFile = diskCache.get(new ImageURL(uriRequest.getRequest().getUrl()));
                    if (imageFile != null && imageFile.exists() && imageFile.length() > 0) {
                        Console.log.d("image", "image url:" + uriRequest.getRequest().getUrl());
                        Console.log.d("image", "image path:" + imageFile.getPath());
                        if (isCancelled()) {
                            return;
                        }
                        if (responseHandler != null) {
                            responseHandler.sendResponseMessage(HttpCode.HTTP_IMAGE_CACHE,
                                    ByteBufferUtil.toBytes(ByteBufferUtil.fromFile(imageFile)));
                        }
                    } else {
                        makeRequest();
                    }
                } else {
                    makeRequest();
                }
            } else {
                makeRequest();
            }
        } catch (Exception e) {
            if (!isCancelled()) {
                if (responseHandler != null) {
                    responseHandler.sendFailureMessage(uriRequest.getResponseCode(), null, e);
                }
            }
        } finally {
            uriRequest.close();
        }
        if (isCancelled()) {
            return;
        }
        if (responseHandler != null) {
            responseHandler.sendFinishMessage();
        }
        isFinished = true;
    }

    private void makeRequest() throws IOException {
        try {
            uriRequest.setRequest();
            if (isCancelled()) {
                return;
            }
            InputStream is = uriRequest.getInputStream();
            byte[] responseBody = ByteBufferUtil.toBytes(ByteBufferUtil.fromStream(is));
            if (responseHandler != null) {
                responseHandler.sendResponseMessage(uriRequest.getResponseCode(), responseBody);
            }
            if (uriRequest.getRequest().isImageCache()) {
                if (diskCache != null) {
                    ByteBufferEncoder encoder = new ByteBufferEncoder();
                    SaveImpl writer = new SaveImpl(encoder, ByteBuffer.wrap(responseBody));
                    diskCache.put(new ImageURL(uriRequest.getRequest().getUrl()), writer);
                }
            }
        } finally {
            uriRequest.close();
        }

    }

    public boolean isCancelled() {
        boolean cancelled = isCancelled.get();
        if (cancelled) {
            sendCancelNotification();
        }
        return cancelled;
    }

    private synchronized void sendCancelNotification() {
        if (!isFinished && isCancelled.get()) {
            if (responseHandler != null) {
                responseHandler.sendCancelMessage();
            }
        }
    }

    public boolean isDone() {
        return isCancelled() || isFinished;
    }

    public boolean cancel() {
        isCancelled.set(true);
        uriRequest.close();
        return isCancelled();
    }

    public Object getTag() {
        return this.tag;
    }


    @Override
    public int compareTo(@NonNull HttpRequestTask o) {
        return 0;
    }
}
