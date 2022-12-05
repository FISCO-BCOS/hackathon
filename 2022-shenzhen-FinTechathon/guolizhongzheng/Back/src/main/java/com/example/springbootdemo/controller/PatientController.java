package com.example.springbootdemo.controller;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.springbootdemo.service.PatientService;
import com.example.springbootdemo.utils.CommonResponse;
import com.example.springbootdemo.utils.bo.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
@RestController
@CrossOrigin
@ResponseBody
public class PatientController {

    @Resource
    private PatientService patientService;

    //患者注册:
    @RequestMapping("/PatientRegister")
    public CommonResponse PatientRegister(@RequestBody Patient patient){
        JSONObject _obj= patientService.PatientRegister(patient);
        if (_obj == null){
            return CommonResponse.fail("50001","患者注册失败，请重试");
        }
        return CommonResponse.ok(null);
    }

    //患者登陆:
    @RequestMapping("/PatientEnter")
    public CommonResponse PatientEnter(@RequestBody Enter enter){
        JSONArray _list = patientService.PatientEnter(enter);
        return CommonResponse.ok(_list);
    }

    //查询患者信息
    @RequestMapping("/QueryPat")
    public CommonResponse QueryPat(@RequestBody test t){
        JSONArray _list = patientService.QueryPat(t);
        return CommonResponse.ok(_list);
    }
    //查询患者电子病历本里面所有的电子病历
    @RequestMapping("/QueryAllEvi")
    public CommonResponse QueryAllEvi(@RequestBody test t){
        JSONArray _list = patientService.QueryAllEvi(t);
        return CommonResponse.ok(_list);
    }
    //查询患者某个电子病历的信息:
    @RequestMapping("/QuerEviMessage")
    public CommonResponse QuerEviMessage(@RequestBody EMR emr){
        JSONArray _list = patientService.QuerEviMessage(emr);
        return CommonResponse.ok(_list);
    }
    //查询患者所有的体检报告地址:
    @RequestMapping("/QueryAllReport")
    public CommonResponse QueryAllReport(@RequestBody  test t){
        JSONArray _list = patientService.QueryAllReport(t);
        return CommonResponse.ok(_list);
    }
    //查询某个体检报告体检信息:
    @RequestMapping("/QuerReportMessage")
    public CommonResponse QuerReportMessage(@RequestBody Report report){
        JSONArray _list = patientService.QuerReportMessage(report);
        return CommonResponse.ok(_list);
    }
    //查询所有的医院:
    @RequestMapping("/QueryAllHospital")
    public CommonResponse QueryAllHospital(){
        JSONArray _list = patientService.QueryAllHospital();
        return CommonResponse.ok(_list);
    }
    //查询该医院里面所有的医生:
    @RequestMapping("/QueryAllDoc")
    public CommonResponse QueryAllDoc(){
        JSONArray _list = patientService.QueryAllDoc();
        return CommonResponse.ok(_list);
    }
    //患者在主治医生那里挂号
    @RequestMapping("/Registration")
    public CommonResponse Registration(@RequestBody Registration registration){
        JSONObject _obj= patientService.Registration(registration);
        if (_obj == null){
            return CommonResponse.fail("50001","患者挂号失败，请重试");
        }
        return CommonResponse.ok(null);
    }

