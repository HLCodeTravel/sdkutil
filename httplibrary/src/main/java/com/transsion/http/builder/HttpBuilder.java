package com.transsion.http.builder;

import android.text.TextUtils;

import com.transsion.http.HttpResponse;
import com.transsion.http.HttpURL;
import com.transsion.http.log.Console;
import com.transsion.http.util.ByteBufferUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by wenshuai.liu on 2017/5/12.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class HttpBuilder {
    public static final int GET_METHOD = 0;
    public static final int POST_METHOD = 1;

    public static HttpURLConnection buildConnect(String url,
                                                 int requestMethod,
                                                 int connectTimeout,
                                                 int readTimeOut,
                                                 boolean useHttpCache,
                                                 Map<String, String> headsMap) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new HttpURL(url).toURL().openConnection();
        conn.setUseCaches(useHttpCache);
        conn.setConnectTimeout(connectTimeout);
        conn.setReadTimeout(readTimeOut);
        if (headsMap != null) {
            for (String key : headsMap.keySet()) {
                conn.setRequestProperty(key, headsMap.get(key));
            }
        }
        switch (requestMethod) {
            case GET_METHOD:
                conn.setRequestMethod("GET");
                break;
            case POST_METHOD:
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                break;
            default:
                break;
        }
        return conn;
    }

    public static HttpResponse getHttpResponse(String url,
                                               int connectTimeout,
                                               int readTimeOut) throws IOException {
        HttpURLConnection connection = buildConnect(url, GET_METHOD, connectTimeout, readTimeOut, false, null);
        InputStream inputStream = connection.getInputStream();
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.statusCode = connection.getResponseCode();
        httpResponse.headers = connection.getHeaderFields();
        httpResponse.responseBody = ByteBufferUtil.toBytes(ByteBufferUtil.fromStream(inputStream));
        Console.log.d("get", "--------------------------------------------------------");
        Console.log.d("get", "code:" + httpResponse.statusCode);
        Console.log.d("get", "headers:" + httpResponse.headers);
        Console.log.d("get", "--------------------------------------------------------");
        connection.disconnect();
        inputStream.close();
        return httpResponse;
    }

    public static HttpResponse postHttpResponse(String url, String postData,
                                                int connectTimeout,
                                                int readTimeout,
                                                Map<String, String> headsMap,String contentType) throws IOException {
        if (headsMap == null) {
            headsMap = new LinkedHashMap<>();
        }
        headsMap.put("accept", "*/*");
        headsMap.put("connection", "Keep-Alive");
        headsMap.put("charset", "utf-8");
        if (!TextUtils.isEmpty(contentType)) {
            headsMap.put("Content-Type",contentType);
        }
        HttpURLConnection connection = buildConnect(url, POST_METHOD, connectTimeout, readTimeout, false, headsMap);
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(postData.getBytes());
        outputStream.flush();
        outputStream.close();
        InputStream is;
        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            is = connection.getErrorStream();
        } else {
            is = connection.getInputStream();
        }
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.statusCode = connection.getResponseCode();
        httpResponse.headers = connection.getHeaderFields();
        httpResponse.responseBody = ByteBufferUtil.toBytes(ByteBufferUtil.fromStream(is));
        Console.log.d("post", "--------------------------------------------------------");
        Console.log.d("post", "code:" + httpResponse.statusCode);
        Console.log.d("post", "headers:" + httpResponse.headers);
        Console.log.d("post", "body:" + new String(httpResponse.responseBody).toString());
        Console.log.d("post", "--------------------------------------------------------");
        connection.disconnect();
        is.close();
        return httpResponse;
    }
}
