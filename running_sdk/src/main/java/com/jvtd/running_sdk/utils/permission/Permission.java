package com.jvtd.running_sdk.utils.permission;

import android.Manifest;
import android.os.Build;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class Permission
{
  public static final String[] CALENDAR;   // 读写日历。
  public static final String[] CAMERA;     // 相机。
  public static final String[] CONTACTS;   // 读写联系人。
  public static final String[] LOCATION;   // 读位置信息。
  public static final String[] MICROPHONE; // 使用麦克风。
  public static final String[] PHONE;      // 读电话状态、打电话、读写电话记录。
  public static final String[] SENSORS;    // 传感器。
  public static final String[] SMS;        // 读写短信、收发短信。
  public static final String[] STORAGE;    // 读写存储卡。

  // 权限相关
  public static final int TYPE_CALL_PHONE = 0x01;
  public static final int TYPE_CAMERA = 0x02;
  public static final int TYPE_LOCATION = 0x03;
  public static final int TYPE_STORAGE = 0x04;
  public static final int TYPE_CONTACTS = 0x05;
  public static final int TYPE_RECORD_AUDIO = 0x06;

  @IntDef({TYPE_CALL_PHONE, TYPE_CAMERA,TYPE_LOCATION,TYPE_STORAGE,TYPE_CONTACTS,TYPE_RECORD_AUDIO})
  @Retention(RetentionPolicy.SOURCE)
  public @interface PermissionType {}

  static
  {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
    {
      CALENDAR = new String[]{};
      CAMERA = new String[]{};
      CONTACTS = new String[]{};
      LOCATION = new String[]{};
      MICROPHONE = new String[]{};
      PHONE = new String[]{};
      SENSORS = new String[]{};
      SMS = new String[]{};
      STORAGE = new String[]{};
    } else
    {
      CALENDAR = new String[]{
              Manifest.permission.READ_CALENDAR,
              Manifest.permission.WRITE_CALENDAR};

      CAMERA = new String[]{
              Manifest.permission.CAMERA};

      CONTACTS = new String[]{
              Manifest.permission.READ_CONTACTS
//              Manifest.permission.WRITE_CONTACTS,
//              Manifest.permission.GET_ACCOUNTS
      };

      LOCATION = new String[]{
              Manifest.permission.ACCESS_FINE_LOCATION,
              Manifest.permission.ACCESS_COARSE_LOCATION};

      MICROPHONE = new String[]{
              Manifest.permission.RECORD_AUDIO};

      PHONE = new String[]{
              Manifest.permission.READ_PHONE_STATE,
              Manifest.permission.CALL_PHONE
//              Manifest.permission.READ_CALL_LOG,
//              Manifest.permission.WRITE_CALL_LOG,
//              Manifest.permission.USE_SIP
//              Manifest.permission.PROCESS_OUTGOING_CALLS
      };

      SENSORS = new String[]{
              Manifest.permission.BODY_SENSORS};

      SMS = new String[]{
              Manifest.permission.SEND_SMS,
              Manifest.permission.RECEIVE_SMS,
              Manifest.permission.READ_SMS,
              Manifest.permission.RECEIVE_WAP_PUSH,
              Manifest.permission.RECEIVE_MMS};

      STORAGE = new String[]{
              Manifest.permission.READ_EXTERNAL_STORAGE,
              Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }
  }
}
