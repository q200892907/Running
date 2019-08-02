package com.jvtd.running_sdk.base.widget.imageSelector;

import android.graphics.Color;

import com.jvtd.running_sdk.R;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.config.ISCameraConfig;
import com.yuyh.library.imgsel.config.ISListConfig;

public class JvtdIS {
    private static JvtdIS instance;

    public static JvtdIS getInstance() {
        if (instance == null) {
            synchronized (ISNav.class) {
                if (instance == null) {
                    instance = new JvtdIS();
                }
            }
        }
        return instance;
    }

    private JvtdIS() {
        ISNav.getInstance().init(new JvtdISLoader());
    }

    public void toListActivity(Object source, int num, int reqCode){
        ISListConfig.Builder builder = new ISListConfig.Builder().backResId(R.drawable.icon_back).statusBarColor(Color.WHITE).titleBgColor(Color.WHITE)
                .titleColor(Color.BLACK).btnTextColor(Color.BLACK);
        if (num > 1){
            builder.maxNum(num);
            builder.multiSelect(true);
        }else {
            builder.multiSelect(false);
        }
        toListActivity(source,builder.build(),reqCode);
    }

    public void toListActivity(Object source, ISListConfig config, int reqCode) {
        ISNav.getInstance().toListActivity(source,config,reqCode);
    }

    public void toCameraActivity(Object source, ISCameraConfig config, int reqCode) {
        ISNav.getInstance().toCameraActivity(source,config,reqCode);
    }
}
