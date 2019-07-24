package com.jvtd.running_sdk.location;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.VisibleRegion;
import com.jvtd.running_sdk.R;
import com.jvtd.running_sdk.location.widget.JvtdMapScaleView;
import com.jvtd.running_sdk.utils.JvtdUiUtils;

/**
 * 地图界面
 * 作者:chenlei
 * 时间:2019-07-24 09:26
 */
public class JvtdMapView extends RelativeLayout implements AMap.OnMyLocationChangeListener, AMap.OnCameraChangeListener {
    private static final String TAG = "JvtdMapView";
    private Context mContext;

    private TextureMapView mMapView;//地图视图
    private AMap mAMap;//地图控制
    private UiSettings mUiSettings;//地图ui样式
    private JvtdMapScaleView mMapScaleView;//比例尺
    private ImageView mCompassView;//指北针

    private float lastBearing = 0;//指北针上次方向

    private LatLng mChinaLatLng = new LatLng(39.908692, 116.397477);//默认地点
    private LatLng mLocation;//当前位置

    private boolean isMove = true;//第一次是否移动到我的位置

    private boolean isShowMyLocation = true;//是否显示我的位置信息
    private int mMaxZoom = 20;//最大级别
    private int mMinZoom = 3;//最小级别
    private int mLocationInterval = 10000;//定位间隔

    private AMap.OnMyLocationChangeListener mOnMyLocationChangeListener;

    public JvtdMapView(Context context) {
        this(context, null);
    }

