package com.jvtd.running_sdk.base.widget.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 简化自定义布局初始化
 *
 * @author Chenlei
 * created at 2018/9/19
 **/
public abstract class JvtdCustomLinearLayout extends LinearLayout implements JvtdCustomInterface{
    protected View mView;
    protected Context mContext;
    public JvtdCustomLinearLayout(Context context) {
        this(context,null);
    }

    public JvtdCustomLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public JvtdCustomLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs);
    }

    private void initView(Context context,@Nullable AttributeSet attrs) {
        mContext = context;
        mView = LayoutInflater.from(context).inflate(getLayoutRes(),this);
        bindView(mView,attrs);
    }
}
