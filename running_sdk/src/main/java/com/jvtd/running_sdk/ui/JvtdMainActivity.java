package com.jvtd.running_sdk.ui;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jvtd.running_sdk.R;
import com.jvtd.running_sdk.bean.JvtdLineBean;
import com.jvtd.running_sdk.constants.RunningSdk;
import com.jvtd.running_sdk.eventBus.EventCenter;
import com.jvtd.running_sdk.location.manager.JvtdLocationStatusManager;
import com.jvtd.running_sdk.location.service.JvtdLocationService;
import com.jvtd.running_sdk.receiver.JvtdLocationChangeReceiver;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.Socket;

public class JvtdMainActivity extends AppCompatActivity {
    private TextView mTextView;
    private JvtdLocationChangeReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jvtd_activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        EventBus.getDefault().register(this);
        mTextView = findViewById(R.id.info_view);

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
                Log.d("定位信息", infoViewString);
                break;
            case RunningSdk.EVENT_CODE_GPS_STATUS:
                Log.d("定位信息","GPS精度为-"+eventCenter.getData());
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
        super.onDestroy();
    }
}
