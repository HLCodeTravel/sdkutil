package com.transsion.http.cache;

/**
 * Created by wenshuai.liu on 2017/7/10.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class DownloadCacheProvider implements IDiskCache.Provider {
    private volatile IDiskCache diskCache;
    private final String pathname;

    public DownloadCacheProvider(String pathname) {
        this.pathname = pathname;
    }

    @Override
    public IDiskCache getDiskCache() {
        if (diskCache == null) {
            synchronized (this) {
                if (diskCache == null) {
                    diskCache = new DiskLruCacheFactory(pathname).build();
                }
            }
        }
        return diskCache;
    }
}
