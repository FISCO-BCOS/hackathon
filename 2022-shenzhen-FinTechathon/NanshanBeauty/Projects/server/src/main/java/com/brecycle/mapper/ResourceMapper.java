package com.brecycle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.brecycle.entity.Resource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author cmgun
 */
@Mapper
public interface ResourceMapper extends BaseMapper<Resource> {

    /**
     * 根据角色id查找资源
     *
     * @param roleIds
     * @return
     */
    List<Resource> selectByRoleIds(@Param("roleId") List<Long> roleIds);
}
