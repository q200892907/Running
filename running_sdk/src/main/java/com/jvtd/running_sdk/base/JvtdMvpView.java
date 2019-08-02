package com.jvtd.running_sdk.base;

import android.support.annotation.StringRes;

/**
 * Created by Administrator on 2017/10/16.
 * Mvp 基类
 */

public interface JvtdMvpView
{
  /**
   * 开启loading
   *
   * @author Chenlei
   * created at 2018/9/18
   **/
  void showLoading();
  void showLoading(@StringRes int resId);
  /**
   * 关闭loading
   *
   * @author Chenlei
   * created at 2018/9/18
   **/
  void hideLoading();

  /**
   * 开启提醒
   *
   * @author Chenlei
   * created at 2018/9/18
   **/
  void onError(@StringRes int resId);

  void onError(String message);

  void showMessage(String message);

  void showMessage(@StringRes int resId);

  /**
   * 网络是否连接
   *
   * @author Chenlei
   * created at 2018/9/18
   **/
  boolean isNetworkConnected();

  /**
   * 隐藏键盘
   *
   * @author Chenlei
   * created at 2018/9/18
   **/
  void hideKeyboard();
}
