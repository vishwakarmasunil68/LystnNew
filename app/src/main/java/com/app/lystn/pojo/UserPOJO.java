package com.app.lystn.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserPOJO {
    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("isdCode")
    @Expose
    private String isdCode;
    @SerializedName("mobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("vip")
    @Expose
    private Integer vip;
    @SerializedName("isLoggedin")
    @Expose
    private Integer isLoggedin;
    @SerializedName("langCode")
    @Expose
    private String langCode;
    @SerializedName("profileImgUrl")
    @Expose
    private String profileImgUrl;
    @SerializedName("socialProfileUrl")
    @Expose
    private String socialProfileUrl;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIsdCode() {
        return isdCode;
    }

    public void setIsdCode(String isdCode) {
        this.isdCode = isdCode;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVip() {
        return vip;
    }

    public void setVip(Integer vip) {
        this.vip = vip;
    }

    public Integer getIsLoggedin() {
        return isLoggedin;
    }

    public void setIsLoggedin(Integer isLoggedin) {
        this.isLoggedin = isLoggedin;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public String getProfileImgUrl() {
        return profileImgUrl;
    }

    public void setProfileImgUrl(String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }

    public String getSocialProfileUrl() {
        return socialProfileUrl;
    }

    public void setSocialProfileUrl(String socialProfileUrl) {
        this.socialProfileUrl = socialProfileUrl;
    }
}
