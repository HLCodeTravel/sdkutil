package com.transsion.http.cache;

import java.io.File;

/**
 * Created by wenshuai.liu on 2017/5/31.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class SaveImpl<DataType> implements IDiskCache.Save {
    private final DataType dataType;
    private final Encoder<DataType> encoder;

    public SaveImpl(Encoder<DataType> encoder, DataType dataType) {
        this.dataType = dataType;
        this.encoder = encoder;
    }

    @Override
    public boolean write(File file) {
        return encoder.encode(dataType, file,0);
    }
}
