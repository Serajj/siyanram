package com.verbosetech.weshare.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PayModel {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("fname")
    @Expose
    private String fname;
    @SerializedName("mname")
    @Expose
    private String mname;
    @SerializedName("occupation")
    @Expose
    private String occupation;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("fcm_registration_id")
    @Expose
    private Object fcmRegistrationId;
    @SerializedName("notification_on_like")
    @Expose
    private Boolean notificationOnLike;
    @SerializedName("notification_on_dislike")
    @Expose
    private Boolean notificationOnDislike;
    @SerializedName("notification_on_comment")
    @Expose
    private Boolean notificationOnComment;
    @SerializedName("is_admin")
    @Expose
    private Integer isAdmin;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("is_blocked")
    @Expose
    private Integer isBlocked;
    @SerializedName("is_private")
    @Expose
    private Integer isPrivate;
    @SerializedName("is_paid")
    @Expose
    private String isPaid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Object getFcmRegistrationId() {
        return fcmRegistrationId;
    }

    public void setFcmRegistrationId(Object fcmRegistrationId) {
        this.fcmRegistrationId = fcmRegistrationId;
    }

    public Boolean getNotificationOnLike() {
        return notificationOnLike;
    }

    public void setNotificationOnLike(Boolean notificationOnLike) {
        this.notificationOnLike = notificationOnLike;
    }

    public Boolean getNotificationOnDislike() {
        return notificationOnDislike;
    }

    public void setNotificationOnDislike(Boolean notificationOnDislike) {
        this.notificationOnDislike = notificationOnDislike;
    }

    public Boolean getNotificationOnComment() {
        return notificationOnComment;
    }

    public void setNotificationOnComment(Boolean notificationOnComment) {
        this.notificationOnComment = notificationOnComment;
    }

    public Integer getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Integer isAdmin) {
        this.isAdmin = isAdmin;
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

    public Integer getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Integer isBlocked) {
        this.isBlocked = isBlocked;
    }

    public Integer getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Integer isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(String isPaid) {
        this.isPaid = isPaid;
    }

}