package com.transsion.http.request;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.util.Map;

/**
 * Created by wenshuai.liu on 2017/6/28.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class DownloadRequest extends HttpRequest {
    private String filepath;
    private Context context;

    public DownloadRequest(Context context, String filepath, String url, Object tag, HttpMethod method, Map<String, String> headers, boolean log,
                           int connectTimeout, int readTimeout, boolean httpCache) {
        super(url, tag, method, headers, log, connectTimeout, readTimeout, httpCache);
        this.filepath = filepath;
        this.context = context;
        if (TextUtils.isEmpty(filepath)) {
            throw new IllegalArgumentException("the filepath can not be null !");
        } else {
            File file = new File(filepath);
            if (file.isDirectory()) {
                throw new IllegalArgumentException("the filepath can not be directory !");
            }
        }
    }

    @Override
    protected Request buildRequest() {
        return builder.context(context).pathname(filepath).build();
    }
}
