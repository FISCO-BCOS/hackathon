//////单元测试;
//package com.example.springbootdemo;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.example.springbootdemo.service.EduService;
//import com.example.springbootdemo.service.StuService;
//import com.example.springbootdemo.utils.bo.SaveEvi;
//import org.apache.ibatis.annotations.Mapper;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.junit.jupiter.api.Test;
//import javax.annotation.Resource;
//@SpringBootTest
//@Mapper
//public  class SpringbootdemoApplicationTests {
////
////    ////////////智能合约测试/////////
////    //测试该工具类是否能生效;
////    //导入某个类;
////
////    @Resource
////    private StuService stuService;
////    //调用合约的某个查询方法;
////
////    @Test
////    //测试查询该学生;
////    void setStudentDotTest( ) {
////        JSONArray result=stuService.QueryMessage("0x8434047af916f086327679466f7a8c660713c665");
////        System.out.println(result);
////    }
////
////    //查看证书分数;
////    @Test
////    void getCreditMessageTest(){
////        JSONArray result=stuService.getCreditMessage("0x1fbb77716607e9fbcb124fd7d6c9a3b651a5340ab451db5c12126f371ee8bcf2");
////        System.out.println(result);
////    }
////
////    //查看该学历证信息;
////    @Test
////    void getEviMessageTest(){
////        JSONArray result=stuService.getEviMessage("0x1fbb77716607e9fbcb124fd7d6c9a3b651a5340ab451db5c12126f371ee8bcf2");
////        System.out.println(result);
////    }
//    SaveEvi saveEvi;
//    @Resource
//    private EduService eduService;
//    @Test
//    void Generator(){
////        JSONObject result=eduService.SaveEvidence(saveEvi{
////                "eid":"0xdbb2fb1b3d55b29866ddae07635d8e606c3bfb99934baf45c53fc4f2c9fc7cd4",
////                "sch":"0x9b4390363196826b8acb4e9adee5ec83254f7a77",
////                "stu":"0x8434047af916f086327679466f7a8c660713c665",
////                "name":"刘力伟",
////                "gender":"男",
////                "birth":"2022",
////                "id":"360202202",
////                "enterTime":"2020",
////                "outTime":"2024",
////                "major":"CS",
////                "eduType":"全日制",
////                "course":["大学物理","大学语文"],
////        "credit":[100,97],
////        "event":["ICPC","CCPC"]
//});
//        System.out.println(result);
//    }
//
//
//
//
//
//
//
//}
