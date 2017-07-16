package com.transsion.sdk.demo;

/* Top Secret */

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.transsion.core.CoreUtil;
import com.transsion.core.deviceinfo.DeviceInfo;
import com.transsion.core.log.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * usage
 *
 * @author 周粤琦
 * @date 2017/6/21
 * ==================================
 * Copyright (c) 2017 TRANSSION.Co.Ltd.
 * All rights reserved
 */

public class TAnalytics {

    private static final String TAG = "TAnalytics";
    private static final String COUNT_LOG = "count_log";

    public static final String ASCII_FS = "\u001c";
    public static final String ASCII_US = "\u001f";

    public static final long ARG_MAX_RECORD_SIZE = 1000 * 1024;
    public static final String UPLOAD_SWITCH = "SWITCH";

    public static final int MODE_ALL = 1;
    public static final int MODE_SINGLE = 2;

    private static final String LOG_BIZUSEREXTEND = "bizuserextend";
    private static final String LOG_STATLOG = "statlog";
    private static final String LOG_STARTRUNLOG = "startrunlog";
    private static final String LOG_DOWNLOAD = "donwloadlog";

    private static class Preferences {
        public static final String PREFERENCE_LAST_UPLOAD = "PREFERENCE_LAST_UPLOAD";
        public static final String PREFERENCE_IS_UPLOADING = "PREFERENCE_IS_UPLOADING";
        public static final String PREFERENCE_IS_FIRST_OPEN = "PREFERENCE_IS_FIRST_OPEN";
        public static final String PREFERENCE_HAS_RECORD_FIRST = "PREFERENCE_HAS_RECORD_FIRST";
        public static final String PREFERENCE_IP = "PREFERENCE_IP";
        public static final String PREFERENCE_GADID = "PREFERENCE_GADID";
    }


    private static Context mContext;
    private static TAnalytics mInstance;

    private static String mOptionUrl;
    private static String mUploadUrl;
    private static Map<String, String> mBasicInfo;

    private static List<String> mLogList;

