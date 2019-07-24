package com.jvtd.running_sdk.ui;

import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.jvtd.running_sdk.R;
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

import java.net.Socket;

public class JvtdMainActivity extends AppCompatActivity implements AMap.OnMyLocationChangeListener {
    private static final String TAG = "测试地图页面";

    private TextView mTextView;
    private JvtdLocationChangeReceiver receiver;
    private JvtdMapView mMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jvtd_activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        EventBus.getDefault().register(this);
        mTextView = findViewById(R.id.info_view);

        mMapView = findViewById(R.id.jvtd_map_view);
        mMapView.onCreate(savedInstanceState);
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
    @Subscribe(threadMode = ThreadMode.MAIN)
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
                infoViewString = "GPS精度为-"+eventCenter.getData();
                break;
            default:
                break;
        }
        mTextView.setText(infoViewString);
    }

    @Override
    protected void onDestroy()
    {
        EventBus.getDefault().unregister(this);
        JvtdLineBean.getInstance().reset();
        JvtdLocationStatusManager.getInstance().stopLocationService(getApplicationContext());
        unregisterReceiver(receiver);
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onMyLocationChange(Location location) {
        Log.d(TAG,location.toString());
    }
}
