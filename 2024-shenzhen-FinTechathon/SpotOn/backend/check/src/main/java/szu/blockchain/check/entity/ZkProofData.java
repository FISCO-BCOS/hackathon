package szu.blockchain.check.entity;

import lombok.Data;
import java.math.BigInteger;
import java.util.List;

@Data
public class ZkProofData {

    private String scheme;
    private String curve;
    private Proof proof;
    private List<String> inputs;
    private String cmt_zk;

    @Data
    public static class Proof {
        private List<String> a;
        private List<List<String>> b;
        private List<String> c;
    }
}