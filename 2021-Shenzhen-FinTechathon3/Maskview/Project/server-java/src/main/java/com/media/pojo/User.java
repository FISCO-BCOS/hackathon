package com.media.pojo;

import java.io.Serializable;

public class User implements Serializable {

    private long uid;
    private long phoneNumber;
    private String userName;
    private String salt;
    private String key;
    private String points;
    private String headView;
    private String backgroundWall;
    private String birth;
    private String sex;

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

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
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
}
