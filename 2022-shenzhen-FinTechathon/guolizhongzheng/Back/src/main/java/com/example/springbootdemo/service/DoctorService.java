package com.example.springbootdemo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
//import com.example.springbootdemo.dao.DoctorMapper;
import com.example.springbootdemo.utils.HttpUtils;
import com.example.springbootdemo.utils.bo.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
@Service
@ResponseBody
@Resource
public class DoctorService {

	//主治医生注册:
	public  JSONObject DoctorRegister(Doctor doctor) {
		//定义一个列表类型;
		List funParam = new ArrayList();
		funParam.add(JSON.toJSONString(doctor));
		funParam.add(doctor.getCipher());
		funParam.add(doctor.getHospitalname());
		System.out.println(funParam);//输出参数看看有没有传进去:
		JSONObject result = HttpUtils.writeContract("DoctorRegister", funParam);
		System.out.println(result);
		return result;
	}

	//主治医生登陆:
	public  JSONArray DoctorEnter(Enter enter) {
		//定义一个列表类型;
		List funParam = new ArrayList();
		funParam.add(enter.getAdd());
		funParam.add(enter.getCipher());
		System.out.println(funParam);//输出参数看看有没有传进去:
		JSONArray result = HttpUtils.AllwriteContract("DoctorEnter", funParam);
		System.out.println(result);
		return result;
	}

	//主治医生查询基本信息:
	public  JSONArray QueryDoctor(d d1) {
		//定义一个列表类型;
		List funParam = new ArrayList();
		funParam.add(d1.getDocAdd());
		System.out.println(funParam);//输出参数看看有没有传进去:
		JSONArray result = HttpUtils.AllwriteContract("QueryDoctor", funParam);
		System.out.println(result);
		return result;
	}

	//主治医生对患者的电子病历进行医学操作:
	public  JSONObject AttendDoctor(Attend attend) {
		//定义一个列表类型;
		List funParam = new ArrayList();
		funParam.add(attend.getEviAdd());
		String x;
		//症状+生命体征+医疗处方+医嘱+手术操作
		x = attend.getSymptom() +","+ attend.getVital()+","+attend.getPrescribe()+"," + attend.getAdvice()+"," + attend.getOperator();
		funParam.add(x);
		funParam.add(attend.getDocAdd());
		funParam.add(attend.getValue());
		funParam.add(attend.getScarity());
		System.out.println(funParam);//输出参数看看有没有传进去:
		JSONObject result = HttpUtils.writeContract("AttendDoctor", funParam);
		System.out.println(result);
		return result;
	}

	//检测医生注册:
	public  JSONObject TestDoctorRegister(TestDoctor testdoctor) {
		//定义一个列表类型;
		List funParam = new ArrayList();
		funParam.add(JSON.toJSONString(testdoctor));
		funParam.add(testdoctor.getCipher());
       //funParam.add(testdoctor.getHospitalname());
		System.out.println(funParam);//输出参数看看有没有传进去:
		JSONObject result = HttpUtils.writeContract("TestDoctorRegister", funParam);
		System.out.println(result);
		return result;
	}
	//检测医生登陆:
	public  JSONArray TestDoctorEnter(Enter enter) {
		//定义一个列表类型;
		List funParam = new ArrayList();
		funParam.add(enter.getAdd());
		funParam.add(enter.getCipher());
		System.out.println(funParam);//输出参数看看有没有传进去:
		JSONArray result = HttpUtils.AllwriteContract("TestDoctorEnter", funParam);
		System.out.println(result);
		return result;
	}
	//查询检测医生信息:
	public  JSONArray QueryTestDoctor(td t) {
		//定义一个列表类型;
		List funParam = new ArrayList();
		funParam.add(t.getTestAdd());
		System.out.println(funParam);//输出参数看看有没有传进去:
		JSONArray result = HttpUtils.AllwriteContract("QueryTestDoctor", funParam);
		System.out.println(result);
		return result;
	}
//	//检测医生对患者的电子病历进行操作:
//	public  JSONObject Phyexamation(Phyexamination phyexamination){
//		List funParam = new ArrayList();
//		funParam.add()
//
//
//	}
	//检测医生录入体检数据:
	public  JSONObject PhyDoctor(PhyDoctor phyDoctor) {
		List funParam = new ArrayList();
		funParam.add(phyDoctor.getReportadd());
		funParam.add(phyDoctor.getDocAdd());
		String x;
		//血压+血红蛋白+尿检+bmi+白细胞:
		x=phyDoctor.getBloodpress()+","+phyDoctor.getHemoglobin()+","+phyDoctor.getUrinalysis()+","+phyDoctor.getBmi()+","+phyDoctor.getHemameba();
		funParam.add(x);
		funParam.add(phyDoctor.getValue());
		funParam.add(phyDoctor.getScarity());
		System.out.println(funParam);//输出参数看看有没有传进去:
		JSONObject result = HttpUtils.writeContract("PhyDoctor", funParam);
		System.out.println(result);
		return result;
	}

	public  JSONObject HolEvidDoctor(HolEviDoctor holEviDoctor){
		List funParam = new ArrayList();
		funParam.add(holEviDoctor.getDocAdd());
		funParam.add(holEviDoctor.getToDocAdd());
		funParam.add(holEviDoctor.getEviAdd());
		System.out.println(funParam);//输出参数看看有没有传进去:
		JSONObject result = HttpUtils.writeContract("HolEvidDoctor", funParam);
		System.out.println(result);
		return result;
	}
}
