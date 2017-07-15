package com.example.demo.model;

/**
 * Created by pengwan on 2017/7/13.
 */
public class FileMonitorOption {
    private String fieldId;
    private int daysBefore;
    private String deviceId;
    private String[] keyWordsList;

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public int getDaysBefore() {
        return daysBefore;
    }

    public void setDaysBefore(int daysBefore) {
        this.daysBefore = daysBefore;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String[] getKeyWordsList() {
        return keyWordsList;
    }

    public void setKeyWordsList(String[] keyWordsList) {
        this.keyWordsList = keyWordsList;
    }
}
