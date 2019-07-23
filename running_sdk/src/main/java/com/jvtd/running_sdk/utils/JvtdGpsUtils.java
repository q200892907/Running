package com.jvtd.running_sdk.utils;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

/**
 * Gps工具类
 * 作者:chenlei
 * 时间:2019-07-23 13:58
 */
public class JvtdGpsUtils {
    /**
     * 判断GPS是否开启
     * @param context 上下文
     * @return boolean 返回是否开启
     */
    public static boolean isOpen(Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 跳转设置界面，打开gps
     * @param context 上下文
     */
    public static void openGps(Context context) {
        if (isOpen(context)) return;
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(intent);
    }
}
