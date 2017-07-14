package com.transsion.http.builder;

import com.transsion.http.RequestCall;
import com.transsion.http.request.ContentType;
import com.transsion.http.request.HttpMethod;
import com.transsion.http.request.PostRequest;
import com.transsion.http.util.CheckUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by wenshuai.liu on 2017/5/15.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class PostRequestBuilder extends RequestBuilder<PostRequestBuilder> {


    /**
     * parameters separator
     **/
    public static final String PARAMETERS_SEPARATOR = "&";
    /**
     * equal sign
     **/
    public static final String EQUAL_SIGN = "=";
    protected Map<String, String> params;
    private String content;


    public PostRequestBuilder params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    public PostRequestBuilder addParam(String key, String value) {
        if (params == null) {
            params = new HashMap<>();
        }
        params.put(key, value);
        return this;
    }


    public String toForm() {
        StringBuilder paras = new StringBuilder("");
        if (params != null && params.size() > 0) {
            Iterator<Map.Entry<String, String>> ite = params.entrySet().iterator();
            try {
                while (ite.hasNext()) {
                    Map.Entry<String, String> entry = ite.next();
                    paras.append(entry.getKey()).append(EQUAL_SIGN).append(CheckUtil.utf8Encode(entry.getValue()));
                    if (ite.hasNext()) {
                        paras.append(PARAMETERS_SEPARATOR);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return paras.toString();
    }

    @Override
    public RequestCall build() {
        this.content = toForm();
        return new PostRequest(url, tag, HttpMethod.POST, headers, content, log, connectTimeout, readTimeout, httpCache, ContentType.FORM).build();
    }
}