    public JvtdMapView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JvtdMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initMapView();
    }

    /**
     * 初始化地图页面
     */
    private void initMapView() {
        LayoutInflater.from(mContext).inflate(R.layout.jvtd_map_view_layout, this);
        mMapView = findViewById(R.id.amap_view);
        mMapScaleView = findViewById(R.id.jvtd_map_scale_view);
        mCompassView = findViewById(R.id.jvtd_map_compass);
        updateSetting();
    }

    /**
     * 更新当前地图设置
     */
    private void updateSetting() {
        if (mAMap == null)
            mAMap = mMapView.getMap();
        showMyLocation(isShowMyLocation);
        mAMap.setOnMyLocationChangeListener(this);//设置我的位置改变监听
//        mAMap.setOnMarkerClickListener(this);//设置marker触控事件
//        mAMap.setOnMapTouchListener(this);//设置map触控事件
        mAMap.setOnCameraChangeListener(this);//设置视图camera改变监听
        mAMap.setLoadOfflineData(true);//加载离线地图

        if (mUiSettings == null)
            mUiSettings = mAMap.getUiSettings();
        mUiSettings.setLogoBottomMargin(-1000);//移除logo
        mUiSettings.setZoomControlsEnabled(false);//关闭自带级别控制器
        mUiSettings.setScaleControlsEnabled(false);//关闭自带比例尺

        //点击恢复正北方向
        mCompassView.setOnClickListener(view -> {
            CameraPosition cameraPosition = mAMap.getCameraPosition();
            float bearing = 0.0f;  // 地图默认方向
            mAMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(cameraPosition.target, cameraPosition.zoom, cameraPosition.tilt, bearing)));
        });
    }

    /**
     * 设置我的位置更新回调
     *
     * @param onMyLocationChangeListener 回调方法
     * @return 控件自身
     */
    public JvtdMapView setOnMyLocationChangeListener(AMap.OnMyLocationChangeListener
                                                             onMyLocationChangeListener) {
        mOnMyLocationChangeListener = onMyLocationChangeListener;
        return this;
    }

    /**
     * 设置最大缩放级别
     *
     * @param maxZoom 最大缩放级别 最大20
     * @return 控件自身
     */
    public JvtdMapView setMaxZoom(int maxZoom) {
        mMaxZoom = zoomRange(maxZoom);
        return this;
    }

    /**
     * 设置最小缩放级别
     *
     * @param minZoom 最小缩放级别 最小3
     * @return 控件自身
     */
    public JvtdMapView setMinZoom(int minZoom) {
        mMinZoom = zoomRange(minZoom);
        return this;
    }

    /**
     * 获取正确级别信息
     *
     * @param zoom 缩放级别
     * @return 正确级别
     */
    private int zoomRange(int zoom) {
        if (zoom > 20) return 20;
        else if (zoom < 3) return 3;
        else return zoom;
    }

    /**
     * 设置定位间隔时间
     *
     * @param locationInterval 定位间隔时间 毫秒 最小1000
     * @return 控件自身
     */
    public JvtdMapView setLocationInterval(int locationInterval) {
        if (locationInterval < 1000) locationInterval = 1000;
        mLocationInterval = locationInterval;
        return this;
    }

    /**
     * 指北针方向
     *
     * @param bearing 方向
     */
    private void startMapCompass(float bearing) {
        bearing = 360 - bearing;
        RotateAnimation rotateAnimation = new RotateAnimation(lastBearing, bearing, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setFillAfter(true);

        mCompassView.startAnimation(rotateAnimation);
        lastBearing = bearing;
    }

    /**
     * @param top 指北针距离上方距离
     */
    public void compassTopMargin(int top) {
        if (top < 24) {
            top = 24;
        }
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mCompassView.getLayoutParams();
        layoutParams.topMargin = JvtdUiUtils.dp2px(mContext, top);
    }

    /**
     * 显示与隐藏指北针及比例尺
     *
     * @param show 是否显示
     */
    public void showCompassEnabled(boolean show) {
//        mUiSettings.setCompassEnabled(show);
        mMapScaleView.setVisibility(show ? VISIBLE : GONE);
        mCompassView.setVisibility(show ? VISIBLE : GONE);
    }

    /**
     * 是否打开所有控制
     *
     * @param b 是否打开
     */
    public void setAllGesturesEnabled(boolean b) {
        if (mUiSettings != null)
            mUiSettings.setAllGesturesEnabled(b);
    }

    /**
     * 获取地图AMap
     *
     * @return amap
     */
    public AMap getAMap() {
        return mAMap;
    }

    /**
     * 获取可视区域
     *
     * @return 可视区域
     */
    public VisibleRegion getVisibleRegion() {
        return mAMap.getProjection().getVisibleRegion();
    }

    /**
     * 地图放大
     */
    public void zoomAdd() {
        setZoom(mAMap.getCameraPosition().zoom + 1);
    }

    /**
     * 地图缩小
     */
    public void zoomSub() {
        setZoom(mAMap.getCameraPosition().zoom - 1);
    }

    /**
     * 获取当前地图级别
     *
     * @return 当前地图级别
     */
    public int getZoom() {
        return (int) mAMap.getCameraPosition().zoom;
    }

    /**
     * 设置地图显示级别
     *
     * @param zoom 地图显示级别
     */
    public void setZoom(float zoom) {
        zoom = zoom < mMaxZoom ? zoom : mMaxZoom;
        zoom = zoom > mMinZoom ? zoom : mMinZoom;
        mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mAMap.getCameraPosition().target, zoom));
    }

    /**
     * 获取当前位置信息
     *
     * @return 位置信息
     */
    public LatLng getLocation() {
        return mLocation;
    }

    //获取地图当前我的位置
    public LatLng getMapLatLng() {
        if (mMapView.getMap().getMyLocation() == null)
            return null;
        return new LatLng(mMapView.getMap().getMyLocation().getLatitude(), mMapView.getMap().getMyLocation().getLongitude());
    }

    /**
     * 默认17视图等级
     * 移动地图镜头到指定位置，如果指定位置为空，则默认北京天安门
     *
     * @param latLng 位置
     */
    public void move(LatLng latLng) {
        if (latLng.longitude == 0.0 || latLng.latitude == 0.0) {
            latLng = mChinaLatLng;
        }
        if (mAMap != null)
            mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
    }

    /**
     * 根据当前视图等级
     * 移动地图镜头到指定位置，如果指定位置为空，则默认北京天安门
     *
     * @param latLng 位置
     */
    public void moveWithCurrentZoom(LatLng latLng) {
        if (latLng.longitude == 0.0 || latLng.latitude == 0.0) {
            latLng = mChinaLatLng;
        }
        if (mAMap != null)
            mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, mAMap.getCameraPosition().zoom));
    }

    /**
     * 显示我的位置
     *
     * @param show 是否显示
     */
    public void showMyLocation(boolean show) {
        showMyLocation(show, R.drawable.icon_map_my_location);
    }

    public void showMyLocation(boolean show, int res) {
        isShowMyLocation = show;
        if (show) {
            MyLocationStyle myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle
            // .myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
            // 连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
            myLocationStyle.interval(mLocationInterval); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
            //连续定位、蓝点不会移动到地图中心点，并且蓝点会跟随设备移动。
            myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                    .fromResource(res));
            myLocationStyle.anchor(0.5f, 0.5f);
            myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
            myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
            mAMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
            mAMap.setMaxZoomLevel(mMaxZoom);
            mAMap.setMinZoomLevel(mMinZoom);
        }
        mAMap.setMyLocationEnabled(show);//是否显示我的位置
        mAMap.setMaxZoomLevel(mMaxZoom);//最大级别设置
        mAMap.setMinZoomLevel(mMinZoom);//最小级别设置
    }

    @Override
    public void onMyLocationChange(Location location) {
        mLocation = new LatLng(location.getLatitude(), location.getLongitude());

        if (isMove) {
            isMove = false;
            move(mLocation);
        }
        if (mOnMyLocationChangeListener != null)
            mOnMyLocationChangeListener.onMyLocationChange(location);
    }

    /**
     * 创建地图
     * Activity onCreate 执行
     */
    public void onCreate(Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);
    }

    /**
     * 销毁地图
     */
    public void onDestroy() {
        mMapView.onDestroy();
        mMapScaleView.destroy();
    }

    /**
     * 重新绘制地图
     */
    public void onResume() {
        mMapView.onResume();
        updateSetting();
    }

    /**
     * 暂停地图的绘制
     *
     * @author Chenlei
     * created at 2018/4/8
     **/
    public void onPause() {
        mMapView.onPause();
    }

    /**
     * 保存地图当前的状态
     * Activity onSaveInstanceState 执行
     */
    public void onSaveInstanceState(Bundle outState) {
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 指北针相关回调
     *
     * @param cameraPosition 视图信息
     */
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        if (cameraPosition == null) return;
        mMapScaleView.refreshScaleView(mAMap);
        //旋转指北针
        startMapCompass(cameraPosition.bearing);
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {

    }
}
