package com.example.springbootdemo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientEntity {
	private String cipher;//密码
	private String name;//姓名
	private String sex;//性别
	private String age;//年龄
	private String id;//身份证
}
