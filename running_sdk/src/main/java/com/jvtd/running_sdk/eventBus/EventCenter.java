package com.jvtd.running_sdk.eventBus;

/**
 * 事件订阅
 *
 * @author Chenlei
 * created at 2019-07-23
 **/

public class EventCenter<T>
{
  /**
   * 事件携带的数据
   */
  private T data;

  /**
   * 区分不同的事件
   */
  private int eventCode = -1;

  public EventCenter(T data, int eventCode)
  {
    this.data = data;
    this.eventCode = eventCode;
  }

  public EventCenter(int eventCode)
  {
    this(null, eventCode);
  }

  public T getData()
  {
    return data;
  }

  public int getEventCode()
  {
    return eventCode;
  }
}
