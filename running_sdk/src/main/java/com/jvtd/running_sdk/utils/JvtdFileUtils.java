package com.jvtd.running_sdk.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

import com.jvtd.running_sdk.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class JvtdFileUtils
{
  private static String getBaseFolder(Context context)
  {
    String baseFolder = String.format("%s/%s/", Environment.getExternalStorageDirectory().getPath(), context.getString(R.string.app_file_dir_name));
    File f = new File(baseFolder);
    if (!f.exists())
    {
      boolean b = f.mkdirs();
      if (!b)
      {
        baseFolder = String.format("%s/", context.getExternalFilesDir(null).getAbsolutePath());
      }
    }
    return baseFolder;
  }

  private static String getPath(Context context, String fileName)
  {
    String p = getBaseFolder(context);
    File f = new File(p);
    if (!f.exists() && !f.mkdirs())
    {
      return getBaseFolder(context) + fileName;
    }
    return p + fileName;
  }

  /**
   * 获取文件路径
   *
   * @author Chenlei
   * created at 2018/9/14
   **/
  public static String getFilePath(Context context, String fileName, String fileType)
  {
    fileName = String.format("%s%s", JvtdStringUtils.md5(fileName), fileType);
    return getPath(context, fileName);
  }

  /**
   * 判断文件是否存在
   *
   * @author Chenlei
   * created at 2018/9/14
   **/
  public static boolean isExistFile(Context context, String fileName, String fileType)
  {
    try
    {
      File f = new File(getFilePath(context, fileName, fileType));
      if (!f.exists())
      {
        return false;
      }
    } catch (Exception e)
    {
      return false;
    }
    return true;
  }

  /**
   * 删除文件，可以是文件或文件夹
   *
   * @param fileName 要删除的文件名
   * @return 删除成功返回true，否则返回false
   */
  public static boolean delete(String fileName)
  {
    File file = new File(fileName);
    if (!file.exists())
    {
      System.out.printf("删除文件失败:%s不存在！%n", fileName);
      return false;
    } else
    {
      if (file.isFile()) return deleteFile(fileName);
      else return deleteDirectory(fileName);
    }
  }

  /**
   * 删除单个文件
   *
   * @param fileName 要删除的文件的文件名
   * @return 单个文件删除成功返回true，否则返回false
   */
  private static boolean deleteFile(String fileName)
  {
    File file = new File(fileName);
    // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
    if (file.exists() && file.isFile())
    {
      if (file.delete())
      {
        System.out.printf("删除单个文件%s成功！%n", fileName);
        return true;
      } else
      {
        System.out.printf("删除单个文件%s失败！%n", fileName);
        return false;
      }
    } else
    {
      System.out.printf("删除单个文件失败：%s不存在！%n", fileName);
      return false;
    }
  }

  /**
   * 删除目录及目录下的文件
   *
   * @param dir 要删除的目录的文件路径
   * @return 目录删除成功返回true，否则返回false
   */
  private static boolean deleteDirectory(String dir)
  {
    // 如果dir不以文件分隔符结尾，自动添加文件分隔符
    if (!dir.endsWith(File.separator)) dir = dir + File.separator;
    File dirFile = new File(dir);
    // 如果dir对应的文件不存在，或者不是一个目录，则退出
    if ((!dirFile.exists()) || (!dirFile.isDirectory()))
    {
      System.out.printf("删除目录失败：%s不存在！%n", dir);
      return false;
    }
    boolean flag = true;
    // 删除文件夹中的所有文件包括子目录
    File[] files = dirFile.listFiles();
    for (File file : files)
    {
      // 删除子文件
      if (file.isFile())
      {
        flag = JvtdFileUtils.deleteFile(file.getAbsolutePath());
        if (!flag) break;
      }
      // 删除子目录
      else if (file.isDirectory())
      {
        flag = JvtdFileUtils.deleteDirectory(file.getAbsolutePath());
        if (!flag) break;
      }
    }
    if (!flag)
    {
      System.out.println("删除目录失败！");
      return false;
    }
    // 删除当前目录
    if (dirFile.delete())
    {
      System.out.printf("删除目录%s成功！%n", dir);
      return true;
    } else
    {
      return false;
    }
  }

  public static String getJson(Context context, String fileName)
  {

    StringBuilder stringBuilder = new StringBuilder();
    try
    {
      AssetManager assetManager = context.getAssets();
      BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
      String line;
      while ((line = bf.readLine()) != null)
      {
        stringBuilder.append(line);
      }
    } catch (IOException e)
    {
      e.printStackTrace();
    }
    return stringBuilder.toString();
  }
}
