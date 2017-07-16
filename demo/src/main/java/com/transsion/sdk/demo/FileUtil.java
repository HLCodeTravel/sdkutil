package com.transsion.sdk.demo;

/* Top Secret */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.GZIPOutputStream;

/**
 * usage
 *
 * @author 周粤琦
 * @date 2017/6/22
 * ==================================
 * Copyright (c) 2017 TRANSSION.Co.Ltd.
 * All rights reserved
 */

public class FileUtil {
    public static int getFileSize(String path) {
        int size = 0;

        try {
            FileInputStream fin = new FileInputStream(path);
            size = fin.available();
            fin.close();

        } catch (Exception e) {
        }

        return size;
    }

    public static boolean gzip(String src, String dest) {

        try {
            File file = new File(src);
            File zipFile = new File(dest);
            InputStream input = new FileInputStream(file);
            GZIPOutputStream zipOut = null;
            zipOut = new GZIPOutputStream(new FileOutputStream(zipFile));

            int temp = 0;
            while ((temp = input.read()) != -1) {
                zipOut.write(temp);
            }
            input.close();
            zipOut.finish();
            zipOut.flush();
            zipOut.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isFileExist(String path) {
        File file = new File(path);
        if (file.exists())
            return true;
        return false;
    }
}
