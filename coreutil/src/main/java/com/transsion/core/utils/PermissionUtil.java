package com.transsion.core.utils;
/* Top Secret */

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.transsion.core.CoreUtil;


/**
 * 权限工具
 *
 * @author peng.sun
 * @data 2017/06/28
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */

public class PermissionUtil {
    /**
     * 判断是否缺少权限缺少则返回trues
     *
     * @param permissions
     * @return
     */
    public static boolean lacksPermissions(String... permissions) {
        for (String permission : permissions) {
            if (lacksPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否缺少权限,缺少则返回true
     *
     * @param permission
     * @return
     */
    public static boolean lacksPermission(String permission) {
        Context context = CoreUtil.getContext();
        return ContextCompat.checkSelfPermission(context, permission) ==
                PackageManager.PERMISSION_DENIED;
    }

}
