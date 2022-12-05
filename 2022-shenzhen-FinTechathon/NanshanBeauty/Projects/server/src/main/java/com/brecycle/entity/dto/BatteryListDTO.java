package com.brecycle.entity.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 电池列表查询返回信息体
 *
 * @author cmgun
 */
@Data
public class BatteryListDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String status;
}
