package com.media.pojo;

import java.math.BigInteger;

/**
 * @author YZR
 * @date 2020/11/10 14:42
 */

public class OtherCode {

    private BigInteger phoneNumber;
    private int phoneCode;
    private BigInteger expireTime;
    //private User user;

    public OtherCode(){}

    public OtherCode(BigInteger phoneNumber, int phoneCode, BigInteger expireTime) {
        this.phoneNumber = phoneNumber;
        this.phoneCode = phoneCode;
        this.expireTime = expireTime;
    }

    /*public OtherCode(long phoneNumber, int phoneCode, long expireTime, User user) {
        this.phoneNumber = phoneNumber;
        this.phoneCode = phoneCode;
        this.expireTime = expireTime;
        this.user = user;
    }*/

    public BigInteger getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(BigInteger phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(int phoneCode) {
        this.phoneCode = phoneCode;
    }

    public BigInteger getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(BigInteger expireTime) {
        this.expireTime = expireTime;
    }

    /*public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }*/
}
