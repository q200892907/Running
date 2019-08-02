package com.jvtd.running_sdk.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class JvtdDateUtils {
    public static final String YMDHMS = "yyyy-MM-dd HH:mm:ss";//年月日 时分秒
    public static final String YMDHM = "yyyy-MM-dd HH:mm";//年月日 时分
    public static final String YMD = "yyyy-MM-dd";//年月日
    public static final String HMS = "HH:mm:ss";//时分秒

    /**
     * 时间戳转字符串
     *
     * @author Chenlei
     * created at 2018/9/14
     **/
    public static String long2String(String pattern,long millisecond) {
        SimpleDateFormat sDateTimeFormat = new SimpleDateFormat(pattern, Locale.CHINA);
        Date mDate = new Date(millisecond);
        return sDateTimeFormat.format(mDate);
    }

    /**
     * 时间戳转年月日时分秒
     *
     * @author Chenlei
     * created at 2018/9/14
     **/
    public static String long2StringYMDHMS(long millisecond){
        return long2String(YMDHMS,millisecond);
    }

    /**
     * 时间戳转年月日时分
     *
     * @author Chenlei
     * created at 2018/9/14
     **/
    public static String long2StringYMDHM(long millisecond){
        return long2String(YMDHM,millisecond);
    }

    /**
     * 时间戳转年月日
     *
     * @author Chenlei
     * created at 2018/9/14
     **/
    public static String long2StringYMD(long millisecond){
        return long2String(YMD,millisecond);
    }

    /**
     * 时间戳转时分秒
     *
     * @author Chenlei
     * created at 2018/9/14
     **/
    public static String long2StringHMS(long millisecond){
        return long2String(HMS,millisecond);
    }

    /**
     * 字符串转时间戳
     *
     * @author Chenlei
     * created at 2018/9/14
     **/
    public static long string2Long(String pattern,String dateStr){
        if (TextUtils.isEmpty(dateStr)) return 0;
        SimpleDateFormat sDateTimeFormat = new SimpleDateFormat(pattern, Locale.CHINA);
        try {
            Date date = sDateTimeFormat.parse(dateStr);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 年月日时分秒转时间戳
     *
     * @author Chenlei
     * created at 2018/9/14
     **/
    public static long stringYMDHMS2Long(String dateStr){
        return string2Long(YMDHMS,dateStr);
    }

    /**
     * 年月日时分转时间戳
     *
     * @author Chenlei
     * created at 2018/9/14
     **/
    public static long stringYMDHM2Long(String dateStr){
        return string2Long(YMDHM,dateStr);
    }

    /**
     * 年月日转时间戳
     *
     * @author Chenlei
     * created at 2018/9/14
     **/
    public static long stringYMD2Long(String dateStr){
        return string2Long(YMD,dateStr);
    }

    /**
     * 时分秒转时间戳
     *
     * @author Chenlei
     * created at 2018/9/14
     **/
    public static long stringHMS2Long(String dateStr){
        return string2Long(HMS,dateStr);
    }

    /**
     * 获取当前时间字符串
     *
     * @author Chenlei
     * created at 2018/9/14
     **/
    public static String currentTime2String(String pattern) {
        return long2String(pattern,System.currentTimeMillis());
    }

    /**
     * 当前时间--年月日时分
     *
     * @author Chenlei
     * created at 2018/9/14
     **/
    public static String currentTime2StringYMDHM(){
        return currentTime2String(YMDHM);
    }

    /**
     * 当前时间--年月日
     *
     * @author Chenlei
     * created at 2018/9/14
     **/
    public static String currentTime2StringYMD(){
        return currentTime2String(YMD);
    }

    /**
     * 当前时间--时分秒
     *
     * @author Chenlei
     * created at 2018/9/14
     **/
    public static String currentTime2StringHMS(){
        return currentTime2String(HMS);
    }

    /**
     * 当前时间--年月日时分秒
     *
     * @author Chenlei
     * created at 2018/9/14
     **/
    public static String currentTime2StringYMDHMS(){
        return currentTime2String(YMDHMS);
    }
}
