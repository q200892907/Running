package com.jvtd.running_sdk.utils;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

/**
 * Created by chenlei on 2017/12/13.
 */

public class JvtdDrawableUtils
{
  /**
   * @param drawableNormal  默认
   * @param drawableSelect  选中
   * @return StateListDrawable
   */
  public static StateListDrawable getSelectorDrawable(Drawable drawableNormal, Drawable drawableSelect)
  {
    StateListDrawable drawable = new StateListDrawable();
    //选中
    drawable.addState(new int[]{android.R.attr.state_selected}, drawableSelect);
    //未选中
    drawable.addState(new int[]{-android.R.attr.state_selected}, drawableNormal);
    return drawable;
  }
}