    //患者进行二维码验证
    @RequestMapping("/Verification")
    public CommonResponse Verification(@RequestBody Verfication verfication){
        JSONArray _list = patientService.Verification(verfication);
        return CommonResponse.ok(_list);
    }
    //患者去体检医生那里做医疗检测数据:
    @RequestMapping("/Phyexamation")
    public CommonResponse Phyexamation(@RequestBody Verfication verfication){
        JSONObject _obj= patientService.Phyexamation(verfication);
        if (_obj == null){
            return CommonResponse.fail("50001","体医疗检测失败，请重试");
        }
        return CommonResponse.ok(null);
    }
    //申请体检并挂号
    @RequestMapping("/Examination")
    public CommonResponse Examination(@RequestBody Registration registration){
        JSONObject _obj= patientService.Examination(registration);
        if (_obj == null){
            return CommonResponse.fail("50001","体检挂号失败，请重试");
        }
        return CommonResponse.ok(null);
    }
    //患者采用CP-ABE访问策略对自己的电子病历进行分权共享：
    //设置访问策略以及共享者的权限:
    @RequestMapping("/ShareData")
    public CommonResponse ShareData(@RequestBody ShareData shareData){
        JSONObject _obj= patientService.ShareData(shareData);
        if (_obj == null){
            return CommonResponse.fail("50001","共享失败，请重试");
        }
        return CommonResponse.ok(null);
    }
    //使用CP-ABE访问策略对自己的体检报告进行分权共享:
    //设置访问策略以及权限
    @RequestMapping("/ShareReport")
    public CommonResponse ShareReport(@RequestBody ShareReport shareReport){
        JSONObject _obj= patientService.ShareReport(shareReport);
        if (_obj == null){
            return CommonResponse.fail("50001","共享失败，请重试");
        }
        return CommonResponse.ok(null);
    }
    //将医生拉入电子病历全局的访问集合:
    @RequestMapping("/ShareEvidenceBen")
    public CommonResponse ShareEvidenceBen(@RequestBody Registration registration){
        JSONObject _obj= patientService.ShareEvidenceBen(registration);
        if (_obj == null){
            return CommonResponse.fail("50001","拉入失败，请重试");
        }
        return CommonResponse.ok(null);
    }
    //将医生拉入体检报告的全局访问集合:
    @RequestMapping("/ShareReportBen")
    public CommonResponse ShareReportBen(@RequestBody Registration registration){
        JSONObject _obj= patientService.ShareReportBen(registration);
        if (_obj == null){
            return CommonResponse.fail("50001","注册失败，请重试");
        }
        return CommonResponse.ok(null);
    }
    //出院之后可以选择删除医生所有的访问权限
    @RequestMapping("/DeleteAllAuthority")
    public CommonResponse DeleteAllAuthority(@RequestBody Registration registration){
        JSONObject _obj= patientService.DeleteAllAuthority(registration);
        if (_obj == null){
            return CommonResponse.fail("50001","注册失败，请重试");
        }
        return CommonResponse.ok(null);
    }
    //修改病历中某个集合元素的权限

    @RequestMapping("/ModifyEviAuthority")
    public CommonResponse ModifyEviAuthority(@RequestBody Modify modify){
        JSONObject _obj= patientService.ModifyEviAuthority(modify);
        if (_obj == null){
            return CommonResponse.fail("50001","注册失败，请重试");
        }
        return CommonResponse.ok(null);
    }
    //修改体检报告中访问集合的权限
    @RequestMapping("/ModifyRepAuthority")
    public CommonResponse ModifyRepAuthority(@RequestBody Modify modify){
        JSONObject _obj= patientService.ModifyRepAuthority(modify);
        if (_obj == null){
            return CommonResponse.fail("50001","修改失败，请重试");
        }
        return CommonResponse.ok(null);
    }

    //将病历中某个元素踢出访问集合
    @RequestMapping("/DeleteDocdd")
    public CommonResponse DeleteDocdd(@RequestBody Delete delete){
        JSONObject _obj= patientService.DeleteDocdd(delete);
        if (_obj == null){
            return CommonResponse.fail("50001","踢出失败，请重试");
        }
        return CommonResponse.ok(null);
    }
    //将体检报告中某个元素踢出访问集合
    @RequestMapping("/DeleteRepdd")
    public CommonResponse DeleteRepdd(@RequestBody Delete delete){
        JSONObject _obj= patientService.DeleteRepdd(delete);
        if (_obj == null){
            return CommonResponse.fail("50001","踢出失败，请重试");
        }
        return CommonResponse.ok(null);
    }
}
