package com.jvtd.running_sdk.location;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.jvtd.running_sdk.R;
import com.jvtd.running_sdk.stepCount.StepCount;
import com.jvtd.running_sdk.stepCount.StepDetector;
import com.jvtd.running_sdk.stepCount.StepValuePassListener;
import com.jvtd.running_sdk.utils.JvtdLocationUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class JvtdLocation implements AMapLocationListener
{
  private Context mContext;
  //声明AMapLocationClient类对象
  private AMapLocationClient mLocationClient;
  //声明AMapLocationClientOption对象
  private AMapLocationClientOption mLocationOption;

  private static final String NOTIFICATION_CHANNEL_NAME = "BackgroundLocation";
  private NotificationManager notificationManager = null;
  private boolean isCreateChannel = false;

  //计时器订阅
  private Disposable mDisposable;

  private JvtdLocationListener mJvtdLocationListener;
  private JvtdLocationGpsListener mJvtdLocationGpsListener;
  private boolean isNeedAddress = false;//不需要定位
  private boolean isSensor = false;
  private boolean isOnlyGPS = false;
  private long interval = 2000;//定位时间间隔
  private AMapLocation mMapLocation;
  private boolean isNeedInfo = true;

  private int mErrorNum = 0;//定位偏移数

  private boolean isStepCount = false;//是否开启计步功能
  private int mStepNumber = 0;//当前步数

  private Sensor mSensor;
  private SensorManager mSensorManager;
  private StepCount mStepCount;
  private StepDetector mStepDetector;

  public interface JvtdLocationListener
  {
    void onLocation(AMapLocation aMapLocation,int stepNumber);

    void onSecondAdd();
  }
  public interface JvtdLocationGpsListener{
    void onGpsStatus(int status);
  }

  public JvtdLocation(Context context, boolean isNeedAddress, boolean isSensor, long interval,
                      boolean isOnlyGPS, boolean isNeedInfo,boolean isStepCount)
  {
    super();
    this.mContext = context;
    this.isNeedAddress = isNeedAddress;
    this.isSensor = isSensor;
    this.interval = interval;
    this.isOnlyGPS = isOnlyGPS;
    this.isNeedInfo = isNeedInfo;
    this.isStepCount = isStepCount;
    if (isStepCount){
      initStepCount();
    }
    initLocation();
  }

  public JvtdLocation(Context context, boolean isNeedAddress, boolean isSensor)
  {
    this(context, isNeedAddress, isSensor, 2000, false,false,false);
  }

  public JvtdLocation(Context context)
  {
    this(context, false, true, 2000, false, true,true);
  }

  /**
   * 启动定时器
   */
  public void startTime()
  {
    Observable.interval(0, 1, TimeUnit.SECONDS)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Long>()
        {
          @Override
          public void onSubscribe(Disposable d)
          {
            mDisposable = d;
          }

          @Override
          public void onNext(Long value)
          {
            //Log.d("Timer",""+value);
            if (mJvtdLocationListener != null)
              mJvtdLocationListener.onSecondAdd();
          }

          @Override
          public void onError(Throwable e)
          {

          }

          @Override
          public void onComplete()
          {

          }
        });
  }


  /**
   * 关闭定时器
   */
  public void closeTimer()
  {
    if (mDisposable != null)
    {
      mDisposable.dispose();
    }
  }

  //初始化定位
  private void initLocation()
  {
    //初始化定位
    mLocationClient = new AMapLocationClient(mContext.getApplicationContext());
    //设置定位回调监听
    mLocationClient.setLocationListener(this);

    //初始化AMapLocationClientOption对象
    mLocationOption = new AMapLocationClientOption();
    mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Sport);
    // 设置定位模式
    if (isOnlyGPS)
      mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
    else {
      mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
      mLocationOption.setLocationCacheEnable(true);//开启缓存策略，如未发生位置改变，返回之前相同坐标，仅在网络、基站模式下生效
    }
    // 设置定位间隔,单位毫秒,默认为2000ms
    mLocationOption.setInterval(interval);
    mLocationOption.setWifiScan(true);//获取最新wifi列表，提高精度
    mLocationOption.setGpsFirst(true);//优先使用GPS
    //开启传感器速度等信息
    mLocationOption.setSensorEnable(isSensor);
    mLocationOption.setNeedAddress(isNeedAddress);

    // TODO: 2018/4/3 正式版去掉虚拟定位
    // 设置是否允许模拟位置,默认为true，允许模拟位置
    mLocationOption.setMockEnable(false);

    if (null != mLocationClient)
    {
      mLocationClient.setLocationOption(mLocationOption);
      //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
      mLocationClient.stopLocation();
    }
  }

  //初始化步数记录
  private void initStepCount() {
    mStepDetector = new StepDetector();
    mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
    mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    mSensorManager.registerListener(mStepDetector,mSensor,SensorManager.SENSOR_DELAY_UI);
    mStepCount = new StepCount();
    mStepCount.initListener(steps -> mStepNumber = steps);
    mStepDetector.initListener(mStepCount);
  }

  @SuppressLint("NewApi")
  private Notification buildNotification() {

    Notification.Builder builder = null;
    Notification notification = null;
    if(android.os.Build.VERSION.SDK_INT >= 26) {
      //Android O上对Notification进行了修改，如果设置的targetSDKVersion>=26建议使用此种方式创建通知栏
      if (null == notificationManager) {
        notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
      }
      String channelId = mContext.getPackageName();
      if(!isCreateChannel) {
        NotificationChannel notificationChannel = new NotificationChannel(channelId,
            NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.enableLights(true);//是否在桌面icon右上角展示小圆点
        notificationChannel.setLightColor(Color.BLUE); //小圆点颜色
        notificationChannel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
        notificationManager.createNotificationChannel(notificationChannel);
        isCreateChannel = true;
      }
      builder = new Notification.Builder(mContext.getApplicationContext(), channelId);
    } else {
      builder = new Notification.Builder(mContext.getApplicationContext());
    }
    builder.setSmallIcon(R.drawable.ic_launcher)
        .setContentTitle(mContext.getString(R.string.app_name))
        .setContentText(mContext.getString(R.string.use_location_service))
        .setWhen(System.currentTimeMillis());
    notification = builder.build();
    return notification;
  }

  public void startLocation()
  {
    //开启定位
    mLocationClient.stopLocation();
//    mLocationClient.enableBackgroundLocation(2001,buildNotification());
    mLocationClient.startLocation();
    startTime();
  }

  public void stopLocation()
  {
    //关闭定位
    mLocationClient.stopLocation();
//    mLocationClient.disableBackgroundLocation(true);
    closeTimer();
  }

  public JvtdLocation setLocationListener(JvtdLocationListener jvtdLocationListener)
  {
    mJvtdLocationListener = jvtdLocationListener;
    return this;
  }

  public JvtdLocation setLocationGpsListener(JvtdLocationGpsListener jvtdLocationGpsListener){
    mJvtdLocationGpsListener = jvtdLocationGpsListener;
    return this;
  }

  @Override
  public void onLocationChanged(AMapLocation aMapLocation)
  {
    if (aMapLocation != null && ((isOnlyGPS && aMapLocation.getLocationType() == AMapLocation.LOCATION_TYPE_GPS) || (!isOnlyGPS && aMapLocation.getLocationType() != AMapLocation.LOCATION_TYPE_CELL))
        && aMapLocation.getAccuracy() < 500f)
      if (aMapLocation.getErrorCode() == AMapLocation.LOCATION_SUCCESS && mJvtdLocationListener !=
          null)
      {
        if (isNeedInfo)
        {
          //2点距离
//          Toast.makeText(mContext,aMapLocation.toStr() , Toast.LENGTH_SHORT).show();
          double mileage = 0;
          if (mMapLocation != null)
            mileage = JvtdLocationUtils.calculateMileage(mMapLocation, aMapLocation);
//          记录规则
//          1、必须GPS定位（室内、地铁、部分区域无法获取）
//          2、第一个点速度可以为0
//          3、当前点速度>0并且当前点与上一个点距离在（当前点时速*间隔时间+5米）之间，此为正常点，否则异常
//          4、异常点如果连续出现5次，下一个点为正常点
//          5、不能出现连续速度为0的点
          if (mMapLocation == null || (mileage < aMapLocation.getSpeed()*(interval * (mErrorNum+1))+5 && aMapLocation.getSpeed() > 0.f)|| mErrorNum >= 5){//异常检测  两点距离小于20米，速度不小于0，并且连续异常不超过5次
            mErrorNum = 0;
            mJvtdLocationListener.onLocation(aMapLocation,mStepNumber);
            mMapLocation = aMapLocation;
          }else{
//            Toast.makeText(mContext, "异常点", Toast.LENGTH_SHORT).show();
            mErrorNum++;
          }
        }else {
          mJvtdLocationListener.onLocation(aMapLocation,mStepNumber);
          mMapLocation = aMapLocation;
        }
      }

    if (mJvtdLocationGpsListener != null && aMapLocation != null)
      mJvtdLocationGpsListener.onGpsStatus(aMapLocation.getGpsAccuracyStatus());
  }

  public void onDestroy()
  {
    stopLocation();
    mLocationClient.onDestroy();
    if (isStepCount && mSensorManager != null){
      mSensorManager.unregisterListener(this.mStepDetector);
    }
  }
}
