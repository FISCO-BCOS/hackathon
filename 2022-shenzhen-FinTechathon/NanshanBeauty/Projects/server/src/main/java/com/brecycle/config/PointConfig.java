package com.brecycle.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 积分模块配置
 *
 * @author cmgun
 */
@Data
@Component
@ConfigurationProperties(prefix = "point")
public class PointConfig {

    public Boolean isFirstYear;

    public Integer varT;

    public Map<String, Integer> marketPredict;

    public Map<String, Integer> systemPredict;

    public Double carAvgKAh;

    public Double batteryAvgKAh;

    public Float alpha;

    public Float firstRecyclePartRatio;

    public Float secondRecyclePartRatio;

    public Float endRecyclePartRatio;

    public Float producePayRatio;

    public String pointController;

    public String dao;
}
