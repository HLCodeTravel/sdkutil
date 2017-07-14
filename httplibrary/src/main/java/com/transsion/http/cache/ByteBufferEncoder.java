package com.transsion.http.cache;

import com.transsion.http.util.ByteBufferUtil;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by wenshuai.liu on 2017/5/31.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class ByteBufferEncoder implements Encoder<ByteBuffer> {
    @Override
    public boolean encode(ByteBuffer byteBuffer, File file,long contentLen) {
        boolean success;
        try {
            ByteBufferUtil.toFile(byteBuffer, file);
            success = true;
        } catch (IOException e) {
            success = false;
        }
        return success;
    }
}
