package com.transsion.core.pool;

import java.lang.ref.WeakReference;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by 孙鹏 on 2017/6/14
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 * <p>
 * 默认线程池管理类
 */
public class TranssionPoolManager implements PoolManagerImpl {
    /**
     * 单例
     */
    public static volatile TranssionPoolManager mInstance;
    private ThreadPoolExecutor executor;

    /**
     * 单例创建对象
     *
     * @return
     */
    public static synchronized TranssionPoolManager getInstance() {
        if (mInstance == null) {
            synchronized (TranssionPoolManager.class) {
                if (mInstance == null) {
                    mInstance = new TranssionPoolManager();
                    mInstance.executor = TranssionPoolExecutor.newTranssionExecutor();
                }
            }
        }
        return mInstance;
    }

    /**
     * 增加新的任务 每增加一个新任务，都要唤醒任务队列
     *
     * @param newTask
     */
    public void addTask(Runnable newTask) {

        if (executor != null) {
            if (executor.isShutdown()) {
                executor.prestartAllCoreThreads();
            }
            executor.execute(newTask);
        }
    }

    /**
     * 销毁线程池
     */
    public void destroy() {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
            executor = null;
        }
    }

    /**
     * 净化
     */
    public void purge() {
        if (executor != null) {
            executor.purge();
        }
    }

    /**
     * shutdown
     */
    public void shutdown() {
        if (executor != null) {
            executor.shutdown();
        }
    }

    /**
     * 弱引用执行任务
     *
     * @param request
     */
    public void execute(WeakReference<Runnable> request) {
        Runnable runnable = request.get();
        if (runnable != null) {
            this.addTask(runnable);
        }
    }

}
