package com.matt.myutils.utils;

import android.content.Context;
import android.os.Environment;

/**
 * @author matt.Ljp
 * @time 2019/12/5 17:28
 * @description 获取手机存储路径工具类
 */
public class PathUtil {
    /**
     * 检查SD卡是否存在
     */
    private static boolean ExistSDCard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ||
                !Environment.isExternalStorageRemovable()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 如果外部存储可用获取"/storage/emulated/0/Android/data/你的包名/files/子目录"路径
     * 否则获取"data/data/你的包名/files"路径
     *
     * @param context
     * @param s       子目录名称
     * @return
     */
    public static String getFiles(Context context, String s) {
        if (ExistSDCard()) {
            return context.getExternalFilesDir(s).getPath();
        } else {
            return context.getFilesDir().getPath();
        }
    }
    /**
     * 如果外部存储可用获取"/storage/emulated/0/Android/data/你的包名/cache"路径
     * 否则获取"data/data/你的包名/cache"路径
     *
     * @param context
     * @return
     */
    public static String getCache(Context context) {
        if (ExistSDCard()) {
            return context.getExternalCacheDir().getPath();
        } else {
            return context.getCacheDir().getPath();
        }
    }
}
