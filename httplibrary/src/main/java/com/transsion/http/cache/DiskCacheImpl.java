package com.transsion.http.cache;

import com.transsion.http.ImageURL;
import com.transsion.http.Urlkey;

import java.io.File;
import java.io.IOException;

/**
 * Created by wenshuai.liu on 2017/5/27.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class DiskCacheImpl implements IDiskCache {
    private static final String TAG = "DiskCache";
    private static final int APP_VERSION = 1;
    private static final int VALUE_COUNT = 1;

    private DiskLruCache diskLruCache;
    private final DiskCacheWriteLocker writerLocker = new DiskCacheWriteLocker();
    private final SafeKeyGenerator safeKeyGenerator = new SafeKeyGenerator();
    private final File directory;
    private final int maxSize;


    protected DiskCacheImpl(File directory, int maxSize) {
        this.directory = directory;
        this.maxSize = maxSize;
    }

    private synchronized DiskLruCache getDiskLruCache() throws IOException {
        if (diskLruCache == null) {
            diskLruCache = DiskLruCache.open(directory, APP_VERSION, VALUE_COUNT, maxSize);
        }
        return diskLruCache;
    }

    @Override
    public File get(Urlkey key) {
        return getImageFile(key);
    }

    protected File getImageFile(Urlkey key) {
        String safeKey = safeKeyGenerator.getSafeKey(key);
        File resultFile = null;
        final DiskLruCache.Value value;
        try {
            value = getDiskLruCache().get(safeKey);
            if (value != null) {
                resultFile = value.getFile(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultFile;
    }

    @Override
    public void put(Urlkey key, Save writer) {
        writerLocker.acquire(key);
        String safeKey = safeKeyGenerator.getSafeKey(key);
        try {
            final DiskLruCache.Value currentValue = getDiskLruCache().get(safeKey);
            if (currentValue != null) {
                return;
            }
            DiskLruCache.Editor editor = diskLruCache.edit(safeKey);
            if (editor == null) {
                throw new IllegalStateException("Had two simultaneous puts for: " + safeKey);
            }
            try {
                File file = editor.getFile(0);
                if (writer.write(file)) {
                    editor.commit();
                }
            } finally {
                editor.abortUnlessCommitted();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writerLocker.release(key);
        }
    }

    @Override
    public void delete(Urlkey key) {
        String safeKey = safeKeyGenerator.getSafeKey(key);
        try {
            getDiskLruCache().remove(safeKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clear() {
        try {
            getDiskLruCache().delete();
            resetDiskCache();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void resetDiskCache() {
        diskLruCache = null;
    }
}
