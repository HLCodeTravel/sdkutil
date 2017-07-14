package com.transsion.http.execute;

import com.transsion.http.RequestCall;
import com.transsion.http.impl.IHttpCallback;

/**
 * Created by 孙鹏 on 2017/6/14
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 * <p>
 * 默认线程网络请求执行接口
 */

public interface IRequestExecute {

    /**
     * 执行请求
     *
     * @param requestCall
     * @param callback
     */
    void execute(RequestCall requestCall, IHttpCallback callback);

    void cancelByTag(Object tag);

    void cancelAll();

}
