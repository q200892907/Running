package com.jvtd.running_sdk.base.widget.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jvtd.running_sdk.R;
import com.jvtd.running_sdk.base.widget.adapter.viewHolder.JvtdViewHolder;

import java.util.List;

public abstract class JvtdQuickAdapter<T,K extends JvtdViewHolder> extends BaseQuickAdapter<T,JvtdViewHolder>
{
  public JvtdQuickAdapter(int layoutResId, @Nullable List<T> data)
  {
    super(layoutResId, data);
  }

  @Override
  protected View getItemView(int layoutResId, ViewGroup parent)
  {
    ViewDataBinding binding = DataBindingUtil.inflate(mLayoutInflater, layoutResId, parent, false);
    if (binding == null) {
      return super.getItemView(layoutResId, parent);
    }
    View view = binding.getRoot();
    view.setTag(R.id.BaseQuickAdapter_databinding_support, binding);
    return view;
  }
}
