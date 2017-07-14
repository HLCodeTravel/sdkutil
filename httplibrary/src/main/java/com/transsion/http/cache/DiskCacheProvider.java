package com.transsion.http.cache;

import android.content.Context;

import com.transsion.http.log.Console;

/**
 * Created by wenshuai.liu on 2017/5/27.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class DiskCacheProvider implements IDiskCache.Provider {

    private volatile IDiskCache diskCache;
    private final Context context;

    public DiskCacheProvider(Context context) {
        this.context = context;
    }

    @Override
    public IDiskCache getDiskCache() {
        if (diskCache == null) {
            synchronized (this) {
                if (diskCache == null) {
                    diskCache = new DiskLruCacheFactory(context).build();
                }
            }
        }
        return diskCache;
    }
}
