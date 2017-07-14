package com.transsion.http.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;

import com.transsion.http.util.ByteBufferUtil;

import java.nio.ByteBuffer;

/**
 * Created by wenshuai.liu on 2017/5/19.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public abstract class BitmapCallback extends HttpCallbackImpl {


    public BitmapCallback(Looper looper) {
        super(looper);
    }

    public BitmapCallback() {
        super();
    }

    public abstract void onFailure(int statusCode, Bitmap responseString, Throwable throwable);

    public abstract void onSuccess(int statusCode, Bitmap responseFile);

    @Override
    public void onSuccess(final int statusCode, final byte[] responseBody) {
        Runnable parser = new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = decodeBitmap(responseBody);
                postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        onSuccess(statusCode, bitmap);
                    }
                });
            }
        };
        if (!getUseSyncMode() && !getUsePoolThread()) {
            new Thread(parser).start();
        } else {
            parser.run();
        }
    }

    @Override
    public void onFailure(final int statusCode, byte[] responseBody, final Throwable error) {
        postRunnable(new Runnable() {
            @Override
            public void run() {
                onFailure(statusCode, (Bitmap) null, error);
            }
        });

    }

    private Bitmap decodeBitmap(byte[] responseBody) {
        return BitmapFactory.decodeStream(ByteBufferUtil.toStream(ByteBuffer.wrap(responseBody)));
    }
}
