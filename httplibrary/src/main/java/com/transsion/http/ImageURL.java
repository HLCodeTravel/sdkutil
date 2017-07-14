package com.transsion.http;

import android.net.Uri;
import android.text.TextUtils;

import com.transsion.http.util.MD5;

import java.security.MessageDigest;

/**
 * Created by wenshuai.liu on 2017/5/27.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class ImageURL implements Urlkey {
    private static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
    private volatile byte[] cacheKeyBytes;
    private final String safeStringUrl;

    public ImageURL(String url) {
        if (!TextUtils.isEmpty(url)) {
            safeStringUrl = Uri.encode(url, ALLOWED_URI_CHARS);
        } else {
            throw new NullPointerException("url must be can not null or empty");
        }
    }

    public String getKey() {
        return toMD5();
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(getCacheKeyBytes());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ImageURL) {
            ImageURL other = (ImageURL) obj;
            return getKey().equals(other.getKey());
        }
        return false;
    }

    public String toMD5() {
        return MD5.MD5Encode(safeStringUrl);
    }

    private byte[] getCacheKeyBytes() {
        if (cacheKeyBytes == null) {
            cacheKeyBytes = safeStringUrl.getBytes(CHARSET);
        }
        return cacheKeyBytes;
    }

    @Override
    public int hashCode() {
        int hashCode = safeStringUrl.hashCode();
        hashCode = 31 * hashCode;
        return hashCode;
    }
}
