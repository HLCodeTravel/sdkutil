package com.transsion.http.builder;

import com.transsion.http.request.GetRequest;
import com.transsion.http.RequestCall;
import com.transsion.http.request.HttpMethod;

/**
 * Created by wenshuai.liu on 2017/5/16.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class GetRequestBuilder extends RequestBuilder<GetRequestBuilder> {
    @Override
    public RequestCall build() {
        return new GetRequest(url, tag, HttpMethod.GET, headers, log, connectTimeout, readTimeout, httpCache).build();
    }
}
