package com.transsion.core.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.transsion.core.CoreUtil;


/**
 * 线程安全的toast
 *
 * @author peng.sun
 * @date 2017/6/29.
 * ==================================
 * Copyright (c) 2017 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class ToastUtil {

    /**
     * handler to show toasts safely
     */
    private static Handler mHandler = new Handler(Looper.getMainLooper());

    private static Toast toast = null;

    /**
     * 短时间显示toast
     *
     * @param resId 字串id
     */
    public static void showToast(int resId) {
        final Context context = CoreUtil.getContext();
        final int resid = resId;
        if (resid <= 0) return;
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                if (toast != null) {
                    toast.setText(resid);
                    toast.setDuration(Toast.LENGTH_SHORT);
                } else {
                    toast = Toast.makeText(context.getApplicationContext(), resid, Toast.LENGTH_SHORT);
                }

                toast.show();
            }
        });

    }

    /**
     * 短时间显示toast
     *
     * @param text 字串内容
     */
    public static void showToast(String text) {
        final Context context = CoreUtil.getContext();
        final String msg = text;
        if (TextUtils.isEmpty(msg)) return;
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                if (toast != null) {
                    toast.setText(msg);
                    toast.setDuration(Toast.LENGTH_SHORT);
                } else {
                    toast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
                }
                toast.show();
            }
        });
    }

    /**
     * 长时间显示toast
     *
     * @param resId 字串id
     */
    public static void showLongToast(int resId) {
        final Context context = CoreUtil.getContext();
        final int resid = resId;
        if (resid <= 0) return;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (toast != null) {
                    toast.setText(resid);
                    toast.setDuration(Toast.LENGTH_LONG);
                } else {
                    toast = Toast.makeText(context.getApplicationContext(), resid, Toast.LENGTH_LONG);
                }
                toast.show();
            }
        });

    }

    /**
     * 长时间显示toast
     *
     * @param text 字串内容
     */
    public static void showLongToast(String text) {
        final Context context = CoreUtil.getContext();
        final String msg = text;
        if (TextUtils.isEmpty(msg)) return;
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                if (toast != null) {
                    toast.setText(msg);
                    toast.setDuration(Toast.LENGTH_LONG);
                } else {
                    toast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_LONG);
                }
                toast.show();
            }
        });
    }

}
