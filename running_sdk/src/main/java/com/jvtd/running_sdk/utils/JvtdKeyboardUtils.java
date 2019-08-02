
package com.jvtd.running_sdk.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by Administrator on 2017/10/16.
 * 键盘
 */
public final class JvtdKeyboardUtils
{
  /**
   * 隐藏输入法
   *
   * @author Chenlei
   * created at 2018/9/14
   **/
  public static void hideSoftInput(Activity activity)
  {
    View view = activity.getCurrentFocus();
    if (view == null) view = new View(activity);
    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
    if (imm != null) {
      imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
  }
  public static void hideSoftInput(EditText editText,Context context)
  {
    InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
    if (imm != null)
    {
      imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
  }

  /**
   * 显示输入法 (不推荐使用)
   *
   * @param edit    EditText
   * @param context Content
   */
  public static void showSoftInput(EditText edit, Context context)
  {
    edit.setFocusable(true);
    edit.setFocusableInTouchMode(true);
    edit.requestFocus();
    InputMethodManager imm = (InputMethodManager) context
            .getSystemService(Context.INPUT_METHOD_SERVICE);
    if (imm != null) {
      imm.showSoftInput(edit, 0);
    }
  }

  /**
   * 输入法是否显示
   *
   * @author Chenlei
   * created at 2018/9/14
   **/
  public static boolean isKeyboardShow(Context context) {
    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    return imm != null && imm.isActive();
  }

  /**
   * 切换输入法
   */
  public static void toggleSoftInput(Context context)
  {
    // 到InputMethodManager的实例
    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    // 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
    if (imm != null) {
      imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
    }
  }
}
