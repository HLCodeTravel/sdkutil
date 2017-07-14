package com.transsion.http.cache;

import java.io.File;

/**
 * Created by wenshuai.liu on 2017/7/10.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class DownloadSave<DataType> implements IDiskCache.Save {
    private final DataType dataType;
    private final Encoder<DataType> encoder;
    private final long contentLen;

    public DownloadSave(Encoder<DataType> encoder, DataType dataType, long conetentLen) {
        this.dataType = dataType;
        this.encoder = encoder;
        this.contentLen = conetentLen;
    }

    @Override
    public boolean write(File file) {
        return encoder.encode(dataType, file, contentLen);
    }
}
