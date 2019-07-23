package com.jvtd.running_sdk.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class JvtdLocationBean implements Parcelable {
    private double altitude = 0;//当前海拔
    private double speed = 0;//当前速度
    private double mileage = 0;//里程
    private double longitude = 0;//当前经度
    private double latitude = 0;//当前纬度
    private double time = 0;//当前时间
    private int stepNumber = 0;//当前时间步数

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    @Override
    public String toString() {
        return "JvtdLocationBean{" +
                "海拔=" + altitude +
                ", 速度=" + speed +
                ", 里程=" + mileage +
                ", 经度=" + longitude +
                ", 纬度=" + latitude +
                ", 时间=" + time +
                ", 步数=" + stepNumber +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.altitude);
        dest.writeDouble(this.speed);
        dest.writeDouble(this.mileage);
        dest.writeDouble(this.longitude);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.time);
        dest.writeInt(this.stepNumber);
    }

    public JvtdLocationBean() {
    }

    protected JvtdLocationBean(Parcel in) {
        this.altitude = in.readDouble();
        this.speed = in.readDouble();
        this.mileage = in.readDouble();
        this.longitude = in.readDouble();
        this.latitude = in.readDouble();
        this.time = in.readDouble();
        this.stepNumber = in.readInt();
    }

    public static final Creator<JvtdLocationBean> CREATOR = new Creator<JvtdLocationBean>() {
        @Override
        public JvtdLocationBean createFromParcel(Parcel source) {
            return new JvtdLocationBean(source);
        }

        @Override
        public JvtdLocationBean[] newArray(int size) {
            return new JvtdLocationBean[size];
        }
    };
}
