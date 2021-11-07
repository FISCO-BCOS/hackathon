package com.media.model;

/**
 * @author YZR
 * @date 2020/11/9 14:04
 */

public class LoginModel {

    private long uid;
    private long phoneNumber;
    private String userName;
    private String headView;
    private String backgroundWall;
    private String birth;
    private String sex;
    private String token;

    public LoginModel(long uid, long phoneNumber, String userName, String headView,
                      String backgroundWall, String birth, String sex, String token) {
        this.uid = uid;
        this.phoneNumber = phoneNumber;
        this.userName = userName;
        this.headView = headView;
        this.backgroundWall = backgroundWall;
        this.birth = birth;
        this.sex = sex;
        this.token = token;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHeadView() {
        return headView;
    }

    public void setHeadView(String headView) {
        this.headView = headView;
    }

    public String getBackgroundWall() {
        return backgroundWall;
    }

    public void setBackgroundWall(String backgroundWall) {
        this.backgroundWall = backgroundWall;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
