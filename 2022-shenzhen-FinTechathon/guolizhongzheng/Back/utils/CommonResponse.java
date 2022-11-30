package com.zgxt.springbootdemo.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 响应用户请求封装工具类
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse {
    public static final String OK = "200";

    public String code;    //响应码
    public String message; //响应描述
    public Object data;    //响应数据

    public static CommonResponse ok(Object data){
        return new CommonResponse(OK, "200",data);
    }

    public static CommonResponse fail(String code, String message){
        return new CommonResponse(code, message, null);
    }

    public static CommonResponse set(String code, String message,Object data){
        return new CommonResponse(code, message, data);
    }
}
