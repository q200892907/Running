package com.jvtd.running_sdk.base.widget.dropDownMenu;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.jvtd.running_sdk.R;
import com.jvtd.running_sdk.base.widget.popupWindow.JvtdPopupWindow;
import com.jvtd.running_sdk.utils.JvtdUiUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public class JvtdDropDownMenu<T> extends JvtdPopupWindow implements JvtdDropDownMenuBaseAdapter.OnItemClickListener {
    public static final int ONE = 0x11;//单列选择
    public static final int TWO = 0x12;//双列选择

    @IntDef({ONE, TWO})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
    }

    private Builder mBuilder;

    private RecyclerView mOneRecyclerView;
    private JvtdDropDownMenuAdapter mOneAdapter;
    private RecyclerView mTwoRecyclerView;
    private JvtdDropDownMenuAdapter mTwoAdapter;
    private View bgLine;

    private int leftPos;

    public interface OnOneListener<T> {
        void onItemClick(int pos);

        List<T> getData();

        int getCurrentSelect();
    }

    public interface OnTwoListener<T> {
        void onLeftItemClick(int pos);

        void onRightItemClick(int leftPos, int rightPos);

        List<T> getLeftData();

        List<T> getRightData(int leftPos);

        int getCurrentLeftSelect();

        int getCurrentRightSelect();
    }

    public interface OnDismissListener {
        void onDismiss();
    }

    public JvtdDropDownMenu(Builder builder) {
        super(builder.getContext());
        this.mBuilder = builder;
    }

    @Override
    public int getLayoutRes() {
        if (mBuilder.getType() == ONE)
            return R.layout.jvtd_drop_down_menu_one_layout;
        return R.layout.jvtd_drop_down_menu_two_layout;
    }

    @Override
    public int getWidth() {
        return WindowManager.LayoutParams.MATCH_PARENT;
    }

    @Override
    public int getHeight() {
        if (mBuilder.getType() == ONE && mBuilder.getHeightPer() <= 0)
            return WindowManager.LayoutParams.WRAP_CONTENT;
        float heightPer = mBuilder.getHeightPer();
        heightPer = Math.min(heightPer, 1);
        heightPer = Math.max(heightPer, 0);
        return (int) (JvtdUiUtils.windowHeight(mBuilder.getContext()) * heightPer);
    }

    @Override
    public void bindView(View v) {
        setFocusable(true);
        mOneRecyclerView = findViewById(R.id.jvtd_drop_down_menu_one_rv);

        if (mBuilder.getType() == TWO) {
            mTwoRecyclerView = findViewById(R.id.jvtd_drop_down_menu_two_rv);
            bgLine = findViewById(R.id.jvtd_drop_down_menu_line);

            setViewPer();

            updateLine();

            mTwoRecyclerView.setBackgroundColor(mBuilder.getLeftBgColor() == 0 ? Color.WHITE : ContextCompat.getColor(mBuilder.getContext(), mBuilder.getLeftBgColor()));
        }

        mOneRecyclerView.setBackgroundColor(mBuilder.getRightBgColor() == 0 ? Color.WHITE : ContextCompat.getColor(mBuilder.getContext(), mBuilder.getRightBgColor()));

        initAdapter();
    }

    /**
     * 配置左右比例 仅双列有效
     *
     * @author Chenlei
     * created at 2018/9/27
     **/
    private void setViewPer() {
        float leftPer = 1 - mBuilder.getRightPer();
        LinearLayout.LayoutParams layoutParamsLeft = (LinearLayout.LayoutParams) mTwoRecyclerView.getLayoutParams();
        layoutParamsLeft.weight = leftPer;
        mTwoRecyclerView.setLayoutParams(layoutParamsLeft);

        //设置右侧列表比例
        LinearLayout.LayoutParams layoutParamsRight = (LinearLayout.LayoutParams) mOneRecyclerView.getLayoutParams();
        layoutParamsRight.weight = mBuilder.getRightPer();
        mOneRecyclerView.setLayoutParams(layoutParamsRight);
    }

    private void initAdapter() {
        mOneAdapter = new JvtdDropDownMenuAdapter<T>(mBuilder.getContext(), mBuilder.getRightTextBuilder(),
                new ArrayList<>(), 0);
        mOneAdapter.setOnItemClickListener(this);
        mOneRecyclerView.setLayoutManager(new JvtdDropDownMenuManager(mBuilder.getContext()));
        mOneRecyclerView.setAdapter(mOneAdapter);

        if (mBuilder.getType() == TWO) {
            mTwoAdapter = new JvtdDropDownMenuAdapter<T>(mBuilder.getContext(), mBuilder.getLeftTextBuilder(), new ArrayList<>(), 0);
            mTwoAdapter.setOnItemClickListener(pos -> {
                leftPos = pos;
                if (mBuilder.getOnTwoListener() != null) {
                    mBuilder.getOnTwoListener().onLeftItemClick(pos);
                    setRightData(mBuilder.getOnTwoListener().getRightData(leftPos), mBuilder.getOnTwoListener().getCurrentRightSelect());
                }
            });
            mTwoRecyclerView.setLayoutManager(new JvtdDropDownMenuManager(mBuilder.getContext()));
            mTwoRecyclerView.setAdapter(mTwoAdapter);
        }
    }

    private void setLeftData(List<T> leftData, int selectIndex) {
        leftPos = selectIndex;
        mTwoAdapter.setSelectIndex(selectIndex);
        mTwoAdapter.updateAdapterClearBeforeDatas(leftData);
    }

    private void setRightData(List<T> rightData, int selectIndex) {
        mOneAdapter.setSelectIndex(selectIndex);
        mOneAdapter.updateAdapterClearBeforeDatas(rightData);
    }

    @Override
    public void onItemClick(int pos) {
        if (mBuilder.getType() == ONE) {
            if (mBuilder.getOnOneListener() != null)
                mBuilder.getOnOneListener().onItemClick(pos);
        }else {
            if (mBuilder.getOnTwoListener() != null)
                mBuilder.getOnTwoListener().onRightItemClick(leftPos,pos);
        }
        dismiss();
    }

    public void show() {
        showAsDropDown(mBuilder.getTopView(), 0, 0);
        if (mBuilder.getType() == ONE){
            if (mBuilder.getOnOneListener() != null) {
                setRightData(mBuilder.getOnOneListener().getData(), mBuilder.getOnOneListener().getCurrentSelect());
                scrollToPosition(mOneRecyclerView,mBuilder.getOnOneListener().getCurrentSelect());
            }
        }else {
            if (mBuilder.getOnTwoListener() != null){
                setLeftData(mBuilder.getOnTwoListener().getLeftData(),mBuilder.getOnTwoListener().getCurrentLeftSelect());
                setRightData(mBuilder.getOnTwoListener().getRightData(mBuilder.getOnTwoListener().getCurrentLeftSelect()),mBuilder.getOnTwoListener().getCurrentRightSelect());
                scrollToPosition(mTwoRecyclerView,mBuilder.getOnTwoListener().getCurrentLeftSelect());
                scrollToPosition(mOneRecyclerView,mBuilder.getOnTwoListener().getCurrentRightSelect());
            }
        }
    }

    private void scrollToPosition(RecyclerView recyclerView, int position){
        if (position != -1) {
            recyclerView.scrollToPosition(position);
            JvtdDropDownMenuManager mLayoutManager = (JvtdDropDownMenuManager)recyclerView.getLayoutManager();
            if (mLayoutManager != null) {
                mLayoutManager.scrollToPositionWithOffset(position, 0);
            }
        }
    }

    @Override
    public void dismiss() {
        if (mBuilder.getOnDismissListener() != null)
            mBuilder.getOnDismissListener().onDismiss();
        super.dismiss();
    }

    /**
     * 设置线段样式 仅双列有效
     *
     * @author Chenlei
     * created at 2018/9/27
     **/
    private void updateLine() {
        bgLine.setBackgroundColor(mBuilder.getLineColor() == 0 ? Color.parseColor("#e8e8e8") : ContextCompat.getColor(mBuilder.getContext(), mBuilder.getLineColor()));
        bgLine.setVisibility(mBuilder.isShowLine() ? View.VISIBLE : View.GONE);
    }


    public static class TextBuilder {
        private int textSize = 16;//文字大小

        private @ColorRes
        int normalTextColor = 0;//默认文字颜色
        private @ColorRes
        int selectTextColor = 0;//选中文字颜色

        private @ColorRes
        int normalBgColor = 0;//默认背景颜色
        private @ColorRes
        int selectBgColor = 0;//选中背景颜色

        //仅支持单列 及 双列右侧
        private @DrawableRes
        int normalDrawable = 0;//默认选中图片
        private @DrawableRes
        int selectDrawable = 0;//选中状态图片

        private int height = 44;//单行高度  dp

        private boolean showLine = false;//是否显示间隔线
        private int lineMarginStart = 0;//间隔线左边距
        private int lineMarginEnd = 0;//间隔线右边距
        private @ColorRes
        int lineColor = 0;//间隔线颜色

        private boolean textLeft = true;
        private int textMarginStart = 16;//文本左边距
        private int textMarginEnd = 16;//文本右边距

        public int getTextSize() {
            return textSize;
        }

        public TextBuilder setTextSize(int textSize) {
            this.textSize = textSize;
            return this;
        }

        public int getNormalTextColor() {
            return normalTextColor;
        }

        public TextBuilder setNormalTextColor(int normalTextColor) {
            this.normalTextColor = normalTextColor;
            return this;
        }

        public int getSelectTextColor() {
            return selectTextColor;
        }

        public TextBuilder setSelectTextColor(int selectTextColor) {
            this.selectTextColor = selectTextColor;
            return this;
        }

        public int getNormalBgColor() {
            return normalBgColor;
        }

        public TextBuilder setNormalBgColor(int normalBgColor) {
            this.normalBgColor = normalBgColor;
            return this;
        }

        public int getSelectBgColor() {
            return selectBgColor;
        }

        public TextBuilder setSelectBgColor(int selectBgColor) {
            this.selectBgColor = selectBgColor;
            return this;
        }

        public int getNormalDrawable() {
            return normalDrawable;
        }

        public TextBuilder setNormalDrawable(int normalDrawable) {
            this.normalDrawable = normalDrawable;
            return this;
        }

        public int getSelectDrawable() {
            return selectDrawable;
        }

        public TextBuilder setSelectDrawable(int selectDrawable) {
            this.selectDrawable = selectDrawable;
            return this;
        }

        public int getHeight() {
            return height;
        }

        public TextBuilder setHeight(int height) {
            this.height = height;
            return this;
        }

        public boolean isShowLine() {
            return showLine;
        }

        public TextBuilder setShowLine(boolean showLine) {
            this.showLine = showLine;
            return this;
        }

        public int getLineMarginStart() {
            return lineMarginStart;
        }

        public TextBuilder setLineMarginStart(int lineMarginStart) {
            this.lineMarginStart = lineMarginStart;
            return this;
        }

        public int getLineMarginEnd() {
            return lineMarginEnd;
        }

        public TextBuilder setLineMarginEnd(int lineMarginEnd) {
            this.lineMarginEnd = lineMarginEnd;
            return this;
        }

        public int getLineColor() {
            return lineColor;
        }

        public TextBuilder setLineColor(int lineColor) {
            this.lineColor = lineColor;
            return this;
        }

        public boolean isTextLeft() {
            return textLeft;
        }

        public TextBuilder setTextLeft(boolean textLeft) {
            this.textLeft = textLeft;
            return this;
        }

        public int getTextMarginStart() {
            return textMarginStart;
        }

        public TextBuilder setTextMarginStart(int textMarginStart) {
            this.textMarginStart = textMarginStart;
            return this;
        }

        public int getTextMarginEnd() {
            return textMarginEnd;
        }

        public TextBuilder setTextMarginEnd(int textMarginEnd) {
            this.textMarginEnd = textMarginEnd;
            return this;
        }
    }

    public static class Builder<T> {
        private int mType = ONE;//样式   单列  双列
        private Context mContext;
        private View mTopView;

        private float rightPer = 0.5f;//右侧百分比

        private @ColorRes
        int lineColor = 0;//线颜色   默认颜色为#f2f2f2

        private boolean showLine = true;//是否显示分割线

        private float heightPer = 0;//弹出框高度百分比

        private @ColorRes
        int leftBgColor = 0;//左侧列表背景  默认#ffffff
        private @ColorRes
        int rightBgColor = 0;//右侧列表背景  默认#ffffff

        private TextBuilder leftTextBuilder = new TextBuilder();//左侧文本样式
        private TextBuilder rightTextBuilder = new TextBuilder();//右侧文本样式

        private OnOneListener<T> mOnOneListener;
        private OnTwoListener<T> mOnTwoListener;
        private OnDismissListener mOnDismissListener;

        public Context getContext() {
            return mContext;
        }

        public Builder<T> setContext(Context context) {
            mContext = context;
            return this;
        }

        public View getTopView() {
            return mTopView;
        }

        public Builder<T> setTopView(View topView) {
            mTopView = topView;
            return this;
        }

        public int getType() {
            return mType;
        }

        public Builder<T> setType(int type) {
            this.mType = type;
            return this;
        }

        public float getRightPer() {
            return rightPer;
        }

        public Builder<T> setRightPer(float rightPer) {
            this.rightPer = rightPer;
            return this;
        }

        public int getLineColor() {
            return lineColor;
        }

        public Builder<T> setLineColor(int lineColor) {
            this.lineColor = lineColor;
            return this;
        }

        public boolean isShowLine() {
            return showLine;
        }

        public Builder<T> setShowLine(boolean showLine) {
            this.showLine = showLine;
            return this;
        }

        public float getHeightPer() {
            if (mType == TWO && heightPer <= 0)
                return 0.5f;
            return heightPer;
        }

        public Builder<T> setHeightPer(float heightPer) {
            this.heightPer = heightPer;
            return this;
        }

        public int getLeftBgColor() {
            return leftBgColor;
        }

        public Builder<T> setLeftBgColor(int leftBgColor) {
            this.leftBgColor = leftBgColor;
            return this;
        }

        public int getRightBgColor() {
            return rightBgColor;
        }

        public Builder<T> setRightBgColor(int rightBgColor) {
            this.rightBgColor = rightBgColor;
            return this;
        }

        public TextBuilder getLeftTextBuilder() {
            return leftTextBuilder;
        }

        public Builder<T> setLeftTextBuilder(TextBuilder leftTextBuilder) {
            this.leftTextBuilder = leftTextBuilder;
            return this;
        }

        public TextBuilder getRightTextBuilder() {
            return rightTextBuilder;
        }

        public Builder<T> setRightTextBuilder(TextBuilder rightTextBuilder) {
            this.rightTextBuilder = rightTextBuilder;
            return this;
        }

        public OnOneListener<T> getOnOneListener() {
            return mOnOneListener;
        }

        public Builder<T> setOnOneListener(OnOneListener<T> onOneListener) {
            mOnOneListener = onOneListener;
            return this;
        }

        public OnTwoListener<T> getOnTwoListener() {
            return mOnTwoListener;
        }

        public Builder<T> setOnTwoListener(OnTwoListener<T> onTwoListener) {
            mOnTwoListener = onTwoListener;
            return this;
        }

        public OnDismissListener getOnDismissListener() {
            return mOnDismissListener;
        }

        public Builder<T> setOnDismissListener(OnDismissListener onDismissListener) {
            mOnDismissListener = onDismissListener;
            return this;
        }

        public JvtdDropDownMenu<T> build() {
            return new JvtdDropDownMenu<>(this);
        }
    }
}
