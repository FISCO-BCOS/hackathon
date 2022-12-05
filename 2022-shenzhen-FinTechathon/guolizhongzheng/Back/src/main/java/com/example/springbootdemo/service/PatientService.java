package com.example.springbootdemo.service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
//import com.example.springbootdemo.dao.PatientMapper;
import com.example.springbootdemo.utils.HttpUtils;
import com.example.springbootdemo.utils.bo.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
@ResponseBody
@Service
@Resource

public class PatientService {

	public String x=",";
	//@Resource
//	private PatientMapper patientMapper;
	//患者注册:
	public  JSONObject PatientRegister(Patient patient){
		//定义一个列表类型;
		List<String> funParam = new ArrayList<String>();
		funParam.add(patient.getCipher());
		String All;
		All=patient.getName()+patient.getSex()+patient.getAge()+patient.getId();
		funParam.add(JSON.toJSONString(All));
		//密码，名字，性别，年龄，身份证号:
		System.out.println(funParam);//输出参数看看有没有传进去:
		JSONObject result = HttpUtils.writeContract("PatientRegister", funParam);
		System.out.println(result);
		return result;
	}
	//患者登陆:
	public  JSONArray PatientEnter(Enter enter){

		//定义一个列表类型;
		List<String> funParam = new ArrayList<>();
		funParam.add(enter.getAdd());
		funParam.add(enter.getCipher());
		System.out.println(funParam);//输出参数看看有没有传进去:
		JSONArray result = HttpUtils.AllwriteContract("PatientEnter", funParam);
		System.out.println(result);
		return result;
	}
	//查询患者信息:
	public  JSONArray QueryPat(test t){
		//定义一个列表类型;
		List<String> funParam = new ArrayList<>();
		funParam.add(t.getPatAdd());
		System.out.println(funParam);//输出参数看看有没有传进去:
		JSONArray result = HttpUtils.AllwriteContract("QueryPat", funParam);
		System.out.println(result);
		return result;
	}
	//查询患者电子病历本里面所有的电子病历:
	public  JSONArray QueryAllEvi(test t){
		//定义一个列表类型;
		List<String> funParam = new ArrayList<String>();
		funParam.add(t.getPatAdd());
		System.out.println(funParam);//输出参数看看有没有传进去:
		JSONArray result = HttpUtils.AllwriteContract("QueryAllEvi", funParam);
		System.out.println(result);
		return result;
	}
	//查询患者某个电子病历的信息:
	public  JSONArray QuerEviMessage(EMR emr){
		//定义一个列表类型;
		List<String> funParam = new ArrayList<String>();
		funParam.add(emr.getPatAdd());
		funParam.add(emr.getEviadd());
		System.out.println(funParam);//输出参数看看有没有传进去:
		JSONArray result = HttpUtils.AllwriteContract("QuerEviMessage", funParam);
		System.out.println(result);
		return result;
	}

	//查询患者所有的体检报告地址:
	public  JSONArray QueryAllReport(test t){
		//定义一个列表类型;
		List<String> funParam = new ArrayList<String>();
		funParam.add(t.getPatAdd());
		System.out.println(funParam);//输出参数看看有没有传进去:
		JSONArray result = HttpUtils.AllwriteContract("QueryAllReport", funParam);
		System.out.println(result);
		return result;
	}

	//查询某个体检报告体检信息:
	public  JSONArray QuerReportMessage(Report report){
		//定义一个列表类型;
		List<String> funParam = new ArrayList<String>();
		funParam.add(report.getPatAdd());
		funParam.add(report.getRepadd());
		System.out.println(funParam);//输出参数看看有没有传进去:
		JSONArray result = HttpUtils.AllwriteContract("QuerReportMessage", funParam);
		System.out.println(result);
		return result;
	}
	//查询所有的医院:
	public  JSONArray QueryAllHospital(){
		//定义一个列表类型;
//        List funParam=new ArrayList();
//        funParam.add(report.getPatAdd());
//        funParam.add(report.getRepadd());
//        System.out.println(funParam);//输出参数看看有没有传进去:
		JSONArray result = HttpUtils.readContract("QueryAllHospital");
		System.out.println(result);
		return result;
	}

