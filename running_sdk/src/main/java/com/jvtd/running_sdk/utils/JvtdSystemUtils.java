package com.jvtd.running_sdk.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class JvtdSystemUtils
{
  /**
   * 方法描述：判断某一应用是否正在运行
   * Created by cafeting on 2017/2/4.
   *
   * @param context     上下文
   * @param packageName 应用的包名
   * @return true 表示正在运行，false 表示没有运行
   */
  public static boolean isAppAlive(Context context, String packageName)
  {
    ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    if (am == null) return false;
    List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
    if (list.size() <= 0)
    {
      return false;
    }
    for (ActivityManager.RunningTaskInfo info : list)
    {
      if (info.baseActivity.getPackageName().equals(packageName))
      {
        return true;
      }
    }
    return false;
  }

  /**
   * 获取唯一标识
   *
   * @author Chenlei
   * created at 2018/9/14
   **/
  @SuppressLint("HardwareIds")
  public static String getUniqueId(Context context) {
    String androidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    String id = androidID + Build.SERIAL;
    return JvtdStringUtils.md5(id);
  }

  /**
   * 返回桌面
   *
   * @author Chenlei
   * created at 2018/9/19
   **/
  public static void returnDesktop(Context context){
    Intent intent = new Intent();
    // 为Intent设置Action、Category属性
    intent.setAction(Intent.ACTION_MAIN);// "android.intent.action.MAIN"
    intent.addCategory(Intent.CATEGORY_HOME); //"android.intent.category.HOME"
    context.startActivity(intent);
  }

  //解决Android 9 以上系统已映射的方式调用私有api警告弹窗问题
  @SuppressLint("PrivateApi")
  public static void closeAndroidPDialog(){
    try {
      Class aClass = Class.forName("android.content.pm.PackageParser$Package");
      Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
      declaredConstructor.setAccessible(true);
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      Class cls = Class.forName("android.app.ActivityThread");
      Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
      declaredMethod.setAccessible(true);
      Object activityThread = declaredMethod.invoke(null);
      Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
      mHiddenApiWarningShown.setAccessible(true);
      mHiddenApiWarningShown.setBoolean(activityThread, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
