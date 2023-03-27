package com.filter.scene.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author : kevinbai
 * date      : 2021/3/29 下午1:32
 */
public class NetworkUtil {
    /**
     * 判断Wi-Fi是否连接
     * @param cxt
     * @return
     */
    public static boolean isNetConnect(Context cxt) {
        try {
            ConnectivityManager connManager = (ConnectivityManager) cxt.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifiInfo = connManager.getActiveNetworkInfo();
            return mWifiInfo.isConnected();
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 判断Wi-Fi是否连接
     * @param cxt
     * @return
     */
    public static boolean isWiFiConnect(Context cxt) {
        try {
            ConnectivityManager connManager = (ConnectivityManager) cxt.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            return mWifiInfo.isConnected();
        } catch (Exception e) {
        }
        return false;
    }
}
