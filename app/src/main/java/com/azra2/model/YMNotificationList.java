package com.azra2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class YMNotificationList implements Serializable{

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("data")
    @Expose
    private List<YMNotification> dataList = null;
    @SerializedName("message")
    @Expose
    private String message;

    /**
     * No args constructor for use in serialization
     *
     */
    public YMNotificationList() {
    }

    /**
     *
     * @param result
     * @param data
     */
    public YMNotificationList(String result, List<YMNotification> data) {
        super();
        this.result = result;
        this.dataList = data;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<YMNotification> getData() {
        return dataList;
    }

    public void setData(List<YMNotification> data) {
        this.dataList = data;
    }

    public String getMessage(){
        return this.message;
    }

    public void setMessage(String message){
        this.message = message;
    }

}

