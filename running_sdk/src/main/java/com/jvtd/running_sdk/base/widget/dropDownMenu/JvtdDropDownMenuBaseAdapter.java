package com.jvtd.running_sdk.base.widget.dropDownMenu;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by chenlei on 2017/12/13.
 */

public abstract class JvtdDropDownMenuBaseAdapter<T> extends RecyclerView.Adapter<JvtdDropDownMenuViewHolder>
{
  protected Context mContext;
  private List<T> mDatas;
  private int mLayoutId;
  public OnItemClickListener mListener;

  public JvtdDropDownMenuBaseAdapter(Context context, List<T> datas, int layoutId)
  {
    mDatas = datas;
    mContext = context;
    mLayoutId = layoutId;
  }

  @NonNull
  @Override
  public JvtdDropDownMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
  {
    return JvtdDropDownMenuViewHolder.get(mContext, parent, mLayoutId);
  }

  @Override
  public void onBindViewHolder(@NonNull JvtdDropDownMenuViewHolder holder, int position)
  {
    convert(holder, mDatas.get(position), position);
  }

  @Override
  public int getItemCount()
  {
    return mDatas.size();
  }

  public abstract void convert(JvtdDropDownMenuViewHolder holder, T t, int position);

  public void updateAdapterClearBeforeDatas(List<T> list)
  {
    mDatas.clear();
    mDatas.addAll(list);
    notifyDataSetChanged();
  }

  /**
   * 延时启动
   *
   * @param view View
   */
  protected final void postDelayed(View view)
  {
    view.postDelayed(this::postDelayedStart, 200);
  }

  protected abstract void postDelayedStart();

  public interface OnItemClickListener
  {
    void onItemClick(int pos);
  }

  public void setOnItemClickListener(OnItemClickListener listener)
  {
    mListener = listener;
  }
}
