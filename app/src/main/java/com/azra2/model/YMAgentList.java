package com.azra2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class YMAgentList implements Serializable {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("data")
    @Expose
    private List<YMAgent> data = null;
    @SerializedName("message")
    @Expose
    private String message;


    /**
     * No args constructor for use in serialization
     *
     */
    public YMAgentList() {
    }

    /**
     *
     * @param result
     * @param data
     */
    public YMAgentList(String result, List<YMAgent> data) {
        super();
        this.result = result;
        this.data = data;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<YMAgent> getData() {
        return data;
    }

    public void setData(List<YMAgent> data) {
        this.data = data;
    }

    public String getMessage(){
        return this.message;
    }

    public void setMessage(String message){
        this.message = message;
    }

}
