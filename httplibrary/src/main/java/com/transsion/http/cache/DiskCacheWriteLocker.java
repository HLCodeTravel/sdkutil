package com.transsion.http.cache;

import com.transsion.http.Urlkey;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by wenshuai.liu on 2017/5/27.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public final class DiskCacheWriteLocker {
    private final Map<Urlkey, WriteLock> locks = new HashMap<>();
    private final WriteLockPool writeLockPool = new WriteLockPool();

    public void acquire(Urlkey key) {
        WriteLock writeLock;
        synchronized (this) {
            writeLock = locks.get(key);
            if (writeLock == null) {
                writeLock = writeLockPool.obtain();
                locks.put(key, writeLock);
            }
            writeLock.interestedThreads++;
        }

        writeLock.lock.lock();
    }

    public void release(Urlkey key) {
        WriteLock writeLock;
        synchronized (this) {
            writeLock = locks.get(key);
            if (writeLock.interestedThreads < 1) {
                throw new IllegalStateException("Cannot release a lock that is not held"
                        + ", key: " + key
                        + ", interestedThreads: " + writeLock.interestedThreads);
            }

            writeLock.interestedThreads--;
            if (writeLock.interestedThreads == 0) {
                WriteLock removed = locks.remove(key);
                if (!removed.equals(writeLock)) {
                    throw new IllegalStateException("Removed the wrong lock"
                            + ", expected to remove: " + writeLock
                            + ", but actually removed: " + removed
                            + ", key: " + key);
                }
                writeLockPool.offer(removed);
            }
        }

        writeLock.lock.unlock();
    }

    private static class WriteLock {
        final Lock lock = new ReentrantLock();
        int interestedThreads;
    }

    private static class WriteLockPool {
        private static final int MAX_POOL_SIZE = 10;
        private final Queue<WriteLock> pool = new ArrayDeque<>();

        WriteLock obtain() {
            WriteLock result;
            synchronized (pool) {
                result = pool.poll();
            }
            if (result == null) {
                result = new WriteLock();
            }
            return result;
        }

        void offer(WriteLock writeLock) {
            synchronized (pool) {
                if (pool.size() < MAX_POOL_SIZE) {
                    pool.offer(writeLock);
                }
            }
        }
    }
}