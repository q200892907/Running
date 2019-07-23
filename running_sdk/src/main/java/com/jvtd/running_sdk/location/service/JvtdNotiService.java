package com.jvtd.running_sdk.location.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.jvtd.running_sdk.JvtdLocationHelperServiceAidl;
import com.jvtd.running_sdk.JvtdLocationServiceAidl;
import com.jvtd.running_sdk.utils.JvtdLocationUtils;

/**
 * 利用双service进行notification绑定，进而将Service的OOM_ADJ提高到1
 * 同时利用LocationHelperService充当守护进程，在NotiService被关闭后，重启他。（如果LocationHelperService被停止，NotiService不负责唤醒)
 * @author Chenlei
 * created at 2019-07-23
 **/
public class JvtdNotiService extends Service {

    /**i
     * startForeground的 noti_id
     */
    private static int NOTI_ID = 20180409;

    private JvtdLocationUtils.CloseServiceReceiver mCloseReceiver;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mCloseReceiver = new JvtdLocationUtils.CloseServiceReceiver(this);
        registerReceiver(mCloseReceiver, JvtdLocationUtils.getCloseServiceFilter());
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        if (mCloseReceiver != null) {
            unregisterReceiver(mCloseReceiver);
            mCloseReceiver = null;
        }

        super.onDestroy();
    }


    private final String mHelperServiceName = "com.jvtd.running_sdk.location.service.JvtdLocationHelperService";

    /**
     * 触发利用notification增加进程优先级
     */
    protected void applyNotiKeepMech() {
        startForeground(NOTI_ID, JvtdLocationUtils.buildNotification(getBaseContext()));
//        stopForeground(true);
        startBindHelperService();
    }

    public void unApplyNotiKeepMech() {
        stopForeground(true);
    }

    public Binder mBinder;

    public class LocationServiceBinder extends JvtdLocationServiceAidl.Stub {
        public void onFinishBind() {
        }
    }

    private JvtdLocationHelperServiceAidl mHelperAIDL;

    private void startBindHelperService() {
        connection = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                //doing nothing
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                JvtdLocationHelperServiceAidl l = JvtdLocationHelperServiceAidl.Stub.asInterface(service);
                mHelperAIDL = l;
                try {
                    l.onFinishBind(NOTI_ID);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        Intent intent = new Intent();
        intent.setAction(mHelperServiceName);
        bindService(JvtdLocationUtils.getExplicitIntent(getApplicationContext(), intent), connection, Service.BIND_AUTO_CREATE);
    }

    private ServiceConnection connection;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (mBinder == null) {
            mBinder = new LocationServiceBinder();
        }
        return mBinder;
    }

}
