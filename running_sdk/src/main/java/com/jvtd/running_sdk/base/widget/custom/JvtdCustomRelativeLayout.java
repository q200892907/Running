package com.jvtd.running_sdk.base.widget.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
/**
 * 简化RelativeLayout自定义布局方法
 *
 * @author Chenlei
 * created at 2019-08-02
 **/
public abstract class JvtdCustomRelativeLayout extends RelativeLayout implements JvtdCustomInterface
{
  protected View mView;
  protected Context mContext;

  public JvtdCustomRelativeLayout(Context context)
  {
    this(context, null);
  }

  public JvtdCustomRelativeLayout(Context context, @Nullable AttributeSet attrs)
  {
    this(context, attrs, 0);
  }

  public JvtdCustomRelativeLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
  {
    super(context, attrs, defStyleAttr);
    initView(context, attrs);
  }

  private void initView(Context context, @Nullable AttributeSet attrs)
  {
    mContext = context;
    mView = LayoutInflater.from(context).inflate(getLayoutRes(), this);
    bindView(mView, attrs);
  }
}
