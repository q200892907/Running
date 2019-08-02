package com.jvtd.running_sdk.rxjava;

import com.jvtd.running_sdk.bean.JvtdEmptyBean;
import com.jvtd.running_sdk.bean.http.BaseResponse;
import com.jvtd.running_sdk.bean.http.JvtdApiException;

import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/10/16.
 * Rx工具类
 */
public class JvtdRxSchedulers
{

  private JvtdRxSchedulers()
  {
  }

  public static final int RESPONSE_CODE_SUCCESS_MIN = 200;
  public static final int RESPONSE_CODE_SUCCESS_MAX = 299;

  public static final int CODE_SUCCESS = 200;

  public static final int CODE_NEED_LOGIN = 401;

  /**
   * 请求统一线程处理
   */
  public static <T> ObservableTransformer<T, T> applyObservableAsync()
  {
    return upstream -> upstream.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
  }

  /**
   * 请求统一线程处理
   */
  public static <T> ObservableTransformer<T, T> applyObservableCompute()
  {
    return upstream -> upstream.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread());
  }

  /**
   * 请求统一线程处理
   */
  public static <T> ObservableTransformer<T, T> applyObservableWorkThread()
  {
    return upstream -> upstream.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread());
  }

  /**
   * 请求统一线程处理
   */
  public static <T> ObservableTransformer<T, T> applyObservableMainThread()
  {
    return upstream -> upstream.observeOn(AndroidSchedulers.mainThread());
  }


  /**
   * 请求统一线程处理
   */
  public static <T> FlowableTransformer<T, T> applyFlowableAsync()
  {
    return upstream -> upstream.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
  }


  public static <T> FlowableTransformer<T, T> applyFlowableCompute()
  {
    return upstream -> upstream.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread());
  }

  /**
   * 请求统一线程处理
   */
  public static <T> FlowableTransformer<T, T> applyFlowableWorkThread()
  {
    return upstream -> upstream.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread());
  }


  public static <T> FlowableTransformer<T, T> applyFlowableMainThread()
  {
    return upstream -> upstream.observeOn(AndroidSchedulers.mainThread());
  }


  /**
   * 数据请求正常返回，统一处理连接正确的异常码  JWT模式   200-299 正确
   */
  public static <T> ObservableTransformer<Response<BaseResponse<T>>, T> handleJwtObservableResult()
  {
    return upstream -> upstream.flatMap((Function<Response<BaseResponse<T>>,
            ObservableSource<T>>) tBaseResponse -> {
      if (tBaseResponse.code() >= RESPONSE_CODE_SUCCESS_MIN && tBaseResponse.code() <= RESPONSE_CODE_SUCCESS_MAX)
        return createData(tBaseResponse.body().getData());
      else {
        return Observable.error(new JvtdApiException(tBaseResponse.body().getMessage(), tBaseResponse.code()));
      }
    });
  }

  /**
   * 数据请求正常返回，统一处理   200正确
   */
  public static <T> ObservableTransformer<BaseResponse<T>, T> handleObservableResult()
  {
    return upstream -> upstream.flatMap((Function<BaseResponse<T>, ObservableSource<T>>) tResponse -> {
      if (tResponse.getCode() == CODE_SUCCESS)
        return createData(tResponse.getData());
      else
        return Observable.error(new JvtdApiException(tResponse.getMessage()));
    });
  }

  /**
   * 其他请求返回
   */
  public static <T> ObservableTransformer<T, T> handleOtherObservableResult()
  {
    return upstream -> upstream.flatMap((Function<T, ObservableSource<T>>) JvtdRxSchedulers::createData);
  }


  private static <T> ObservableSource<T> createData(final T t)
  {
    return Observable.create(emitter -> {
      try
      {
        if (t == null)
        {
          JvtdEmptyBean bean = new JvtdEmptyBean();
          emitter.onNext((T) bean);
        }
        else
          emitter.onNext(t);
        emitter.onComplete();
      } catch (Exception e)
      {
        emitter.onError(e);
        e.printStackTrace();
      }
    });
  }

}
