package com.jvtd.running_sdk.bean;

import android.support.annotation.DrawableRes;

public interface JvtdBarData {
    String getTitle();
    @DrawableRes int getUnselectDrawable();
    @DrawableRes int getSelectDrawable();
}
