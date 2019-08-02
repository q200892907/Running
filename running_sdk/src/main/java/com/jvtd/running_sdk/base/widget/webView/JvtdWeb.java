package com.jvtd.running_sdk.base.widget.webView;

import android.webkit.GeolocationPermissions;
import android.webkit.WebView;

public class JvtdWeb
{
  public interface ChromeClientListener
  {
    void onProgressChanged(WebView view, int newProgress);

    void onReceivedTitle(WebView view, String string);

    void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback);
  }

  public interface ClientListener
  {
    boolean shouldOverrideUrlLoading(WebView view, String url);

    void onPageStarted(String url);

    void onPageFinished(WebView view, String url);

    void onReceivedError(WebView view, int errorCode, String description, String failingUrl);
  }

  public interface OnLoadListener
  {
    boolean shouldOverrideUrlLoading(WebView view, String url);

    void onPageStarted(String url);

    void onPageFinished(WebView view, String url);

    void onReceivedTitle(String title);

    void onReceivedError(WebView view, int errorCode, String description, String failingUrl);

    void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback);
  }
}
