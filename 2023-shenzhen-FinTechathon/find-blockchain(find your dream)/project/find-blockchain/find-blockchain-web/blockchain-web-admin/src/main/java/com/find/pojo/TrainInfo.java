package com.find.pojo;

import com.find.annotation.VerifyParam;
import lombok.Data;

/**
 * Description:
 * Author: Su
 * Date: 2023/11/6
 */

@Data
public class TrainInfo {

    private String nodeId;

    private String times;

    private String username;

    private String timestamp;

    private double reward;

    private float accuracy;

    private int work_num;

}
