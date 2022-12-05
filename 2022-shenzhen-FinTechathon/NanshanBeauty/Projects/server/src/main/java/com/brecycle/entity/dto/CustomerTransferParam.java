package com.brecycle.entity.dto;

import lombok.Data;

/**
 * @author cmgun
 */
@Data
public class CustomerTransferParam extends BatteryTransferParam {

    /**
     * 电池充放电次数
     */
    private Long chargeTimes;
}
