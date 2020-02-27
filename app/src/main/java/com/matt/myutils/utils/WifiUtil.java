package com.matt.myutils.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author matt.Ljp
 * @time 2019/11/21 10:09
 * @description wifi管理工具类
 */
public class WifiUtil {
    private static WifiUtil instance;
    private WifiManager mWifiManager;
    private Context mContext;

    private WifiUtil(Context context) {
        mContext = context;

        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    /**
     * 单一实例
     */
    public static WifiUtil getWifiAdmin(Context context) {
        if (instance == null) {
            instance = new WifiUtil(context);
        }
        return instance;
    }


    /**
     * 开启WiFi
     */
    public void openWifi() {
        if (!isOpenWifi()) {
            mWifiManager.setWifiEnabled(true);
        }
    }

    public boolean isOpenWifi() {
        return mWifiManager.isWifiEnabled();
    }


    /**
     * 关闭WiFi
     */
    public void closeWifi() {
        if (isOpenWifi()) {
            mWifiManager.setWifiEnabled(false);
        }
    }

    /**
     * 获取WIFI列表
     *
     * @return
     */
    public List<ScanResult> getWifiList() {
        if (mWifiManager != null) {
            List<ScanResult> olist = mWifiManager.getScanResults();
            if (olist != null) {
                List<ScanResult> nlist = new ArrayList<>();
                for (int i = 0; i < olist.size(); i++) {
                    if (!TextUtils.isEmpty(olist.get(i).SSID)) {
                        // 该热点SSID是否已在列表中
                        int position = getItemPosition(nlist, olist.get(i));
                        // 已在列表
                        if (position != -1) {
                            // 相同SSID热点，取信号强的
                            if (nlist.get(position).level < olist.get(i).level) {
                                nlist.remove(position);
                                nlist.add(position, olist.get(i));
                            }
                        } else {
                            nlist.add(olist.get(i));
                        }
                    }

                }
                return sortByLevel(nlist);
            }
        }
        return null;
    }

    /**
     * 返回item在list中的坐标
     */
    private int getItemPosition(List<ScanResult> list, ScanResult item) {
        for (int i = 0; i < list.size(); i++) {
            if (item.SSID.equals(list.get(i).SSID)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 将搜索到的wifi根据信号强度从强到弱进行排序
     *
     * @param resultList
     * @return
     */
    private List<ScanResult> sortByLevel(List<ScanResult> resultList) {
        for (int i = 0; i < resultList.size(); i++) {
            for (int j = 1; j < resultList.size(); j++) {
                //level属性即为强度
                if (resultList.get(i).level < resultList.get(j).level) {
                    ScanResult temp = null;
                    temp = resultList.get(i);
                    resultList.set(i, resultList.get(j));
                    resultList.set(j, temp);
                }
            }
        }
        return resultList;
    }

    /**
     * 获取当前连接的wifi名称
     *
     * @return
     */
    public String getWIFIName() {
        if (isWifi(mContext)) {
            WifiInfo info = mWifiManager.getConnectionInfo();
            return info != null ? info.getSSID().replace("\"", "") : null;
        } else {
            return "";
        }

    }

    /**
     * 判断Wifi是否连接
     *
     * @param context
     * @return
     */
    private boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }
}