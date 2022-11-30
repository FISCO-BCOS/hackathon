package com.zgxt.springbootdemo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zgxt.springbootdemo.entity.StudentEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Binge
 * @desc
 * @date 2022/9/20 14:29
 */
@Mapper
public interface StudentDao extends BaseMapper<StudentEntity> {
}
