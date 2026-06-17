package szu.zhl.test1.entity;

import lombok.Data;
import java.math.BigInteger;
import java.util.List;

@Data
public class SendZkProofData {

    private String scheme;
    private String curve;
    private Proof proof;
    private List<BigInteger> inputs;
    private String cmt_zk;

    @Data
    public static class Proof {
        private List<BigInteger> a;
        private List<List<BigInteger>> b;
        private List<BigInteger> c;
    }
}