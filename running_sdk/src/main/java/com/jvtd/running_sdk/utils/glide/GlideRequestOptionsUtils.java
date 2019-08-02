package com.jvtd.running_sdk.utils.glide;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class GlideRequestOptionsUtils
{
  private static RequestOptions options;

  @SuppressLint("CheckResult")
  public static RequestOptions getOptions(Drawable error)
  {
    if (options == null)
      options = new RequestOptions().centerCrop().priority(Priority.HIGH).diskCacheStrategy(DiskCacheStrategy.ALL);
    if (error != null) {
      options.placeholder(error);
      options.error(error);
    }
    return options;
  }
}
