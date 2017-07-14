package com.transsion.http.cache;

import com.transsion.http.ImageURL;
import com.transsion.http.Urlkey;

import java.io.File;

/**
 * Created by wenshuai.liu on 2017/5/27.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public interface IDiskCache {
    interface Factory {
        int DEFAULT_DISK_CACHE_SIZE = 300 * 1024 * 1024;///default image cache size
        int DOWNLOAD_DISK_CACHE_SIZE = 1024 * 1024 * 1024;///download size
        String DEFAULT_DISK_CACHE_DIR = "images";

        IDiskCache build();
    }

    interface Save {
        boolean write(File file);
    }

    interface Provider {
        IDiskCache getDiskCache();
    }

    File get(Urlkey key);

    void put(Urlkey key, Save writer);

    void delete(Urlkey key);

    void clear();
}
