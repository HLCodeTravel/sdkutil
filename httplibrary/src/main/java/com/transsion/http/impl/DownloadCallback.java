package com.transsion.http.impl;

import android.os.Looper;

import java.io.File;

/**
 * Created by wenshuai.liu on 2017/7/10.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public abstract class DownloadCallback extends HttpCallbackImpl {

    public abstract void onFailure(String url, String message);

    public abstract void onSuccess(String url, File downFile);

    public abstract void onLoading(String url, long current, long total);

    public DownloadCallback(Looper looper) {
        super(looper);
    }

    public DownloadCallback() {
        super();
    }

    @Override
    public void onSuccess(int statusCode, byte[] responseBody) {
        //do nothing
    }

    @Override
    public void onFailure(int statusCode, byte[] responseBody, Throwable error) {
        //do nothing
    }

    public void onFailure(final int code, final String url, final String message) {
        postRunnable(new Runnable() {
            @Override
            public void run() {
                onFailure(url, message);
            }
        });
    }

    public void onSuccess(final int code, final String url, final File downFile) {
        postRunnable(new Runnable() {
            @Override
            public void run() {
                onSuccess(url, downFile);
            }
        });
    }

    public void onLoading(final int code, final String url, final long current, final long total) {
        postRunnable(new Runnable() {
            @Override
            public void run() {
                onLoading(url, current, total);
            }
        });
    }

}