    private TAnalytics(Context context, Map<String, String> basicInfo,
                       String optionUrl, String uploadUrl, List<String> logList) {

        /**工具类初始化**/
        CoreUtil.init(context);
        mContext = context;

        /**初始化基础参数**/
        initBasicInfo();
        mBasicInfo.putAll(basicInfo);

        mOptionUrl = optionUrl;
        mUploadUrl = uploadUrl;

        mLogList = logList;

        if (mLogList.contains(LOG_BIZUSEREXTEND) && FileUtil.isFileExist(mContext.getFilesDir().getPath() + "/" + LOG_BIZUSEREXTEND)) {
            if (PreferenceUtils.readBoolean(context, Preferences.PREFERENCE_IS_FIRST_OPEN, true)) {
                recordLog(null, LOG_BIZUSEREXTEND);
                PreferenceUtils.writeBoolean(mContext, Preferences.PREFERENCE_IS_FIRST_OPEN, false);
            }
        }


        if (PreferenceUtils.readLong(context, Preferences.PREFERENCE_LAST_UPLOAD) != 0) {
            if (System.currentTimeMillis() - PreferenceUtils.readLong(mContext, Preferences.PREFERENCE_LAST_UPLOAD) > 24 * 60 * 60 * 1000) {
                if ((System.currentTimeMillis() - PreferenceUtils.readLong(mContext, Preferences.PREFERENCE_LAST_UPLOAD)) / 1000 < 30 * 24 * 60 * 60) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            if (TextUtils.isEmpty(PreferenceUtils.readString(mContext, Preferences.PREFERENCE_IP))) {
                                recordIP();
                            }

                            if (!PreferenceUtils.readBoolean(mContext, Preferences.PREFERENCE_IS_UPLOADING, false)) {
                                PreferenceUtils.writeBoolean(mContext, Preferences.PREFERENCE_IS_UPLOADING, true);


                                uploadToFlume();
                            }
                        }
                    }).start();
                } else {

                }
            }
        }

        recordLog(null, LOG_STARTRUNLOG);
    }

    public static TAnalytics init(Context context, Map<String, String> basicInfo,
                                  String optionUrl, String uploadUrl, List<String> logList) {
        if (mInstance == null) {
            synchronized (TAnalytics.class) {
                if (mInstance == null) {
                    mInstance = new TAnalytics(context, basicInfo, optionUrl, uploadUrl, logList);
                }
            }
        }
        return mInstance;
    }


    /**
     * @param params  所需记录的日志参数
     * @param logName 日志名称
     */
    private static void recordLog(Map<String, String> params, String logName) {


        StringBuffer buffer = new StringBuffer();


        for (String key : mBasicInfo.keySet()) {
            buffer.append(key + ASCII_FS + mBasicInfo.get(key) + ASCII_US);
        }

        if (params != null) {
            for (String key : params.keySet()) {
                buffer.append(key + ASCII_FS + params.get(key) + ASCII_US);
            }
        }

        buffer.append("IP" + ASCII_FS + PreferenceUtils.readString(mContext, Preferences.PREFERENCE_IP));

        record(logName, buffer.toString());
    }


    /**
     * usage:次数统计
     *
     * @param eventName 事件名称 || 日志名称
     * @param key
     * @param targetId  对象ID
     */
    public static void actionEvent(String eventName, String key, String targetId) {
        /**计数**/
        addCount(eventName, key);

        String log = getStatLog(eventName);
        Map<String, String> paramMap = new HashMap<>();

        paramMap.put(key, log);

        /**只记录最新的一条**/
        mContext.deleteFile(eventName);
        recordLog(paramMap, eventName);
    }

    public static void actionEvent(String eventName, Map<String, String> params) {
        recordLog(params, eventName);
    }

    public static void customEvent(String eventName, String key, int value, int mode) {
        Map<String, String> params = new HashMap<>();
        params.put(key, String.valueOf(value));
        if (mode == MODE_SINGLE) {
            mContext.deleteFile(eventName);
        }
        recordLog(params, eventName);
    }

    public static void customEvent(String eventName, String key, String value, int mode) {
        Map<String, String> params = new HashMap<>();
        params.put(key, value);
        if (mode == MODE_SINGLE) {
            mContext.deleteFile(eventName);
        }
        recordLog(params, eventName);
    }

    public static void customEvent(String eventName, String key, float value, int mode) {
        Map<String, String> params = new HashMap<>();
        params.put(key, String.valueOf(value));
        if (mode == MODE_SINGLE) {
            mContext.deleteFile(eventName);
        }
        recordLog(params, eventName);
    }

    public static void customEvent(String eventName, String key, long value, int mode) {
        Map<String, String> params = new HashMap<>();
        params.put(key, String.valueOf(value));
        if (mode == MODE_SINGLE) {
            mContext.deleteFile(eventName);
        }
        recordLog(params, eventName);
    }


    public static void customEvent(String eventName, Map<String, String> params, int mode) {
        if (mode == MODE_SINGLE) {
            mContext.deleteFile(eventName);
        }
        recordLog(params, eventName);
    }


    private static void uploadToFlume() {

        String uploadInfo = getUploadInfo();
        if (!TextUtils.isEmpty(uploadInfo)) {
            Map<String, Boolean> options = getUploadFileName(uploadInfo);

            if (options != null && !options.isEmpty()) {

                if (options.get("SWITCH")) {
                    List<String> filePathList = new ArrayList<>();
                    for (String logName : mLogList) {
                        boolean enabled = false;

                        enabled = options.get(logName);
                        String filePath = mContext.getFilesDir().getPath() + "/" + logName;
                        LogUtils.e(TAG, "filePath:" + filePath);
                        if (FileUtil.isFileExist(filePath)) {
                            if (enabled) {
                                if (FileUtil.getFileSize(filePath) > ARG_MAX_RECORD_SIZE) {
                                    String mZipPath = filePath + ".gz";
                                    FileUtil.gzip(filePath, mZipPath);
                                    filePathList.add(mZipPath);
                                } else {
                                    filePathList.add(filePath);
                                }
                            } else {
                                mContext.deleteFile(logName);
                            }
                        }

                        upload(filePathList);

                    }
                }
            }
        }
    }


    private static String getUploadInfo() {
        InputStream in = null;
        BufferedReader reader = null;
        StringBuffer buffer = new StringBuffer();
        HttpURLConnection conn = null;
        try {
            URL url = new URL(mOptionUrl);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10 * 1000);

            int code = conn.getResponseCode();
            if (code == 200) {
                in = conn.getInputStream();
                reader = new BufferedReader(new InputStreamReader(in));
                String readStr = null;
                while ((readStr = reader.readLine()) != null) {
                    buffer.append(readStr);
                    buffer.append("\r\n");
                }

                return buffer.toString();
            } else {
                return "";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (in != null) {
                in.close();
            }

            if (reader != null) {
                reader.close();
            }

            if (conn != null) {
                conn.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static Map<String, Boolean> getUploadFileName(String uploadInfo) {
        Map<String, Boolean> options = new HashMap<>();

        try {
            JSONObject object = new JSONObject(uploadInfo);
            JSONObject option = object.getJSONObject("option");
            String ip = object.getString("ip");
            //Util.record(mContext,"basic","ip\u001c" + ip);
            PreferenceUtils.writeString(mContext, "IP", ip);
            long duration = option.getLong("duration");

            for (String logName : mLogList) {
                if (option.getInt(logName) == 1) {
                    options.put(logName, true);
                } else {
                    options.put(logName, false);
                }
            }

            if (option.getInt("switch") == 1) {
                options.put(UPLOAD_SWITCH, true);
            } else {
                options.put(UPLOAD_SWITCH, false);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return options;
    }

    private static void upload(List<String> uploadFiles) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = UUID.randomUUID().toString();
        try {
            URL url = new URL(mUploadUrl);


            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            // 发送POST请求必须设置如下两行
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestMethod("POST");
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
            DataOutputStream ds = new DataOutputStream(con.getOutputStream());

            for (int i = 0; i < uploadFiles.size(); i++) {
                String uploadFile = uploadFiles.get(i);
                String filename = uploadFile.substring(uploadFile.lastIndexOf("/") + 1);


                ds.writeBytes(twoHyphens + boundary + end);
                ds.writeBytes("Content-Disposition: form-data;name=\"" + "file" +
                        "\";filename=\"" + filename + "\"" + end);
                ds.writeBytes("Content-Type: text/plain; charset=US-ASCII" + end);
                ds.writeBytes("Content-Transfer-Encoding: 8bit" + end);
                ds.writeBytes(end);
                FileInputStream fStream = new FileInputStream(uploadFile);
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];
                int length = -1;
                while ((length = fStream.read(buffer)) != -1) {
                    ds.write(buffer, 0, length);
                }

                ds.writeBytes("\r");
                /* close streams */
                fStream.close();
            }

            ds.writeBytes(twoHyphens + boundary + "-" + twoHyphens + end);
            ds.flush();

            int code = con.getResponseCode();
            if (code == 200) {
                LogUtils.e(TAG, "upload success");
                for (int i = 0; i < uploadFiles.size(); i++) {
                    String fileName = uploadFiles.get(i).substring(uploadFiles.get(i).lastIndexOf("/") + 1);

                    if (fileName.endsWith(".gz")) {
                        mContext.deleteFile(uploadFiles.get(i));
                    } else {
                        mContext.deleteFile(fileName);
                    }

                    if (fileName.equals(LOG_STATLOG)) {
                        clearCount();
                    }
                }
            } else {

            }
            con.disconnect();
            ds.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private static void record(String file, String records) {
        int size = 0;

        try {
            FileInputStream fin = mContext.openFileInput(file);
            size = fin.available();
            fin.close();
            if (size > ARG_MAX_RECORD_SIZE) {
                mContext.deleteFile(file);
            }
        } catch (Exception e) {
        }

        try {
            FileOutputStream fout = mContext.openFileOutput(file, Context.MODE_APPEND);

            fout.write((records + "\n").getBytes());
            fout.close();
        } catch (Exception e) {

        }
    }

    private static String getStatLog(String eventName) {
        Map<String, ?> logMap = PreferenceUtils.getAll(mContext, eventName);
        Set<String> keySet = logMap.keySet();
        StringBuffer logBuffer = new StringBuffer();
        logBuffer.append("{");
        for (String key : keySet) {
            logBuffer.append(key + ":" + PreferenceUtils.readInt(mContext, key, eventName) + ",");
        }
        if (logBuffer.length() > 0) {
            logBuffer.deleteCharAt(logBuffer.length() - 1);
        }

        logBuffer.append("}");
        return logBuffer.toString();
    }

    private static void clearCount() {
        PreferenceUtils.claerLog(mContext, COUNT_LOG);
    }

    private static void recordIP() {
        try {
            String uploadInfo = getUploadInfo();
            JSONObject object = new JSONObject(uploadInfo);
            String ip = object.getString("IP");
            PreferenceUtils.writeString(mContext, Preferences.PREFERENCE_IP, ip);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void initBasicInfo() {

        if (mBasicInfo == null) {
            mBasicInfo = new HashMap<>();
        }

        if (mBasicInfo != null) {
            /**用户基本信息**/
            mBasicInfo.put("IMEI", DeviceInfo.getIMEI());
            mBasicInfo.put("GADID", DeviceInfo.getGAId());
            mBasicInfo.put("IMSI", DeviceInfo.getIMSI());
            mBasicInfo.put("ADNROIDID", DeviceInfo.getAndroidID());

            mBasicInfo.put("APKPKGNAME", mContext.getPackageName());
            try {
                PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
                mBasicInfo.put("APKVERNAME", "" + packageInfo.versionName);
                mBasicInfo.put("APKVERCODE", "" + packageInfo.versionCode);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void addCount(String name, String key) {
        int oldCount = PreferenceUtils.readInt(mContext, key, 0, name);
        PreferenceUtils.writeInt(mContext, key, ++oldCount, name);
    }
}
