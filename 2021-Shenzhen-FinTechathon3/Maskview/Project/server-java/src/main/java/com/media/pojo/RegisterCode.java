package com.media.pojo;

import java.math.BigInteger;
import java.util.Date;

/**
 * @author YZR
 * @date 2020/11/10 10:08
 */

public class RegisterCode {

    private BigInteger phoneNumber;
    private int phoneCode;
    private BigInteger expireTime;

    public RegisterCode(BigInteger phoneNumber, int phoneCode, BigInteger expireTime) {
        this.phoneNumber = phoneNumber;
        this.phoneCode = phoneCode;
        this.expireTime = expireTime;
    }

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
}
