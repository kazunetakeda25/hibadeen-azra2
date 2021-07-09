package com.azra2.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YMUserCall {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("agent_id")
    @Expose
    private String agentId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("room_type")
    @Expose
    private String roomType;
    @SerializedName("started_at")
    @Expose
    private String startedAt;
    @SerializedName("ended_at")
    @Expose
    private String endedAt;
    @SerializedName("duration")
    @Expose
    private String duration;
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

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }

    public String getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(String endedAt) {
        this.endedAt = endedAt;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
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
