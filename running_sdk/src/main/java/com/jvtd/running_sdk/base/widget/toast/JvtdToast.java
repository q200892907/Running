package com.jvtd.running_sdk.base.widget.toast;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.TextView;
import android.widget.Toast;

import com.jvtd.running_sdk.R;

/**
 * Created by NCL on 2017/5/9.
 * Toast 工具
 */

public class JvtdToast extends Toast
{
  private Context mContext;
  private boolean mScreenFront;
  private TextView mTextMsgTitle;
  private View mMsgLayout;
  private int mFlag;//用来区分是在底部显示还是中间显示
  public static final int FLAG_BOTTOM = 1;
  public static final int FLAG_CENTER = 0;

  public JvtdToast(Context context, int flag)
  {
    super(context);
    this.mContext = context;
    this.mFlag = flag;
    init();
  }

  private void init()
  {
    if (mFlag == FLAG_CENTER)
    {
      setGravity(Gravity.CENTER, 0, 0);
    } else if (mFlag == FLAG_BOTTOM)
    {
      setGravity(Gravity.BOTTOM, 0, 200);
    }

    View view = LayoutInflater.from(mContext).inflate(R.layout.jvtd_toast_layout, null);
    mTextMsgTitle = view.findViewById(R.id.textMsgTitle);
    mMsgLayout = view.findViewById(R.id.relMsgLayout);

    mMsgLayout.setVisibility(View.GONE);

    setView(view);
  }

  public void onResume()
  {
    mScreenFront = true;
  }

  public void onPause()
  {
    mScreenFront = false;
  }

  public void setScreenFront(boolean screenFront)
  {
    mScreenFront = screenFront;
  }

  public void showMessage(int titleRes, int duration)
  {
    String title = null;
    if (titleRes > 0) title = mContext.getString(titleRes);
    showMessage(title, duration);
  }

  public void showMessage(String title, int duration)
  {
    //如果当前界面不在用户前面，则取消弹出
    if (!mScreenFront) return;

    if (title != null)
    {
      mTextMsgTitle.setVisibility(View.VISIBLE);
      mTextMsgTitle.setText(title);
    }
    mMsgLayout.setVisibility(View.VISIBLE);
    setDuration(duration);
    show();
  }

  @Override
  public void show()
  {
    AnimationSet animationSet = new AnimationSet(true);
    animationSet.setDuration(200);
    mMsgLayout.startAnimation(animationSet);
    super.show();
  }
}
