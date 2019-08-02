package com.jvtd.running_sdk.base;


import android.content.Context;

import com.jvtd.running_sdk.api.ApiHelper;
import com.jvtd.running_sdk.constants.RunningSdk;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Administrator on 2017/10/16.
 * Presenter 基类
 */

public class JvtdPresenter<V extends JvtdMvpView> implements JvtdMvpPresenter<V>
{
  /**
   * 获取的页数 default 1
   */
  protected int mPageIndex = 1;
  protected int mPageSize = RunningSdk.LOAD_NUM;
  protected boolean isLoading = false;// 是否显示LoadingDialog

  private CompositeDisposable mDisposable;
  private V mMvpView;
  private ApiHelper mApiHelper;
  private Context mContext;

  public JvtdPresenter(Context context,V mvpView) {
    mContext = context;
    mMvpView = mvpView;
  }

  /**
   * 注册界面方法
   *
   * @author Chenlei
   * created at 2018/9/25
   **/
  @Override
  public void onAttach(V mvpView)
  {
    mMvpView = mvpView;
  }

  /**
   * 注销界面方法
   *
   * @author Chenlei
   * created at 2018/9/25
   **/
  @Override
  public void onDetach()
  {
    mDisposable.clear();
    mMvpView = null;
  }

  /**
   * 是否已注册界面方法
   *
   * @author Chenlei
   * created at 2018/9/25
   **/
  public boolean isAttachView()
  {
    return mMvpView != null;
  }

  /**
   * 获取当前注册界面方法
   *
   * @author Chenlei
   * created at 2018/9/25
   **/
  public V getMvpView()
  {
    return mMvpView;
  }

  public ApiHelper getApiHelper() {
    if (mApiHelper == null) mApiHelper = ApiHelper.getInstance(mContext);
    return mApiHelper;
  }

  public CompositeDisposable getCompositeDisposable()
  {
    if (mDisposable == null) mDisposable = new CompositeDisposable();
    return mDisposable;
  }
}
