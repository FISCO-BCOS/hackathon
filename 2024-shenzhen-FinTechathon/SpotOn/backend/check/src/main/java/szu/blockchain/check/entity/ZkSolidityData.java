package szu.blockchain.check.entity;

import lombok.Data;
import java.math.BigInteger;
import java.util.List;

@Data
public class ZkSolidityData {

    private Proof proof;
    private List<BigInteger> input;

    @Data
    public static class Proof {
        private G1Point a;
        private G2Point b;
        private G1Point c;
    }

    @Data
    public static class G1Point {
        private BigInteger X;
        private BigInteger Y;
    }

    @Data
    public static class G2Point {
        private List<BigInteger> X;
        private List<BigInteger> Y;
    }
}