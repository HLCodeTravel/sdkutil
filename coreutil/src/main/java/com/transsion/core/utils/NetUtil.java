package com.transsion.core.utils;
/* Top Secret */

import android.Manifest;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.transsion.core.CoreUtil;


/**
 * @author peng.sun
 * @data 2017/6/28
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */

public class NetUtil {
    private static final int NETWORK_TYPE_UNAVAILABLE = -1;
    private static final int NETWORK_TYPE_WIFI = -101;
    /**
     * wifi
     */
    public static final int NETWORK_CLASS_WIFI = -101;
    /**
     * 网络不可用
     */
    public static final int NETWORK_CLASS_UNAVAILABLE = -1;
    /**
     * Network type is unknown
     */
    public static final int NETWORK_CLASS_UNKNOWN = 0;
    /**
     * Current network is2G
     */
    public static final int NETWORK_CLASS_2_G = 1;
    /**
     * Current network is3G
     */
    public static final int NETWORK_CLASS_3_G = 2;
    /**
     * Current network is4G .
     */
    public static final int NETWORK_CLASS_4_G = 3;

    // 适配低版本手机
    /**
     * Network type is unknown
     */
    private static final int NETWORK_TYPE_UNKNOWN = 0;
    /**
     * Current network is GPRS
     */
    private static final int NETWORK_TYPE_GPRS = 1;
    /**
     * Current network is EDGE
     */
    private static final int NETWORK_TYPE_EDGE = 2;
    /**
     * Current network is UMTS
     */
    private static final int NETWORK_TYPE_UMTS = 3;
    /**
     * Current network is CDMA: Either IS95A or IS95B
     */
    private static final int NETWORK_TYPE_CDMA = 4;
    /**
     * Current network is EVDO revision 0
     */
    private static final int NETWORK_TYPE_EVDO_0 = 5;
    /**
     * Current network is EVDO revision A
     */
    private static final int NETWORK_TYPE_EVDO_A = 6;
    /**
     * Current network is 1xRTT
     */
    private static final int NETWORK_TYPE_1xRTT = 7;
    /**
     * Current network is HSDPA
     */
    private static final int NETWORK_TYPE_HSDPA = 8;
    /**
     * Current network is HSUPA
     */
    private static final int NETWORK_TYPE_HSUPA = 9;
    /**
     * Current network is HSPA
     */
    private static final int NETWORK_TYPE_HSPA = 10;
    /**
     * Current network is iDen
     */
    private static final int NETWORK_TYPE_IDEN = 11;
    /**
     * Current network is EVDO revision B
     */
    private static final int NETWORK_TYPE_EVDO_B = 12;
    /**
     * Current network is LTE
     */
    private static final int NETWORK_TYPE_LTE = 13;
    /**
     * Current network is eHRPD
     */
    private static final int NETWORK_TYPE_EHRPD = 14;
    /**
     * Current network is HSPA+
     */
    private static final int NETWORK_TYPE_HSPAP = 15;

    /**
     * 检查是否有网络。有则返回true
     */
    public static boolean checkNetworkState() {
        if (CoreUtil.getContext() == null) return false;
        ConnectivityManager connectMgr = (ConnectivityManager) CoreUtil.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectMgr == null) {
            return false;
        }
        NetworkInfo nwInfo = connectMgr.getActiveNetworkInfo();
        if (nwInfo == null || !nwInfo.isAvailable()) {
            return false;
        }
        return true;
    }


    /**
     * 获取Mac地址
     */
    public static String getWLANMAC() {
        if (CoreUtil.getContext() != null) {
            WifiManager wifi = (WifiManager) CoreUtil.getContext().getSystemService(Context.WIFI_SERVICE);
            boolean lack = PermissionUtil.lacksPermission(Manifest.permission.ACCESS_WIFI_STATE);
            if (!lack) {
                WifiInfo info = wifi.getConnectionInfo();
                return info.getMacAddress();
            }
        }
        return "";
    }

    /**
     * 运营商名称
     */
    public static String getNetworkOperatorName() {
        if (getTeleMgr() == null) return "";
        return getTeleMgr().getNetworkOperatorName();
    }

    /**
     * 运营商编号(MCC+MNC)
     */
    public static String getNetworkOperator() {
        if (getTeleMgr() == null) return "";
        return getTeleMgr().getNetworkOperator();
    }

    /**
     * 国家ISO代码
     */
    public static String getNetworkCountryIso() {
        if (getTeleMgr() == null) return "";
        return getTeleMgr().getNetworkCountryIso();
    }


    /**
     * 获得telephoneManager
     *
     * @return
     */
    private static TelephonyManager getTeleMgr() {
        if (CoreUtil.getContext() == null) return null;
        TelephonyManager teleMgr = (TelephonyManager) CoreUtil.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        return teleMgr;
    }

    /**
     * 获得网络类型
     *
     * @return
     */
    public static int getNetworkType() {
        int networkType = NETWORK_TYPE_UNKNOWN;
        try {
            if (CoreUtil.getContext() == null && CoreUtil.getContext().getSystemService(Context.CONNECTIVITY_SERVICE) == null) {
                return 0;
            }
            final NetworkInfo network = ((ConnectivityManager) CoreUtil.getContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            if (network != null && network.isAvailable() && network.isConnected()) {
                int type = network.getType();
                if (type == ConnectivityManager.TYPE_WIFI) {
                    networkType = NETWORK_TYPE_WIFI;
                } else if (type == ConnectivityManager.TYPE_MOBILE) {
                    TelephonyManager telephonyManager = (TelephonyManager) CoreUtil.getContext()
                            .getSystemService(Context.TELEPHONY_SERVICE);
                    networkType = telephonyManager.getNetworkType();
                }
            } else {
                networkType = NETWORK_TYPE_UNAVAILABLE;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return classifyNetType(networkType);
    }

    /**
     * 归类网络类型
     *
     * @param networkType
     * @return
     */
    private static int classifyNetType(int networkType) {
        switch (networkType) {
            case NETWORK_TYPE_UNAVAILABLE:
                return NETWORK_CLASS_UNAVAILABLE;
            case NETWORK_TYPE_WIFI:
                return NETWORK_CLASS_WIFI;
            case NETWORK_TYPE_GPRS:
            case NETWORK_TYPE_EDGE:
            case NETWORK_TYPE_CDMA:
            case NETWORK_TYPE_1xRTT:
            case NETWORK_TYPE_IDEN:
                return NETWORK_CLASS_2_G;
            case NETWORK_TYPE_UMTS:
            case NETWORK_TYPE_EVDO_0:
            case NETWORK_TYPE_EVDO_A:
            case NETWORK_TYPE_HSDPA:
            case NETWORK_TYPE_HSUPA:
            case NETWORK_TYPE_HSPA:
            case NETWORK_TYPE_EVDO_B:
            case NETWORK_TYPE_EHRPD:
            case NETWORK_TYPE_HSPAP:
                return NETWORK_CLASS_3_G;
            case NETWORK_TYPE_LTE:
                return NETWORK_CLASS_4_G;
            default:
                return NETWORK_CLASS_UNKNOWN;
        }
    }

}
