
package com.jvtd.running_sdk.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Administrator on 2017/10/16.
 * 网络连接
 */
public final class JvtdNetworkUtils
{
  public static final int NETWORK_NONE = -1;//没有连接网络
  public static final int NETWORK_MOBILE = 0;//移动网络
  public static final int NETWORK_WIFI = 1;//无线网络

  @IntDef({NETWORK_NONE, NETWORK_MOBILE,NETWORK_WIFI})
  @Retention(RetentionPolicy.SOURCE)
  public @interface NetworkState {}

  public static @NetworkState int getNetworkState(Context context)
  {
    // 得到连接管理器对象
    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    if (connectivityManager == null) return NETWORK_NONE;
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    if (activeNetworkInfo != null && activeNetworkInfo.isConnected())
    {
      if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI))
        return NETWORK_WIFI;
      else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE))
        return NETWORK_MOBILE;
    } else
      return NETWORK_NONE;
    return NETWORK_NONE;
  }
  /**
   * 网络是否链接
   *
   * @param context Context
   * @return {@code true} : 网络链接 <br> {@code false} : 未链接
   */
  public static boolean isNetworkConnected(Context context)
  {
    ConnectivityManager cm =
            (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetwork = null;
    if (cm != null) {
      activeNetwork = cm.getActiveNetworkInfo();
    }
    return activeNetwork != null && activeNetwork.isConnected();
  }
}
