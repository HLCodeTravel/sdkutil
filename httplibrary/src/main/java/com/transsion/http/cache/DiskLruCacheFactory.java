package com.transsion.http.cache;

import android.content.Context;

import com.transsion.http.util.StorageUtils;

import java.io.File;

/**
 * Created by wenshuai.liu on 2017/5/27.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class DiskLruCacheFactory implements IDiskCache.Factory {

    private final int diskCacheSize;
    private final CacheDirectoryGetter cacheDirectoryGetter;


    public interface CacheDirectoryGetter {
        File getCacheDirectory();
    }

    public DiskLruCacheFactory(final Context context) {
        this(new CacheDirectoryGetter() {
            @Override
            public File getCacheDirectory() {
                return StorageUtils.getCacheDirectory(context);
            }
        }, IDiskCache.Factory.DEFAULT_DISK_CACHE_SIZE);
    }

    public DiskLruCacheFactory(final String diskCacheFolder, int diskCacheSize) {
        this(new CacheDirectoryGetter() {
            @Override
            public File getCacheDirectory() {
                return new File(diskCacheFolder);
            }
        }, diskCacheSize);
    }

    public DiskLruCacheFactory(final String diskCacheFolder, final String diskCacheName,
                               int diskCacheSize) {
        this(new CacheDirectoryGetter() {
            @Override
            public File getCacheDirectory() {
                return new File(diskCacheFolder, diskCacheName);
            }
        }, diskCacheSize);
    }

    public DiskLruCacheFactory(final String pathname) {
        this(new CacheDirectoryGetter() {
            @Override
            public File getCacheDirectory() {
                return new File(pathname);
            }
        }, IDiskCache.Factory.DOWNLOAD_DISK_CACHE_SIZE);
    }

    public DiskLruCacheFactory(CacheDirectoryGetter cacheDirectoryGetter, int diskCacheSize) {
        this.diskCacheSize = diskCacheSize;
        this.cacheDirectoryGetter = cacheDirectoryGetter;
    }

    @Override
    public IDiskCache build() {
        File cacheDir = cacheDirectoryGetter.getCacheDirectory();
        if (cacheDir == null) {
            return null;
        }
        if (!cacheDir.mkdirs() && (!cacheDir.exists() || !cacheDir.isDirectory())) {
            return null;
        }
        return new DiskCacheImpl(cacheDir, diskCacheSize);
    }
}
