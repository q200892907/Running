package com.jvtd.running_sdk.location.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.jvtd.running_sdk.JvtdLocationHelperServiceAidl;
import com.jvtd.running_sdk.JvtdLocationServiceAidl;
import com.jvtd.running_sdk.utils.JvtdLocationUtils;

/**
 * @author Chenlei
 * created at 2019-07-23
 **/
public class JvtdLocationHelperService extends Service {

    private JvtdLocationUtils.CloseServiceReceiver mCloseReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        startBind();
        mCloseReceiver = new JvtdLocationUtils.CloseServiceReceiver(this);
        registerReceiver(mCloseReceiver, JvtdLocationUtils.getCloseServiceFilter());
    }

    @Override
    public void onDestroy() {
        if (mInnerConnection != null) {
            unbindService(mInnerConnection);
            mInnerConnection = null;
        }

        if (mCloseReceiver != null) {
            unregisterReceiver(mCloseReceiver);
            mCloseReceiver = null;
        }

        super.onDestroy();
    }

    private ServiceConnection mInnerConnection;

    private void startBind() {
        final String locationServiceName = "com.jvtd.running_sdk.location.service.JvtdLocationService";
        mInnerConnection = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                Intent intent = new Intent();
                intent.setAction(locationServiceName);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(JvtdLocationUtils.getExplicitIntent(getApplicationContext(), intent));
                } else
                    startService(JvtdLocationUtils.getExplicitIntent(getApplicationContext(), intent));
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                JvtdLocationServiceAidl l = JvtdLocationServiceAidl.Stub.asInterface(service);
                try {
                    l.onFinishBind();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };

        Intent intent = new Intent();
        intent.setAction(locationServiceName);
        bindService(JvtdLocationUtils.getExplicitIntent(getApplicationContext(), intent), mInnerConnection, Service.BIND_AUTO_CREATE);
    }

    private HelperBinder mBinder;

    private class HelperBinder extends JvtdLocationHelperServiceAidl.Stub {
        @Override
        public void onFinishBind(int notiId) {
            startForeground(notiId, JvtdLocationUtils.buildNotification(JvtdLocationHelperService.this.getApplicationContext()));
            stopForeground(true);
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (mBinder == null) {
            mBinder = new HelperBinder();
        }
        return mBinder;
    }
}
