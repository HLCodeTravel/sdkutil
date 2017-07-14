package com.transsion.http.builder;

import com.transsion.http.RequestCall;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by wenshuai.liu on 2017/5/9.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public abstract class RequestBuilder<T extends RequestBuilder> {
    protected String url;
    protected Object tag;
    protected Map<String, String> headers = new LinkedHashMap<>();
    protected int id;
    protected boolean log = true;
    protected int connectTimeout = 10000;//默认
    protected int readTimeout = 10000;
    protected boolean httpCache = false;

    public T id(int id) {
        this.id = id;
        return (T) this;
    }

    public T log(boolean value) {
        this.log = value;
        return (T) this;
    }

    public T url(String url) {
        this.url = url;
        return (T) this;
    }


    public T tag(Object tag) {
        this.tag = tag;
        return (T) this;
    }

    public T headers(Map<String, String> headers) {
        if (this.headers != null) {
            this.headers.putAll(headers);
        } else {
            this.headers = headers;
        }
        return (T) this;
    }

    public T addHeader(String key, String val) {
        headers.put(key, val);
        return (T) this;
    }

    public T connectTimeout(int time) {
        this.connectTimeout = time;
        return (T) this;
    }

    public T readTimeout(int time) {
        this.readTimeout = time;
        return (T) this;
    }

    public T httpCache(boolean httpCache) {
        this.httpCache = httpCache;
        return (T) this;
    }

    public abstract RequestCall build();
}
