package com.transsion.http;

import android.content.Context;

import com.transsion.http.builder.DownloadRequestBuilder;
import com.transsion.http.builder.GetRequestBuilder;
import com.transsion.http.builder.ImageRequestBuilder;
import com.transsion.http.builder.PostJsonBuilder;
import com.transsion.http.builder.PostRequestBuilder;
import com.transsion.http.util.FileUtil;

/**
 * Created by wenshuai.liu on 2017/5/9.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class HttpClient {

    private static volatile HttpClient mInstance;

    private HttpClient() {

    }

    public static HttpClient getClient() {
        if (mInstance == null) {
            synchronized (HttpClient.class) {
                if (mInstance == null) {
                    mInstance = new HttpClient();
                }
            }
        }
        return mInstance;
    }

    public static DownloadRequestBuilder download(Context context) {
        return new DownloadRequestBuilder(context);
    }

    public static PostRequestBuilder post() {
        return new PostRequestBuilder();
    }

    public static PostJsonBuilder postJson() {
        return new PostJsonBuilder();
    }

    public static GetRequestBuilder get() {
        return new GetRequestBuilder();
    }

    public static ImageRequestBuilder image(Context context) {
        return new ImageRequestBuilder(context);
    }

    public static String getCacheSize(Context context) {
        return FileUtil.getDiskCacheSize(context);
    }

    public static void clearCache(Context context) {
        FileUtil.clearCache(context);
    }
}
