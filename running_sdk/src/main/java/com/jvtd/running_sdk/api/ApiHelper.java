package com.jvtd.running_sdk.api;

import android.content.Context;
import android.preference.PreferenceManager;

import com.jvtd.running_sdk.api.http.RetrofitManager;
import com.jvtd.running_sdk.bean.JvtdEmptyBean;
import com.jvtd.running_sdk.bean.JvtdLineBean;
import com.jvtd.running_sdk.rxjava.JvtdRxSchedulers;

import io.reactivex.Observable;

/**
 * api帮助类
 * 作者:chenlei
 * 时间:2019-08-02 11:02
 */
public class ApiHelper implements ApiService {
    private static volatile ApiHelper instance;

    private Context mContext;

    public static ApiHelper getInstance(Context context) {
        if (instance == null)
            synchronized (JvtdLineBean.class) {
                if (instance == null)
                    instance = new ApiHelper(context);
            }
        return instance;
    }

    public ApiHelper(Context context) {
        mContext = context;
    }

    @Override
    public Observable<JvtdEmptyBean> login() {
        return RetrofitManager.create(mContext, ApiService.class)
                .login()
                .compose(JvtdRxSchedulers.applyObservableAsync());
    }
}
