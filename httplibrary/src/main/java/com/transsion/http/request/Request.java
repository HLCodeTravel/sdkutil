package com.transsion.http.request;

import android.content.Context;

import com.transsion.http.log.Console;

import java.util.Map;

/**
 * Created by wenshuai.liu on 2017/6/29.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public final class Request {

    private final int connectTimeout;
    private final int readTimeout;
    private final boolean httpCache;
    private final String url;
    private final String content;
    private final Object tag;
    private final HttpMethod method;
    private final boolean imageCache;
    private final Map<String, String> headers;
    private final Context context;
    private final String pathname;
    private final ContentType contentType;

    public Request(final Builder builder) {
        this.connectTimeout = builder.connectTimeout;
        this.readTimeout = builder.readTimeout;
        this.httpCache = builder.httpCache;
        this.url = builder.url;
        this.content = builder.content;
        this.tag = builder.tag != null ? builder.tag : this;
        this.method = builder.method;
        this.headers = builder.headers;
        this.imageCache = builder.imageCache;
        this.context = builder.context;
        this.pathname = builder.pathname;
        this.contentType = builder.contentType;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public boolean isHttpCache() {
        return httpCache;
    }

    public String getUrl() {
        return url;
    }

    public String getContent() {
        return content;
    }

    public Object getTag() {
        return tag;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public boolean isImageCache() {
        return imageCache;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Context getContext() {
        return context;
    }

    public String getPathname() {
        return pathname;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    public static final class Builder {
        private boolean log;
        private int connectTimeout;
        private int readTimeout;
        private boolean httpCache;
        private String url;
        private String content;
        private Object tag;
        private HttpMethod method;
        private boolean imageCache;
        private Context context;
        private String pathname;
        private ContentType contentType;
        private Map<String, String> headers;

        public Builder() {
            Console.log.setLoggingEnabled(log);
        }

        public Builder(Request request) {
            this.connectTimeout = request.connectTimeout;
            this.readTimeout = request.readTimeout;
            this.httpCache = request.httpCache;
            this.url = request.url;
            this.content = request.content;
            this.tag = request.tag;
            this.method = request.method;
            this.headers = request.headers;
            this.imageCache = request.imageCache;
            this.pathname = request.pathname;
            this.contentType = request.contentType;
            this.context = request.context.getApplicationContext();
            Console.log.setLoggingEnabled(log);
        }

        public Builder connectTimeout(int timeout) {
            this.connectTimeout = timeout;
            return this;
        }

        public Builder readTimeout(int readtime) {
            this.readTimeout = readtime;
            return this;
        }

        public Builder httpCache(boolean value) {
            this.httpCache = value;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder content(String postData) {
            this.content = postData;
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder tag(Object tag) {
            this.tag = tag;
            return this;
        }

        public Builder method(HttpMethod method) {
            this.method = method;
            return this;
        }

        public Builder imageCache(boolean cache) {
            this.imageCache = cache;
            return this;
        }

        public Builder context(Context context) {
            this.context = context;
            return this;
        }

        public Builder log(boolean value) {
            this.log = value;
            Console.log.setLoggingEnabled(log);
            return this;
        }

        public Builder contentType(ContentType contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder pathname(String filename) {
            this.pathname = filename;
            return this;
        }

        public Request build() {
            if (url == null) throw new IllegalStateException("url == null");
            return new Request(this);
        }
    }
}
