package com.jvtd.running_sdk.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.jvtd.running_sdk.bean.JvtdLineBean;
import com.jvtd.running_sdk.bean.JvtdLocationBean;
import com.jvtd.running_sdk.constants.RunningSdk;
import com.jvtd.running_sdk.eventBus.EventCenter;

import org.greenrobot.eventbus.EventBus;

/**
 * 定位广播接收器
 * 作者:chenlei
 * 时间:2019-07-23 15:15
 */
public class JvtdLocationChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        //判断接收数据类型 基于类型进行对应数据处理
        if (RunningSdk.LOCATION_IN_BACKGROUND.equals(action)) {
            int type = intent.getIntExtra(RunningSdk.LOCATION_SEND_TYPE, RunningSdk.LOCATION_SEND_DATA);
            if (type == RunningSdk.LOCATION_SEND_DATA) {
                Bundle locationResult = intent.getBundleExtra(RunningSdk.LOCATION_DATA);
                if (locationResult != null) {
                    JvtdLocationBean jvtdLocationBean = locationResult.getParcelable(RunningSdk.LOCATION_DATA);
                    if (jvtdLocationBean != null) {
                        JvtdLineBean.getInstance().addLocation(jvtdLocationBean);
                    }
                    EventBus.getDefault().post(new EventCenter<>(RunningSdk.EVENT_CODE_LOCATION));
                }
            } else if (type == RunningSdk.LOCATION_SEND_TIME) {
                JvtdLineBean.getInstance().addTime();
            } else if (type == RunningSdk.LOCATION_SEND_GPS) {
                //gps状态监听
                Bundle locationResult = intent.getBundleExtra(RunningSdk.LOCATION_DATA);
                if (locationResult != null) {
                    int status = locationResult.getInt(RunningSdk.LOCATION_DATA, AMapLocation.GPS_ACCURACY_UNKNOWN);
                    EventBus.getDefault().post(new EventCenter<>(status, RunningSdk.EVENT_CODE_GPS_STATUS));
                }
            }
        }
    }
}
