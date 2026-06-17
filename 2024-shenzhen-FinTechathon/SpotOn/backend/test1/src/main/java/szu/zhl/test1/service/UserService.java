package szu.zhl.test1.service;

import org.springframework.stereotype.Service;
import szu.zhl.test1.utils.ZKVerify;
import szu.zhl.test1.entity.*;
import szu.zhl.test1.entityschance.ReceiveRegistertoRegister;
import szu.zhl.test1.entityschance.RegisterDataToIdentityJson;
import szu.zhl.test1.utils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {



 public static Map<String,String> login(ReceiveLoginData receiveloginData) {
     Map<String,String> result = new HashMap<>();
     IsFilesEmpty isFilesEmpty = new IsFilesEmpty();
     if (isFilesEmpty.isAttributeNull()) {
         result.put("message", "请先注册");
         return result;
     }
     //读取本地人脸数据
        ReadFile readFile = new ReadFile();
        Map<String, Object> old_data = readFile.readJsonFileToMap();
        List<String> oldfaceinfo = (List<String>) old_data.get("oldfaceinfo");
        System.out.println(old_data);
     //1.判断两张人脸的(相似度)欧式距离是否阈值
        boolean isDistanceLessThanThreshold = DistancemoreThanThreshold.isDistanceLessThanThreshold(receiveloginData.getNewfaceinfo(),oldfaceinfo);
     if(!isDistanceLessThanThreshold){
         result.put("message", "用户生物特征错误");
         return result;


     }
     //修改人脸数据的大小，也就是扩大倍数，匹配师兄算法
     ChanceBiginteger chanceBiginteger = new ChanceBiginteger();
     List<String> newfaceinfo = chanceBiginteger.ChanceBiginteger(receiveloginData.getNewfaceinfo());
     receiveloginData.setNewfaceinfo(newfaceinfo);
     System.out.println("扫脸变换"+receiveloginData);
     List<String> oldfaceinfos = chanceBiginteger.ChanceBiginteger((List<String>) old_data.get("oldfaceinfo"));
     old_data.put("oldfaceinfo",oldfaceinfos);
     System.out.println("修改后的人脸数据"+old_data.get("oldfaceinfo"));

     System.out.println(old_data);



     //调用createproof函数生成proof.json文件
     CreateProofFile proofFile = new CreateProofFile();
      Map<String, Object> proofjson = proofFile.GenerateProofString(old_data, receiveloginData);
      System.out.println("emb_dist:"+proofjson.get("emb_dist"));
      System.out.println("rand_dist:"+proofjson.get("rand_dist"));
      System.out.println("cmt_dist:"+proofjson.get("cmt_dist"));

      //保存中间变量，方便python生成jzk

     TempZk tempzk = new TempZk();
     tempzk.toJson(proofjson.get("emb_dist").toString(), proofjson.get("rand_dist").toString());

    //调用python生成zk-proof.json
     ZKProof zkProof = new ZKProof();
     try {
         boolean zkpy = zkProof.verify();

     } catch (Exception e) {
         throw new RuntimeException(e);
     }




     result.put("emb_dist", proofjson.get("emb_dist").toString());
      result.put("rand_dist", proofjson.get("rand_dist").toString());
      proofjson.remove("emb_dist");
      proofjson.remove("rand_dist");


      proofjson.put("oldcmt",old_data.get("oldcmt"));
      proofFile.saveToJsonFile(proofjson,  true);


result.put("message", "登录成功");


     return result;

         }

         public static boolean register(ReceiveRegisterData receiveregisterData) {
            boolean chainresult = false;

             //1.上链，得到地址







             //2.保存文件，并且设置只读

             FileSaveData fileSaveData = new FileSaveData();
             try {
                 StringConverter stringConverter = new StringConverter();
                 List<String> commitment =stringConverter.convertStrings(receiveregisterData.getCommitment());

//
                System.out.println("承诺转换成功"+commitment);
                 RegisterData registerData =ReceiveRegistertoRegister.convert(receiveregisterData,commitment);

                 IdentityJson identityJson = RegisterDataToIdentityJson.convert(registerData);




                 boolean result = fileSaveData.writeFirstDataToFile(identityJson);
                 if(result){
                     try {
                         ZKVerify putchain = new ZKVerify();
                         chainresult = putchain.verify();

                     } catch (Exception e) {
                         throw new RuntimeException(e);
                     }

                     OnlyRead onlyRead = new OnlyRead();
                     onlyRead.setFileReadOnly();
                     System.out.println("设置只读成功");
                 }

                 return true&&chainresult;

             } catch (Exception e) {
                 System.out.println("设置只读失败");
                 e.printStackTrace();
             }
             return false;



//             //3.receiveregisterData转成jsondata，并且存app名字统一，统一格式
//             DataConvert dataConvert = new DataConvert();
//             JsonData jsonData = dataConvert.convert(receiveregisterData);

             //4.保存json文件，并设置只读



         }
         public static boolean UpdateApp(String appname) {
            FilePermissionUtils filePermissionUtils = new FilePermissionUtils();
            if (!filePermissionUtils.setFileReadableWritable()) {
                System.out.println("设置文件权限失败");
                return false;
     }
            else {
                 FileSaveData fileSaveData = new FileSaveData();
             //判断app字段有没有，没有的话就添加
                try {
                 fileSaveData.ensureAppNameExists();
                 System.out.println("app字段存在");
                 fileSaveData.updateAppName(appname);
                 System.out.println("app字段添加成功");
                 OnlyRead onlyRead = new OnlyRead();
                 onlyRead.setFileReadOnly();

                 return true;

             }
                catch (Exception e){
                 e.printStackTrace();
             }

            }


                return false;
         }
     }



