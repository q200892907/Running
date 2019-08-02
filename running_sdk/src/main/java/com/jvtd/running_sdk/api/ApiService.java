package com.jvtd.running_sdk.api;

import com.jvtd.running_sdk.bean.JvtdEmptyBean;
import com.jvtd.running_sdk.constants.RunningSdk;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * api 方法类
 * 作者:chenlei
 * 时间:2019-08-02 10:55
 */
public interface ApiService {
    @POST(RunningSdk.HTTP_API_URL)
    Observable<JvtdEmptyBean> login();
}
