package com.jvtd.running_sdk.base;

import android.content.Intent;
import android.support.annotation.StringRes;

import com.jvtd.running_sdk.utils.JvtdNetworkUtils;

import me.yokeyword.fragmentation.ISupportFragment;

public abstract class JvtdMvpFragment extends JvtdFragment implements JvtdMvpView {
    @Override
    public void onDestroyView() {
        hideKeyboard();
        super.onDestroyView();
    }

    @Override
    public void showLoading() {
        if (getActivity() == null) return;
        ((JvtdMvpActivity) getActivity()).showLoading();
    }

    @Override
    public void showLoading(int resId) {
        if (getActivity() == null) return;
        ((JvtdMvpActivity) getActivity()).showLoading(resId);
    }

    @Override
    public void hideLoading() {
        if (getActivity() == null) return;
        ((JvtdMvpActivity) getActivity()).hideLoading();
    }

    @Override
    public void onError(@StringRes int resId) {
        if (getActivity() == null) return;
        ((JvtdMvpActivity) getActivity()).onError(resId);
    }

    @Override
    public void onError(String message) {
        if (getActivity() == null) return;
        ((JvtdMvpActivity) getActivity()).onError(message);
    }

    @Override
    public void showMessage(String message) {
        if (getActivity() == null) return;
        ((JvtdMvpActivity) getActivity()).showMessage(message);
    }

    @Override
    public void showMessage(@StringRes int resId) {
        if (getActivity() == null) return;
        ((JvtdMvpActivity) getActivity()).showMessage(resId);
    }

    @Override
    public boolean isNetworkConnected() {
        return JvtdNetworkUtils.isNetworkConnected(mContext);
    }

    @Override
    public void hideKeyboard() {
        if (getActivity() == null) return;
        ((JvtdMvpActivity) getActivity()).hideKeyboard();
    }

    @Override
    public void start(ISupportFragment toFragment)
    {
        hideKeyboard();
        super.start(toFragment);
    }

    @Override
    public void startActivity(Intent intent)
    {
        hideKeyboard();
        super.startActivity(intent);
    }
}
