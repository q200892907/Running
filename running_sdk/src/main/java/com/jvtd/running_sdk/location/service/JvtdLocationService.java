package com.jvtd.running_sdk.location.service;

import android.content.Intent;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.jvtd.running_sdk.bean.JvtdLocationBean;
import com.jvtd.running_sdk.constants.RunningSdk;
import com.jvtd.running_sdk.location.JvtdLocation;
import com.jvtd.running_sdk.location.delegate.IWifiAutoCloseDelegate;
import com.jvtd.running_sdk.location.delegate.JvtdWifiAutoCloseDelegate;
import com.jvtd.running_sdk.utils.JvtdLocationUtils;
import com.jvtd.running_sdk.utils.JvtdNetUtils;
import com.jvtd.running_sdk.utils.JvtdPowerManagerUtils;

/**
 * 后台定位服务
 *
 * @author Chenlei
 * created at 2019-07-23
 **/
public class JvtdLocationService extends JvtdNotiService
{
  private JvtdLocation mJvtdLocation;

  /**
   * 处理息屏关掉wifi的delegate类
   */
  private IWifiAutoCloseDelegate mWifiAutoCloseDelegate = new JvtdWifiAutoCloseDelegate();

  /**
   * 记录是否需要对息屏关掉wifi的情况进行处理
   */
  private boolean mIsWifiCloseable = false;

  @Override
  public void onCreate()
  {
    super.onCreate();
    // TODO: 2019-07-23 aidl无法开启  暂时添加
    startForeground(123123, JvtdLocationUtils.buildNotification(getApplicationContext()));
  }


  @Override
  public int onStartCommand(Intent intent, int flags, int startId)
  {
    super.onStartCommand(intent, flags, startId);
    // TODO: 2019-07-23 aidl无法开启  暂时关闭
//    applyNotiKeepMech(); //开启利用notification提高进程优先级的机制

    if (mWifiAutoCloseDelegate.isUseful(getApplicationContext()))
    {
      mIsWifiCloseable = true;
      mWifiAutoCloseDelegate.initOnServiceStarted(getApplicationContext());
    }

    startLocation();

    return START_STICKY;
  }


  @Override
  public void onDestroy()
  {
    // TODO: 2019-07-23 aidl无法开启  暂时关闭
//    unApplyNotiKeepMech();
    stopLocation();
    if (mJvtdLocation != null)
      mJvtdLocation.onDestroy();
    super.onDestroy();
  }

  /**
   * 启动定位
   */
  void startLocation()
  {
    stopLocation();
    if (mJvtdLocation == null)
    {
      mJvtdLocation = new JvtdLocation(this.getApplicationContext());
      mJvtdLocation.setLocationListener(new JvtdLocation.JvtdLocationListener()
      {
        @Override
        public void onLocation(AMapLocation aMapLocation,int stepNumber)
        {
          JvtdLocationBean locationBean = new JvtdLocationBean();
          double speed = aMapLocation.getSpeed() < 0 ? 0 : aMapLocation.getSpeed();
          double altitude = aMapLocation.getAltitude() < 0 ? 0 : aMapLocation.getAltitude();
          locationBean.setAltitude(altitude);
          locationBean.setLatitude(aMapLocation.getLatitude());
          locationBean.setLongitude(aMapLocation.getLongitude());
          locationBean.setSpeed(speed * 3.6);
          locationBean.setStepNumber(stepNumber);

          sendLocation(locationBean);

          if (!mIsWifiCloseable)
          {
            return;
          }

          if (aMapLocation.getErrorCode() == AMapLocation.LOCATION_SUCCESS)
          {
            mWifiAutoCloseDelegate.onLocateSuccess(getApplicationContext(), JvtdPowerManagerUtils
                .getInstance().isScreenOn(getApplicationContext()), JvtdNetUtils.getInstance()
                .isMobileAva(getApplicationContext()));
          } else
          {
            mWifiAutoCloseDelegate.onLocateFail(getApplicationContext(), aMapLocation
                    .getErrorCode(),
                JvtdPowerManagerUtils.getInstance().isScreenOn(getApplicationContext()), JvtdNetUtils
                    .getInstance().isWifiCon(getApplicationContext()));
          }
        }

        @Override
        public void onSecondAdd()
        {
          Intent mIntent = new Intent(RunningSdk.LOCATION_IN_BACKGROUND);
          configIntent(mIntent);
          mIntent.putExtra(RunningSdk.LOCATION_SEND_TYPE, RunningSdk.LOCATION_SEND_TIME);
          //发送广播
          sendBroadcast(mIntent);
        }

        private void sendLocation(JvtdLocationBean jvtdLocationBean)
        {
          Intent mIntent = new Intent(RunningSdk.LOCATION_IN_BACKGROUND);
          configIntent(mIntent);
          Bundle bundle = new Bundle();
          bundle.putParcelable(RunningSdk.LOCATION_DATA, jvtdLocationBean);
          mIntent.putExtra(RunningSdk.LOCATION_SEND_TYPE, RunningSdk.LOCATION_SEND_DATA);
          mIntent.putExtra(RunningSdk.LOCATION_DATA, bundle);
          //发送广播
          sendBroadcast(mIntent);
        }

      }).setLocationGpsListener(status -> {
        Intent mIntent = new Intent(RunningSdk.LOCATION_IN_BACKGROUND);
        configIntent(mIntent);
        Bundle bundle = new Bundle();
        bundle.putInt(RunningSdk.LOCATION_DATA, status);
        mIntent.putExtra(RunningSdk.LOCATION_SEND_TYPE, RunningSdk.LOCATION_SEND_GPS);
        mIntent.putExtra(RunningSdk.LOCATION_DATA, bundle);
        //发送广播
        sendBroadcast(mIntent);
      });
    }
    mJvtdLocation.startLocation();
  }

  private void configIntent(Intent intent){
//          intent.setComponent(new ComponentName("com.jvtd.gsa","com.jvtd.gsa.receiver.LocationChangeReceiver"));

//          intent.setComponent(new ComponentName("com.jvtd.gsa.widget.mapView.service.GsaLocationService","com.jvtd.gsa.app.MyApplication"));
  }


  /**
   * 停止定位
   */
  void stopLocation()
  {
    if (null != mJvtdLocation)
    {
      mJvtdLocation.stopLocation();
      mJvtdLocation = null;
    }
  }
}
