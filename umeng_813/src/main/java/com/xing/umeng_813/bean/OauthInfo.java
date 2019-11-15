package com.xing.umeng_813.bean;

import java.io.Serializable;


public class OauthInfo implements Serializable{
    private static final long serialVersionUID = -2644184353648014102L;
    private String uid;
    private String openid;
    private String token;
    private String expires_in;
    private String nickname;
    private String type;
    private String avatar;
    private String gender;
    private String signature;
    private String registration;
    private String devicecode;
    private String version;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getDevicecode() {
        return devicecode;
    }

    public void setDevicecode(String devicecode) {
        this.devicecode = devicecode;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "OauthInfo{" +
                "uid='" + uid + '\'' +
                ", openid='" + openid + '\'' +
                ", token='" + token + '\'' +
                ", expires_in='" + expires_in + '\'' +
                ", nickname='" + nickname + '\'' +
                ", type='" + type + '\'' +
                ", avatar='" + avatar + '\'' +
                ", gender='" + gender + '\'' +
                ", signature='" + signature + '\'' +
                ", registration='" + registration + '\'' +
                ", devicecode='" + devicecode + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
