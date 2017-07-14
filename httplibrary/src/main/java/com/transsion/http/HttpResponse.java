package com.transsion.http;

import java.util.List;
import java.util.Map;

/**
 * Created by wenshuai.liu on 2017/5/8.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class HttpResponse {
    public int statusCode;
    public byte[] responseBody;
    public Map<String, List<String>> headers;
}