	//查询该医院里面所有的医生:
	public  JSONArray QueryAllDoc(){
		//定义一个列表类型;
		List<String> funParam = new ArrayList<String>();
//		funParam.add(hospital.getHospitalNmae());
		System.out.println(funParam);//输出参数看看有没有传进去:
		JSONArray result = HttpUtils.readContract("QueryAllDoc");
		System.out.println(result);
		return result;
	}
	//患者在医生那里挂号:
	public  JSONObject Registration(Registration registration){
		//定义一个列表类型;
		List<String> funParam = new ArrayList<String>();
		funParam.add(registration.getPatAdd());
		funParam.add(registration.getDocterAddress());
		System.out.println(funParam);//输出参数看看有没有传进去:
		JSONObject result = HttpUtils.writeContract("Registration", funParam);
		System.out.println(result);
		return result;
	}
	//患者进行二维码验证:
	public  JSONArray Verification(Verfication verfication){
		//定义一个列表类型;
		List<String> funParam = new ArrayList<String>();
		funParam.add(verfication.getPatAdd());
		funParam.add(verfication.getDocAdd());
		funParam.add(verfication.getEviaddress());
		System.out.println(funParam);//输出参数看看有没有传进去:
		JSONArray result = HttpUtils.AllwriteContract("Verification", funParam);
		System.out.println(result);
		return result;
	}
	//患者去体检医生那里做医疗检测数据:
	public  JSONObject Phyexamation(Verfication verfication){
		//定义一个列表类型;
		List<String> funParam = new ArrayList<String>();
		funParam.add(verfication.getPatAdd());
		funParam.add(verfication.getDocAdd());
		funParam.add(verfication.getEviaddress());
		System.out.println(funParam);//输出参数看看有没有传进去:
		JSONObject result = HttpUtils.writeContract("Phyexamation", funParam);
		System.out.println(result);
		return result;
	}
	//申请体检并挂号:
	public  JSONObject Examination(Registration registration){
		//定义一个列表类型;
		List<String> funParam = new ArrayList<String>();
		funParam.add(registration.getPatAdd());
		funParam.add(registration.getDocterAddress());
		System.out.println(funParam);//输出参数看看有没有传进去:
		JSONObject result = HttpUtils.writeContract("Examination", funParam);
		System.out.println(result);
		return result;
	}
	//患者采用CP-ABE访问策略对自己的电子病历进行分权共享：
	//设置访问策略以及共享者的权限:
	public  JSONObject ShareData(ShareData shareData){
		//定义一个列表类型;
		List funParam=new ArrayList();
		funParam.add(shareData.getPatAdd());
		funParam.add(shareData.getEviAdd());
		funParam.add(shareData.getAdd());
		funParam.add(shareData.getLevle());
		System.out.println(funParam);//输出参数看看有没有传进去:
		JSONObject result = HttpUtils.writeContract("ShareData", funParam);
		System.out.println(result);
		return result;
	}
	//使用CP-ABE访问策略对自己的体检报告进行分权共享:
	//设置访问策略以及权限
	public  JSONObject ShareReport(ShareReport shareReport){
		//定义一个列表类型;
		List funParam=new ArrayList();
		funParam.add(shareReport.getPatAdd());
		funParam.add(shareReport.getRepAdd());
		funParam.add(shareReport.getAdd());
		funParam.add(shareReport.getLevle());
		System.out.println(funParam);//输出参数看看有没有传进去:
		JSONObject result = HttpUtils.writeContract("ShareReport", funParam);
		System.out.println(result);
		return result;
	}
	//将医生拉入电子病历全局的访问集合:
	public  JSONObject ShareEvidenceBen(Registration registration){
		//定义一个列表类型;
		List funParam=new ArrayList();
		funParam.add(registration.getPatAdd());
		funParam.add(registration.getDocterAddress());
		System.out.println(funParam);//输出参数看看有没有传进去:
		JSONObject result = HttpUtils.writeContract("ShareEvidenceBen", funParam);
		System.out.println(result);
		return result;
	}
	//将医生拉入体检报告的全局访问集合:
	public  JSONObject ShareReportBen(Registration registration){
		//定义一个列表类型;
		List funParam=new ArrayList();
		funParam.add(registration.getPatAdd());
		funParam.add(registration.getDocterAddress());
		System.out.println(funParam);//输出参数看看有没有传进去:
		JSONObject result = HttpUtils.writeContract("ShareReportBen", funParam);
		System.out.println(result);
		return result;
	}
	//出院之后可以选择删除医生所有的访问权限
	public  JSONObject DeleteAllAuthority(Registration registration){
		//定义一个列表类型;
		List funParam=new ArrayList();
		funParam.add(registration.getPatAdd());
		funParam.add(registration.getDocterAddress());
		System.out.println(funParam);//输出参数看看有没有传进去:
		JSONObject result = HttpUtils.writeContract("DeleteAllAuthority", funParam);
		System.out.println(result);
		return result;
	}
	//修改病历中某个集合元素的权限
	public  JSONObject ModifyEviAuthority(Modify modify){
		//定义一个列表类型;
		List funParam=new ArrayList();
		funParam.add(modify.getEviadd());
		funParam.add(modify.getAdd());
		funParam.add(modify.getLevel());
		System.out.println(funParam);//输出参数看看有没有传进去:
		JSONObject result = HttpUtils.writeContract("ModifyEviAuthority", funParam);
		System.out.println(result);
		return result;
	}
	//修改体检报告中访问集合的权限
	public  JSONObject ModifyRepAuthority(Modify modify){
		//定义一个列表类型;
		List funParam=new ArrayList();
		funParam.add(modify.getEviadd());
		funParam.add(modify.getAdd());
		funParam.add(modify.getLevel());
		System.out.println(funParam);//输出参数看看有没有传进去:
		JSONObject result = HttpUtils.writeContract("ModifyRepAuthority", funParam);
		System.out.println(result);
		return result;
	}
	//将病历中某个元素踢出访问集合
	public  JSONObject DeleteDocdd(Delete delete){
		//定义一个列表类型;
		List funParam=new ArrayList();
		funParam.add(delete.getDocadd());
		funParam.add(delete.getEviadd());
		System.out.println(funParam);//输出参数看看有没有传进去:
		JSONObject result = HttpUtils.writeContract("DeleteDocdd", funParam);
		System.out.println(result);
		return result;
	}
	//将体检报告中某个元素踢出访问集合
	public  JSONObject DeleteRepdd(Delete delete){
		//定义一个列表类型;
		List funParam=new ArrayList();
		funParam.add(delete.getDocadd());
		funParam.add(delete.getEviadd());
		System.out.println(funParam);//输出参数看看有没有传进去:
		JSONObject result = HttpUtils.writeContract("DeleteRepdd", funParam);
		System.out.println(result);
		return result;
	}

}
