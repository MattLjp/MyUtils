package com.matt.myutils.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

/**
 * @author matt.Ljp
 * @time 2019/11/21 10:09
 * @description APP版本管理类
 */
public class VersionUtil {
    /**
     * 获取版本名称
     *
     * @param context 上下文
     * @return 版本名称
     */
    public static String getVersionName(Context context) {

        //获取包管理器
        PackageManager pm = context.getPackageManager();
        //获取包信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            //返回版本号
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 获取版本号
     *
     * @param context 上下文
     * @return 版本号
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    public static long getVersionCode(Context context) {

        //获取包管理器
        PackageManager pm = context.getPackageManager();
        //获取包信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            //返回版本号
            return packageInfo.getLongVersionCode();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return 0;

    }

    /**
     * 获取App的名称
     *
     * @param context 上下文
     * @return 名称
     */
    public static String getAppName(Context context) {
        PackageManager pm = context.getPackageManager();
        //获取包信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            //获取应用 信息
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            //获取albelRes
            int labelRes = applicationInfo.labelRes;
            //返回App的名称
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 判断是否需要升级
     *
     * @param oldVersion 当前手机APP版本
     * @param newVersion 最新APP版本
     * @return
     */
    public static boolean isUpdata(String oldVersion, String newVersion) {
        String[] lArr = oldVersion.split("\\.");
        String[] sArr = newVersion.split("\\.");
        for (int i = 0; i < sArr.length; i++) {
            Integer lI = Integer.valueOf(lArr[i]);
            Integer sI = Integer.valueOf(sArr[i]);
            if (sI > lI) {
                return true;
            }
        }
        return false;
    }
}
