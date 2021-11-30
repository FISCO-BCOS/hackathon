package com.media.dao;

import com.media.pojo.RegisterCode;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author YZR
 * @date 2020/11/10 10:25
 */
@Mapper
@Repository
public interface RegisterCodeDao {

    void insertRegisterCode(RegisterCode registerCode);

}
