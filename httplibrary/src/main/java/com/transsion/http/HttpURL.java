package com.transsion.http;

import android.net.Uri;
import android.text.TextUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;

/**
 * Created by wenshuai.liu on 2017/5/9.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class HttpURL implements Urlkey {
    private static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
    private final String safeStringUrl;
    private URL safeURL;
    private volatile byte[] cacheKeyBytes;

    public HttpURL(String url) {
        if (!TextUtils.isEmpty(url)) {
            safeStringUrl = Uri.encode(url, ALLOWED_URI_CHARS);
        } else {
            throw new NullPointerException("url must be can not null or empty");
        }
    }

    public URL toURL() throws MalformedURLException {
        return getSafeUrl();
    }

    private URL getSafeUrl() throws MalformedURLException {
        if (safeURL == null) {
            safeURL = new URL(safeStringUrl);
        }
        return safeURL;
    }

    @Override
    public String toString() {
        return safeStringUrl;
    }

    private byte[] getCacheKeyBytes() {
        if (cacheKeyBytes == null) {
            cacheKeyBytes = safeStringUrl.getBytes(CHARSET);
        }
        return cacheKeyBytes;
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(getCacheKeyBytes());
    }
}
