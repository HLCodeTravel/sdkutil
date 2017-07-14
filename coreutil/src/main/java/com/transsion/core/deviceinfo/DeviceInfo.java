package com.transsion.core.deviceinfo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.transsion.core.CoreUtil;
import com.transsion.core.log.LogUtils;
import com.transsion.core.utils.PermissionUtil;

/**
 * usage
 *
 * @author 王纪清
 * @data 2017/6/6
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
@SuppressLint("HardwareIds")
public class DeviceInfo {
    private static String mGAId = "";

    /**
     * get IMEI
     *
     * @return
     */
    public static String getIMEI() {
        String imei = SysProp.get("prop.sim1.imei", "");
        if (imei == null || imei.length() == 0 || imei.length() != 15) {
            try {
                imei = ((TelephonyManager) CoreUtil.getContext().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
            } catch (SecurityException e) {
                imei = null;
                e.printStackTrace();
            }
        }
        LogUtils.i("IMEI is " + imei);
        return imei == null ? "" : imei;
    }

    /**
     * get GAID
     *
     * @return
     */
    public static String getGAId() {
        if (mGAId.length() == 0) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        AdvertisingIdClient.AdInfo adInfo = AdvertisingIdClient
                                .getAdvertisingIdInfo(CoreUtil.getContext());
                        mGAId = adInfo.getId();
                        LogUtils.i("advertisingId is " + mGAId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        return mGAId;
    }

    //==================================================================================================================================add by peng.sun

    /**
     * 获得IMSI即国际移动用户识别码
     *
     * @return
     */
    public static String getIMSI() {
        Context context = CoreUtil.getContext();
        if (PackageManager.PERMISSION_GRANTED != context.getPackageManager()
                .checkPermission("android.permission.READ_PHONE_STATE", context.getApplicationContext().getPackageName())) {
            return "";
        }

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (null == tm || null == tm.getSubscriberId()) {
            return "";
        }

        return tm.getSubscriberId();
    }

    /**
     * getSimopertator 获得运营商信息
     *
     * @return
     */
    public static String getSimOperator() {
        Context context = CoreUtil.getContext();
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (null == tm || null == tm.getSubscriberId()) {
            return "";
        }

        return tm.getSimOperator();
    }

    /**
     * 获取蓝牙MAC地址
     *
     * @return
     */
    public static String getBTMAC() {
        BluetoothAdapter m_BluetoothAdapter = null; // Local Bluetooth adapter
        m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean lack = PermissionUtil.lacksPermission(Manifest.permission.ACCESS_WIFI_STATE);
        if (!lack && m_BluetoothAdapter != null) {
            return m_BluetoothAdapter.getAddress();
        }
        return "";
    }

    /**
     * 是否是pad
     *
     * @return
     */
    public static boolean isPad() {
        Context context = CoreUtil.getContext();
        if (context.getResources() != null) {
            return (context.getResources().getConfiguration().screenLayout
                    & Configuration.SCREENLAYOUT_SIZE_MASK)
                    >= Configuration.SCREENLAYOUT_SIZE_LARGE;
        }
        return false;
    }

    /**
     * 获得androiId
     *
     * @return
     */
    public static String getAndroidID() {
        Context context = CoreUtil.getContext();
        String mAndroidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return mAndroidID;
    }
}

