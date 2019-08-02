package com.jvtd.running_sdk.base.widget.viewPager;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.jvtd.running_sdk.R;
import com.jvtd.running_sdk.base.widget.viewPager.pagerAdapter.JvtdInfinitePagerAdapter;


public class JvtdViewPager extends ViewPager {
    private boolean isScroll = true;
    private boolean isInfinite = false;

    public JvtdViewPager(Context context) {
        this(context,null);
    }

    public JvtdViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.JvtdViewPager);
        isScroll = ta.getBoolean(R.styleable.JvtdViewPager_jvtd_is_scroll, true);
        isInfinite = ta.getBoolean(R.styleable.JvtdViewPager_jvtd_is_infinite, false);
        ta.recycle();
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
        // offset first element so that we can scroll to the left
        setCurrentItem(0);
    }

    @Override
    public void setCurrentItem(int item) {
        // offset the current item to ensure there is space to scroll
        setCurrentItem(item, false);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        if (!isInfinite) {
            super.setCurrentItem(item,smoothScroll);
            return;
        }
        if (getAdapter() == null) return;
        if (getAdapter().getCount() == 0) {
            super.setCurrentItem(item, smoothScroll);
            return;
        }
        item = getOffsetAmount() + (item % getAdapter().getCount());
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public int getCurrentItem() {
        if (!isInfinite) return super.getCurrentItem();
        if (getAdapter() == null) return 0;
        if (getAdapter().getCount() == 0) {
            return super.getCurrentItem();
        }
        int position = super.getCurrentItem();
        if (getAdapter() instanceof JvtdInfinitePagerAdapter) {
            JvtdInfinitePagerAdapter infAdapter = (JvtdInfinitePagerAdapter) getAdapter();
            // Return the actual item position in the data backing JvtdInfinitePagerAdapter
            return (position % infAdapter.getRealCount());
        } else {
            return super.getCurrentItem();
        }
    }

    private int getOffsetAmount() {
        if (!isInfinite) return 0;
        if (getAdapter() == null || getAdapter().getCount() == 0) {
            return 0;
        }
        if (getAdapter() instanceof JvtdInfinitePagerAdapter) {
            JvtdInfinitePagerAdapter infAdapter = (JvtdInfinitePagerAdapter) getAdapter();
            // allow for 100 back cycles from the beginning
            // should be enough to create an illusion of infinity
            // warning: scrolling to very high values (1,000,000+) results in
            // strange drawing behaviour
            return infAdapter.getRealCount() * 100;
        } else {
            return 0;
        }
    }

    /**
     * 1.dispatchTouchEvent一般情况不做处理
     * ,如果修改了默认的返回值,子孩子都无法收到事件
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        return super.dispatchTouchEvent(ev);   // return true;不行
    }

    /**
     * 是否拦截
     * 拦截:会走到自己的onTouchEvent方法里面来
     * 不拦截:事件传递给子孩子
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        // return false;//可行,不拦截事件,
        // return true;//不行,孩子无法处理事件
        //return super.onInterceptTouchEvent(ev);//不行,会有细微移动
        return isScroll && super.onInterceptTouchEvent(ev);
    }

    /**
     * 是否消费事件
     * 消费:事件就结束
     * 不消费:往父控件传
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        //return false;// 可行,不消费,传给父控件
        //return true;// 可行,消费,拦截事件
        //super.onTouchEvent(ev); //不行,
        //虽然onInterceptTouchEvent中拦截了,
        //但是如果viewpage里面子控件不是viewgroup,还是会调用这个方法.

        return !isScroll || super.onTouchEvent(ev);
    }

    public void setScroll(boolean scroll)
    {
        isScroll = scroll;
    }

    public void setInfinite(boolean infinite) {
        isInfinite = infinite;
    }
}

