package com.media.dao;

import com.media.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Mapper
@Repository
public interface UserDao {

    User getUserInfoByUid(BigInteger uid);

    User getUserInfoByPhoneNumber(BigInteger phoneNumber);
}
