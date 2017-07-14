package com.transsion.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;

import com.transsion.core.deviceinfo.DeviceInfo;
import com.transsion.core.utils.SharedPreferencesUtil;

/**
 * usage
 *
 * @author 王纪清
 * @data 2017/6/7
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class CoreUtil {
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private static boolean isDebug;

    private CoreUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(@NonNull Context context) {
        CoreUtil.context = context.getApplicationContext();
        DeviceInfo.getGAId();
        SharedPreferencesUtil.bindApplication(CoreUtil.context);
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) return context;
        throw new NullPointerException("u should init first");
    }

    /**
     * 获取isDebug 的值
     * @return isDebug
     */
    public static boolean isDebug() {
        return isDebug;
    }

    /**
     * 设置isDebug 的值
     * @param isDebug
     */
    public static void setDebug(boolean isDebug) {
        CoreUtil.isDebug = isDebug;
    }
}
