package com.jvtd.running_sdk.base.widget.imageSelector;

import android.content.Context;
import android.widget.ImageView;

import com.jvtd.running_sdk.utils.glide.GlideUtils;
import com.yuyh.library.imgsel.common.ImageLoader;

public class JvtdISLoader implements ImageLoader {
    @Override
    public void displayImage(Context context, String path, ImageView imageView) {
        GlideUtils.showImage(context,path,imageView);
    }
}
