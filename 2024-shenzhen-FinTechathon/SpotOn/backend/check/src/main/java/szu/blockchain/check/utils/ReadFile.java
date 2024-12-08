package szu.blockchain.check.utils; // 定义包名，表示该类属于 szu.zhl.test1.utils 包

import com.fasterxml.jackson.core.type.TypeReference; // 导入 TypeReference 类，用于泛型类型引用
import com.fasterxml.jackson.databind.ObjectMapper; // 导入 ObjectMapper 类，用于 JSON 解析
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value; // 导入 Value 注解，用于从配置文件中注入属性值
import org.springframework.stereotype.Component;

import java.io.File; // 导入 File 类，用于文件操作
import java.io.FileNotFoundException;
import java.io.IOException; // 导入 IOException 类，用于处理 IO 异常
import java.math.BigInteger; // 导入 BigInteger 类，用于大整数运算
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap; // 导入 HashMap 类，用于存储键值对
import java.util.List; // 导入 List 接口，用于列表操作
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


@Component
public class ReadFile { // 定义 ReadFile 类





    // 定义一个方法，用于读取 JSON 文件并将其内容转换为 HashMap

    public HashMap<String, Object> readJsonFileToMap(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // 确保 filePath 不为 null
            if (filePath == null) {
                throw new IllegalArgumentException("File path cannot be null");
            }

            // 使用 ObjectMapper 读取 JSON 文件并转换为 HashMap
            File file = new File(filePath);
            if (!file.exists()) {
                throw new FileNotFoundException("File not found: " + filePath);
            }

            HashMap<String, Object> map = objectMapper.readValue(file, new TypeReference<HashMap<String, Object>>() {});





            return map;
        } catch (IOException e) {
            System.err.println("An error occurred while reading the JSON File.");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("An error occurred while parsing the JSON data.");
            e.printStackTrace();
        }
        return null;
    }



}