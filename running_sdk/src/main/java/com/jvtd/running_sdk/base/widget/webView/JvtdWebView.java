package com.jvtd.running_sdk.base.widget.webView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.webkit.GeolocationPermissions;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jvtd.running_sdk.R;
import com.jvtd.running_sdk.base.widget.custom.JvtdCustomRelativeLayout;

/**
 * Created by chenlei on 2017/10/18.
 */

public class JvtdWebView extends JvtdCustomRelativeLayout
{
  /**
   * 网页加载的进度条
   */
  private ProgressBar mProgressBar;
  /**
   * WebView实体
   */
  private JvtdJsWebView mWebView;
  /**
   * TextView，用于显示Host
   */
  private TextView mTextView;

  /**
   * 网页当前加载进度
   */
  private int mCurrentProgress;
  /**
   * 是否显示进度条
   */
  private boolean mShowProgressBar = true;
  /**
   * 动画是否开启的标识
   */
  private boolean mIsAnimStart = false;
  /**
   * 网页加载自定义监听
   */
  private JvtdWeb.OnLoadListener mOnLoadListener;

  public JvtdWebView(Context context)
  {
    super(context);
  }

  public JvtdWebView(Context context, @Nullable AttributeSet attrs)
  {
    super(context, attrs);
  }

  public JvtdWebView(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
  {
    super(context, attrs, defStyleAttr);
  }

  @Override
  public int getLayoutRes()
  {
    return R.layout.jvtd_web_view_layout;
  }

  @Override
  public void bindView(View view, @Nullable AttributeSet attrs)
  {
    mWebView = view.findViewById(R.id.webview);
    mTextView = view.findViewById(R.id.webview_host);
    mProgressBar = view.findViewById(R.id.webview_pg);

    setBackgroundColor(ContextCompat.getColor(mContext, R.color.jvtd_web_view_progress_color));

    // 设置WebChromeClient
    mWebView.setWebChromeClient(new JvtdWebChromeClient(new JvtdWeb.ChromeClientListener()
    {
      @Override
      public void onProgressChanged(WebView view, int newProgress)
      {
        if (mShowProgressBar)
        {
          mCurrentProgress = mProgressBar.getProgress();
          if (newProgress >= 100 && !mIsAnimStart)
          {
            // 防止调用多次动画
            mIsAnimStart = true;
            mProgressBar.setProgress(newProgress);
            // 开启属性动画让进度条平滑消失
            startDismissAnimation(mProgressBar.getProgress());
          } else
            // 开启属性动画让进度条平滑递增
            startProgressAnimation(newProgress);
        }
      }

      @Override
      public void onReceivedTitle(WebView view, String title)
      {
        if (mOnLoadListener != null)
          mOnLoadListener.onReceivedTitle(title);
      }

      @Override
      public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback)
      {
        if (mOnLoadListener != null)
          mOnLoadListener.onGeolocationPermissionsShowPrompt(origin, callback);
      }
    }));

    // 设置WebViewClient
    mWebView.setWebViewClient(new JvtdWebViewClient(mWebView, new JvtdWeb.ClientListener()
    {
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url)
      {
        if (mOnLoadListener != null)
          return mOnLoadListener.shouldOverrideUrlLoading(view, url);
        else
        {
          mWebView.loadUrl(url);
          return true;
        }
      }

      @Override
      public void onPageStarted(String url)
      {
        if (mOnLoadListener != null)
          mOnLoadListener.onPageStarted(url);

        mProgressBar.setVisibility(mShowProgressBar ? View.VISIBLE : View.GONE);
        mProgressBar.setAlpha(1.0f);
        setHostTitle(url);
      }

      @Override
      public void onPageFinished(WebView view, String url)
      {
        if (mOnLoadListener != null)
          mOnLoadListener.onPageFinished(view, url);
      }

      @Override
      public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
      {
        if (mOnLoadListener != null)
          mOnLoadListener.onReceivedError(view, errorCode, description, failingUrl);
      }
    }));

    initWebViewSettings();
  }


  private void initWebViewSettings()
  {
    WebSettings webSetting = mWebView.getSettings();
    // 提高渲染的优先级
    webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
    // 允许访问文件
    webSetting.setAllowFileAccess(true);

    // 支持内容重新布局
    //webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
    // 关闭webview中缓存
    //webSetting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
  }

  /**
   * 获取WebView实体
   *
   * @author Jack Zhang
   * create at 2018/12/24 11:34 PM
   */
  public JvtdJsWebView getWebView()
  {
    return mWebView;
  }

  /**
   * progressBar递增动画
   */
  private void startProgressAnimation(int newProgress)
  {
    ObjectAnimator animator = ObjectAnimator.ofInt(mProgressBar, "progress", mCurrentProgress, newProgress);
    animator.setDuration(300);
    animator.setInterpolator(new DecelerateInterpolator());
    animator.start();
  }

  /**
   * progressBar消失动画
   */
  private void startDismissAnimation(final int progress)
  {
    ObjectAnimator anim = ObjectAnimator.ofFloat(mProgressBar, "alpha", 1.0f, 0.0f);
    anim.setDuration(1500);  // 动画时长
    anim.setInterpolator(new DecelerateInterpolator());     // 减速
    // 关键, 添加动画进度监听器
    anim.addUpdateListener(valueAnimator ->
    {
      float fraction = valueAnimator.getAnimatedFraction();      // 0.0f ~ 1.0f
      int offset = 100 - progress;
      mProgressBar.setProgress((int) (progress + offset * fraction));
    });

    anim.addListener(new AnimatorListenerAdapter()
    {
      @Override
      public void onAnimationEnd(Animator animation)
      {
        // 动画结束
        mProgressBar.setProgress(0);
        mProgressBar.setVisibility(View.GONE);
        mIsAnimStart = false;
      }
    });
    anim.start();
  }

  /**
   * 设置域名提示
   *
   * @author Jack Zhang
   * create at 2018/12/24 11:35 PM
   */
  private void setHostTitle(String url)
  {
    Uri uri = Uri.parse(url).buildUpon().build();
    String host = uri.getHost();
    String hostTitle = String.format(getResources().getString(R.string.jvtd_web_view_host_title), host);
    mTextView.setText(hostTitle);
  }

  /**
   * 设置webview加载自定义监听
   *
   * @author Jack Zhang
   * create at 2018/12/24 11:35 PM
   */
  public void setOnLoadListener(JvtdWeb.OnLoadListener onLoadListener)
  {
    mOnLoadListener = onLoadListener;
  }

  /**
   * 设置是否显示加载进度条
   *
   * @author Jack Zhang
   * create at 2018/12/24 11:36 PM
   */
  public void isProgress(boolean progress)
  {
    mShowProgressBar = progress;
  }

  /**
   * 设置是否开启弹性效果
   *
   * @author Jack Zhang
   * create at 2018/12/24 11:36 PM
   */
  public void isFlexiable(boolean flexiable)
  {
    mWebView.isFlexiable(flexiable);
  }
}
