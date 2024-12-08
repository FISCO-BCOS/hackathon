package szu.zhl.test1.utils;

import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import szu.zhl.test1.entity.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileSaveData {
    private String filePath="C:/example/identity.json";
    private final ObjectMapper objectMapper;

    public FileSaveData() {
        this.objectMapper = new ObjectMapper();
        // 配置 StreamReadConstraints
        objectMapper.getFactory().setStreamReadConstraints(
                StreamReadConstraints.builder()
                        .maxStringLength(50_000_000)
                        .maxNumberLength(1000)
                        .build()
        );

        // 检查文件是否存在，如果不存在则创建一个新的 JsonData 对象并保存
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                IdentityJson initialData = new IdentityJson();  // 创建新的 IdentityJson 实例
                objectMapper.writeValue(file, initialData);
                System.out.println("Initial IdentityJson file created at " + filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 检查并添加 appname 属性

    public void ensureAppNameExists() {
        try {
            File file = new File(filePath);
            JsonNode rootNode = objectMapper.readTree(file);

            if (!rootNode.has("appnames")) {
                // 如果没有 appname 属性，添加一个空的 appname 列表
                if (rootNode.isObject()) {
                    ((ObjectNode) rootNode).set("appnames", objectMapper.createArrayNode());
                    objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, rootNode);
                    System.out.println("Appname attribute added to JSON file.");
                }
            } else {
                System.out.println("Appname attribute already exists.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 更新 appname
    public void updateAppName(String appNames) {
        try {
            // 读取现有数据
            IdentityJson identityJson = objectMapper.readValue(new File(filePath), IdentityJson.class);
            // 更新 appname 字段
            List<String> app = identityJson.getAppnames();
            for (String s : app) {
                if (s.equals(appNames)) {
                    return;
                }
            }
            app.add(appNames);
            identityJson.setAppnames(app);
            // 写回更新后的对象
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), identityJson);
            System.out.println("Updated successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 写入整个 IdentityJson 对象到文件
    public boolean writeFirstDataToFile(IdentityJson identityJson) throws IOException {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), identityJson);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}