package com.transsion.http.impl;

import android.os.Looper;

import java.io.UnsupportedEncodingException;

/**
 * Created by wenshuai.liu on 2017/5/16.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public abstract class StringCallback extends HttpCallbackImpl {

    public StringCallback(Looper looper) {
        super(looper);
    }

    public StringCallback() {
        super();
    }


    public abstract void onFailure(int statusCode, String responseString, Throwable throwable);

    public abstract void onSuccess(int statusCode, String responseString);

    public static String getResponseString(byte[] stringBytes, String charset) throws UnsupportedEncodingException {
        String toReturn = (stringBytes == null) ? null : new String(stringBytes, charset);
        if (toReturn != null && toReturn.startsWith(UTF8_BOM)) {
            return toReturn.substring(1);
        }
        return toReturn;
    }

    @Override
    public void onSuccess(final int statusCode, final byte[] responseBody) {
        Runnable parser = new Runnable() {
            @Override
            public void run() {
                try {
                    final String responseString = getResponseString(responseBody, DEFAULT_CHARSET);
                    postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            onSuccess(statusCode, responseString);
                        }
                    });
                } catch (final UnsupportedEncodingException e) {
                    postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            onFailure(statusCode, (String) null, e.getCause());
                        }
                    });
                }
            }
        };
        if (!getUseSyncMode() && !getUsePoolThread()) {
            new Thread(parser).start();
        } else {
            parser.run();
        }
    }

    @Override
    public void onFailure(final int statusCode, final byte[] responseBody, final Throwable error) {
        Runnable parser = new Runnable() {
            @Override
            public void run() {
                try {
                    final String responseString = getResponseString(responseBody, DEFAULT_CHARSET);
                    postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            onFailure(statusCode, responseString, error);
                        }
                    });
                } catch (final UnsupportedEncodingException e) {
                    postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            onFailure(statusCode, (String) null, e.getCause());
                        }
                    });
                }
            }
        };
        if (!getUseSyncMode() && !getUsePoolThread()) {
            new Thread(parser).start();
        } else {
            parser.run();
        }
    }
}
