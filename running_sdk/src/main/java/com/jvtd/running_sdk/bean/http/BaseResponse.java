package com.jvtd.running_sdk.bean.http;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/10/16.
 * 结果类
 */

public class BaseResponse<T> extends JvtdResponseData
{
  @SerializedName("code")
  private String resCode;
  @SerializedName("msg")
  private String resMsg;
  @SerializedName("data")
  private T resData;

  public String getResCode() {
    return resCode;
  }

  public void setResCode(String resCode) {
    this.resCode = resCode;
  }

  public String getResMsg() {
    return resMsg;
  }

  public void setResMsg(String resMsg) {
    this.resMsg = resMsg;
  }

  public T getResData() {
    return resData;
  }

  public void setResData(T resData) {
    this.resData = resData;
  }

  @Override
  public int getCode() {
    return Integer.valueOf(resCode);
  }

  @Override
  public T getData() {
    return resData;
  }

  @Override
  public String getMessage() {
    return resMsg;
  }

  @Override
  public String toString() {
    return "BaseResponse{" +
            "resCode='" + resCode + '\'' +
            ", resMsg='" + resMsg + '\'' +
            ", resData=" + resData +
            '}';
  }
}
