package com.transsion.http.request;

import android.os.Build;

import com.transsion.http.HttpURL;
import com.transsion.http.builder.HttpBuilder;
import com.transsion.http.util.IOUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

/**
 * Created by wenshuai.liu on 2017/6/30.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class UriRequest implements Cloneable {
    protected final Request request;
    private HttpURLConnection connection = null;
    private InputStream inputStream = null;
    private int responseCode = 0;

    public UriRequest(Request request) {
        this.request = request;
    }

    public Request getRequest() {
        return request;
    }

    public InputStream getInputStream() throws IOException {
        if (connection != null && inputStream == null) {
            inputStream = connection.getResponseCode() >= 400 ?
                    connection.getErrorStream() : connection.getInputStream();
        }
        return inputStream;
    }

    public void setRequest() throws IOException {
        connection = (HttpURLConnection) new HttpURL(request.getUrl()).toURL().openConnection();
        connection.setReadTimeout(request.getReadTimeout());
        connection.setConnectTimeout(request.getConnectTimeout());
        if (request.getHeaders() != null) {
            for (String key : request.getHeaders().keySet()) {
                connection.setRequestProperty(key, request.getHeaders().get(key));
            }
        }
        HttpMethod method = request.getMethod();
        connection.setRequestMethod(method.toString());
        if (HttpMethod.permitsRequestBody(method)) {
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Content-Type", request.getContentType().toString());
            long contentLength = request.getContent().getBytes().length;
            if (contentLength < 0) {
                connection.setChunkedStreamingMode(256 * 1024);
            } else {
                if (contentLength < Integer.MAX_VALUE) {
                    connection.setFixedLengthStreamingMode((int) contentLength);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    connection.setFixedLengthStreamingMode(contentLength);
                } else {
                    connection.setChunkedStreamingMode(256 * 1024);
                }
            }
            connection.setRequestProperty("Content-Length", String.valueOf(contentLength));
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(request.getContent().getBytes());
            outputStream.flush();
            outputStream.close();
        }
        responseCode = connection.getResponseCode();
    }

    public long getContentLength() {
        long result = 0;
        if (connection != null) {
            try {
                result = connection.getContentLength();
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
            if (result < 1) {
                try {
                    result = this.getInputStream().available();
                } catch (Throwable ignored) {
                }
            }
        } else {
            try {
                result = this.getInputStream().available();
            } catch (Throwable ignored) {
            }
        }
        return result;
    }

    public void close() {
        if (inputStream != null) {
            IOUtil.closeQuietly(inputStream);
            inputStream = null;
        }
        if (connection != null) {
            connection.disconnect();
        }
    }

    public Map<String, List<String>> getResponseHeaders() {
        if (connection == null) return null;
        return connection.getHeaderFields();
    }


    public int getResponseCode() {
        if (connection != null) {
            return responseCode;
        }
        return 404;
    }

    public long getLastModified() {
        return getHeaderFieldDate("Last-Modified", System.currentTimeMillis());
    }

    public long getHeaderFieldDate(String name, long defaultValue) {
        if (connection == null) return defaultValue;
        return connection.getHeaderFieldDate(name, defaultValue);
    }

    public String getResponseMessage() throws IOException {
        if (connection != null) {
            return URLDecoder.decode(connection.getResponseMessage(), "UTF-8");
        } else {
            return null;
        }
    }

    public String getETag() {
        if (connection == null) return null;
        return connection.getHeaderField("ETag");
    }

    public String getResponseHeader(String name) {
        if (connection == null) return null;
        return connection.getHeaderField(name);
    }

}
