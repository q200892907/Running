package com.jvtd.running_sdk.bean;

import com.amap.api.maps.model.LatLng;
import com.jvtd.running_sdk.constants.RunningSdk;
import com.jvtd.running_sdk.utils.JvtdLocationUtils;

import java.util.ArrayList;

public class JvtdLineBean {
    private static volatile JvtdLineBean instance;

    public static JvtdLineBean getInstance() {
        if (instance == null)
            synchronized (JvtdLineBean.class) {
                if (instance == null)
                    instance = new JvtdLineBean();
            }
        return instance;
    }

    public void reset() {
        lineID = System.currentTimeMillis();
        allTime = 0;
        totalMileage = 0;
        currentAltitude = 0;
        risingAltitude = 0;
        fallingAltitude = 0;
        startAltitude = 0;
        speed = 0;
        mLocations.clear();
        instance = null;
    }

    private long lineID = System.currentTimeMillis();
    private long allTime = 0;//总时间
    private double totalMileage = 0;//总里程
    private double currentAltitude = 0;//当前海拔
    private double risingAltitude = 0;//上升海拔
    private double fallingAltitude = 0;//下降海拔
    private double speed = 0;//速度
    private String maximumSpeed;//最大速度
    private String average;//平均速度
    private double startAltitude = 0;//初始海拔

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }

    public String getMaximumSpeed() {
        return maximumSpeed;
    }

    public void setMaximumSpeed(String maximumSpeed) {
        this.maximumSpeed = maximumSpeed;
    }

    private ArrayList<JvtdLocationBean> mLocations = new ArrayList<>();//记录坐标点

    public long getAllTime() {
        return allTime;
    }

    public void setAllTime(long allTime) {
        this.allTime = allTime;
    }

    //时间加1
    public void addTime() {
//    if (allTime == 0)
//      RecordUtils.writeTxtToFile("开始记录-"+SimpleDateFormatUtils.currentTime2StringYMDHMS(),String.valueOf(lineID));
//    if (allTime % 10 == 0)
//      RecordUtils.writeTxtToFile("记录中-"+SimpleDateFormatUtils.currentTime2StringYMDHMS()+"\t间距"+allTime,String.valueOf(lineID));
        setAllTime(allTime + RunningSdk.ONE_SECOND);
    }

    public double getTotalMileage() {
        return totalMileage;
    }

    public void setTotalMileage(double totalMileage) {
        this.totalMileage = totalMileage;
    }

    public double getCurrentAltitude() {
        return currentAltitude;
    }

    public void setCurrentAltitude(double currentAltitude) {
        this.currentAltitude = currentAltitude;
    }

    public double getRisingAltitude() {
        return risingAltitude;
    }

    public void setRisingAltitude(double risingAltitude) {
        this.risingAltitude = risingAltitude;
    }

    public double getFallingAltitude() {
        return fallingAltitude;
    }

    public void setFallingAltitude(double fallingAltitude) {
        this.fallingAltitude = fallingAltitude;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * 添加一个点
     *
     * @param locationBean 点数据
     */
    public void addLocation(JvtdLocationBean locationBean) {
        if (locationBean.getSpeed() < 0) return;
        //获取上一个点
        JvtdLocationBean bean = null;
        if (getLocations().size() > 0) {
            bean = getLocations().get(getLocations().size() - 1);
        }
        if (bean == null) {
            setTotalMileage(0);
        } else {
            if (bean.getSpeed() == 0 && locationBean.getSpeed() <= 0) return;
            LatLng startLocation = new LatLng(bean.getLatitude(), bean.getLongitude());
            LatLng endLocation = new LatLng(locationBean.getLatitude(), locationBean.getLongitude());
            setTotalMileage(totalMileage + JvtdLocationUtils.calculateLineDistance(startLocation, endLocation));
        }
        if (startAltitude == 0 && bean != null && bean.getAltitude() > 0) {
            startAltitude = bean.getAltitude();
        }
        if (locationBean.getSpeed() == 0 && locationBean.getAltitude() == 0 && bean != null)
            locationBean.setAltitude(bean.getAltitude());
        if (startAltitude != 0) {
            double tempAltitude = locationBean.getAltitude() - startAltitude;
            if (tempAltitude > 0) {
                setRisingAltitude(risingAltitude > tempAltitude ? risingAltitude :
                        tempAltitude);
            } else {
                tempAltitude = Math.abs(tempAltitude);
                setFallingAltitude(fallingAltitude > tempAltitude ? fallingAltitude :
                        tempAltitude);
            }
        }
        locationBean.setTime(allTime);//设置时间
        locationBean.setMileage(totalMileage);
        getLocations().add(locationBean);
        setCurrentAltitude(locationBean.getAltitude());
        setSpeed(locationBean.getSpeed());
    }

    public ArrayList<JvtdLocationBean> getLocations() {
        return mLocations;
    }

    public void setLocations(ArrayList<JvtdLocationBean> locations) {
        mLocations = locations;
    }
}
