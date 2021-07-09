package com.azra2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YMUserMinuteResponse {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("data")
    @Expose
    private YMUserMinuteData data;
    @SerializedName("message")
    @Expose
    private String message;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public YMUserMinuteData getData() {
        return data;
    }

    public void setData(YMUserMinuteData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
