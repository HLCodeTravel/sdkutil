package com.transsion.sdk.demo.bean;

import com.transsion.json.annotations.TserializedName;

/**
 * Created by wenshuai.liu on 2017/5/11.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class AdBean {
    @TserializedName(name = "showTime")
    public int showTime;
    @TserializedName(name = "requestTimeOut")
    public int requestTimeout;

    public void setShowTime(int time) {
        this.showTime = time;
    }
    public void setRequestTimeout(int time) {
        this.requestTimeout = time;
    }
}
