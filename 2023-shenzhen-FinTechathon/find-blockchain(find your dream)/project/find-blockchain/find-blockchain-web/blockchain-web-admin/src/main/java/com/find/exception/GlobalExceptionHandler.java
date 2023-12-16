package com.find.exception;


import com.find.common.ReturnCode;
import com.find.common.ReturnMsg;
import com.find.common.ReturnResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Description:
 * Author: Su
 * Date: 2023/10/24
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ValidationException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ReturnResult<Object> handleRuntimeException(ValidationException ex) {

        ReturnResult<Object> ret = new ReturnResult<>();
        System.out.println("+++++++++++++" + ex.getMessage());
        ret.setMessage(ReturnMsg.PARAMS_VALIDATE_FAILED + "," + ex.getMessage(), ReturnCode.PARAMS_VALIDATE_FAILED);
        return ret;
    }
    // 其他异常处理方法
}

