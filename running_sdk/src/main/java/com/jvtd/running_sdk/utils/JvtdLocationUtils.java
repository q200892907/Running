package com.jvtd.running_sdk.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.model.LatLng;
import com.jvtd.running_sdk.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * 辅助工具类-定位
 * 作者:chenlei
 * 时间:2019-07-23 14:23
 */
public class JvtdLocationUtils {
    /**
     * 根据定位结果返回定位信息的字符串
     *
     * @param location 定位信息
     * @return 内容
     */
    public synchronized static String getLocationStr(AMapLocation location) {
        if (null == location) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
        if (location.getErrorCode() == 0) {
            sb.append("定位成功" + "\n");
            sb.append("定位类型: ").append(location.getLocationType()).append("\n");
            sb.append("经    度    : ").append(location.getLongitude()).append("\n");
            sb.append("纬    度    : ").append(location.getLatitude()).append("\n");
            sb.append("精    度    : ").append(location.getAccuracy()).append("米").append("\n");
            sb.append("提供者    : ").append(location.getProvider()).append("\n");
            sb.append("海    拔    : ").append(location.getAltitude()).append("米").append("\n");
            sb.append("速    度    : ").append(location.getSpeed()).append("米/秒").append("\n");
            sb.append("角    度    : ").append(location.getBearing()).append("\n");
            if (location.getProvider().equalsIgnoreCase(android.location.LocationManager.GPS_PROVIDER)) {
                // 以下信息只有提供者是GPS时才会有
                // 获取当前提供定位服务的卫星个数
                sb.append("星    数    : ").append(location.getSatellites()).append("\n");
            }
            //逆地理信息
            sb.append("国    家    : ").append(location.getCountry()).append("\n");
            sb.append("省            : ").append(location.getProvince()).append("\n");
            sb.append("市            : ").append(location.getCity()).append("\n");
            sb.append("城市编码 : ").append(location.getCityCode()).append("\n");
            sb.append("区            : ").append(location.getDistrict()).append("\n");
            sb.append("区域 码   : ").append(location.getAdCode()).append("\n");
            sb.append("地    址    : ").append(location.getAddress()).append("\n");
            sb.append("兴趣点    : ").append(location.getPoiName()).append("\n");
            //定位完成的时间
            sb.append("定位时间: ").append(formatUTC(location.getTime(), "yyyy-MM-dd HH:mm:ss")).append("\n");
        } else {
            //定位失败
            sb.append("定位失败" + "\n");
            sb.append("错误码:").append(location.getErrorCode()).append("\n");
            sb.append("错误信息:").append(location.getErrorInfo()).append("\n");
            sb.append("错误描述:").append(location.getLocationDetail()).append("\n");
        }
        //定位之后的回调时间
        sb.append("回调时间: ").append(formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss")).append("\n");
        return sb.toString();
    }

    private static SimpleDateFormat sdf = null;

    private synchronized static String formatUTC(long l, String strPattern) {
        if (TextUtils.isEmpty(strPattern)) {
            strPattern = "yyyy-MM-dd HH:mm:ss";
        }
        if (sdf == null) {
            try {
                sdf = new SimpleDateFormat(strPattern, Locale.CHINA);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else {
            sdf.applyPattern(strPattern);
        }
        return sdf == null ? "NULL" : sdf.format(l);
    }

    public static Intent getExplicitIntent(Context context, Intent implicitIntent) {

        if (context.getApplicationInfo().targetSdkVersion < Build.VERSION_CODES.LOLLIPOP) {
            return implicitIntent;
        }

        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);
        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }
        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);
        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);
        // Set the component to be explicit
        explicitIntent.setComponent(component);
        return explicitIntent;
    }


    /**
     * 　　* 保存文件 　　* @param toSaveString 　　* @param filePath
     */
    public static void saveFile(String toSaveString, String fileName, boolean append) {
        try {
            String sdCardRoot = Environment.getExternalStorageDirectory()
                    .getAbsolutePath();
            File saveFile = new File(sdCardRoot + "/" + fileName);
            if (!saveFile.exists()) {
                File dir = new File(saveFile.getParent());
                dir.mkdirs();
                saveFile.createNewFile();
            }
            FileOutputStream outStream = new FileOutputStream(saveFile, append);
            outStream.write(toSaveString.getBytes());
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Notification buildNotification(Context context) {
        String NOTIFICATION_CHANNEL_NAME = "JvtdBackgroundLocation";
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        ;
        Notification.Builder builder;
        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = context.getPackageName();
            NotificationChannel notificationChannel = new NotificationChannel(channelId,
                    NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableLights(true);//是否在桌面icon右上角展示小圆点
            notificationChannel.setLightColor(Color.BLUE); //小圆点颜色
            notificationChannel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
            builder = new Notification.Builder(context.getApplicationContext(), channelId);
        } else {
            builder = new Notification.Builder(context.getApplicationContext());
        }
        builder.setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(context.getString(R.string.jvtd_use_location_service))
                .setWhen(System.currentTimeMillis());
        notification = builder.build();
        return notification;
    }


    /**
     * 开始wifi
     *
     * @param context 上下文
     */
    public static void startWifi(Context context) {
        WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wm != null) {
            wm.setWifiEnabled(true);
            wm.reconnect();
        }
    }

    /**
     * wifi是否开启
     *
     * @param context 上下文
     * @return boolean
     */
    public static boolean isWifiEnabled(Context context) {
        WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return wm != null && wm.isWifiEnabled();
    }

    public static String getManufacture(Context context) {
        return Build.MANUFACTURER;
    }

    private static String CLOSE_BRODECAST_INTENT_ACTION_NAME = "com.jvtd.running_sdk.CloseService";

    public static Intent getCloseBrodecastIntent() {
        return new Intent(CLOSE_BRODECAST_INTENT_ACTION_NAME);
    }

    public static IntentFilter getCloseServiceFilter() {
        return new IntentFilter(CLOSE_BRODECAST_INTENT_ACTION_NAME);
    }

    public static class CloseServiceReceiver extends BroadcastReceiver {
        Service mService;

        public CloseServiceReceiver(Service service) {
            this.mService = service;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (mService == null) {
                return;
            }
            mService.onDestroy();
        }
    }

    //计算两点距离
    public static double calculateMileage(AMapLocation start, AMapLocation end) {
        return calculateLineDistance(new LatLng(start.getLatitude(), start.getLongitude()), new
                LatLng(end.getLatitude(), end.getLongitude()));
    }

    public static double calculateLineDistance(LatLng start, LatLng end) {
        double lon1 = (Math.PI / 180) * start.longitude;
        double lon2 = (Math.PI / 180) * end.longitude;
        double lat1 = (Math.PI / 180) * start.latitude;
        double lat2 = (Math.PI / 180) * end.latitude;
        // 地球半径
        double R = 6371;
        // 两点间距离 km，如果想要米的话，结果*1000就可以了
        Double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math
                .cos(lon2 - lon1))
                * R;
        if (d.isInfinite() || d.isNaN())
            return 0.0;
        return Math.abs(d * 1000);
    }
}
