package com.jvtd.running_sdk.base.widget.webView;

import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class JvtdWebChromeClient extends WebChromeClient
{
  private JvtdWeb.ChromeClientListener mChromeClientListener;

  public JvtdWebChromeClient()
  {
    this(null);
  }

  public JvtdWebChromeClient(JvtdWeb.ChromeClientListener chromeClientListener)
  {
    super();
    this.mChromeClientListener = chromeClientListener;
  }

  @Override
  public void onProgressChanged(WebView view, int newProgress)
  {
    super.onProgressChanged(view, newProgress);
    if (mChromeClientListener != null)
      mChromeClientListener.onProgressChanged(view, newProgress);
  }

  @Override
  public void onReceivedTitle(WebView view, String title)
  {
    super.onReceivedTitle(view, title);
    if (mChromeClientListener != null)
      mChromeClientListener.onReceivedTitle(view, title);
  }

  /**
   * 处理定位权限相关问题
   *
   * @author Jack Zhang
   * create at 2019/1/4 10:48 AM
   */
  @Override
  public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback)
  {
    super.onGeolocationPermissionsShowPrompt(origin, callback);
    if (mChromeClientListener != null)
      mChromeClientListener.onGeolocationPermissionsShowPrompt(origin, callback);
  }
}
