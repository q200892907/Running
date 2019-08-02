package com.jvtd.running_sdk.base.widget.viewPager.pagerAdapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2017/10/16.
 * 引导页
 */

public class JvtdPagerAdapter extends PagerAdapter
{
  private List<View> viewList;

  public JvtdPagerAdapter(List<View> viewList)
  {
    this.viewList = viewList;
  }

  @Override
  public int getCount()
  {
    return viewList.size();
  }

  @Override
  public boolean isViewFromObject(@NonNull View view, @NonNull Object object)
  {
    return view == object;
  }

  @NonNull
  @Override
  public Object instantiateItem(@NonNull ViewGroup container, int position)
  {
    container.addView(viewList.get(position));
    return viewList.get(position);
  }

  @Override
  public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object)
  {
    container.removeView(viewList.get(position));
  }
}
