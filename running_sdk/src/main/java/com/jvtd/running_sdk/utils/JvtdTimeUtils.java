package com.jvtd.running_sdk.utils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/10/16.
 * 倒计时
 */
public final class JvtdTimeUtils {
    /**
     * 总时长
     */
    private long mTotalLong;
    /**
     * 计时是否启动
     */
    private boolean mIsStart;

    private Disposable mDisposable;

    private TimeListener mListener;

    private JvtdTimeUtils(Build build) {
        this.mTotalLong = build.mLong;
        this.mListener = build.mListener;

    }

    public static Build builder() {
        return new Build();
    }

    public void start() {
        if (mIsStart) return;
        //59秒开始
        final int count = (int) (mTotalLong / 1000);

        mDisposable = Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(count, TimeUnit.SECONDS)
                .map(aLong -> {
                    return count - aLong - 1;  // 59S -> 0S
                })
                .subscribeOn(Schedulers.computation())
                .doOnSubscribe(disposable -> {
                    //运行在主线程,将 TextView.setEnable(false)
                    if (mListener != null)
                        mListener.onTimeStart();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if (mListener != null)
                        mListener.onALong(aLong);
                    //主线程 , 更新 UI 文字

                }, throwable -> {
                    mIsStart = false;
                    if (mListener != null)
                        mListener.onError();
                }, () -> {
                    mIsStart = false;
                    if (mListener != null)
                        mListener.onComplete();
                });

    }

    public void cancel() {
        if (mDisposable != null)
            mDisposable.dispose();
    }

    public static final class Build {

        private long mLong;
        private TimeListener mListener;

        public Build setTotalTime(long aLong) {
            this.mLong = aLong;
            return this;
        }

        public Build setTimeListener(TimeListener timeListener) {
            mListener = timeListener;
            return this;
        }

        public JvtdTimeUtils build() {
            return new JvtdTimeUtils(this);
        }


    }

    public interface TimeListener {
        void onComplete();

        void onError();

        void onALong(long aLong);

        void onTimeStart();
    }
}
