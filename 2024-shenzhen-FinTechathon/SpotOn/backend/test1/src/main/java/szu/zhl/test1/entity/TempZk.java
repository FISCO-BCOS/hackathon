package szu.zhl.test1.entity;

import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class TempZk {
    private String filePath="C:/example/tempzk.json";
    private String embDist;
    private String randDist;


    public void toJson(String embDist, String randDist) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("emb_dist", embDist);
        jsonObject.put("rand_dist", randDist);
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(jsonObject.toString(4)); // 格式化输出，缩进4个空格
            System.out.println("JSON数据已保存到文件: " + filePath);
        } catch (IOException e) {
            System.err.println("保存JSON到文件时出错: " + e.getMessage());
        }
    }
//
//    public static void main(String[] args) {
//        TempZk proofJsonSaver = new ProofJsonSaver("example_emb_dist", "example_rand_dist");
//        JSONObject json = proofJsonSaver.toJson();
//        System.out.println(json.toString());
//    }
}
