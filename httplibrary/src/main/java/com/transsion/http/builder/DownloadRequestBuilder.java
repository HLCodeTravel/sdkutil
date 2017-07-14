package com.transsion.http.builder;

import android.content.Context;

import com.transsion.http.RequestCall;
import com.transsion.http.request.DownloadRequest;
import com.transsion.http.request.HttpMethod;

/**
 * Created by wenshuai.liu on 2017/6/28.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class DownloadRequestBuilder extends RequestBuilder<DownloadRequestBuilder> {
    private String pathname;
    private Context context;

    public DownloadRequestBuilder(Context context) {
        this.context = context;
    }

    public DownloadRequestBuilder pathname(String pathname) {
        this.pathname = pathname;
        return this;
    }

    @Override
    public RequestCall build() {
        return new DownloadRequest(context,pathname, url, tag,
                HttpMethod.GET, headers, log, connectTimeout, readTimeout, httpCache).build();
    }
}
