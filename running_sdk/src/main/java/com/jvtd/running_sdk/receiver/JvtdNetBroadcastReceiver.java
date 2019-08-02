package com.jvtd.running_sdk.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.text.TextUtils;

import com.jvtd.running_sdk.utils.JvtdNetworkUtils;

/**
 * 自定义检查手机网络状态是否切换的广播接受器
 *
 * @author cj
 */
public class JvtdNetBroadcastReceiver extends BroadcastReceiver
{
  private NetEvevt netEvevt;

  @Override
  public void onReceive(Context context, Intent intent)
  {
    // 如果相等的话就说明网络状态发生了变化
    if (TextUtils.equals(intent.getAction(), ConnectivityManager.CONNECTIVITY_ACTION))
    {
      int netWorkState = JvtdNetworkUtils.getNetworkState(context);
      // 接口回调传过去状态的类型
      netEvevt.onNetChange(netWorkState);
    }
  }

  public void setNetWorkChangerListener(NetEvevt netEvent)
  {
    this.netEvevt = netEvent;
  }

  // 自定义接口
  public interface NetEvevt
  {
    public void onNetChange(int netMobile);
  }
}
