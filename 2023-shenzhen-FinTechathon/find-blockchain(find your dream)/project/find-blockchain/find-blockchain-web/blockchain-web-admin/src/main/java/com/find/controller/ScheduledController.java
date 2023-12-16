package com.find.controller;


import com.find.NodeSecurService;
import com.find.util.HttpUtil;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//定时任务
@Controller
@RequestMapping("/blk")
public class ScheduledController {

    @Reference(loadbalance = "roundrobin")
    private NodeSecurService nodeSecurService;

    //定时功能--定时查询安全节点（检查证书以及信誉值）
    @Scheduled(fixedRate = 1200000) // 每隔20分钟执行一次
    public void querySecurityTask() {
        // 执行你的任务逻辑
        String url = "http://127.0.0.1:8085/fl/querySecurity";
        //发起http
        HttpUtil.get(url);
        System.out.println("定时查询安全节点任务执行");
    }

//    //定时功能--执行联邦学习业务
//    @Scheduled(fixedRate = 1200000) // 每隔20分钟执行一次
//    public void startTrainingTask() {
//        // 执行你的任务逻辑
//        String url = "http://192.168.253.129:8085/fl/startTraining?rounds=5&dataset=MINST&nodeNums=5";
//        //发起http
//        HttpUtil.get(url);
//        System.out.println("定时定时发布联邦学习任务任务执行");
//    }




//    @Reference
//    NodeManaging nodeManaging;

//    @Reference(loadbalance="roundrobin")
//    AsyncContractService asyncContractService;
//
//    @RequestMapping("/observerRegister")
//    public String observerRegister(@RequestParam("nodeId") String nodeId,@RequestParam("nodeType") String nodeType){
//
//        try {
//            nodeManaging.ObserverRegister(nodeId);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return "done";
//    }

//        @RequestMapping("/getOberverList")
//    public String getOberverList(){
//
//        return nodeManaging.getObserverList();
//    }

//    @RequestMapping("/queryAllData")
//    @ResponseBody
//    public Object queryAlldata(){
//
////        JSONArray array = new JSONArray();
////
////        Tuple3<List<String>, List<BigInteger>, List<String>> queryData = asyncContractService.queryData();
////
////        List<String> idList = queryData.getValue1();
////        List<BigInteger> timesList = queryData.getValue2();
////        List<String> hashList = queryData.getValue3();
////
////        for (int i = 0; i < idList.size(); i++) {
////            OnChainData data = new OnChainData();
////            data.setId(idList.get(i));
////            data.setTimes(String.valueOf(timesList.get(i)));
////            data.setModelHash(hashList.get(i));
////            array.add(JSONObject.toJSON(data));
////        }
//        JSONArray dataArray = asyncContractService.queryData();
////
//        Table table = new Table(0, "", dataArray.size(),dataArray);
//        return JSONObject.toJSON(table);
//
//    }
//
//    @RequestMapping("/createData")
//    public String createData(@RequestParam("id") String id,  @RequestParam("times") String times,
//                             @RequestParam("modelHash") String modelHash) {
//        try {
//            asyncContractService.createData(id,  times, modelHash);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "redirect:/gradienttest";
//    }

}
