package com.jvtd.running_sdk.base.widget.emptyView;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.jvtd.running_sdk.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class JvtdEmptyView extends FrameLayout implements View.OnClickListener {
    //空布局
    private View mEmptyView;
    private TextView mEmptyText;//一级提醒
    private TextView mEmptyTipsText;//二级功能性提醒
    private ImageView mEmptyImg;//空布局图片
    private TextView mEmptyBtn;//功能性按钮
    //加载布局
    private View mLoadingView;
    private TextView mLoadingText;//加载提醒
    private SpinKitView mLoadingSpinKit;//加载界面
    //错误布局
    private View mErrorView;
    private ImageView mErrorImage;//异常图片
    private TextView mErrorText;//异常提醒

    @IntDef({STATE_LOADING, STATE_NORMAL,STATE_ERROR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {}

    public static final int STATE_LOADING = 1;
    public static final int STATE_NORMAL = 2;
    public static final int STATE_ERROR = 3;

    private @State int state = STATE_LOADING;

    //空布局触控方法
    public interface OnClickListener{
        void onClick(View view, @State int state);
    }
    private OnClickListener mOnClickListener;
    public JvtdEmptyView setOnClickListener(OnClickListener clickListener) {
        mOnClickListener = clickListener;
        return this;
    }

    private Context mContext;

    public JvtdEmptyView(Context context) {
        this(context,null);
    }

    public JvtdEmptyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public JvtdEmptyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initEmptyView(context,attrs);
    }

    private void initEmptyView(Context context,@Nullable AttributeSet attrs) {
        mContext = context;

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        //居中
        params.gravity = Gravity.CENTER;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.JvtdEmptyView, 0, 0);
        //数据为空时的布局
        int emptyLayout = ta.getResourceId(R.styleable.JvtdEmptyView_jvtdEmptyLayout, R.layout.jvtd_empty_view_empty_layout);
        mEmptyView = View.inflate(context, emptyLayout, null);
        mEmptyText = mEmptyView.findViewById(R.id.jvtd_empty_msg);
        mEmptyTipsText = mEmptyView.findViewById(R.id.jvtd_empty_tips);
        mEmptyImg = mEmptyView.findViewById(R.id.jvtd_empty_img);
        mEmptyBtn = mEmptyView.findViewById(R.id.jvtd_empty_founction_btn);
        mEmptyBtn.setOnClickListener(this);
        addView(mEmptyView,params);

        //加载中的布局
        int loadingLayout = ta.getResourceId(R.styleable.JvtdEmptyView_jvtdLoadingLayout, R.layout.jvtd_empty_view_loading_layout);
        mLoadingView = View.inflate(context, loadingLayout, null);
        mLoadingText = mLoadingView.findViewById(R.id.jvtd_empty_loading_msg);
        mLoadingSpinKit = mLoadingView.findViewById(R.id.jvtd_empty_loading_view);
        addView(mLoadingView,params);

        //错误时的布局
        int errorLayout = ta.getResourceId(R.styleable.JvtdEmptyView_jvtdErrorLayout, R.layout.jvtd_empty_view_error_layout);
        mErrorView = View.inflate(context, errorLayout, null);
        mErrorImage = mErrorView.findViewById(R.id.jvtd_empty_error_img);
        mErrorText = mErrorView.findViewById(R.id.jvtd_empty_error_msg);
        mErrorView.setOnClickListener(this);
        addView(mErrorView, params);

        ta.recycle();

        //全部隐藏
        setGone();
    }

    @Override
    public void onClick(View view) {
        if (mOnClickListener != null)
            mOnClickListener.onClick(view,state);
    }

    private void setGone(){
        mEmptyView.setVisibility(GONE);
        mErrorView.setVisibility(GONE);
        mLoadingView.setVisibility(GONE);
    }

    /**
     * 设置空布局图片
     *
     * @author Chenlei
     * created at 2018/9/17
     **/
    public JvtdEmptyView setEmptyImage(int resId){
        if (resId <= 0)
        {
            mEmptyImg.setVisibility(INVISIBLE);
            return this;
        }
        mEmptyImg.setVisibility(VISIBLE);
        mEmptyImg.setImageResource(resId);
        return this;
    }

    /**
     * 设置空布局文字
     *
     * @author Chenlei
     * created at 2018/9/17
     **/
    public JvtdEmptyView setEmptyText(int strId){
       return setEmptyText(mContext.getString(strId));
    }
    public JvtdEmptyView setEmptyText(String str){
        mEmptyText.setText(str);
        return this;
    }

    /**
     * 设置空布局二级提示 设置就显示
     *
     * @author Chenlei
     * created at 2018/9/17
     **/
    public JvtdEmptyView setEmptyTips(int strId){
        return setEmptyTips(mContext.getString(strId));
    }
    public JvtdEmptyView setEmptyTips(String str){
        mEmptyTipsText.setText(str);
        mEmptyTipsText.setVisibility(TextUtils.isEmpty(str) ? GONE : VISIBLE);
        return this;
    }

    /**
     * 设置空布局功能按钮 设置就显示
     *
     * @author Chenlei
     * created at 2018/9/17
     **/
    public JvtdEmptyView setEmptyBtnText(int strId){
        return setEmptyBtnText(mContext.getString(strId));
    }
    public JvtdEmptyView setEmptyBtnText(String str){
        mEmptyBtn.setText(str);
        mEmptyBtn.setVisibility(TextUtils.isEmpty(str) ? GONE : VISIBLE);
        return this;
    }

    /**
     * 设置加载布局文字
     *
     * @author Chenlei
     * created at 2018/9/17
     **/
    public JvtdEmptyView setLoadingText(int strId){
        return setLoadingText(mContext.getString(strId));
    }
    public JvtdEmptyView setLoadingText(String str){
        mLoadingText.setText(str);
        return this;
    }

    /**
     * 设置错误布局文字
     *
     * @author Chenlei
     * created at 2018/9/17
     **/
    public JvtdEmptyView setErrorText(int strId){
        return setErrorText(mContext.getString(strId));
    }
    public JvtdEmptyView setErrorText(String str){
        mErrorText.setText(str);
        return this;
    }

    /**
     * 设置错误布局图片
     *
     * @author Chenlei
     * created at 2018/9/17
     **/
    public JvtdEmptyView setErrorImage(int resId){
        if (resId <= 0)
        {
            mErrorImage.setVisibility(INVISIBLE);
            return this;
        }
        mErrorImage.setVisibility(VISIBLE);
        mErrorImage.setImageResource(resId);
        return this;
    }

    /**
     * 当前空布局状态
     *
     * @author Chenlei
     * created at 2018/9/17
     **/
    public JvtdEmptyView setState(@State int state){
        this.state = state;
        switch (state) {
            case STATE_ERROR:
                mEmptyView.setVisibility(GONE);
                mErrorView.setVisibility(VISIBLE);
                mLoadingView.setVisibility(GONE);
                break;
            case STATE_LOADING:
                mEmptyView.setVisibility(GONE);
                mErrorView.setVisibility(GONE);
                mLoadingView.setVisibility(VISIBLE);
                break;
            case STATE_NORMAL:
                mEmptyView.setVisibility(VISIBLE);
                mErrorView.setVisibility(GONE);
                mLoadingView.setVisibility(GONE);
                break;
        }
        return this;
    }
}
