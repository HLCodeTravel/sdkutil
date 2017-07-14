package com.transsion.http;

import com.transsion.http.cache.DiskCacheProvider;
import com.transsion.http.cache.IDiskCache;
import com.transsion.http.execute.DefaultHttpRequestExecute;
import com.transsion.http.execute.IRequestExecute;
import com.transsion.http.impl.IHttpCallback;
import com.transsion.http.request.HttpRequest;
import com.transsion.http.request.Request;
import com.transsion.http.request.UriRequest;

/**
 * Created by wenshuai.liu on 2017/5/8.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class RequestCall {

    //private final HttpRequest httpRequest;
    private IRequestExecute requestExecute;
    private IDiskCache diskCache;
    private final Request request;


    public RequestCall(HttpRequest httpRequest) {
        //this.httpRequest = httpRequest;
        this.request = httpRequest.getRequest();
        this.requestExecute = new DefaultHttpRequestExecute();
    }

    public IDiskCache getDiskCache() {
        if (request.getContext() != null) {
            diskCache = new DiskCacheProvider(request.getContext()).getDiskCache();
        }
        return diskCache;
    }

    public UriRequest getUriRequest() {
        return new UriRequest(request);
    }

    public Request getRequest() {
        return request;
    }

    public void execute(IHttpCallback callback) {
        requestExecute.execute(this, callback);
    }

    public void execute() {
        execute(null);
    }
}
