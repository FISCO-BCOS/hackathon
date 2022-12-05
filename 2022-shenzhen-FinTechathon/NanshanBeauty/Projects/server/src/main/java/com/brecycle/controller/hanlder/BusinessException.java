package com.brecycle.controller.hanlder;

/**
 * 业务异常
 *
 * @author cmgun
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 4088001991951162L;

    public BusinessException(String msg) {
        super(msg);
    }

    public BusinessException(String msg, Throwable cause){
        super(msg, cause);
    }
}
