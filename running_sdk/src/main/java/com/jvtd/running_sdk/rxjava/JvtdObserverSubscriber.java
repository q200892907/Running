package com.jvtd.running_sdk.rxjava;

import android.text.TextUtils;

import com.google.gson.JsonSyntaxException;
import com.jvtd.running_sdk.R;
import com.jvtd.running_sdk.base.JvtdMvpView;
import com.jvtd.running_sdk.bean.http.JvtdApiException;

import java.net.SocketException;
import java.net.UnknownHostException;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.ResourceObserver;
import retrofit2.HttpException;

/**
 * Created by Administrator on 2017/10/16.
 * 统一处理
 */
public abstract class JvtdObserverSubscriber<T> extends ResourceObserver<T> {
    private JvtdMvpView mJvtdMvpView;
    private String mErrorMsg;

    public JvtdObserverSubscriber(JvtdMvpView jvtdMvpView) {
        mJvtdMvpView = jvtdMvpView;
    }

    public JvtdObserverSubscriber(JvtdMvpView jvtdMvpView, String errorMsg) {
        mJvtdMvpView = jvtdMvpView;
        mErrorMsg = errorMsg;
    }

    @Override
    public void onNext(@NonNull T t) {
        if (mJvtdMvpView == null) return;
        mJvtdMvpView.hideLoading();
    }

    @Override
    public void onError(@NonNull Throwable e) {
        if (mJvtdMvpView == null) return;

        mJvtdMvpView.hideLoading();

        if (mErrorMsg != null && !TextUtils.isEmpty(mErrorMsg))
            //请求数据正确连接后返回的异常信息
            mJvtdMvpView.onError(mErrorMsg);
        else if (e instanceof JvtdApiException) {
            mJvtdMvpView.onError(e.getMessage());
//            if (((JvtdApiException) e).getCode() == mJvtdMvpView.mustLogoutCode())
//                mJvtdMvpView.mustLogout();
        } else if (e instanceof JsonSyntaxException)
            mJvtdMvpView.onError(R.string.jvtd_api_data_analysis_failed_error);
        else if (e instanceof HttpException)
            mJvtdMvpView.onError(R.string.jvtd_api_data_load_failed_error);
        else if (e instanceof UnknownHostException || e instanceof SocketException)
            mJvtdMvpView.onError(R.string.jvtd_api_network_error);
        else
            mJvtdMvpView.onError(R.string.jvtd_api_server_connect_error);
    }

    @Override
    public void onComplete() {

    }
}
