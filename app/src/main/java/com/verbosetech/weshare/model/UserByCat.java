package com.verbosetech.weshare.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserByCat {

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
    private Integer notificationOnLike;
    @SerializedName("notification_on_dislike")
    @Expose
    private Integer notificationOnDislike;
    @SerializedName("notification_on_comment")
    @Expose
    private Integer notificationOnComment;
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
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("dheshouldbe")
    @Expose
    private String dheshouldbe;
    @SerializedName("dageofaround")
    @Expose
    private String dageofaround;
    @SerializedName("dmartialstatus")
    @Expose
    private String dmartialstatus;
    @SerializedName("dchallenged")
    @Expose
    private String dchallenged;
    @SerializedName("dreligion")
    @Expose
    private String dreligion;
    @SerializedName("dmothertounge")
    @Expose
    private String dmothertounge;
    @SerializedName("dcaste")
    @Expose
    private String dcaste;
    @SerializedName("dcity")
    @Expose
    private String dcity;
    @SerializedName("dcountry")
    @Expose
    private String dcountry;
    @SerializedName("deducation")
    @Expose
    private String deducation;
    @SerializedName("ehighest")
    @Expose
    private String ehighest;
    @SerializedName("eugdegree")
    @Expose
    private String eugdegree;
    @SerializedName("eugcollege")
    @Expose
    private String eugcollege;
    @SerializedName("epgcollege")
    @Expose
    private String epgcollege;
    @SerializedName("epgdegree")
    @Expose
    private String epgdegree;
    @SerializedName("chighesteducation")
    @Expose
    private String chighesteducation;
    @SerializedName("cemployedin")
    @Expose
    private String cemployedin;
    @SerializedName("cannuanincome")
    @Expose
    private String cannuanincome;
    @SerializedName("ftype")
    @Expose
    private String ftype;
    @SerializedName("fvalue")
    @Expose
    private String fvalue;
    @SerializedName("fstatus")
    @Expose
    private String fstatus;
    @SerializedName("fincome")
    @Expose
    private String fincome;
    @SerializedName("foccupation")
    @Expose
    private String foccupation;
    @SerializedName("fbrother")
    @Expose
    private String fbrother;
    @SerializedName("fsister")
    @Expose
    private String fsister;
    @SerializedName("caste")
    @Expose
    private String caste;
    @SerializedName("religion")
    @Expose
    private String religion;
    @SerializedName("height")
    @Expose
    private String height;
    @SerializedName("martial_status")
    @Expose
    private String martialStatus;
    @SerializedName("mother_tounge")
    @Expose
    private String motherTounge;
    @SerializedName("profilefor")
    @Expose
    private String profilefor;
    @SerializedName("complexion")
    @Expose
    private String complexion;
    @SerializedName("bodytype")
    @Expose
    private String bodytype;
    @SerializedName("aboutme")
    @Expose
    private String aboutme;

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

    public Integer getNotificationOnLike() {
        return notificationOnLike;
    }

    public void setNotificationOnLike(Integer notificationOnLike) {
        this.notificationOnLike = notificationOnLike;
    }

    public Integer getNotificationOnDislike() {
        return notificationOnDislike;
    }

    public void setNotificationOnDislike(Integer notificationOnDislike) {
        this.notificationOnDislike = notificationOnDislike;
    }

    public Integer getNotificationOnComment() {
        return notificationOnComment;
    }

    public void setNotificationOnComment(Integer notificationOnComment) {
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDheshouldbe() {
        return dheshouldbe;
    }

    public void setDheshouldbe(String dheshouldbe) {
        this.dheshouldbe = dheshouldbe;
    }

    public String getDageofaround() {
        return dageofaround;
    }

    public void setDageofaround(String dageofaround) {
        this.dageofaround = dageofaround;
    }

    public String getDmartialstatus() {
        return dmartialstatus;
    }

    public void setDmartialstatus(String dmartialstatus) {
        this.dmartialstatus = dmartialstatus;
    }

    public String getDchallenged() {
        return dchallenged;
    }

    public void setDchallenged(String dchallenged) {
        this.dchallenged = dchallenged;
    }

    public String getDreligion() {
        return dreligion;
    }

    public void setDreligion(String dreligion) {
        this.dreligion = dreligion;
    }

    public String getDmothertounge() {
        return dmothertounge;
    }

    public void setDmothertounge(String dmothertounge) {
        this.dmothertounge = dmothertounge;
    }

    public String getDcaste() {
        return dcaste;
    }

    public void setDcaste(String dcaste) {
        this.dcaste = dcaste;
    }

    public String getDcity() {
        return dcity;
    }

    public void setDcity(String dcity) {
        this.dcity = dcity;
    }

    public String getDcountry() {
        return dcountry;
    }

    public void setDcountry(String dcountry) {
        this.dcountry = dcountry;
    }

    public String getDeducation() {
        return deducation;
    }

    public void setDeducation(String deducation) {
        this.deducation = deducation;
    }

    public String getEhighest() {
        return ehighest;
    }

    public void setEhighest(String ehighest) {
        this.ehighest = ehighest;
    }

    public String getEugdegree() {
        return eugdegree;
    }

    public void setEugdegree(String eugdegree) {
        this.eugdegree = eugdegree;
    }

    public String getEugcollege() {
        return eugcollege;
    }

    public void setEugcollege(String eugcollege) {
        this.eugcollege = eugcollege;
    }

    public String getEpgcollege() {
        return epgcollege;
    }

    public void setEpgcollege(String epgcollege) {
        this.epgcollege = epgcollege;
    }

    public String getEpgdegree() {
        return epgdegree;
    }

    public void setEpgdegree(String epgdegree) {
        this.epgdegree = epgdegree;
    }

    public String getChighesteducation() {
        return chighesteducation;
    }

    public void setChighesteducation(String chighesteducation) {
        this.chighesteducation = chighesteducation;
    }

    public String getCemployedin() {
        return cemployedin;
    }

    public void setCemployedin(String cemployedin) {
        this.cemployedin = cemployedin;
    }

    public String getCannuanincome() {
        return cannuanincome;
    }

    public void setCannuanincome(String cannuanincome) {
        this.cannuanincome = cannuanincome;
    }

    public String getFtype() {
        return ftype;
    }

    public void setFtype(String ftype) {
        this.ftype = ftype;
    }

    public String getFvalue() {
        return fvalue;
    }

    public void setFvalue(String fvalue) {
        this.fvalue = fvalue;
    }

    public String getFstatus() {
        return fstatus;
    }

    public void setFstatus(String fstatus) {
        this.fstatus = fstatus;
    }

    public String getFincome() {
        return fincome;
    }

    public void setFincome(String fincome) {
        this.fincome = fincome;
    }

    public String getFoccupation() {
        return foccupation;
    }

    public void setFoccupation(String foccupation) {
        this.foccupation = foccupation;
    }

    public String getFbrother() {
        return fbrother;
    }

    public void setFbrother(String fbrother) {
        this.fbrother = fbrother;
    }

    public String getFsister() {
        return fsister;
    }

    public void setFsister(String fsister) {
        this.fsister = fsister;
    }

    public String getCaste() {
        return caste;
    }

    public void setCaste(String caste) {
        this.caste = caste;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getMartialStatus() {
        return martialStatus;
    }

    public void setMartialStatus(String martialStatus) {
        this.martialStatus = martialStatus;
    }

    public String getMotherTounge() {
        return motherTounge;
    }

    public void setMotherTounge(String motherTounge) {
        this.motherTounge = motherTounge;
    }

    public String getProfilefor() {
        return profilefor;
    }

    public void setProfilefor(String profilefor) {
        this.profilefor = profilefor;
    }

    public String getComplexion() {
        return complexion;
    }

    public void setComplexion(String complexion) {
        this.complexion = complexion;
    }

    public String getBodytype() {
        return bodytype;
    }

    public void setBodytype(String bodytype) {
        this.bodytype = bodytype;
    }

    public String getAboutme() {
        return aboutme;
    }

    public void setAboutme(String aboutme) {
        this.aboutme = aboutme;
    }

}