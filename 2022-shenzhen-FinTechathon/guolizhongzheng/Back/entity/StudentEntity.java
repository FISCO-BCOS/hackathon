package com.zgxt.springbootdemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Binge
 * @desc
 * @date 2022/9/20 14:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("test") //指定数据库表名
public class StudentEntity {
    @TableId(type = IdType.AUTO) //指明自增主键id
    private Integer id;
    private String name;
    private int age;
    private String gender;
    private String address;
    private String email;
}
