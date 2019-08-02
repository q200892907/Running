package com.jvtd.running_sdk.base.widget.viewPager.pagerAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/10/16.
 */

public class JvtdFragmentPagerAdapter extends FragmentPagerAdapter
{
  private List<Fragment> mFragments;

  public JvtdFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments)
  {
    super(fm);
    mFragments = fragments;
  }

  @Override
  public Fragment getItem(int position)
  {
    return mFragments.get(position);
  }

  @Override
  public int getCount()
  {
    return mFragments.size();
  }
}
