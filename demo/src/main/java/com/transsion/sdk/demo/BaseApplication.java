package com.transsion.sdk.demo;

import android.app.Application;

import com.transsion.core.CoreUtil;


/**
 * usage
 *
 * @author 王纪清
 * @data 2017/6/7
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CoreUtil.init(getApplicationContext());
    }
}
