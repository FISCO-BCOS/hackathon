package com.media.dao;

import com.media.pojo.OtherCode;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

/**
 * @author YZR
 * @date 2020/11/10 11:42
 */

@Mapper
@Repository
public interface OtherCodeDao {

    void insertOtherCode(OtherCode otherCode);

    int getHerCode(BigInteger phoneNumber);

    Integer isExist(BigInteger phoneNumber);

    void updateCode(OtherCode otherCode);

}
