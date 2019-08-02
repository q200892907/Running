package com.jvtd.running_sdk.utils;

import android.content.Context;

import com.jvtd.running_sdk.R;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

/**
 * Created by chenlei on 2018/1/30.
 */

public class JvtdSmartRefreshUtils
{
  public static void initRefresh(Context context, SmartRefreshLayout smartRefreshLayout, OnRefreshListener onRefreshListener)
  {
    initRefresh(context, smartRefreshLayout, R.color.jvtd_refresh_layout_bg_color, onRefreshListener);
  }

  public static void initRefresh(Context context, SmartRefreshLayout smartRefreshLayout, int colorId, OnRefreshListener onRefreshListener)
  {
    initRefresh(context, smartRefreshLayout, colorId, true, true, onRefreshListener);
  }

  public static void initRefresh(Context context, SmartRefreshLayout smartRefreshLayout, int colorId, boolean headerTranslation, boolean nestedScrolling, OnRefreshListener onRefreshListener)
  {
    // 设置首页下拉刷新
    smartRefreshLayout.setOnRefreshListener(onRefreshListener);
    // 设置 Header 为 Material风格
    smartRefreshLayout.setRefreshHeader(new MaterialHeader(context).setShowBezierWave(true));
    smartRefreshLayout.setPrimaryColorsId(colorId);
    smartRefreshLayout.setNestedScrollingEnabled(nestedScrolling);// 设置与子布局嵌套滚动
    smartRefreshLayout.setEnableHeaderTranslationContent(headerTranslation);//内容是否偏移
  }
}
