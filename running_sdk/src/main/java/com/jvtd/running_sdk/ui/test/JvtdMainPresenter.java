package com.jvtd.running_sdk.ui.test;


import android.content.Context;

import com.jvtd.running_sdk.base.JvtdPresenter;
import com.jvtd.running_sdk.bean.JvtdEmptyBean;
import com.jvtd.running_sdk.rxjava.JvtdObserverSubscriber;
import com.jvtd.running_sdk.rxjava.JvtdRxSchedulers;

/**
 * 首页
 * <p>
 * 作者:chenlei
 * 时间:2018/11/15 1:11 PM
 */
public class JvtdMainPresenter<V extends JvtdMainMvpView> extends JvtdPresenter<V> implements JvtdMainMvpPresenter<V> {
    public JvtdMainPresenter(Context context, V mvpView) {
        super(context, mvpView);
    }

    @Override
    public void test() {
        getCompositeDisposable().add(getApiHelper().login()
                .compose(JvtdRxSchedulers.handleOtherObservableResult())
                .subscribeWith(new JvtdObserverSubscriber<JvtdEmptyBean>(getMvpView()) {
                    @Override
                    public void onNext(JvtdEmptyBean jvtdEmptyBean) {
                        super.onNext(jvtdEmptyBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                }));
    }
}
