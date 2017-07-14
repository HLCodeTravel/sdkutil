package com.transsion.http;

import java.nio.charset.Charset;
import java.security.MessageDigest;

/**
 * Created by wenshuai.liu on 2017/6/1.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public interface Urlkey {
    String STRING_CHARSET_NAME = "UTF-8";
    Charset CHARSET = Charset.forName(STRING_CHARSET_NAME);

    void updateDiskCacheKey(MessageDigest messageDigest);

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();
}
