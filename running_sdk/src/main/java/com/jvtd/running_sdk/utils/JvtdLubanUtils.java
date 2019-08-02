package com.jvtd.running_sdk.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.List;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by Administrator on 2018/3/8.
 */

public class JvtdLubanUtils
{
  public static final String LUBAN_PATH = Environment.getExternalStorageDirectory() + "/Luban/image/";

  /**
   * 头像压缩  质量50k以下
   */
  public static void compressHeadPhoto(Context context, String photo, OnCompressListener listener)
  {
    Luban.with(context)
            .load(photo)                                   // 传人要压缩的图片列表
            .ignoreBy(50)                                  // 忽略不压缩图片的大小
            .setTargetDir(getPath())                        // 设置压缩后文件存储位置
            .setCompressListener(listener)
            .launch();    //启动压缩
  }

  /**
  * 图片压缩 质量300k以下
  */
  public static void compressPhoto(Context context, String photo, OnCompressListener listener)
  {
    Luban.with(context)
            .load(photo)                                   // 传人要压缩的图片列表
            .ignoreBy(300)                                  // 忽略不压缩图片的大小
            .setTargetDir(getPath())                        // 设置压缩后文件存储位置
            .setCompressListener(listener)
            .launch();    //启动压缩
  }

  /**
   * 图片组 质量300k以下
   */
  public static void compressPhotos(Context context, List<String> photos, OnCompressListener listener)
  {
    Luban.with(context)
            .load(photos)                                   // 传人要压缩的图片列表
            .ignoreBy(300)                                  // 忽略不压缩图片的大小
            .setTargetDir(getPath())                        // 设置压缩后文件存储位置
            .setCompressListener(listener)
            .launch();    //启动压缩
  }

  private static String getPath()
  {
    File file = new File(LUBAN_PATH);
    if (file.mkdirs())
      return LUBAN_PATH;
    return LUBAN_PATH;
  }

  /**
   * 删除Luban文件夹
   *
   * @author Chenlei
   * created at 2018/9/19
   **/
  public static void delLubanDir(){
    JvtdFileUtils.delete(LUBAN_PATH);
  }
}
