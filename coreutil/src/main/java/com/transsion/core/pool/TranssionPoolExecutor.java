package com.transsion.core.pool;

import android.os.StrictMode;
import android.support.annotation.NonNull;

import com.transsion.core.log.LogUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * Created by wenshuai.liu on 2017/5/31.
 * function:
 * 线程池处理，网络请求和缓存（from glide）
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public final class TranssionPoolExecutor extends ThreadPoolExecutor {

    public static final String DEFAULT_SOURCE_EXECUTOR_NAME = "source";
    public static final String TRANSSION_EXECUTOR_NAME = "transsion";
    public static final String DEFAULT_DISK_CACHE_EXECUTOR_NAME = "disk-cache";
    public static final int DEFAULT_DISK_CACHE_EXECUTOR_THREADS = 1;

    private static final String TAG = "TranssionPoolExecutor";
    private static final String CPU_NAME_REGEX = "cpu[0-9]+";
    private static final String CPU_LOCATION = "/sys/devices/system/cpu/";
    // Don't use more than four threads when automatically determining thread count..
    private static final int MAXIMUM_AUTOMATIC_THREAD_COUNT = 4;
    private final boolean executeSynchronously;

    public static TranssionPoolExecutor newDiskCacheExecutor() {
        return newDiskCacheExecutor(DEFAULT_DISK_CACHE_EXECUTOR_THREADS,
                DEFAULT_DISK_CACHE_EXECUTOR_NAME, UncaughtThrowableStrategy.DEFAULT);
    }


    public static TranssionPoolExecutor newDiskCacheExecutor(int threadCount, String name,
                                                             UncaughtThrowableStrategy uncaughtThrowableStrategy) {
        return new TranssionPoolExecutor(threadCount, name, uncaughtThrowableStrategy,
                true /*preventNetworkOperations*/, false /*executeSynchronously*/);
    }

    public static TranssionPoolExecutor newSourceExecutor() {
        return newSourceExecutor(calculateBestThreadCount(), DEFAULT_SOURCE_EXECUTOR_NAME,
                UncaughtThrowableStrategy.DEFAULT);
    }

    public static TranssionPoolExecutor newSourceExecutor(int threadCount, String name,
                                                          UncaughtThrowableStrategy uncaughtThrowableStrategy) {
        return new TranssionPoolExecutor(threadCount, name, uncaughtThrowableStrategy,
                false /*preventNetworkOperations*/, false /*executeSynchronously*/);
    }

    public static TranssionPoolExecutor newTranssionExecutor() {
        return new TranssionPoolExecutor(TRANSSION_EXECUTOR_NAME, UncaughtThrowableStrategy.DEFAULT, false, false);
    }

    // Visible for testing.
    public TranssionPoolExecutor(int poolSize, String name,
                                 UncaughtThrowableStrategy uncaughtThrowableStrategy, boolean preventNetworkOperations,
                                 boolean executeSynchronously) {
        super(
                poolSize /*corePoolSize*/,
                poolSize /*maximumPoolSize*/,
                0 /*keepAliveTime*/,
                TimeUnit.MILLISECONDS,
                new PriorityBlockingQueue<Runnable>(),
                new DefaultThreadFactory(name, uncaughtThrowableStrategy, preventNetworkOperations));
        this.executeSynchronously = executeSynchronously;
    }

    public TranssionPoolExecutor(String name,
                                 UncaughtThrowableStrategy uncaughtThrowableStrategy, boolean preventNetworkOperations,
                                 boolean executeSynchronously) {
        super(
                0 /*corePoolSize*/,
                Integer.MAX_VALUE /*maximumPoolSize*/,
                60L /*keepAliveTime*/,
                TimeUnit.MILLISECONDS,
                new SynchronousQueue<Runnable>(),
                new DefaultThreadFactory(name, uncaughtThrowableStrategy, preventNetworkOperations));
        this.executeSynchronously = executeSynchronously;
    }

    @Override
    public void execute(Runnable command) {
        if (executeSynchronously) {
            command.run();
        } else {
            super.execute(command);
        }
    }

    @NonNull
    @Override
    public Future<?> submit(Runnable task) {
        return maybeWait(super.submit(task));
    }

    private <T> Future<T> maybeWait(Future<T> future) {
        if (executeSynchronously) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        return future;
    }

    @NonNull
    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return maybeWait(super.submit(task, result));
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return maybeWait(super.submit(task));
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
        return new ComparableFutureTask<T>(runnable, value);
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        return new ComparableFutureTask<T>(callable);
    }


    protected class ComparableFutureTask<V>
            extends FutureTask<V> implements Comparable<ComparableFutureTask<V>> {
        private Object object;

        public ComparableFutureTask(Callable<V> callable) {
            super(callable);
            object = callable;
        }

        public ComparableFutureTask(Runnable runnable, V result) {
            super(runnable, result);
            object = runnable;
        }

        @Override
        @SuppressWarnings("unchecked")
        public int compareTo(ComparableFutureTask<V> o) {
            if (this == o) {
                return 0;
            }
            if (o == null) {
                return -1; // high priority
            }
            if (object != null && o.object != null) {
                if (object.getClass().equals(o.object.getClass())) {
                    if (object instanceof Comparable) {
                        return ((Comparable) object).compareTo(o.object);
                    }
                }
            }
            return 0;
        }
    }

    public static int calculateBestThreadCount() {
        File[] cpus = null;
        try {
            File cpuInfo = new File(CPU_LOCATION);
            final Pattern cpuNamePattern = Pattern.compile(CPU_NAME_REGEX);
            cpus = cpuInfo.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File file, String s) {
                    return cpuNamePattern.matcher(s).matches();
                }
            });
        } catch (Throwable t) {
            LogUtils.d(TAG, "Failed to calculate accurate cpu count", t);
        }

        int cpuCount = cpus != null ? cpus.length : 0;
        int availableProcessors = Math.max(1, Runtime.getRuntime().availableProcessors());
        return Math.min(MAXIMUM_AUTOMATIC_THREAD_COUNT, Math.max(availableProcessors, cpuCount));
    }

    public enum UncaughtThrowableStrategy {
        /**
         * Silently catches and ignores the uncaught {@link Throwable}s.
         */
        IGNORE,
        /**
         * Logs the uncaught {@link Throwable}s using {@link #TAG} and {@link android.util.Log}.
         */
        LOG {
            @Override
            protected void handle(Throwable t) {
                LogUtils.d(TAG, "Request threw uncaught throwable", t);
            }
        },
        /**
         * Rethrows the uncaught {@link Throwable}s to crash the app.
         */
        THROW {
            @Override
            protected void handle(Throwable t) {
                super.handle(t);
                if (t != null) {
                    throw new RuntimeException("Request threw uncaught throwable", t);
                }
            }
        };
        public static final UncaughtThrowableStrategy DEFAULT = LOG;

        protected void handle(Throwable t) {
            // Ignore.
        }
    }

    private static final class DefaultThreadFactory implements ThreadFactory {
        private final String name;
        private final UncaughtThrowableStrategy uncaughtThrowableStrategy;
        private final boolean preventNetworkOperations;
        private int threadNum;

        DefaultThreadFactory(String name, UncaughtThrowableStrategy uncaughtThrowableStrategy,
                             boolean preventNetworkOperations) {
            this.name = name;
            this.uncaughtThrowableStrategy = uncaughtThrowableStrategy;
            this.preventNetworkOperations = preventNetworkOperations;
        }

        @Override
        public synchronized Thread newThread(@NonNull Runnable runnable) {
            final Thread result = new Thread(runnable, "transsion-" + name + "-thread-" + threadNum) {
                @Override
                public void run() {
                    android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                    if (preventNetworkOperations) {
                        StrictMode.setThreadPolicy(
                                new StrictMode.ThreadPolicy.Builder()
                                        .detectNetwork()
                                        .penaltyDeath()
                                        .build());
                    }
                    try {
                        super.run();
                    } catch (Throwable t) {
                        uncaughtThrowableStrategy.handle(t);
                    }
                }
            };
            threadNum++;
            return result;
        }
    }
}
