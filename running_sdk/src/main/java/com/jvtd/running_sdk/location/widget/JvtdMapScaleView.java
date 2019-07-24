package com.jvtd.running_sdk.location.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.autonavi.amap.mapcore.DPoint;
import com.autonavi.amap.mapcore.IPoint;
import com.autonavi.amap.mapcore.VirtualEarthProjection;
import com.jvtd.running_sdk.R;

public class JvtdMapScaleView extends RelativeLayout {
    //最大宽度，超过该宽度就向下一级转换
    private static final int maxWidth = 800;
    //比例尺文本
    private String text = "";
    //比例尺宽度
    private int lineWidth = 50;
    private IPoint point;
    private final int[] level = new int[]{10000000, 5000000, 2000000, 1000000, 500000, 200000, 100000, 50000, 30000, 20000, 10000, 5000, 2000, 1000, 500, 200, 100, 50, 25, 10, 5};
    private TextView mTextView;
    private RelativeLayout mLayout;

    public JvtdMapScaleView(Context context) {
        this(context, null);
    }

    public JvtdMapScaleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JvtdMapScaleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.jvtd_map_scale_view_layout, this);
        mLayout = findViewById(R.id.scale_view_rl);
        mTextView = findViewById(R.id.scale_text_view);
        this.point = new IPoint();
    }

    public void destroy() {
        this.text = null;
        this.point = null;
    }

    public void refreshScaleView(AMap aMap) {
        if (aMap == null) return;
        try {
            float zoom = aMap.getCameraPosition().zoom;

            //获取地图中心坐标
            aMap.getP20MapCenter(this.point);
            if (this.point != null) {
                DPoint dPoint = VirtualEarthProjection.pixelsToLatLong((long) this.point.x, (long) this.point.y, 20);
                float var3 = .5f;
                //屏幕上1像素点对应的地图上距离长度，单位米，等同于aMap.getScalePerPixel();
                double scalePerPixel = (double) ((float) (Math.cos(dPoint.y * 3.141592653589793D / 180.0D) * 2.0D * 3.141592653589793D * 6378137.0D / (256.0D * Math.pow(2.0D, (double) zoom))));

                //比例尺宽度
                int lineWidth = (int) ((double) this.level[(int) zoom] / (scalePerPixel * (double) var3));
                String lineText = formatDistance(this.level[(int) zoom]);

                //如果超过最大宽度，则向下一级转换
                while (lineWidth > maxWidth && zoom < 21) {
                    zoom++;
                    lineWidth = (int) ((double) this.level[(int) zoom] / (scalePerPixel * (double) var3));
                    lineText = formatDistance(this.level[(int) zoom]);
                }
                this.lineWidth = lineWidth;
                this.text = lineText;

                dPoint.recycle();

                if (mTextView != null)
                    mTextView.setText(this.text);
                if (mLayout != null){
                    LayoutParams params = (LayoutParams) mLayout.getLayoutParams();
                    params.width = this.lineWidth;
                    mLayout.setLayoutParams(params);
                }
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }

    //返回显示字符串
    public static String formatDistance(int m) {
        String distance;
        if (m < 1000) {
            distance = m + "m";
        } else {
            distance = m / 1000 + "km";
        }
        return distance;
    }
}
