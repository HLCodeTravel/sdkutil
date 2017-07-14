package com.transsion.http.builder;

import com.transsion.http.RequestCall;
import com.transsion.http.request.ContentType;
import com.transsion.http.request.HttpMethod;
import com.transsion.http.request.PostRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wenshuai.liu on 2017/7/11.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class PostJsonBuilder extends RequestBuilder<PostJsonBuilder> {
    protected String content;
    protected Map<String, String> params;

    public PostJsonBuilder content(String content) {
        this.content = content;
        return this;
    }

    public PostJsonBuilder params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    public PostJsonBuilder addParam(String key, String value) {
        if (params == null) {
            params = new HashMap<>();
        }
        params.put(key, value);
        return this;
    }


    @Override
    public RequestCall build() {
        if (params != null) {
            JSONObject jsonObject = new JSONObject(params);
            this.content = jsonObject.toString();
        }
        return new PostRequest(url, tag, HttpMethod.POST, headers, content, log, connectTimeout, readTimeout, httpCache, ContentType.JSON).build();
    }
}
