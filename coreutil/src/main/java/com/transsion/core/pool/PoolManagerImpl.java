package com.transsion.core.pool;

import java.lang.ref.WeakReference;

/**
 * Created by 孙鹏 on 2017/6/14
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 * <p>
 * 线程池管理接口
 */
public interface PoolManagerImpl {
    /**
     * 增加新的任务 每增加一个新任务，都要唤醒任务队列
     *
     * @param newTask
     */
    void addTask(Runnable newTask);

    /**
     * 净化
     */
    void purge();

    /**
     * shutdown
     */
    void shutdown();

    /**
     * 销毁线程池
     */
    void destroy();

    /**
     * 弱引用执行任务
     *
     * @param request
     */
    void execute(WeakReference<Runnable> request);
}
