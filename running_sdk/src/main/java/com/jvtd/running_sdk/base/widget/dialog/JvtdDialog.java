package com.jvtd.running_sdk.base.widget.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.jvtd.running_sdk.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 、
 */
public abstract class JvtdDialog extends DialogFragment implements DialogInterface.OnKeyListener {

    private static final String TAG = "JvtdDialog";

    private static final float DEFAULT_DIM = 0.2f;

    public static final int NORMAL = 0;
    public static final int BOTTOM = 1;
    public static final int TOP = 2;

    protected boolean keyBackEnabled = true;
    protected boolean cancelOutside = true;

    boolean mDismissed;
    boolean mShownByMe;

    @IntDef({NORMAL, BOTTOM, TOP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Style {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, getAnimStyle());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getDialog().getWindow() != null)
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(getCancelOutside());

        View v = inflater.inflate(getLayoutRes(), container, false);
        bindView(v);
        return v;
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

    /**
     * 样式管理
     *
     * @author Chenlei
     * created at 2018/9/17
     **/
    @Style
    public abstract int getStyle();

    @StyleRes
    private int getAnimStyle() {
        switch (getStyle()) {
            case BOTTOM:
                return R.style.JvtdBottomDialog;
            case NORMAL:
                return R.style.JvtdDialog;
            case TOP:
                return R.style.JvtdTopDialog;
        }
        return R.style.JvtdDialog;
    }

    private int getGravity() {
        switch (getStyle()) {

            case BOTTOM:
                return Gravity.BOTTOM;
            case NORMAL:
                return Gravity.CENTER;
            case TOP:
                return Gravity.TOP;
        }
        return Gravity.CENTER;
    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params;
        if (window != null) {
            params = window.getAttributes();
            params.dimAmount = getDimAmount();
            params.width = getWidth();
            params.height = getHeight();
            params.gravity = getGravity();
            window.setAttributes(params);
        }

        getDialog().setOnKeyListener(this);
    }

    public int getHeight() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    public int getWidth(){ return WindowManager.LayoutParams.MATCH_PARENT; }

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
        return cancelOutside;
    }

    /**
     * 返回键是否销毁
     *
     * @author Chenlei
     * created at 2018/9/17
     **/
    public boolean getKeyBackEnabled() {
        return keyBackEnabled;
    }

    /**
     * 设置返回键是否可以销毁
     * @param keyBackEnabled 是与否
     */
    protected JvtdDialog setKeyBackEnabled(boolean keyBackEnabled) {
        this.keyBackEnabled = keyBackEnabled;
        return this;
    }

    /**
     * 外部是否可以销毁
     * @param cancelOutside 是与否
     */
    protected JvtdDialog setCancelOutside(boolean cancelOutside) {
        this.cancelOutside = cancelOutside;
        return this;
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK && !getKeyBackEnabled();
    }

    public String getFragmentTag() {
        return TAG;
    }

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, getFragmentTag());
    }

//    @Override
//    public void show(FragmentManager manager, String tag) {
//        mDismissed = false;
//        mShownByMe = true;
//        FragmentTransaction ft = manager.beginTransaction();
//        ft.add(this, tag);
//        // 这里吧原来的commit()方法换成了commitAllowingStateLoss()
//        ft.commitAllowingStateLoss();
//    }
}
