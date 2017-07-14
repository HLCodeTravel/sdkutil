package com.transsion.http.log;

import com.transsion.core.log.ObjectLogUtils;

/**
 * Created by wenshuai.liu on 2017/5/8.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class HttpLog implements LogInterface {

    boolean mLoggingEnabled = false;///default false
    int mLoggingLevel = VERBOSE;
    private ObjectLogUtils logBuilder;

    public HttpLog() {
        logBuilder = new ObjectLogUtils.Builder()
                .setLogSwitch(mLoggingEnabled).create();
    }

    @Override
    public boolean isLoggingEnabled() {
        return mLoggingEnabled;
    }

    @Override
    public void setLoggingEnabled(boolean loggingEnabled) {
        if (loggingEnabled != mLoggingEnabled) {
            this.mLoggingEnabled = loggingEnabled;
            logBuilder = new ObjectLogUtils.Builder()
                    .setLogSwitch(mLoggingEnabled).create();
        }
    }

    @Override
    public int getLoggingLevel() {
        return mLoggingLevel;
    }

    @Override
    public void setLoggingLevel(int loggingLevel) {
        this.mLoggingLevel = loggingLevel;
    }

    @Override
    public boolean shouldLog(int logLevel) {
        return logLevel >= mLoggingLevel;
    }

    public void log(int logLevel, String tag, String msg) {
        logWithThrowable(logLevel, tag, msg, null);
    }

    public void logWithThrowable(int logLevel, String tag, String msg, Throwable t) {
        if (isLoggingEnabled() && shouldLog(logLevel)) {
            switch (logLevel) {
                case VERBOSE:
                    logBuilder.v(tag, msg, t);
                    break;
                case WARN:
                    logBuilder.w(tag, msg, t);
                    break;
                case ERROR:
                    logBuilder.e(tag, msg, t);
                    break;
                case DEBUG:
                    logBuilder.d(tag, msg, t);
                    break;
                case INFO:
                    logBuilder.i(tag, msg, t);
                    break;
            }
        }
    }

    @Override
    public void v(String tag, String msg) {
        log(VERBOSE, tag, msg);
    }

    @Override
    public void v(String tag, String msg, Throwable t) {
        logWithThrowable(VERBOSE, tag, msg, t);
    }

    @Override
    public void d(String tag, String msg) {
        log(VERBOSE, tag, msg);
    }

    @Override
    public void d(String tag, String msg, Throwable t) {
        logWithThrowable(DEBUG, tag, msg, t);
    }

    @Override
    public void i(String tag, String msg) {
        log(INFO, tag, msg);
    }

    @Override
    public void i(String tag, String msg, Throwable t) {
        logWithThrowable(INFO, tag, msg, t);
    }

    @Override
    public void w(String tag, String msg) {
        log(WARN, tag, msg);
    }

    @Override
    public void w(String tag, String msg, Throwable t) {
        logWithThrowable(WARN, tag, msg, t);
    }

    @Override
    public void e(String tag, String msg) {
        log(ERROR, tag, msg);
    }

    @Override
    public void e(String tag, String msg, Throwable t) {
        logWithThrowable(ERROR, tag, msg, t);
    }

    @Override
    public void wtf(String tag, String msg) {
        log(WTF, tag, msg);
    }

    @Override
    public void wtf(String tag, String msg, Throwable t) {
        logWithThrowable(WTF, tag, msg, t);
    }
}
