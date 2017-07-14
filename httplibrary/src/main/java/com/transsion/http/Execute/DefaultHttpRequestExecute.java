package com.transsion.http.execute;

import com.transsion.core.pool.HttpPoolManager;
import com.transsion.core.pool.PoolManagerImpl;
import com.transsion.http.HttpRequestTask;
import com.transsion.http.RequestCall;
import com.transsion.http.impl.IHttpCallback;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 孙鹏 on 2017/6/14
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 * <p>
 * 默认线程网络请求执行类
 */

public class DefaultHttpRequestExecute implements IRequestExecute {

    private String TAG = DefaultHttpRequestExecute.class.getSimpleName();
    private PoolManagerImpl poolManager = HttpPoolManager.getInstance();
    private List<HttpRequestTask> requestTaskList = Collections.synchronizedList(new LinkedList<HttpRequestTask>());

    @Override
    public void execute(RequestCall requestCall, IHttpCallback callback) {
        //装饰成runnable
        HttpRequestTask httpRequestRunnable = new HttpRequestTask(requestCall, callback); // TODO: 2017/6/14 cancel的逻辑在这里加入
        if (requestCall.getRequest().getTag() != null) {
            requestTaskList.add(httpRequestRunnable);
        }
        //放在线程池中执行网络请求
        poolManager.addTask(httpRequestRunnable);

    }

    @Override
    public void cancelByTag(Object tag) {
        if (tag == null) {
            return;
        }
        for (HttpRequestTask requestTask : requestTaskList) {
            if (tag.equals(requestTask.getTag())) {
                requestTask.cancel();
                requestTaskList.remove(requestTask);
            }
        }
    }

    @Override
    public void cancelAll() {
        for (HttpRequestTask requestTask : requestTaskList) {
            requestTask.cancel();
            requestTaskList.remove(requestTask);
        }
    }


}
