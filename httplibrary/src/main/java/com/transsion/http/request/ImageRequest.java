package com.transsion.http.request;

import android.content.Context;

import com.transsion.http.log.Console;

import java.util.Map;

/**
 * Created by wenshuai.liu on 2017/5/31.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class ImageRequest extends HttpRequest {
    private boolean imageCache;
    private Context context;

    public ImageRequest(Context context, String url, boolean cache, Object tag, HttpMethod method, Map<String, String> headers, boolean log, int connectTimeout,
                        int readTimeout, boolean httpCache) {
        super(url, tag, method, headers, log, connectTimeout, readTimeout, httpCache);
        this.imageCache = cache;
        this.context = context;
        Console.log.d("image", "origin image url:" + url);
    }

    public boolean getImageCache() {
        return imageCache;
    }

    public Context getContext() {
        return context;
    }

    @Override
    protected Request buildRequest() {
        return builder.imageCache(imageCache).context(context).build();
    }
}
