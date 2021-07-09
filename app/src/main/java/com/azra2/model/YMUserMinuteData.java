package com.azra2.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YMUserMinuteData {

    @SerializedName("calls")
    @Expose
    private List<YMUserCall> calls = null;
    @SerializedName("lives")
    @Expose
    private List<YMUserLive> lives = null;

    public List<YMUserCall> getCalls() {
        return this.calls;
    }

    public void setCalls(List<YMUserCall> calls) {
        this.calls = calls;
    }

    public List<YMUserLive> getLives() {
        return this.lives;
    }

    public void setLives(List<YMUserLive> lives) {
        this.lives = lives;
    }

}