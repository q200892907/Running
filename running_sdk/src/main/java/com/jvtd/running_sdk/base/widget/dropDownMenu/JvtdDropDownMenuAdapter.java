package com.jvtd.running_sdk.base.widget.dropDownMenu;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jvtd.running_sdk.R;
import com.jvtd.running_sdk.bean.JvtdDropDownMenuData;
import com.jvtd.running_sdk.utils.JvtdColorUtils;
import com.jvtd.running_sdk.utils.JvtdDrawableUtils;
import com.jvtd.running_sdk.utils.JvtdUiUtils;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

/**
 * Created by chenlei on 2017/12/13.
 */

public class JvtdDropDownMenuAdapter<T> extends JvtdDropDownMenuBaseAdapter<T> {
    private List<T> mDatas;//数据
    private int mIndex;//选中

    private TextView mBeforeTV;//上个选中文本
    private LinearLayout mBeforeLayout;

    //文本样式
    private JvtdDropDownMenu.TextBuilder textBuilder;


    public JvtdDropDownMenuAdapter(Context context, JvtdDropDownMenu.TextBuilder builder, List<T> datas, int selectIndex) {
        super(context, datas, R.layout.jvtd_drop_down_menu_adapter_item);
        textBuilder = builder;
        mIndex = selectIndex;
        mDatas = datas;
    }

    @Override
    public void convert(final JvtdDropDownMenuViewHolder holder, T data, final int position) {
        final TextView tv = holder.getView(R.id.adapter_textview);
        final View line = holder.getView(R.id.adapter_line);
        final LinearLayout layout = holder.getView(R.id.adapter_bg);

        //左侧最后一个自定义
        int height;
        int textSize;

        //间隔线设置
        if (!textBuilder.isShowLine()) {
            line.setVisibility(View.GONE);
        } else {
            line.setVisibility(View.VISIBLE);
            line.setBackgroundColor(textBuilder.getLineColor() == 0 ? Color.parseColor("#e8e8e8") : ContextCompat.getColor(mContext, textBuilder.getLineColor()));
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) line.getLayoutParams();
            layoutParams.setMarginStart(JvtdUiUtils.dp2px(mContext, textBuilder.getLineMarginStart()));
            layoutParams.setMarginEnd(JvtdUiUtils.dp2px(mContext, textBuilder.getLineMarginEnd()));
            line.setLayoutParams(layoutParams);
        }

        //文本左侧对齐
        if (textBuilder.isTextLeft()) {
            tv.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        } else {
            tv.setGravity(Gravity.CENTER);
        }

        tv.setPadding(JvtdUiUtils.dp2px(mContext, textBuilder.getTextMarginStart()), 0, UIUtil.dip2px(mContext, textBuilder.getTextMarginEnd()), 0);

        height = textBuilder.getHeight();
        textSize = textBuilder.getTextSize();

        //设置文本背景选择
        if (textBuilder.getNormalBgColor() != 0 && textBuilder.getSelectBgColor() != 0) {
            ColorDrawable nd = new ColorDrawable(ContextCompat.getColor(mContext, textBuilder.getNormalBgColor()));
            ColorDrawable sd = new ColorDrawable(ContextCompat.getColor(mContext, textBuilder.getSelectBgColor()));
            StateListDrawable stateListDrawable = JvtdDrawableUtils.getSelectorDrawable(nd, sd);
            layout.setBackground(stateListDrawable);
        }

        //设置文本选择图标  右侧
        if (textBuilder.getNormalDrawable() != 0 && textBuilder.getSelectDrawable() != 0) {
            Drawable nd = ContextCompat.getDrawable(mContext, textBuilder.getNormalDrawable());
            Drawable sd = ContextCompat.getDrawable(mContext, textBuilder.getSelectDrawable());
            StateListDrawable stateListDrawable = JvtdDrawableUtils.getSelectorDrawable(nd, sd);
            stateListDrawable.setBounds(0, 0, JvtdUiUtils.dp2px(mContext, height / 2), JvtdUiUtils.dp2px(mContext, height / 2));
            tv.setCompoundDrawables(null, null, stateListDrawable, null);
        }

        //设置文本选择颜色
        int nColor = Color.GRAY;
        int sColor = Color.BLACK;
        if (textBuilder.getSelectTextColor() != 0 && textBuilder.getNormalTextColor() != 0) {
            nColor = ContextCompat.getColor(mContext, textBuilder.getNormalTextColor());
            sColor = ContextCompat.getColor(mContext, textBuilder.getSelectTextColor());
        }
        ColorStateList colorStateList = JvtdColorUtils.getSelectorColor(nColor, sColor);
        tv.setTextColor(colorStateList);

        //设置文本信息
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, JvtdUiUtils.sp2px(mContext, textSize));
        if (data instanceof String)
            tv.setText((String)data);
        else if (data instanceof JvtdDropDownMenuData)
            tv.setText(((JvtdDropDownMenuData)data).getText());

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tv.getLayoutParams();
        layoutParams.height = JvtdUiUtils.dp2px(mContext, height);
        tv.setLayoutParams(layoutParams);

        //设置是否选择
        if (position == mIndex) {
            tv.setSelected(true);
            layout.setSelected(true);
            mBeforeTV = tv;
            mBeforeLayout = layout;
        } else {
            tv.setSelected(false);
            layout.setSelected(false);
        }

        tv.setOnClickListener(v -> {
            if (mBeforeTV != null) {
                mBeforeTV.setSelected(false);
            }
            if (mBeforeLayout != null)
                mBeforeLayout.setSelected(false);
            tv.setSelected(true);
            layout.setSelected(true);
            mListener.onItemClick(position);
            mIndex = position;
            mBeforeTV = tv;
            mBeforeLayout = layout;
        });
    }

    @Override
    protected void postDelayedStart() {

    }

    public void setSelectIndex(int selectIndex) {
        mIndex = selectIndex;
    }
}
