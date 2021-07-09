package com.azra2.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YMUserLive {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("play_id")
    @Expose
    private String playId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("entered_at")
    @Expose
    private String enteredAt;
    @SerializedName("leaved_at")
    @Expose
    private String leavedAt;
    @SerializedName("pricepermin")
    @Expose
    private String pricepermin;
    @SerializedName("totalprice")
    @Expose
    private String totalprice;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("agent_nick_name")
    @Expose
    private String agentNickName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlayId() {
        return playId;
    }

    public void setPlayId(String playId) {
        this.playId = playId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEnteredAt() {
        return enteredAt;
    }

    public void setEnteredAt(String enteredAt) {
        this.enteredAt = enteredAt;
    }

    public String getLeavedAt() {
        return leavedAt;
    }

    public void setLeavedAt(String leavedAt) {
        this.leavedAt = leavedAt;
    }

    public String getPricepermin() {
        return pricepermin;
    }

    public void setPricepermin(String pricepermin) {
        this.pricepermin = pricepermin;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getAgentNickName() {
        return agentNickName;
    }

    public void setAgentNickName(String agentNickName) {
        this.agentNickName = agentNickName;
    }

}