package com.transsion.http.impl;

import com.transsion.http.HttpResponse;

import java.io.IOException;

/**
 * Created by wenshuai.liu on 2017/5/8.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public interface IHttpCallback {
    void sendResponseMessage(int code, byte[] responseBody);

    void sendProgressMessage(long bytesWritten, long bytesTotal);

    void sendStartMessage();

    void sendFinishMessage();

    void sendCancelMessage();

    void sendPauseMessage();

    void sendRetryMessage(int retryNo);

    boolean getUseSyncMode();

    void setUseSyncMode(boolean useSyncMode);

    boolean getUsePoolThread();

    void setUsePoolThread(boolean usePoolThread);

    void sendSuccessMessage(int statusCode, byte[] responseBody);

    void sendFailureMessage(int statusCode, byte[] responseBody, Throwable error);
}
