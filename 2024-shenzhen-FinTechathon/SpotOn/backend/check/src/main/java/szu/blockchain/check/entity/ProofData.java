package szu.blockchain.check.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class ProofData {

    private String id;
    private String time;
    private List<String> oldcmt; // 如果需要映射 oldcmt 字段
    private ProofDetails proof;
    private String cmt_dist ;

    @Data
    public static class ProofDetails {
        private List<String> emb;
        private Cmt cmt; // 嵌套的 Cmt 类
        private List<String> a;
        private List<String> z;
        private List<String> p;
    }

    @Data
    public static class Cmt {
        @JsonProperty("cmt_eq_old_json")
        private List<String> cmtEqOldJson; // 对应 cmt_eq_old_json

        @JsonProperty("cmt_eq_new_json")
        private List<String> cmtEqNewJson; // 对应 cmt_eq_new_json

        @JsonProperty("cmt_mult_json")
        private List<String> cmtMultJson;
    }
}