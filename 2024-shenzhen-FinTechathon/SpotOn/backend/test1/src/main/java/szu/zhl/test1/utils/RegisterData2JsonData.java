//package szu.zhl.test1.utils; // 定义包名，表示该类属于 szu.zhl.test1.utils 包
//
//import szu.zhl.test1.entity.JsonData; // 导入 JsonData 类，用于 JSON 数据结构
//import szu.zhl.test1.entity.RegisterData; // 导入 RegisterData 类，用于注册数据结构
//
//import java.util.ArrayList; // 导入 ArrayList 类，用于动态数组
//import java.util.List; // 导入 List 接口，用于列表操作
//
//public class RegisterData2JsonData { // 定义 Register2JsonData 类
//    public JsonData convert(RegisterData registerdata) { // 定义 convert 方法，将 RegisterData 转换为 JsonData
//        JsonData jsonData = new JsonData(); // 创建 JsonData 对象
//        jsonData.setId(registerdata.getId()); // 设置 JsonData 的 id 属性
//        jsonData.setOldfaceinfo(registerdata.getOldfaceinfo()); // 设置 JsonData 的 oldfaceinfo 属性
//        jsonData.setRandomnumbers(registerdata.getRandomnumbers()); // 设置 JsonData 的 randomnumbers 属性
//        jsonData.setCommitment(registerdata.getCommitment()); // 设置 JsonData 的 commitment 属性
//
//        List<String> appnames = new ArrayList<>(); // 创建一个新的 ArrayList 用于存储应用名称
//        appnames.add(registerdata.getAppname()); // 将 RegisterData 的 appname 添加到列表中
//        jsonData.setAppname(appnames); // 设置 JsonData 的 appname 属性为列表
//
//        return jsonData; // 返回转换后的 JsonData 对象
//    }
//}