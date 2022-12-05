package com.example.springbootdemo.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EMREntity {

	private String eviAdd;//科室
	private String symptom;//症状
	private String vital;//生命体征
	private String prescribe;//开药处方
	private String advice;//医嘱
	private String operator;//手术操作
	private String docAdd;//医生地址
	private int value;
	private int scarity;

}