package szu.zhl.test1.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import szu.zhl.test1.entity.*;
import szu.zhl.test1.service.UserService;
import szu.zhl.test1.utils.*;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/login")

    public ResponseEntity<Map<String, Object>> login(@RequestBody ReceiveLoginData receiveloginData) {
        Map<String, Object> response = new HashMap<>();//存放返回前端信息
       System.out.println("接收到前端数据");

        //调用登录服务函数
        Map<String,String> result = UserService.login(receiveloginData);
        if(result.get("message").equals("请先注册") || result.get("message").equals("用户生物特征错误")){
            response.put("success", false);
            response.put("message", result);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }





        response.put("success", true);
        response.put("emb_dist", result.get("emb_dist"));
        response.put("rand_dist", result.get("rand_dist"));

        response.put("message", "登录成功");
        System.out.println("responses"+response);
        return new ResponseEntity<>(response, HttpStatus.OK);
            //测试：
//        ReadFile readFile = new ReadFile();
//
//        Map<String, Object> old_data = readFile.readJsonFileToMap();
//        // 使用entrySet()方法获取键值对集合
//        for (Map.Entry<String, Object> entry : old_data.entrySet()) {
//            // 打印每个键值对
//            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
//        }
//        System.out.println(receiveloginData.
//                getNewfaceinfo());
//        receiveloginData.setNewfaceinfo((List<BigInteger>) old_data.get("newfaceinfo"));
//        receiveloginData.setAppname("bbb");


            // 将 LoginData 对象传递给 UserService 的 login 方法
            //String result = UserService.login(receiveloginData);
            //return "success";
            // return result;
        }


    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody ReceiveRegisterData receiveregisterdata) {
        HashMap<String,String> resultmessage= new HashMap();//用来存储每步的标记，便于看到操作情况
        Map<String, Object> response = new HashMap<>();//存放返回前端信息

        System.out.println("接收到前端数据");
        System.out.println("计算过的承诺向量c0："+receiveregisterdata.getCommitment());
        System.out.println("账号标识符id："+receiveregisterdata.getId());
        System.out.println("准备上传区块链");

         if(!new IsFile().isFileExists()||!new IsFilesEmpty().isAttributeNull()){

                 response.put("success", false);
                 response.put("message", "用户已注册");
                 return new ResponseEntity<>(response, HttpStatus.OK);

             }


         //2.调用注册服务函数
        boolean result = UserService.register(receiveregisterdata);
         if(result) {


             response.put("success", true);
             response.put("message", "注册成功");

             return new ResponseEntity<>(response, HttpStatus.OK);
         }else {
             response.put("success", false);
             response.put("message", "注册失败");
             return new ResponseEntity<>(response, HttpStatus.OK);
         }
        }



    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/appname")
    public ResponseEntity<Map<String, Object>> Appname(@RequestBody ReceiveAppdata appdata) {
        System.out.println("接收到前端数据");
        System.out.println(appdata);
         Map<String, Object> response = new HashMap<>();//存放返回前端信息
        boolean result = UserService.UpdateApp(appdata.getAppname());

        // 处理注册逻辑
        System.out.println("result"+result);

        response.put("success", result);
        if (result) {
            response.put("message", "注册成功");
            return new ResponseEntity<>(response, HttpStatus.OK);




        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/getappnames")
    public ResponseEntity<Map<String, Object>> ReadAppname() {
        System.out.println("是否执行getappname前端数据");
        Map<String, Object> response = new HashMap<>();//存放返回前端信息


        ReadFile readFile = new ReadFile();
        Map<String, Object> old_data = readFile.readJsonFileToMap();
        List<String> appnames = (List<String>) old_data.get("appnames");
        response.put("success", true);
        response.put("apps", appnames);
        String userid = (String) old_data.get("id");
        response.put("userid",userid);
        return new ResponseEntity<>(response, HttpStatus.OK);

        }
     @CrossOrigin(origins = "http://localhost:8080")
     @PostMapping("/verifyapp")
     public ResponseEntity<Map<String, Object>> VerifyApp() {
        System.out.println("是否执行getappnae前端数据");
        Map<String, Object> response = new HashMap<>();//存放返回前端信息
         boolean result1 = false;
         boolean result2 = false;

            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // 创建一个 HttpPost 请求
            HttpPost postRequest = new HttpPost("http://172.31.100.87:8081/check");

            // 设置请求头
            postRequest.setHeader("Content-Type", "application/json");
// 读取 JSON 文件内容
            String jsonFilePath = "C:/example/proof.json"; // 替换为你的 JSON 文件路径
            String json = new String(Files.readAllBytes(Paths.get(jsonFilePath)));

            // 设置请求体
            postRequest.setEntity(new StringEntity(json));

            // 执行请求并获取响应
            try (CloseableHttpResponse responses = httpClient.execute(postRequest)) {
                String responseBody = EntityUtils.toString(responses.getEntity());
                System.out.println("请求服务方"+responseBody);
                result1  = responseBody.equals("true");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


             try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // 创建一个 HttpPost 请求
            HttpPost postRequest = new HttpPost("http://172.31.100.87:8081/zkcheck");

            // 设置请求头
            postRequest.setHeader("Content-Type", "application/json");
// 读取 JSON 文件内容
            String jsonFilePath = "C:/example/zkproof.json"; // 替换为你的 JSON 文件路径
            String json = new String(Files.readAllBytes(Paths.get(jsonFilePath)));

            // 设置请求体
            postRequest.setEntity(new StringEntity(json));

            // 执行请求并获取响应
            try (CloseableHttpResponse responses = httpClient.execute(postRequest)) {
                String responseBody = EntityUtils.toString(responses.getEntity());
                System.out.println("请求服务方核验zk"+responseBody);
                result2  = responseBody.equals("true");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
             boolean result = result1&&result2;






        response.put("success",result);

        return new ResponseEntity<>(response, HttpStatus.OK);
  }


  @CrossOrigin(origins = "http://localhost:8080")
     @PostMapping("/login_zk")
     public ResponseEntity<Map<String, Object>> ZkLogin(@RequestBody ZkProofRequest request) {
        System.out.println("是否执行loginzk前端数据");
         ObjectMapper objectMapper = new ObjectMapper();

        // 假设 request 是通过 @RequestBody 注解接收到的 ZkProofRequest 对象
        // 将 zk_proof 转换为 Map
       //Map<String, Object> zkdata1 = objectMapper.convertValue(new TypeReference<Map<String, Object>>() {});

        // 处理 cmt_zk 作为字符串
        String cmtZkString = request.getCmt_zk();
        ReadTempZkFile readTempZkFile = new ReadTempZkFile();
        Map<String, Object> zkdatas = readTempZkFile.readJsonFileToMap();
        zkdatas.put("cmt_zk", cmtZkString);

        // 合并 Map 和字符串
//        Map<String, Object> zkdatas = new HashMap<>(zkdata1);
//        zkdatas.put("cmt_zk", cmtZkString);


        System.out.println("cmt_zk"+request.getCmt_zk());
        Map<String, Object> response = new HashMap<>();//存放返回前端信息



try {
    SaveZKData saveZKData = new SaveZKData();
    saveZKData.saveToZkJsonFile(zkdatas, true);

     response.put("success",true);

     return new ResponseEntity<>(response, HttpStatus.OK);
}catch (Exception e){
    e.printStackTrace();
}




        response.put("success",false);

        return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
