package com.transsion.http.request;

/**
 * Created by wenshuai.liu on 2017/7/11.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public enum ContentType {
    FORM("application/x-www-form-urlencoded"),
    JSON("application/json");

    private final String value;

    ContentType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
