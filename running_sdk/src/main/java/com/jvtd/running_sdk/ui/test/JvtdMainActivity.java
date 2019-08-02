package com.jvtd.running_sdk.ui.test;

import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.jvtd.running_sdk.R;
import com.jvtd.running_sdk.base.JvtdMvpActivity;
import com.jvtd.running_sdk.base.JvtdMvpPresenter;
import com.jvtd.running_sdk.bean.JvtdLineBean;
import com.jvtd.running_sdk.constants.RunningSdk;
import com.jvtd.running_sdk.eventBus.EventCenter;
import com.jvtd.running_sdk.location.JvtdMapView;
import com.jvtd.running_sdk.location.manager.JvtdLocationStatusManager;
import com.jvtd.running_sdk.location.service.JvtdLocationService;
import com.jvtd.running_sdk.receiver.JvtdLocationChangeReceiver;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.net.Socket;

public class JvtdMainActivity extends JvtdMvpActivity implements JvtdMainMvpView, AMap.OnMyLocationChangeListener {
    private static final String TAG = "测试地图页面";

    private JvtdMainPresenter<JvtdMainMvpView> mPresenter;

    private TextView mTextView;
    private JvtdLocationChangeReceiver receiver;
    private JvtdMapView mMapView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapView.onCreate(savedInstanceState);
    }

    @Override
    protected void bindView() {
        toolbar = findViewById(R.id.tool_bar);
        mTextView = findViewById(R.id.info_view);
        mMapView = findViewById(R.id.jvtd_map_view);
    }

    @Override
    protected void getLayout() {
        setContentView(R.layout.jvtd_activity_main);
    }

    @Override
    protected void initViewAndData() {
        mPresenter = new JvtdMainPresenter<>(this,this);
        mPresenter.test();
        TextView textView = toolbar.findViewById(R.id.tool_bar_title);
        textView.setText(R.string.app_name);
        setToolbar(toolbar,true);
        mMapView.setOnMyLocationChangeListener(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RunningSdk.LOCATION_IN_BACKGROUND);
        receiver = new JvtdLocationChangeReceiver();
        registerReceiver(receiver, intentFilter);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            JvtdLocationStatusManager.getInstance().resetToInit(getApplicationContext());
            JvtdLocationStatusManager.getInstance().startLocationService(getApplicationContext());
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     * 事件处理
     *
     * @param eventCenter 事件实体类
     */
    public void activityOnEvent(EventCenter eventCenter)
    {
        String infoViewString = getString(R.string.app_name);
        switch (eventCenter.getEventCode()) {
            case RunningSdk.EVENT_CODE_LOCATION:
                infoViewString = JvtdLineBean.getInstance().getLocations().get(JvtdLineBean.getInstance().getLocations().size()-1).toString();
                Log.d(TAG, infoViewString);
                break;
            case RunningSdk.EVENT_CODE_GPS_STATUS:
                Log.d(TAG,"GPS精度为-"+eventCenter.getData());
//                infoViewString = "GPS精度为-"+eventCenter.getData();
                return;
            default:
                break;
        }
        mTextView.setText(infoViewString);
    }

    @Override
    protected void onDestroy()
    {
        JvtdLineBean.getInstance().reset();
        JvtdLocationStatusManager.getInstance().stopLocationService(getApplicationContext());
        unregisterReceiver(receiver);
        mMapView.onDestroy();
        mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void onMyLocationChange(Location location) {
        Log.d(TAG,location.toString());
    }

    @Override
    public void test(String str) {
        showMessage(str);
    }
}
