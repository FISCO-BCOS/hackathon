package com.media.service;

import com.media.pojo.User;

import java.math.BigInteger;


public interface UserService {

    User getUserInfoByUid(BigInteger uid);

    User getUserInfoByPhoneNumber(BigInteger phoneNumber);
}
