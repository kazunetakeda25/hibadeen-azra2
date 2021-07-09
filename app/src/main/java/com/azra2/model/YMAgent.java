package com.azra2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class YMAgent {
    public boolean selected = false;

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("nick_name")
    @Expose
    private String nickName;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("birthday")
    @Expose
    private String birthday;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("availability")
    @Expose
    private Integer availability;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("room_type")
    @Expose
    private Integer roomType;
    @SerializedName("call_type")
    @Expose
    private Integer callType;
    @SerializedName("performance_price")
    @Expose
    private Integer performancePrice;
    @SerializedName("bio")
    @Expose
    private String bio;
    @SerializedName("about")
    @Expose
    private String about;
    @SerializedName("interests")
    @Expose
//    private List<String> interests = null;
    private String interests;
    @SerializedName("audio")
    @Expose
    private String audio;
    @SerializedName("idcard_image")
    @Expose
    private String idcardImage;
    @SerializedName("device_token")
    @Expose
    private String deviceToken;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("gallery")
    @Expose
    private List<YMImageInfo> gallery = null;
    @SerializedName("review_count")
    @Expose
    private Integer reviewCount;
    @SerializedName("review_star")
    @Expose
    private Double reviewStar;
    @SerializedName("review_star5")
    @Expose
    private Double reviewStar5;
    @SerializedName("review_star4")
    @Expose
    private Double reviewStar4;
    @SerializedName("review_star3")
    @Expose
    private Double reviewStar3;
    @SerializedName("review_star2")
    @Expose
    private Double reviewStar2;
    @SerializedName("review_star1")
    @Expose
    private Double reviewStar1;
    @SerializedName("review_spicy")
    @Expose
    private Double reviewSpicy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public Integer getAvailability() {
        return availability;
    }

    public void setAvailability(Integer availability) {
        this.availability = availability;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getRoomType() {
        return roomType;
    }

    public void setRoomType(Integer roomType) {
        this.roomType = roomType;
    }

    public Integer getCallType() {
        return callType;
    }

    public void setCallType(Integer callType) {
        this.callType = callType;
    }

    public Integer getPerformancePrice() {
        return performancePrice;
    }

    public void setPerformancePrice(Integer performancePrice) {
        this.performancePrice = performancePrice;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

//    public List<String> getInterests() {
//        return interests;
//    }

//    public void setInterests(List<String> interests) {
//        this.interests = interests;
//    }

    public String getInterests(){
        return interests;
    }
    public void setInterests(String interests){
        this.interests = interests;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getIdcardImage() {
        return idcardImage;
    }

    public void setIdcardImage(String idcardImage) {
        this.idcardImage = idcardImage;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
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

    public List<YMImageInfo> getGallery() {
        return gallery;
    }

    public void setGallery(List<YMImageInfo> gallery) {
        this.gallery = gallery;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public Double getReviewStar() {
        return reviewStar;
    }

    public void setReviewStar(Double reviewStar) {
        this.reviewStar = reviewStar;
    }

    public Double getReviewStar5() {
        return reviewStar5;
    }

    public void setReviewStar5(Double reviewStar5) {
        this.reviewStar5 = reviewStar5;
    }

    public Double getReviewStar4() {
        return reviewStar4;
    }

    public void setReviewStar4(Double reviewStar4) {
        this.reviewStar4 = reviewStar4;
    }

    public Double getReviewStar3() {
        return reviewStar3;
    }

    public void setReviewStar3(Double reviewStar3) {
        this.reviewStar3 = reviewStar3;
    }

    public Double getReviewStar2() {
        return reviewStar2;
    }

    public void setReviewStar2(Double reviewStar2) {
        this.reviewStar2 = reviewStar2;
    }

    public Double getReviewStar1() {
        return reviewStar1;
    }

    public void setReviewStar1(Double reviewStar1) {
        this.reviewStar1 = reviewStar1;
    }

    public Double getReviewSpicy() {
        return reviewSpicy;
    }

    public void setReviewSpicy(Double reviewSpicy) {
        this.reviewSpicy = reviewSpicy;
    }

}


