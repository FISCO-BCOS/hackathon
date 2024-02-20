package com.find.exception;

/**
 * Description:
 * Author: Su
 * Date: 2023/10/24
 */


public class ValidationException extends RuntimeException {

    private String message;

    public ValidationException(String message) {
        super(message);
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
