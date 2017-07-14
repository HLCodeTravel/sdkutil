package com.transsion.http.request;

import java.util.Map;

/**
 * Created by wenshuai.liu on 2017/5/16.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class GetRequest extends HttpRequest {
    public GetRequest(String url, Object tag, HttpMethod method, Map<String, String> headers, boolean log, int connectTimeout,
                      int readTimeout, boolean httpCache) {
        super(url, tag, method, headers, log, connectTimeout, readTimeout, httpCache);
    }

    @Override
    protected Request buildRequest() {
        return builder.build();
    }
}
