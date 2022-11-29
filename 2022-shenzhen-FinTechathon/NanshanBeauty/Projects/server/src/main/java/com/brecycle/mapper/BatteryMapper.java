package com.brecycle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.brecycle.entity.Battery;
import com.brecycle.entity.dto.YearRecycleEntDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author cmgun
 */
@Mapper
public interface BatteryMapper extends BaseMapper<Battery> {

    List<YearRecycleEntDTO> selectYearRecycleEntInfo(@Param("endTime") String endTime, @Param("startTime") String startTime);
}
