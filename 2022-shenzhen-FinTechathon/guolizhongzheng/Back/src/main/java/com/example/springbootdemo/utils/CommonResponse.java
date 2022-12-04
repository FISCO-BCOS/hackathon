package com.example.springbootdemo.utils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/*
响应用户请求封装的工具类;
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse {

    public static final String OK="200";
    public  String code;//响应码;
    public  String message;//响应描述信息;
    public  Object data;//一个对象;
    //返回正确的值;
    public static CommonResponse ok(Object data){
        return new CommonResponse(OK,"200",data);
    }
    //返回错误的值;
    public static CommonResponse fail(String code,String message){
        return new CommonResponse(code,message,null);
    }

    public static  CommonResponse set(String code,String message,Object data){
        return new CommonResponse(code,message,data);
    }
}