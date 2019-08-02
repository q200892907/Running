package com.jvtd.running_sdk.bean.http;

public abstract class JvtdResponseData<T> {
    public abstract int getCode();
    public abstract T getData();
    public abstract String getMessage();
}
