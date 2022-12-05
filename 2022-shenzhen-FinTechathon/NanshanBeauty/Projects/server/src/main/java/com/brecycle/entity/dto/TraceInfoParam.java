package com.brecycle.entity.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author cmgun
 */
@Data
public class TraceInfoParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 电池编号
     */
    private String id;
}
