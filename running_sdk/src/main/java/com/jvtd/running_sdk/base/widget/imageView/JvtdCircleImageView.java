package com.jvtd.running_sdk.base.widget.imageView;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 圆形图片
 *
 * @author Chenlei
 * created at 2019-08-02
 **/
public class JvtdCircleImageView extends JvtdRcImageView {
    public JvtdCircleImageView(Context context) {
        this(context, null);
    }

    public JvtdCircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JvtdCircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setRoundAsCircle(true);
        setClipBackground(true);
    }
}
