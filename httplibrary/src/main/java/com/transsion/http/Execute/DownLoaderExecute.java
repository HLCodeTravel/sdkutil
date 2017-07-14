package com.transsion.http.execute;

import com.transsion.core.pool.HttpPoolManager;
import com.transsion.core.pool.PoolManagerImpl;
import com.transsion.http.RequestCall;
import com.transsion.http.download.DownLoaderTask;
import com.transsion.http.impl.DownloadCallback;
import com.transsion.http.impl.IHttpCallback;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wenshuai.liu on 2017/7/6.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class DownLoaderExecute implements IRequestExecute {

    private PoolManagerImpl poolManager = HttpPoolManager.getInstance();
    private final Map<Object, DownLoaderTask> requestTaskMap = Collections.synchronizedMap(new WeakHashMap<Object, DownLoaderTask>());
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public void execute(RequestCall requestCall, IHttpCallback callback) {
        if (callback instanceof DownloadCallback) {
            DownLoaderTask downLoaderTask = new DownLoaderTask(requestCall, (DownloadCallback) callback);
            poolManager.addTask(downLoaderTask);
            if (downLoaderTask.getTag() != null) {
                requestTaskMap.put(downLoaderTask.getTag(), downLoaderTask);
            }
        }
    }

    @Override
    public void cancelByTag(Object tag) {
        if (tag == null) {
            return;
        }
        DownLoaderTask downLoaderTask = requestTaskMap.get(tag);
        if (downLoaderTask != null) {
            downLoaderTask.cancel();
        }
        requestTaskMap.remove(tag);
    }

    @Override
    public void cancelAll() {
        for (DownLoaderTask downLoaderTask : requestTaskMap.values()) {
            if (downLoaderTask != null) {
                downLoaderTask.cancel();
            }
        }
        requestTaskMap.clear();
    }

    public void continueByTag(Object tag) {
        if (tag == null) {
            return;
        }
        DownLoaderTask downLoaderTask = requestTaskMap.get(tag);
        if (downLoaderTask != null) {
            poolManager.addTask(downLoaderTask);
        }
    }

    public void pauseByTag(Object tag) {
        if (tag == null) {
            return;
        }
        DownLoaderTask downLoaderTask = requestTaskMap.get(tag);
        if (downLoaderTask != null) {
            downLoaderTask.pause();
        }
    }
}
