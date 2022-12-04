package com.example.springbootdemo.controller;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.springbootdemo.service.DoctorService;
import com.example.springbootdemo.utils.CommonResponse;
import com.example.springbootdemo.utils.bo.*;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@CrossOrigin
@ResponseBody
public class DoctorController {

    @Resource
    DoctorService doctorService;

    //主治医生注册:
    @RequestMapping("/DoctorRegister")
    public CommonResponse DoctorRegister(@RequestBody Doctor doctor){
        JSONObject _obj= doctorService.DoctorRegister(doctor);
        if (_obj == null){
            return CommonResponse.fail("50001","主治医生注册失败，请重试");
        }
        return CommonResponse.ok(null);
    }
    //主治医生登陆
    @RequestMapping("/DoctorEnter")
    public CommonResponse DoctorEnter(@RequestBody Enter enter){
        JSONArray _list = doctorService.DoctorEnter(enter);
        return CommonResponse.ok(_list);
    }
    //主治医生查询基本信息
    @RequestMapping("/QueryDoctor")
    public CommonResponse QueryDoctor(@RequestBody d d1){
        JSONArray _list = doctorService.QueryDoctor(d1);
        return CommonResponse.ok(_list);
    }
    //主治医生对患者的电子病历进行医学操作:
    @RequestMapping("/AttendDoctor")
    public CommonResponse AttendDoctor(@RequestBody Attend attend){
        JSONObject _obj= doctorService.AttendDoctor(attend);
        if (_obj == null){
            return CommonResponse.fail("50001","医学操作失败，请重试");
        }
        return CommonResponse.ok(null);
    }
    //检测医生注册:
    @RequestMapping("/TestDoctorRegister")
    public CommonResponse TestDoctorRegister(@RequestBody TestDoctor testdoctor){
        JSONObject _obj= doctorService.TestDoctorRegister(testdoctor);
        if (_obj == null){
            return CommonResponse.fail("50001","检测医生注册失败，请重试");
        }
        return CommonResponse.ok(null);
    }
    //检测医生登陆:
    @RequestMapping("/TestDoctorEnter")
    public CommonResponse TestDoctorEnter(@RequestBody Enter enter){
        JSONArray _list = doctorService.TestDoctorEnter(enter);
        return CommonResponse.ok(_list);
    }
    //查询检测医生信息
    @RequestMapping("/QueryTestDoctor")
    public CommonResponse QueryTestDoctor(@RequestBody td t ){
        JSONArray _list = doctorService.QueryTestDoctor(t);
        return CommonResponse.ok(_list);
    }

    //检测医生录入体检数据
    @RequestMapping("/PhyDoctor")
    public CommonResponse PhyDoctor(@RequestBody PhyDoctor phyDoctor){
        JSONObject _obj= doctorService.PhyDoctor(phyDoctor);
        if (_obj == null){
            return CommonResponse.fail("50001","体检医生录入数据失败，请重试");
        }
        return CommonResponse.ok(null);
    }
    //拉取医生:
    @RequestMapping("/HolEvidDoctor")
    public CommonResponse HolEvidDoctor(@RequestBody HolEviDoctor holEviDoctor){
        JSONObject _obj= doctorService.HolEvidDoctor(holEviDoctor);
        if (_obj == null){
            return CommonResponse.fail("50001","拉取失败!!!");
        }
        return CommonResponse.ok(null);
    }

}
