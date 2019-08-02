package com.jvtd.running_sdk.base.widget.adapter.viewHolder;

import android.databinding.ViewDataBinding;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.jvtd.running_sdk.R;


public class JvtdViewHolder extends BaseViewHolder
{
  public JvtdViewHolder(View view)
  {
    super(view);
  }

  public ViewDataBinding getBinding()
  {
    return (ViewDataBinding) itemView.getTag(R.id.BaseQuickAdapter_databinding_support);
  }
}
