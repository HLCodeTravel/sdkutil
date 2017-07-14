package com.transsion.http.request;

import com.transsion.http.RequestCall;

import java.util.Map;

/**
 * Created by wenshuai.liu on 2017/5/9.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public abstract class HttpRequest {
    protected final String url;
    protected final Object tag;
    protected final HttpMethod method;
    protected final boolean log;
    protected final Map<String, String> headers;
    protected int connectTimeout;
    protected int readTimeout;
    protected boolean httpCache;
    protected Request.Builder builder = new Request.Builder();

    public HttpRequest(String url, Object tag, HttpMethod method, Map<String, String> headers, boolean log,
                       int connectTimeout, int readTimeout, boolean httpCache) {
        this.url = url;
        this.tag = tag;
        this.method = method;
        this.headers = headers;
        this.log = log;
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
        this.httpCache = httpCache;
        initBuilder();
    }

    private void initBuilder() {
        builder.url(url).tag(tag).method(method).headers(headers).log(log).
                connectTimeout(connectTimeout).readTimeout(readTimeout).httpCache(httpCache);
    }

    public RequestCall build() {
        return new RequestCall(this);
    }

    public Request getRequest() {
        return buildRequest();
    }

    protected abstract Request buildRequest();

    public int getReadTimeout() {
        return readTimeout;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public Object getTag() {
        return tag;
    }

    public String getUrl() {
        return url;
    }

    public Map getHeaders() {
        return headers;
    }

}
