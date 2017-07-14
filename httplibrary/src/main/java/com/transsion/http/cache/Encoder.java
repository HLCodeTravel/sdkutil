package com.transsion.http.cache;

import java.io.File;

/**
 * Created by wenshuai.liu on 2017/5/31.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public interface Encoder<DataType> {
    boolean encode(DataType dataType, File file,long contentLen);
}
