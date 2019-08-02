package com.jvtd.running_sdk.utils;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jvtd.running_sdk.R;
import com.jvtd.running_sdk.bean.JvtdBarData;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;

import java.util.List;

public class JvtdMagicIndicatorUtils {
    public interface OnMagicIndicatorChangeListener{
        void onChange(int index);
    }
    /**
     * 初始化bar样式
     *
     * @author Chenlei
     * created at 2018/9/25
     **/
    public static void initBarMagicIndicator(Context context, ViewPager viewPager, MagicIndicator magicIndicator,
                                             List<JvtdBarData> barDataList, @ColorRes int unselectColor, @ColorRes int selectColor){
        initBarMagicIndicator(context,viewPager,magicIndicator,barDataList,10,unselectColor,selectColor,null);

    }
    public static void initBarMagicIndicator(Context context, ViewPager viewPager, MagicIndicator magicIndicator,
                                             List<JvtdBarData> barDataList, @ColorRes int unselectColor, @ColorRes int selectColor,
                                             OnMagicIndicatorChangeListener onClickListener){
        initBarMagicIndicator(context,viewPager,magicIndicator,barDataList,10,unselectColor,selectColor,onClickListener);

    }
    public static void initBarMagicIndicator(Context context, ViewPager viewPager, MagicIndicator magicIndicator,
                                             List<JvtdBarData> barDataList, int textSize, @ColorRes int unselectColor, @ColorRes int selectColor,
                                             OnMagicIndicatorChangeListener onClickListener) {
        CommonNavigator commonNavigator = new CommonNavigator(context);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return barDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, int index) {
                JvtdBarData barData = barDataList.get(index);
                CommonPagerTitleView commonPagerTitleView = new CommonPagerTitleView(context);

                // load custom layout
                View customLayout = LayoutInflater.from(context).inflate(R.layout.jvtd_bar_layout, null);
                final ImageView ivIcon = customLayout.findViewById(R.id.iv_icon);
                final TextView tvTitle = customLayout.findViewById(R.id.tv_title);
                ivIcon.setImageResource(barData.getUnselectDrawable());
                tvTitle.setText(barData.getTitle());
                tvTitle.setTextSize(textSize);
                commonPagerTitleView.setContentView(customLayout);

                commonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {

                    @Override
                    public void onSelected(int index, int totalCount) {
                        tvTitle.setTextColor(ContextCompat.getColor(context,selectColor));
                        ivIcon.setImageResource(barData.getSelectDrawable());

                    }

                    @Override
                    public void onDeselected(int index, int totalCount) {
                        tvTitle.setTextColor(ContextCompat.getColor(context,unselectColor));
                        ivIcon.setImageResource(barData.getUnselectDrawable());
                    }

                    @Override
                    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
                    }

                    @Override
                    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
                    }
                });

                if (onClickListener == null){
                    commonNavigator.setOnClickListener(v -> viewPager.setCurrentItem(index));
                }else {
                    commonPagerTitleView.setOnClickListener(v -> {
                        viewPager.setCurrentItem(index);
                        onClickListener.onChange(index);
                    });
                }

                return commonPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }
}
