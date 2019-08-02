package com.jvtd.running_sdk.base.widget.dropDownMenu;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by Administrator on 2017/5/19.
 * 设置不可滑动的Recycler
 */
public class JvtdDropDownMenuManager extends LinearLayoutManager
{
  private boolean isScrollEnabled = true;

  public JvtdDropDownMenuManager(Context context)
  {
    super(context);
  }

  public void setScrollEnabled(boolean flag)
  {
    this.isScrollEnabled = flag;
  }

  @Override
  public boolean canScrollVertically()
  {
    //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
    return isScrollEnabled && super.canScrollVertically();
  }
}
