package com.media.service;

import com.media.pojo.OtherCode;

import java.math.BigInteger;

/**
 * @author YZR
 * @date 2020/11/10 14:44
 */

public interface OtherCodeService {

    void insertOtherCode(OtherCode otherCode);

    int getHerCode(BigInteger phoneNumber);

    boolean isExist(BigInteger phoneNumber);

    void updateCode(OtherCode otherCode);

}
