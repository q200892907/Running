package com.jvtd.running_sdk.base.widget.loadMore;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.jvtd.running_sdk.R;

/**
 * Created by chenlei on 2017/10/27.
 */

public final class JvtdLoadMoreView extends LoadMoreView
{
  @Override
  public int getLayoutId()
  {
    return R.layout.jvtd_load_more_layout;
  }

  @Override
  protected int getLoadingViewId()
  {
    return R.id.load_more_loading_view;
  }

  @Override
  protected int getLoadFailViewId()
  {
    return R.id.load_more_load_fail_view;
  }

  @Override
  protected int getLoadEndViewId()
  {
    return R.id.load_more_load_end_view;
  }
}
