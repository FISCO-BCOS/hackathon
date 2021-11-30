package com.media.dao;

import com.media.pojo.ImageKey;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ImageKeyDao {

    // 通过手机号查ImageKey的信息
    List<ImageKey> findImageKeyByPhoneNumber(long phoneNumber);

}
