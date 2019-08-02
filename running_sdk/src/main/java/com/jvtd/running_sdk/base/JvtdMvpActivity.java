package com.jvtd.running_sdk.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.widget.Toast;

import com.jvtd.running_sdk.R;
import com.jvtd.running_sdk.base.widget.dialog.JvtdLoadingDialog;
import com.jvtd.running_sdk.base.widget.toast.JvtdToast;
import com.jvtd.running_sdk.utils.JvtdKeyboardUtils;
import com.jvtd.running_sdk.utils.JvtdNetworkUtils;

import me.yokeyword.fragmentation.ISupportFragment;

public abstract class JvtdMvpActivity extends JvtdActivity implements JvtdMvpView {
    protected JvtdToast mBottomToast;

    @Override
    protected void onViewCreated(Bundle savedInstanceState)
    {
        super.onViewCreated(savedInstanceState);
        initToast();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        initToast();
        mBottomToast.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        initToast();
        mBottomToast.onPause();
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

    /**
     * 初始化toast
     *
     * @author Chenlei
     * created at 2018/9/25
     **/
    private void initToast(){
        if (mBottomToast == null)
            mBottomToast = new JvtdToast(mContext, JvtdToast.FLAG_BOTTOM);
    }

    @Override
    public void showLoading()
    {
        hideLoading();
        JvtdLoadingDialog.show(mContext);
    }

    @Override
    public void showLoading(int resId)
    {
        hideLoading();
        JvtdLoadingDialog.show(mContext, getString(resId));
    }

    @Override
    public void hideLoading()
    {
        JvtdLoadingDialog.dismiss(mContext);
    }

    @Override
    public void onError(@StringRes int resId)
    {
        showMessage(getString(resId));
    }

    @Override
    public void onError(String message)
    {
        if (message != null)
            mBottomToast.showMessage(message, Toast.LENGTH_SHORT);
        else
            mBottomToast.showMessage(getString(R.string.jvtd_error_toast_error_tips), Toast.LENGTH_SHORT);
    }

    @Override
    public void showMessage(@StringRes int resId)
    {
        showMessage(getString(resId));
    }

    @Override
    public void showMessage(String message)
    {
        if (message != null)
            mBottomToast.showMessage(message, Toast.LENGTH_SHORT);
        else
            mBottomToast.showMessage(getString(R.string.jvtd_error_toast_error_tips), Toast.LENGTH_SHORT);
    }

    @Override
    public boolean isNetworkConnected()
    {
        return JvtdNetworkUtils.isNetworkConnected(this);
    }

    @Override
    public void hideKeyboard()
    {
        JvtdKeyboardUtils.hideSoftInput(this);
    }
}
