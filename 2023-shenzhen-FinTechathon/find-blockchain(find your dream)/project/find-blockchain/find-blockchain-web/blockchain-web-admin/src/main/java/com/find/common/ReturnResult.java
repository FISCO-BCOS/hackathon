package com.find.common;

import lombok.Data;

@Data
public class ReturnResult<T> {

    private Integer code = ReturnCode.SUCCESS_MSG;
    private String message = ReturnMsg.SUCCESS;
    private T result;

    public void setMessage(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

}
