package com.jvtd.running_sdk.bean.http;

/**
 * Created by Administrator on 2017/10/16.
 * 网络异常
 */

public class JvtdApiException extends Exception
{
  private int code;

  public JvtdApiException(String message, int code)
  {
    super(message);
    this.code = code;
  }

  public JvtdApiException(String msg)
  {
    super(msg);
  }

  public int getCode()
  {
    return code;
  }

  public void setCode(int code)
  {
    this.code = code;
  }
}
