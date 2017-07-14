package com.transsion.http.impl;

/**
 * Created by wenshuai.liu on 2017/7/10.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public interface ProgressHandler {
    void updateProgress(long current, long total);

    void sendRetryMessage(int msg);

    void sendFailMessage(int code, String message);
}
