package com.jvtd.running_sdk.bean;

import com.contrarywind.interfaces.IPickerViewData;

public class JvtdProvincesBean implements IPickerViewData {

    /**
     * ID : 429629
     * ParentId : 429006
     * Name : 侨乡街道开发区
     * MergerName : 中国,湖北省,天门市,侨乡街道开发区
     * ShortName : 侨乡街道开发区
     * MergerShortName : 中国,湖北,天门,侨乡街道开发区
     * LevelType : 3
     * CityCode : 0728
     * ZipCode : 431700
     * Pinyin : Qiaoxiang
     * Jianpin : QX
     * FirstChar : Q
     * lng : 113.15723
     * Lat : 30.634943
     * Remarks : 拓展
     */

    private String ID;
    private String ParentId;
    private String Name;
    private String MergerName;
    private String ShortName;
    private String MergerShortName;
    private String LevelType;
    private String CityCode;
    private String ZipCode;
    private String Pinyin;
    private String Jianpin;
    private String FirstChar;
    private String lng;
    private String Lat;
    private String Remarks;

    @Override
    public String getPickerViewText() {
        return this.Name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getParentId() {
        return ParentId;
    }

    public void setParentId(String ParentId) {
        this.ParentId = ParentId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getMergerName() {
        return MergerName;
    }

    public void setMergerName(String MergerName) {
        this.MergerName = MergerName;
    }

    public String getShortName() {
        return ShortName;
    }

    public void setShortName(String ShortName) {
        this.ShortName = ShortName;
    }

    public String getMergerShortName() {
        return MergerShortName;
    }

    public void setMergerShortName(String MergerShortName) {
        this.MergerShortName = MergerShortName;
    }

    public String getLevelType() {
        return LevelType;
    }

    public void setLevelType(String LevelType) {
        this.LevelType = LevelType;
    }

    public String getCityCode() {
        return CityCode;
    }

    public void setCityCode(String CityCode) {
        this.CityCode = CityCode;
    }

    public String getZipCode() {
        return ZipCode;
    }

    public void setZipCode(String ZipCode) {
        this.ZipCode = ZipCode;
    }

    public String getPinyin() {
        return Pinyin;
    }

    public void setPinyin(String Pinyin) {
        this.Pinyin = Pinyin;
    }

    public String getJianpin() {
        return Jianpin;
    }

    public void setJianpin(String Jianpin) {
        this.Jianpin = Jianpin;
    }

    public String getFirstChar() {
        return FirstChar;
    }

    public void setFirstChar(String FirstChar) {
        this.FirstChar = FirstChar;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String Lat) {
        this.Lat = Lat;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String Remarks) {
        this.Remarks = Remarks;
    }
}
