package com.jvtd.running_sdk.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.PowerManager;

import java.lang.reflect.Method;
import java.util.concurrent.ThreadFactory;

/**
 * 获得PARTIAL_WAKE_LOCK	， 保证在息屏状体下，CPU可以正常运行
 *
 * @author Chenlei
 * created at 2019-07-23
 **/
public class JvtdPowerManagerUtils {
    private static class Holder {
        public static JvtdPowerManagerUtils instance = new JvtdPowerManagerUtils();
    }

    private PowerManager pm = null;

    private PowerManager.WakeLock pmLock = null;

    private long mLastWakupTime = System.currentTimeMillis();//上次唤醒屏幕的触发时间

    private long mMinWakupInterval = 5 * 60 * 1000;//最小的唤醒时间间隔，防止频繁唤醒。默认5分钟

    private InnerThreadFactory mInnerThreadFactory = null;//内部线程工厂

    public static JvtdPowerManagerUtils getInstance() {
        return Holder.instance;
    }

    /**
     * 判断屏幕是否处于点亮状态
     *
     * @param context 上下文
     * @return boolean
     */
    public boolean isScreenOn(final Context context) {
        try {
            Method isScreenMethod = PowerManager.class.getMethod("isScreenOn", new Class[]{});
            if (pm == null) {
                pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            }
            return (boolean) (Boolean) isScreenMethod.invoke(pm);
        } catch (Exception e) {
            return true;
        }
    }


    /**
     * 唤醒屏幕
     *
     * @param context 上下文
     */
    public void wakeUpScreen(final Context context) {

        try {
            acquirePowerLock(context, PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 根据levelAndFlags，获得PowerManager的WaveLock
     * 利用worker thread去获得锁，以免阻塞主线程
     *
     * @param context       上下文
     * @param levelAndFlags 等级及标志
     */
    @SuppressLint("InvalidWakeLockTag")
    private void acquirePowerLock(final Context context, final int levelAndFlags) {
        if (context == null) {
            throw new NullPointerException("when invoke aquirePowerLock ,  context is null which is unacceptable");
        }

        long currentMills = System.currentTimeMillis();

        if (currentMills - mLastWakupTime < mMinWakupInterval) {
            return;
        }

        mLastWakupTime = currentMills;

        if (mInnerThreadFactory == null) {
            mInnerThreadFactory = new InnerThreadFactory();
        }

        mInnerThreadFactory.newThread(() -> {
            if (pm == null) {
                pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            }

            if (pmLock != null) { // release
                pmLock.release();
                pmLock = null;
            }

            pmLock = pm.newWakeLock(levelAndFlags, "JvtdPowerManager");
            pmLock.acquire();
            pmLock.release();
        }).start();
    }

    /**
     * 线程工厂
     */
    private class InnerThreadFactory implements ThreadFactory {
        @Override
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable);
        }
    }
}
