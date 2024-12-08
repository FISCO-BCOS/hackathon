package szu.zhl.test1.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class SaveZKData {
    private String path = "C:/example/zkproof.json";
     public void saveToZkJsonFile(Map<String, Object> data, boolean prettyPrint) {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    if (prettyPrint) {
                        mapper.enable(SerializationFeature.INDENT_OUTPUT);
                    }
                    String jsonString = mapper.writeValueAsString(data);

                    try (FileWriter file = new FileWriter(path)) {
                        file.write(jsonString);
                        System.out.println("JSON file has been saved: " + path);
                    }
                } catch (IOException e) {
                    System.out.println("An error occurred while writing JSON Object to File.");
                    e.printStackTrace();
                }
            }

}
