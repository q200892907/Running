package com.jvtd.running_sdk.base.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.jvtd.running_sdk.R;

/**
 * 进度 dialog
 */

public class JvtdLoadingDialog extends Dialog
{
  private static Dialog mDialog;

  private boolean canNotCancel;

  @SuppressWarnings("FieldCanBeLocal")
  private String tipMsg;

  @SuppressWarnings("FieldCanBeLocal")
  private TextView mTextView;

  @SuppressWarnings("FieldCanBeLocal")
  private static SpinKitView mSpinKitView;


  public JvtdLoadingDialog(Context context, String tipMsg, boolean canNotCancel)
  {
    super(context);
    this.canNotCancel = canNotCancel;
    this.tipMsg = tipMsg;

    this.getContext().setTheme(android.R.style.Theme_InputMethod);
    setContentView(R.layout.jvtd_loading_dialog);

    if (!TextUtils.isEmpty(this.tipMsg))
    {
      mTextView = findViewById(R.id.load_dialog_tip_msg);
      mTextView.setText(tipMsg);
      mTextView.setVisibility(View.VISIBLE);
    }

    mSpinKitView = findViewById(R.id.load_dialog_spin_kit);

    Window window = getWindow();
    WindowManager.LayoutParams params = window.getAttributes();
    params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
    params.dimAmount = 0.0f;

    window.setAttributes(params);
    window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event)
  {
    if (keyCode == KeyEvent.KEYCODE_BACK)
    {
      if (canNotCancel)
      {
        return true;
      }
    }
    return super.onKeyDown(keyCode, event);
  }

  /**
   * 显示dialog
   *
   * @param context context
   */
  public static void show(Context context)
  {
    show(context, null, false);
  }

  /**
   * 显示dialog
   *
   * @param context context
   * @param msg     提示信息
   */
  public static void show(Context context, String msg)
  {
    show(context, msg, false);
  }

  /**
   * 显示dialog
   *
   * @param context      context
   * @param msg          提示信息
   * @param canNotCancel boolean, true is can't dismiss，false is can dimiss
   */
  public static void show(Context context, String msg, boolean canNotCancel)
  {
    if (context instanceof Activity)
    {
      if (((Activity) context).isFinishing()) return;
    }

    if (mDialog != null && mDialog.isShowing()) return;

    mDialog = new JvtdLoadingDialog(context, msg, canNotCancel);
    mDialog.show();
  }

  public static void dismiss(Context context)
  {
    try
    {
      if (mSpinKitView != null)
      {
        mSpinKitView = null;
      }

      if (context instanceof Activity)
      {
        if (((Activity) context).isFinishing())
        {
          mDialog = null;
          return;
        }
      }

      if (mDialog != null && mDialog.isShowing())
      {
        Context c = mDialog.getContext();
        if (c instanceof Activity)
        {
          if (((Activity) c).isFinishing())
          {
            mDialog = null;
            return;
          }
        }

        mDialog.dismiss();
        mDialog = null;
      }
    } catch (Exception e)
    {
      e.printStackTrace();
      mSpinKitView = null;
      mDialog = null;
    }
  }
}
