package szu.zhl.test1.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Data
public class ZKProofData {

    private String scheme;
    private String curve;
    private ProofData proof;
    private List<String> inputs;

    @Data
    public static class ProofData {
        private List<String> a;
        private List<List<String>> b;
        private List<String> c;
    }
}