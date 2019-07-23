package com.jvtd.running_sdk.constants;

/**
 * SDK 基础配置
 * 作者:chenlei
 * 时间:2019-07-23 14:13
 */
public class RunningSdk {
    public static final String LOCATION_IN_BACKGROUND = "LOCATION_IN_BACKGROUND";//后台定位

    public static final String LOCATION_DATA = "LOCATION_DATA";//定位数据
    public static final String LOCATION_SEND_TYPE = "LOCATION_SEND_TYPE";//定位传输类型
    public static final int LOCATION_SEND_TIME = 1;//时间
    public static final int LOCATION_SEND_DATA = 2;//数据
    public static final int LOCATION_SEND_GPS = 3;//GPS信号

    public static final long ONE_SECOND = 1;//一秒

    //EventBus 状态码
    public static final int EVENT_CODE_LOCATION = 0x01;//后台定位
    public static final int EVENT_CODE_GPS_STATUS = 0x02;//GPS状态
}
