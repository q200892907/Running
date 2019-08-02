package com.jvtd.running_sdk.base;

/**
 * Created by Administrator on 2017/10/16.
 * Mvp Presenter
 */

public interface JvtdMvpPresenter<V extends JvtdMvpView>
{
  void onAttach(V mvpView);

  void onDetach();
}
