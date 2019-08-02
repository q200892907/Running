package com.jvtd.running_sdk.utils;

import android.content.Context;
import android.os.Environment;

import com.jvtd.running_sdk.R;
import com.whieenz.LogCook;

/**
 * 崩溃日志工具类
 *
 * @author Chenlei
 * created at 2018/9/19
 **/
public class JvtdCrashUtils {
    public static void initCrashLog(Context context) {
        String crashPath = String.format("%s/%s", Environment.getExternalStorageDirectory().getPath(), context.getString(R.string.app_crash_dir_name));
        LogCook.getInstance() // 单例获取LogCook实例
                .setLogPath(crashPath) //设置日志保存路径
                .setLogName("test.log") //设置日志文件名
                .isOpen(true)  //是否开启输出日志
                .isSave(true)  //是否保存日志
                .initialize(); //完成吃初始化Crash监听
    }
}
