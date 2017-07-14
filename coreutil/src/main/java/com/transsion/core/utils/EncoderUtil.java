package com.transsion.core.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author peng.sun
 * @data 2017/6/28
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class EncoderUtil {

    public static final String ALGORITHM_MD5 = "MD5";
    public static final String ALGORITHM_SHA_1 = "SHA-1";
    public static final String ALGORITHM_SHA_256 = "SHA-256";
    public static final String ALGORITHM__SHA_384 = "SHA-384";
    public static final String ALGORITHM_SHA_512 = "SHA-512";


    /**
     * 加密工具
     *
     * @param key 加密的字符串 默认MD5
     */
    public static String EncoderByAlgorithm(String key) {
        return EncoderByAlgorithm(key, ALGORITHM_MD5);
    }

    /**
     * 加密工具
     *
     * @param key       加密的字符串
     * @param algorithm 加密方式：MD5、SHA-256、SHA-1、SHA-512、SHA-384等
     */
    public static String EncoderByAlgorithm(String key, String algorithm) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance(algorithm);
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    /**
     * byte2String
     *
     * @param bytes
     * @return
     */
    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
