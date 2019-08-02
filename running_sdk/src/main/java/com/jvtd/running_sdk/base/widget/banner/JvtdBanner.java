package com.jvtd.running_sdk.base.widget.banner;

import android.content.Context;
import android.util.AttributeSet;

import com.youth.banner.Banner;

public class JvtdBanner extends Banner {
    private Context mContext;

    public JvtdBanner(Context context) {
        this(context,null);
    }

    public JvtdBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JvtdBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initBanner(context);
    }

    private void initBanner(Context context) {
        mContext = context;
        setImageLoader(new JvtdBannerImageLoader());
    }
}
