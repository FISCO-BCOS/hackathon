package com.media.service.imp;

import com.media.dao.UserDao;
import com.media.pojo.User;
import com.media.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

@Service
@Transactional
public class UserImp implements UserService {

    @Autowired
    public UserDao userDao;

    @Override
    public User getUserInfoByUid(BigInteger uid) {
        return userDao.getUserInfoByUid(uid);
    }

    @Override
    public User getUserInfoByPhoneNumber(BigInteger phoneNumber) {
        return userDao.getUserInfoByPhoneNumber(phoneNumber);
    }
}
