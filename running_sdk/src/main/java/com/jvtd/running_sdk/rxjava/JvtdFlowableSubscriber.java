package com.jvtd.running_sdk.rxjava;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by Administrator on 2017/10/16.
 * 处理
 */
public abstract class JvtdFlowableSubscriber<T> extends ResourceSubscriber<T>
{
  @Override
  public void onNext(T t)
  {

  }

  @Override
  public void onError(Throwable t)
  {

  }

  @Override
  public void onComplete()
  {

  }
}
