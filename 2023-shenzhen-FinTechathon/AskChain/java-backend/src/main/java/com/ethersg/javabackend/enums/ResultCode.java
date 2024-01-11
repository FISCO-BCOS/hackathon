package com.ethersg.javabackend.enums;

import lombok.Getter;

/**
 * @author ahy231
 * @date 2021/7/24 10:04
 * @description 返回状态码
 */
@Getter
public enum ResultCode {
    SUCCESS(2000, "处理成功"),
    FAILED(4000, "处理失败");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
