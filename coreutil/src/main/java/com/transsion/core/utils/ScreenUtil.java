package com.transsion.core.utils;
/* Top Secret */

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.transsion.core.CoreUtil;

import java.lang.reflect.Method;

/**
 * 屏幕参数工具类
 *
 * @author peng.sun
 */

public class ScreenUtil {
    /**
     * 获得屏幕DisplayMetrics
     *
     * @return
     */
    private static DisplayMetrics getDisplayMetrics() {
        if (CoreUtil.getContext() == null) return null;
        WindowManager windowManager = (WindowManager) CoreUtil.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        Class cls;
        try {
            cls = Class.forName("android.view.Display");
            Method method = cls.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            return dm;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得屏幕宽
     *
     * @return
     */
    public static int getWinWidth() {
        if (CoreUtil.getContext() == null) return -1;
        DisplayMetrics dm = getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获得屏幕高 单位像素
     *
     * @return
     */
    public static int getWinHeight() {
        if (CoreUtil.getContext() == null) return -1;
        DisplayMetrics dm = getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 获得屏幕方向
     *
     * @return
     */
    public static int getScreenOrientation() {
        if (CoreUtil.getContext() == null && CoreUtil.getContext().getResources() == null)
            return 0;
        Configuration mConfiguration = CoreUtil.getContext().getResources().getConfiguration(); //获取设置的配置信息
        return mConfiguration.orientation;
    }

    /**
     * 获得DPI
     *
     * @return
     */
    public static int getDensityDpi() {
        DisplayMetrics dm = getDisplayMetrics();
        if (dm == null) return -1;
        return dm.densityDpi;
    }

    /**
     * 获得scaledDensity
     *
     * @return
     */
    public static float getDensityScale() {
        DisplayMetrics dm = getDisplayMetrics();
        if (dm == null) return -1;
        return dm.scaledDensity;
    }

    /**
     * dp2px
     *
     * @param dpValue
     * @return
     */
    public static int dip2px(float dpValue) {
        final float scale = getDensityDpi();
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px2dp
     *
     * @param pxValue
     * @return
     */
    public static int px2dip(float pxValue) {
        final float scale = getDensityDpi();
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp2px
     *
     * @param spValue
     * @return
     */
    public static int sp2px(float spValue) {
        final float fontScale = getDensityScale();
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px2sp
     *
     * @param pxValue
     * @return
     */
    public static int px2sp(float pxValue) {
        final float fontScale = getDensityScale();
        return (int) (pxValue / fontScale + 0.5f);
    }
}
