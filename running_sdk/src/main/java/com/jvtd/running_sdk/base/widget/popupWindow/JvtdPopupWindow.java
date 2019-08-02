package com.jvtd.running_sdk.base.widget.popupWindow;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import com.zyyoona7.popup.BasePopup;

public abstract class JvtdPopupWindow extends BasePopup<JvtdPopupWindow> implements View.OnKeyListener {
    private static final float DEFAULT_DIM = 0.2f;

    public JvtdPopupWindow(Context context) {
        setContext(context);
    }

    @Override
    protected void initAttributes() {
        setContentView(getLayoutRes());
        setWidth(getWidth());
        setHeight(getHeight());
        setDimValue(getDimAmount());
        setBackgroundDimEnable(true);
        setDimColor(getDimColor());
        setFocusAndOutsideEnable(getCancelOutside());
    }

    @Override
    protected void initViews(View view, JvtdPopupWindow jvtdPopupWindow) {
        bindView(view);
        view.setOnKeyListener(this);
    }

    /**
     * 布局资源
     *
     * @author Chenlei
     * created at 2018/9/17
     **/
    @LayoutRes
    public abstract int getLayoutRes();

    /**
     * 绑定操作
     *
     * @author Chenlei
     * created at 2018/9/17
     **/
    public abstract void bindView(View v);

    public int getHeight() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    public int getWidth(){ return WindowManager.LayoutParams.WRAP_CONTENT; }

    /**
     * 背景透明度 0-1  越小越透明
     *
     * @author Chenlei
     * created at 2018/9/17
     **/
    public float getDimAmount() {
        return DEFAULT_DIM;
    }

    /**
     * 外部是否可点击
     *
     * @author Chenlei
     * created at 2018/9/17
     **/
    public boolean getCancelOutside() {
        return true;
    }

    /**
     * 背景颜色
     *
     * @author Chenlei
     * created at 2018/9/27
     **/
    public int getDimColor(){
        return Color.BLACK;
    }

    /**
     * 返回键是否销毁
     *
     * @author Chenlei
     * created at 2018/9/17
     **/
    public boolean getKeyBackEnabled() {
        return true;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK && !getKeyBackEnabled();
    }
}
