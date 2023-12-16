package com.find.pojo;

import com.alibaba.fastjson.JSONArray;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 表格数据返回格式
 */
@Setter
@Getter
@ToString
public class Table implements Serializable {
    private int code;
    private String msg;
    private int count;
    private JSONArray data;

    public Table(int code, String msg, int count, JSONArray data) {
        this.code = code;
        this.msg = msg;
        this.count = count;
        this.data = data;
    }
}
