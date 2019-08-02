package com.jvtd.running_sdk.api.http;

import android.content.Context;

import com.jvtd.running_sdk.constants.RunningSdk;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit管理
 * 作者:chenlei
 * 时间:2019-08-02 10:57
 */
public class RetrofitManager {
    private static volatile OkHttpClient mOkHttpClient;
    private static OkHttpClient getOkHttpClient(Context context){
        if (mOkHttpClient==null){
            synchronized (RetrofitManager.class){
                if (mOkHttpClient==null){
                    Cache cache=new Cache(context.getCacheDir(),1024*1024*10);//缓存10mib
                    HttpLoggingInterceptor httpLog = new HttpLoggingInterceptor();
                    httpLog.setLevel(HttpLoggingInterceptor.Level.BODY);
                    mOkHttpClient=new OkHttpClient.Builder()
                            .addInterceptor(httpLog)
                            .connectTimeout(30, TimeUnit.SECONDS)//连接超时时间
                            .readTimeout(30,TimeUnit.SECONDS)//读取超时时间
                            .writeTimeout(30,TimeUnit.SECONDS)//写入超时时间
                            .cache(cache)
                            .build();
                }
            }
        }
        return mOkHttpClient;
    }


    public static<T> T create(Context context,Class<T> clazz){
        Retrofit retrofit=new Retrofit.Builder()
                .client(getOkHttpClient(context))
                .baseUrl(RunningSdk.HTTP_API_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(clazz);
    }

}
