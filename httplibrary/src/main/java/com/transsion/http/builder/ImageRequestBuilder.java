package com.transsion.http.builder;

import android.content.Context;

import com.transsion.http.RequestCall;
import com.transsion.http.request.HttpMethod;
import com.transsion.http.request.ImageRequest;

/**
 * Created by wenshuai.liu on 2017/5/19.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class ImageRequestBuilder extends RequestBuilder<ImageRequestBuilder> {


    private boolean imageCache;
    private Context context;

    public ImageRequestBuilder(Context context) {
        this.context = context;
    }

    public ImageRequestBuilder cache(boolean value) {
        this.imageCache = value;
        return this;
    }

    @Override
    public RequestCall build() {
        return new ImageRequest(context, url, imageCache, tag, HttpMethod.GET, headers, log, connectTimeout, readTimeout, httpCache).build();
    }
}
