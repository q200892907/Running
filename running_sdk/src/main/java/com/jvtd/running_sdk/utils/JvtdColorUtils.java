package com.jvtd.running_sdk.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.v4.content.ContextCompat;

public class JvtdColorUtils
{
  /**
   * 修改颜色  color 传 R.color.*  alpha 0-1之间
   *
   * @author Chenlei
   * created at 2018/4/10
   **/
  @ColorInt
  public static int changeColorAlpha(Context context, int color, float alpha){
    int changeColor = ContextCompat.getColor(context, color);
   return changeColorAlpha(changeColor,alpha);
  }
  
  /**
   * 修改颜色  color 传 0x000000  alpha 0-1之间
   *
   * @author Chenlei
   * created at 2018/4/17
   **/
  public static int changeColorAlpha(int color, float alpha){
    int r = (color >> 16) & 0xFF;
    int g = (color >> 8) & 0xFF;
    int b = color & 0xFF;
    int a = (int) (255 * alpha);
    return argb(a,r,g,b);
  }

  @ColorInt
  public static int argb(
      @IntRange(from = 0, to = 255) int alpha,
      @IntRange(from = 0, to = 255) int red,
      @IntRange(from = 0, to = 255) int green,
      @IntRange(from = 0, to = 255) int blue) {
    return (alpha << 24) | (red << 16) | (green << 8) | blue;
  }

  /**
   * @param normal  默认颜色
   * @param checked 选种颜色
   * @return ColorStateList
   */
  public static ColorStateList getSelectorColor(int normal, int checked)
  {
    int[] colors = new int[]{normal, checked, normal};
    int[][] states = new int[3][];
    states[0] = new int[]{-android.R.attr.state_selected};
    states[1] = new int[]{android.R.attr.state_selected};
    states[2] = new int[]{};
    return new ColorStateList(states, colors);
  }
}
