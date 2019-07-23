package com.jvtd.running_sdk.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 用于判断设备是否可以访问网络。
 *
 * @author Chenlei
 * created at 2019-07-23
 **/

public class JvtdNetUtils {
    private static class Holder {
        public static JvtdNetUtils instance = new JvtdNetUtils();
    }

    public static JvtdNetUtils getInstance() {
        return Holder.instance;
    }

    /**
     * 是否手机信号可连接
     *
     * @param context 上下文
     * @return boolean
     */
    public boolean isMobileAva(Context context) {
        boolean hasMobileCon = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfos = cm.getAllNetworkInfo();
        for (NetworkInfo net : netInfos) {

            String type = net.getTypeName();
            if (type.equalsIgnoreCase("MOBILE")) {
                if (net.isConnected()) {
                    hasMobileCon = true;
                }
            }
        }
        return hasMobileCon;
    }

    /**
     * 是否wifi可连接
     *
     * @param context 上下文
     * @return boolean
     */
    public boolean isWifiCon(Context context) {
        boolean hasWifoCon = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfos = cm.getAllNetworkInfo();
        for (NetworkInfo net : netInfos) {

            String type = net.getTypeName();
            if (type.equalsIgnoreCase("WIFI")) {
                if (net.isConnected()) {
                    hasWifoCon = true;
                }
            }
        }
        return hasWifoCon;
    }
}
