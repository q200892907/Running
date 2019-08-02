package com.jvtd.running_sdk.base.widget.banner;

import android.content.Context;
import android.widget.ImageView;

import com.jvtd.running_sdk.R;
import com.jvtd.running_sdk.utils.glide.GlideUtils;
import com.youth.banner.loader.ImageLoader;

public class JvtdBannerImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        GlideUtils.showImageAndError(context,path,imageView, R.drawable.icon_placeholder_banner);
    }
}
