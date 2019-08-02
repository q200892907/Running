package com.jvtd.running_sdk.base.widget.webView;

import android.graphics.Bitmap;
import android.webkit.WebView;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;

public class JvtdWebViewClient extends BridgeWebViewClient
{
  private JvtdWeb.ClientListener mClientListener;

  public JvtdWebViewClient(BridgeWebView webView)
  {
    this(webView, null);
  }

  public JvtdWebViewClient(BridgeWebView webView, JvtdWeb.ClientListener clientListener)
  {
    super(webView);
    this.mClientListener = clientListener;
  }

  @Override
  public boolean shouldOverrideUrlLoading(WebView view, String url)
  {
    if (mClientListener != null)
      return mClientListener.shouldOverrideUrlLoading(view, url);
    else
      return super.shouldOverrideUrlLoading(view, url);
  }

  @Override
  public void onPageStarted(WebView view, String url, Bitmap favicon)
  {
    super.onPageStarted(view, url, favicon);
    if (mClientListener != null)
      mClientListener.onPageStarted(url);
  }

  @Override
  public void onPageFinished(WebView view, String url)
  {
    super.onPageFinished(view, url);
    if (mClientListener != null)
      mClientListener.onPageFinished(view, url);
  }

  @Override
  public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
  {
    super.onReceivedError(view, errorCode, description, failingUrl);
    if (mClientListener != null)
      mClientListener.onReceivedError(view, errorCode, description, failingUrl);
  }
}
