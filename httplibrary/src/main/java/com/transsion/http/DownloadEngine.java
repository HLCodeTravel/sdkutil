package com.transsion.http;

import com.transsion.http.execute.DownLoaderExecute;
import com.transsion.http.impl.DownloadCallback;

/**
 * Created by wenshuai.liu on 2017/7/10.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class DownloadEngine {

    private static volatile DownloadEngine mInstance;
    private DownLoaderExecute mDownLoaderExecute;

    private DownloadEngine() {
        mDownLoaderExecute = new DownLoaderExecute();
    }

    public static DownloadEngine getEngine() {
        if (mInstance == null) {
            synchronized (HttpClient.class) {
                if (mInstance == null) {
                    mInstance = new DownloadEngine();
                }
            }
        }
        return mInstance;
    }


    public void execute(RequestCall requestCall, DownloadCallback callBack) {
        mDownLoaderExecute.execute(requestCall, callBack);
    }

    public void continueLoad(Object tag) {
        mDownLoaderExecute.continueByTag(tag);
    }

    public void pauseLoad(Object tag) {
        mDownLoaderExecute.pauseByTag(tag);
    }

    public void cancelLoad(Object tag) {
        mDownLoaderExecute.cancelByTag(tag);
    }

}
