package com.azra2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YMResponse {
    @SerializedName("result")
    @Expose
    private String result;

    @SerializedName("message")
    @Expose
    private String message;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
