package com.transsion.http.impl;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.transsion.http.HttpResponse;
import com.transsion.http.util.CheckUtil;
import com.transsion.http.log.Console;

import java.io.IOException;

/**
 * Created by wenshuai.liu on 2017/5/8.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public abstract class HttpCallbackImpl implements IHttpCallback {
    private static final String LOG_TAG = "HttpCallbackImpl";
    public static final String DEFAULT_CHARSET = "UTF-8";
    public static final String UTF8_BOM = "\uFEFF";
    protected static final int SUCCESS_MESSAGE = 0;
    protected static final int FAILURE_MESSAGE = 1;
    protected static final int START_MESSAGE = 2;
    protected static final int FINISH_MESSAGE = 3;
    protected static final int PROGRESS_MESSAGE = 4;
    protected static final int RETRY_MESSAGE = 5;
    protected static final int CANCEL_MESSAGE = 6;
    protected static final int PAUSE_MESSAGE = 7;
    private Handler handler;
    private boolean useSyncMode;
    private boolean usePoolThread;
    private Looper looper = null;

    public HttpCallbackImpl() {
        this(null);
    }

    public HttpCallbackImpl(Looper looper) {
        this(looper == null ? Looper.myLooper() : looper, false);
    }

    public HttpCallbackImpl(boolean usePool) {
        this(usePool ? null : Looper.myLooper(), usePool);
    }

    private HttpCallbackImpl(Looper looper, boolean usePool) {
        if (!usePool) {
            CheckUtil.asserts(looper != null, "use looper thread, must call Looper.prepare() first!");
            this.looper = looper;
            this.handler = new ResponderHandler(this, looper);
        } else {
            CheckUtil.asserts(looper == null, "use pool thread, looper should be null!");
            this.looper = null;
            this.handler = null;
        }
        this.usePoolThread = usePool;
    }

    @Override
    public void setUseSyncMode(boolean useSyncMode) {
        if (!useSyncMode && looper == null) {
            useSyncMode = true;
            Console.log.w(LOG_TAG, "Current thread has not called Looper.prepare(). Forcing synchronous mode.");
        }
        if (!useSyncMode && handler == null) {
            handler = new ResponderHandler(this, looper);
        } else if (useSyncMode && handler != null) {
            handler = null;
        }
        this.useSyncMode = useSyncMode;
    }

    @Override
    public boolean getUseSyncMode() {
        return this.useSyncMode;
    }

    @Override
    public boolean getUsePoolThread() {
        return this.usePoolThread;
    }

    @Override
    public void setUsePoolThread(boolean usePoolThread) {
        if (usePoolThread) {
            looper = null;
            handler = null;
        }
        this.usePoolThread = usePoolThread;
    }

    public abstract void onSuccess(int statusCode, byte[] responseBody);

    public abstract void onFailure(int statusCode, byte[] responseBody, Throwable error);

    @Override
    public void sendSuccessMessage(int statusCode, byte[] responseBody) {
        sendMessage(obtainMessage(SUCCESS_MESSAGE, new Object[]{statusCode, responseBody}));
    }

    @Override
    public void sendFailureMessage(int statusCode, byte[] responseBody, Throwable error) {
        sendMessage(obtainMessage(FAILURE_MESSAGE, new Object[]{statusCode, responseBody, error}));
    }

    @Override
    public void sendStartMessage() {
        sendMessage(obtainMessage(START_MESSAGE, null));
    }

    @Override
    public void sendFinishMessage() {
        sendMessage(obtainMessage(FINISH_MESSAGE, null));
    }

    @Override
    public void sendRetryMessage(int retryNo) {
        sendMessage(obtainMessage(RETRY_MESSAGE, new Object[]{retryNo}));
    }

    @Override
    public void sendPauseMessage() {
        sendMessage(obtainMessage(PAUSE_MESSAGE, null));
    }

    @Override
    public void sendProgressMessage(long bytesWritten, long bytesTotal) {
        sendMessage(obtainMessage(PROGRESS_MESSAGE, new Object[]{bytesWritten, bytesTotal}));
    }

    @Override
    final public void sendCancelMessage() {
        sendMessage(obtainMessage(CANCEL_MESSAGE, null));
    }

    protected void postRunnable(Runnable runnable) {
        if (runnable != null) {
            if (getUseSyncMode() || handler == null) {
                runnable.run();
            } else {
                handler.post(runnable);
            }
        }
    }

    protected Message obtainMessage(int msgId, Object msgData) {
        return Message.obtain(handler, msgId, msgData);
    }

    protected void sendMessage(Message msg) {
        if (getUseSyncMode() || handler == null) {
            handleMessage(msg);
        } else if (!Thread.currentThread().isInterrupted()) {
            CheckUtil.asserts(handler != null, "handler should not be null!");
            handler.sendMessage(msg);
        }
    }

    protected void handleMessage(Message message) {
        Object[] response;
        try {
            switch (message.what) {
                case SUCCESS_MESSAGE:
                    response = (Object[]) message.obj;
                    if (response != null && response.length >= 2) {
                        onSuccess((Integer) response[0], (byte[]) response[1]);
                    } else {
                        Console.log.e(LOG_TAG, "SUCCESS_MESSAGE didn't got enough params");
                    }
                    break;
                case FAILURE_MESSAGE:
                    response = (Object[]) message.obj;
                    if (response != null && response.length >= 3) {
                        onFailure((Integer) response[0], (byte[]) response[1], (Throwable) response[2]);
                    } else {
                        Console.log.e(LOG_TAG, "FAILURE_MESSAGE didn't got enough params");
                    }
                    break;
                case START_MESSAGE:
                    onStart();
                    break;
                case FINISH_MESSAGE:
                    onFinish();
                    break;
                case PAUSE_MESSAGE:
                    onPause();
                    break;
                case PROGRESS_MESSAGE:
                    response = (Object[]) message.obj;
                    if (response != null && response.length >= 2) {
                        try {
                            onProgress((Long) response[0], (Long) response[1]);
                        } catch (Throwable t) {
                            Console.log.e(LOG_TAG, "custom onProgress contains an error", t);
                        }
                    } else {
                        Console.log.e(LOG_TAG, "PROGRESS_MESSAGE didn't got enough params");
                    }
                    break;
                case RETRY_MESSAGE:
                    response = (Object[]) message.obj;
                    if (response != null && response.length == 1) {
                        onRetry((Integer) response[0]);
                    } else {
                        Console.log.e(LOG_TAG, "RETRY_MESSAGE didn't get enough params");
                    }
                    break;
                case CANCEL_MESSAGE:
                    onCancel();
                    break;
            }
        } catch (Throwable error) {
            onUserException(error);
        }
    }


    @Override
    public void sendResponseMessage(int code, byte[] responseBody) {
        if (!Thread.currentThread().isInterrupted()) {
            if (code >= 300) {
                sendFailureMessage(code, responseBody, new IOException());
            } else {
                sendSuccessMessage(code, responseBody);
            }
        }
    }

    public void onStart() {
    }

    public void onFinish() {
    }

    public void onCancel() {
        Console.log.d(LOG_TAG, "Request got cancelled");
    }

    public void onRetry(int retry) {
        Console.log.d(LOG_TAG, String.format("Request retry no. %d", retry));
    }

    public void onProgress(long bytesWritten, long totalSize) {
        Console.log.v(LOG_TAG, String.format("Progress %d from %d (%2.0f%%)", bytesWritten, totalSize, (totalSize > 0) ? (bytesWritten * 1.0 / totalSize) * 100 : -1));
    }

    public void onPause() {
        Console.log.d(LOG_TAG, "Request got onPaused");
    }

    public void onUserException(Throwable error) {
        Console.log.e(LOG_TAG, "User-space exception detected!", error);
        throw new RuntimeException(error);
    }

    private static class ResponderHandler extends Handler {
        private final HttpCallbackImpl mResponder;

        ResponderHandler(HttpCallbackImpl mResponder, Looper looper) {
            super(looper);
            this.mResponder = mResponder;
        }

        @Override
        public void handleMessage(Message msg) {
            mResponder.handleMessage(msg);
        }
    }
}
