package com.transsion.http.cache;

import android.support.v4.util.Pools;
import android.util.LruCache;

import com.transsion.http.Urlkey;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by wenshuai.liu on 2017/5/27.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class SafeKeyGenerator {
    private static final String HASH_ALGORITHM = "SHA-256";
    private final LruCache<Urlkey, String> loadIdToSafeHash = new LruCache<>(1000);
    private final Pools.Pool<PoolableDigestContainer> digestPool = FactoryPools.threadSafe(10,
            new FactoryPools.Factory<PoolableDigestContainer>() {
                @Override
                public PoolableDigestContainer create() {
                    try {
                        return new PoolableDigestContainer(MessageDigest.getInstance(HASH_ALGORITHM));
                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

    public String getSafeKey(Urlkey key) {
        String safeKey;
        synchronized (loadIdToSafeHash) {
            safeKey = loadIdToSafeHash.get(key);
        }
        if (safeKey == null) {
            safeKey = calculateHexStringDigest(key);
        }
        synchronized (loadIdToSafeHash) {
            loadIdToSafeHash.put(key, safeKey);
        }
        return safeKey;
    }

    private String calculateHexStringDigest(Urlkey key) {
        PoolableDigestContainer container = digestPool.acquire();
        try {
            key.updateDiskCacheKey(container.messageDigest);
            return DiskCacheUtil.sha256BytesToHex(container.messageDigest.digest());
        } finally {
            digestPool.release(container);
        }
    }

    private static final class PoolableDigestContainer implements FactoryPools.Poolable {

        private final MessageDigest messageDigest;
        private final StateVerifier stateVerifier = StateVerifier.newInstance();

        PoolableDigestContainer(MessageDigest messageDigest) {
            this.messageDigest = messageDigest;
        }

        @Override
        public StateVerifier getVerifier() {
            return stateVerifier;
        }
    }
}