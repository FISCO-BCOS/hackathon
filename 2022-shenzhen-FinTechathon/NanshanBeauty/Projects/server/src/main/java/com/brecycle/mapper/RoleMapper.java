package com.brecycle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.brecycle.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author cmgun
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据userId查找对应角色列表
     * @param userId
     * @return
     */
    List<Role> selectByUserId(@Param("userId") Long userId);
}
