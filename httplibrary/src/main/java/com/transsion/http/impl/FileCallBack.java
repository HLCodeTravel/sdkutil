package com.transsion.http.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by wenshuai.liu on 2017/5/19.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public abstract class FileCallBack extends HttpCallbackImpl {
    /**
     * 目标文件存储的文件夹路径
     */
    private String destFileDir;
    /**
     * 目标文件存储的文件名
     */
    private String destFileName;


    public FileCallBack(String destFileDir, String destFileName) {
        this.destFileDir = destFileDir;
        this.destFileName = destFileName;
    }


    public abstract void onFailure(int statusCode, File responseString, Throwable throwable);

    public abstract void onSuccess(int statusCode, File responseFile);

    @Override
    public void onSuccess(final int statusCode, final byte[] responseBody) {
        Runnable parser = new Runnable() {
            @Override
            public void run() {
                try {
                    final File file = saveFile(responseBody);
                    postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            onSuccess(statusCode, file);
                        }
                    });
                } catch (IOException e) {
                    onFailure(statusCode, (File) null, e.getCause());
                }

            }
        };
        if (!getUseSyncMode() && !getUsePoolThread()) {
            new Thread(parser).start();
        } else {
            parser.run();
        }
    }

    @Override
    public void onFailure(final int statusCode, byte[] responseBody, final Throwable error) {
        try {
            onFailure(statusCode, saveFile(responseBody), error);
        } catch (IOException e) {
            e.printStackTrace();
        }
        postRunnable(new Runnable() {
            @Override
            public void run() {
                onFailure(statusCode, (File) null, error);
            }
        });
    }


    public File saveFile(byte[] responseBody) throws IOException {
        BufferedOutputStream is = null;
        FileOutputStream fos = null;
        try {
            File dir = new File(destFileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, destFileName);
            fos = new FileOutputStream(file);
            is = new BufferedOutputStream(fos);
            is.write(responseBody);
            fos.flush();
            return file;
        } finally {
            if (is != null) is.close();
            if (fos != null) fos.close();
        }
    }
}
