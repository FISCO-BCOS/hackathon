package szu.zhl.test1.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class IsFilesEmpty {

    private String filepath = "C:/example/identity.json";

    /**
     * 判断文件中的某个属性是否为 null
     *
     * @param
     * @return 如果属性为 null 或文件不存在，返回 true；否则返回 false
     */
    public boolean isAttributeNull() {
        String attributeName = "id";
        File file = new File(filepath);
        if (!file.exists()) {
            return true; // 文件不存在，视为属性为 null
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(file);
            JsonNode attributeNode = rootNode.path(attributeName);
            return attributeNode.isNull();
        } catch (IOException e) {
            e.printStackTrace();
            return true; // 读取或解析失败，视为属性为 null
        }
    }
}
