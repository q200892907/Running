package com.jvtd.running_sdk.utils.glide;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jvtd.running_sdk.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Administrator on 2018/2/2.
 */

public class GlideUtils {
    public static final int CENTER_CROP = 0;
    public static final int CENTER_INSIDE = 1;
    public static final int FIT_CENTER = 2;

    @IntDef({CENTER_CROP, CENTER_INSIDE, FIT_CENTER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ScaleType {}

    public static void showImage(Context context, Object path, ImageView imageView) {
        showImage(context,path,imageView,CENTER_CROP);
    }

    public static void showImage(Context context, Object path, ImageView imageView,@ScaleType int scaleType) {
        showImage(context,path,imageView,0,scaleType);
    }

    public static void showImageAndError(Context context, Object path, ImageView imageView,int errorId){
        showImage(context,path,imageView,errorId,CENTER_CROP);
    }
    /**
     * 显示图片
     *
     * @author Chenlei
     * created at 2018/9/19
     **/
    public static void showImage(Context context, Object path, ImageView imageView, int errorId, @ScaleType int scaleType) {
        if (errorId == 0)
            errorId = R.drawable.icon_image_empty;
        RequestOptions options;
        if (path instanceof Integer)
            options = GlideRequestOptionsUtils.getOptions(null);
        else
            options = GlideRequestOptionsUtils.getOptions(ContextCompat.getDrawable(context, errorId));
        switch (scaleType) {
            case CENTER_CROP:
                options.centerCrop();
                break;
            case CENTER_INSIDE:
                options.centerInside();
                break;
            case FIT_CENTER:
                options.fitCenter();
                break;
        }
        Glide.with(context)
                .load(path)
                .apply(options)
                .into(imageView);
    }
}
