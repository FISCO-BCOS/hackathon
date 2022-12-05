package com.brecycle.entity.dto;

import lombok.Data;

import java.util.Collection;

/**
 * @author cmgun
 */
@Data
public class PageResult<T> {

    private long total;

    private long pageNo;

    private long pageSize;

    private long pageCount;

    private Collection<T> data;
}
