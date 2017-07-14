package com.transsion.http.request;

import android.text.TextUtils;

import com.transsion.http.log.Console;

import java.util.Map;

/**
 * Created by wenshuai.liu on 2017/5/15.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class PostRequest extends HttpRequest {

    private String content;
    private ContentType contentType;

    public PostRequest(String url, Object tag, HttpMethod method, Map<String, String> headers, String content, boolean log,
                       int connectTimeout, int readTimeout, boolean httpCache, ContentType contentType) {
        super(url, tag, method, headers, log, connectTimeout, readTimeout, httpCache);
        this.content = content;
        this.contentType = contentType;
        if (TextUtils.isEmpty(content)) {
            throw new IllegalArgumentException("the content can not be null !");
        }
        Console.log.d("post", "post url:" + url);
        Console.log.d("post", "post content:" + content);
    }

    public String getContent() {
        return content;
    }

    @Override
    protected Request buildRequest() {
        return builder.content(content).contentType(contentType).build();
    }
}
