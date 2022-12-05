package com.brecycle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.brecycle.entity.User;
import com.brecycle.entity.dto.EntListDTO;
import com.brecycle.entity.dto.EntListParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author cmgun
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询，带状态
     * @param name
     * @return
     */
    User selectByUserName(@Param("name") String name);

    /**
     * 根据名称查询
     * @param name
     * @return
     */
    User selectByName(@Param("name") String name);

    User selectByAddr(@Param("addr") String addr);

    /**
     * 企业列表查询
     * @param param
     * @return
     */
    IPage<EntListDTO> selectEntListByPage(IPage page, @Param("param") EntListParam param);
}
