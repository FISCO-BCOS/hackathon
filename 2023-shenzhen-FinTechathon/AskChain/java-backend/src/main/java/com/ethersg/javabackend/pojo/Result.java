package com.ethersg.javabackend.pojo;

import com.ethersg.javabackend.enums.ResultCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ahy231
 * @date 2021/7/24 10:03
 * @description
 */
@Getter
@Setter
public class Result<T> {
    /**
     * 状态码
     */
    private int code;

    /**
     * 状态描述
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;

    public Result(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public Result(ResultCode resultCode, T data) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
    }

    public Result(String error) {
        this.code = ResultCode.FAILED.getCode();
        this.message = error;
    }
}
