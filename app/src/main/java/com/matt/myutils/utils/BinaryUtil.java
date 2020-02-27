package com.matt.myutils.utils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author matt.Ljp
 * @time 2020/2/27 15:27
 * @description 读取Bin二进制文件工具类
 */
public class BinaryUtil {
    /**
     * 读取Bin文件
     *
     * @param path
     * @param bytes
     * @return
     */
    public static int getBin(String path, byte[] bytes) {
        DataInputStream dataInputStream;
        BufferedInputStream bufferedInputStream = null;
        try {
            int length;
            dataInputStream = new DataInputStream(new FileInputStream(new File(path)));
            bufferedInputStream = new BufferedInputStream(dataInputStream);
            length = bufferedInputStream.read(bytes);
            return length;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * 获取Bin文件大小
     *
     * @param path
     * @return
     */
    public static long getBinSize(String path) {
        File f = new File(path);
        if (f.exists() && f.isFile()) {
            return f.length();
        } else {
            return 0;
        }
    }

}
