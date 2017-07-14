package com.transsion.core.utils;
/* Top Secret */

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.transsion.core.CoreUtil;


/**
 * App相关信息读取工具
 *
 * @author peng.sun
 * @data 2017/6/28
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */

public class AppUtil {

    /**
     * 获取app名称
     */
    public static String getAppName() {
        String appName = "";
        try {
            PackageManager manager = CoreUtil.getContext().getPackageManager();
            if (manager != null) {
                PackageInfo packageInfo = manager.getPackageInfo(CoreUtil.getContext().getPackageName(), 0);
                appName = packageInfo.applicationInfo.loadLabel(manager).toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appName;
    }

    /**
     * 获得包名
     *
     * @return
     */
    public static String getPkgName() {
        return CoreUtil.getContext().getPackageName();
    }

    /**
     * 获得app 的versionName
     *
     * @return
     */
    public static String getVersionName() {
        String versionName = "";
        Context context = CoreUtil.getContext();
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            if (pi != null) {
                versionName = pi.versionName;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 获得app versionCode
     *
     * @return
     */
    public static int getVersionCode() {
        int versionCode = -1;
        Context context = CoreUtil.getContext();
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            if (pi != null) {
                versionCode = pi.versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }
}
