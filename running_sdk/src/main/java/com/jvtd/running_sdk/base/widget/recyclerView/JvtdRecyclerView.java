package com.jvtd.running_sdk.base.widget.recyclerView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jvtd.running_sdk.base.widget.emptyView.JvtdEmptyView;
import com.jvtd.running_sdk.base.widget.loadMore.JvtdLoadMoreView;
import com.jvtd.running_sdk.utils.JvtdSmartRefreshUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public abstract class JvtdRecyclerView<T> extends RecyclerView {
    protected Context mContext;//上下文
    private BaseQuickAdapter<T, BaseViewHolder> mAdapter;//标准adapter
    private SmartRefreshLayout mRefreshLayout;//标准下拉刷新
    private JvtdEmptyView mEmptyView;//空布局
    private JvtdEmptyView.OnClickListener mOnClickListener;

    public JvtdRecyclerView(@NonNull Context context) {
        this(context,null);
    }

    public JvtdRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public JvtdRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initRecyclerView(context);
    }

    /**
     * 初始化recyclerview
     *
     * @author Chenlei
     * created at 2018/9/17
     **/
    private void initRecyclerView(Context context) {
        mContext = context;
        mAdapter = initAdapter();
        setLayoutManager(initLayoutManager());
        if (getLayoutManager() instanceof GridLayoutManager){
            mAdapter.setHeaderViewAsFlow(false);
            mAdapter.setFooterViewAsFlow(false);
        }
        mAdapter.bindToRecyclerView(this);
//        setAdapter(mAdapter);
    }

    /**
     * 是否嵌套使用recyclerview
     * 解决卡顿
     * @author Chenlei
     * created at 2018/9/17
     **/
    public JvtdRecyclerView setNested(boolean isNested){
        setHasFixedSize(!isNested);
        setNestedScrollingEnabled(!isNested);
        return this;
    }

    /**
     * 开启空布局
     *
     * @author Chenlei
     * created at 201817
     **/
    public JvtdRecyclerView openEmptyView(JvtdEmptyView.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
        mAdapter.setEmptyView(getEmptyView());
        getEmptyView().setOnClickListener((view, state) -> {
            if (state == JvtdEmptyView.STATE_ERROR)
                settingEmptyView();
            if (mOnClickListener != null)
                mOnClickListener.onClick(view,state);
        });
        settingEmptyView();
        return this;
    }
    
    /**
     * 首次加载失败
     *
     * @author Chenlei
     * created at 2018/9/17
     **/
    public JvtdRecyclerView firstLoadFailed(){
        finishRefresh();
        if (mAdapter.getEmptyView() != null) {
            mAdapter.setHeaderAndEmpty(false);
            mAdapter.setNewData(new ArrayList<>());
            getEmptyView().setState(JvtdEmptyView.STATE_ERROR);
            if (mAdapter.getEmptyView().getVisibility() == GONE)
                mAdapter.getEmptyView().setVisibility(VISIBLE);
        }
        return this;
    }

    /**
     * 添加头布局
     *
     * @author Chenlei
     * created at 2018/9/17
     **/
    public JvtdRecyclerView addHeaderView(View headerView){
        return addHeaderView(headerView,0);
    }
    public JvtdRecyclerView addHeaderView(View headerView,int index) {
        mAdapter.setHeaderView(headerView,index);
        return this;
    }
    
    /**
     * 开启加载更多
     *
     * @author Chenlei
     * created at 2018/9/17
     **/
    public JvtdRecyclerView setOnLoadMoreListener(BaseQuickAdapter.RequestLoadMoreListener
                                              requestLoadMoreListener) {
        mAdapter.setLoadMoreView(new JvtdLoadMoreView());
        mAdapter.setOnLoadMoreListener(requestLoadMoreListener, this);
        return this;
    }
    
    /**
     * 加载更多失败
     *
     * @author Chenlei
     * created at 2018/9/17
     **/
    public JvtdRecyclerView loadMoreFailed(){
        mAdapter.loadMoreFail();
        return this;
    }
    
    /**
     * 设置数据
     *
     * @author Chenlei
     * created at 2018/9/17
     **/
    public JvtdRecyclerView setData(List<T> data){
        finishRefresh();
        if (mAdapter.getEmptyView() != null) {
            if (mAdapter.getEmptyView().getVisibility() == GONE)
                mAdapter.getEmptyView().setVisibility(VISIBLE);
            getEmptyView().setState(JvtdEmptyView.STATE_NORMAL);
        }
        mAdapter.setHeaderAndEmpty(true);
        mAdapter.setNewData(data);
        if (data != null && data.size() < getLoadNum())
            mAdapter.disableLoadMoreIfNotFullPage();
        return this;
    }
    
    /**
     * 加载更多数据
     *
     * @author Chenlei
     * created at 2018/9/17
     **/
    public JvtdRecyclerView loadData(List<T> data){
        if (mRefreshLayout != null)
            mRefreshLayout.setEnableRefresh(true);
        mAdapter.addData(data);
        if (data.size() < getLoadNum()){
            mAdapter.loadMoreEnd();
        }else {
            mAdapter.loadMoreComplete();
        }
        return this;
    }
    
    /**
     * 获取数据源
     *
     * @author Chenlei
     * created at 2018/9/17
     **/
    public List<T> getData(){
        return mAdapter.getData();
    }
    
    /**
     * 获取adapter
     *
     * @author Chenlei
     * created at 2018/9/17
     **/
    public BaseQuickAdapter getAdapter(){
        return mAdapter;
    }

    /**
     * 设置下拉刷新  自定义样式后传入
     *
     * @author Chenlei
     * created at 2018/9/17
     **/
    public JvtdRecyclerView setRefreshLayout(SmartRefreshLayout refreshLayout){
        mRefreshLayout = refreshLayout;
        return this;
    }
    /**
     * 通用下拉  无样式
     *
     * @author Chenlei
     * created at 2018/9/17
     **/
    public JvtdRecyclerView setRefreshLayout(SmartRefreshLayout refreshLayout, OnRefreshListener onRefreshListener) {
        JvtdSmartRefreshUtils.initRefresh(mContext,refreshLayout,onRefreshListener);
        return setRefreshLayout(refreshLayout);
    }

    //初始化adapter
    public abstract BaseQuickAdapter initAdapter();
    //初始化布局管理
    public abstract RecyclerView.LayoutManager initLayoutManager();
    //初始化空布局
    public JvtdEmptyView getEmptyView(){
        if (mEmptyView == null){
            mEmptyView = new JvtdEmptyView(mContext);
            configEmptyView(mEmptyView);
        }
        return mEmptyView;
    }
    //配置空布局信息
    protected void configEmptyView(JvtdEmptyView emptyView){

    }

    //recyclerView滑动速度
    @Override
    public boolean fling(int velocityX, int velocityY)
    {
        double ratio;
        if (getFlingRatio() > 1){
            ratio = 1;
        }else if (getFlingRatio() < 0.1){
            ratio = 0.1;
        }else {
            ratio = getFlingRatio();
        }
        velocityX *= ratio;
        velocityY *= ratio;
        return super.fling(velocityX, velocityY);
    }

    //配置滑动recyclerView的速度 0.1~1之间
    public double getFlingRatio(){
        return 1.0;
    }
    //是否显示首次加载界面
    public abstract boolean isFirst();
    //一次加载数量
    public abstract int getLoadNum();

    private void finishRefresh(){
        if (mRefreshLayout != null && mRefreshLayout.isRefreshing())
            mRefreshLayout.finishRefresh();
    }

    private void settingEmptyView(){
        if (isFirst()){
            getEmptyView().setState(JvtdEmptyView.STATE_LOADING);
        }else {
            getEmptyView().setState(JvtdEmptyView.STATE_NORMAL);
            mAdapter.getEmptyView().setVisibility(GONE);
        }
    }
}
