package com.jvtd.running_sdk.location.delegate;

import android.content.Context;

/**
 * 代理类，用于处理息屏造成wifi被关掉时再重新点亮屏幕的逻辑
 *
 * @author Chenlei
 * created at 2019-07-23
 **/
public interface IWifiAutoCloseDelegate {
    /**
     * 判断在该机型下此逻辑是否有效。目前已知的系统是小米系统存在(用户自助设置的)息屏断掉wifi的功能。
     *
     * @param context 上下文
     * @return boolean
     */
    boolean isUseful(Context context);

    /**
     * 点亮屏幕的服务有可能被重启。此处进行初始化
     *
     * @param context 上下文
     */
    void initOnServiceStarted(Context context);

    /**
     * 定位成功时，如果移动网络无法访问，而且屏幕是点亮状态，则对状态进行保存
     *
     * @param context      上下文
     * @param isScreenOn   屏幕是否打开
     * @param isMobileable 是否有手机信号
     */
    void onLocateSuccess(Context context, boolean isScreenOn, boolean isMobileable);

    /**
     * 对定位失败情况的处理
     *
     * @param context    上下文
     * @param errorCode  错误代码
     * @param isScreenOn 屏幕是否打开
     * @param isWifiable 是否有wifi信号
     */
    void onLocateFail(Context context, int errorCode, boolean isScreenOn, boolean isWifiable);
}
