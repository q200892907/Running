package com.jvtd.running_sdk.base.widget.custom;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自定义布局文件方法
 *
 * @author Chenlei
 * created at 2019-08-02
 **/
public interface JvtdCustomInterface {
    /**
     * 设置布局文件
     *
     * @author Chenlei
     * created at 2018/9/19
     **/
    @LayoutRes
    int getLayoutRes();
    /**
     * 绑定布局及属性
     *
     * @author Chenlei
     * created at 2018/9/19
     **/
    void bindView(View view, @Nullable AttributeSet attrs);
}
