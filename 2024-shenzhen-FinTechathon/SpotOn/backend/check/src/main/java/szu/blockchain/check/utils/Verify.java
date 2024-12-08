package szu.blockchain.check.utils;

import com.fasterxml.jackson.databind.JsonNode;
import szu.blockchain.check.entity.ProofData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import szu.blockchain.check.entity.ProofData;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import szu.blockchain.check.entity.Pedersen;

public class Verify {
    // 生物特征编码的维度
    private static final int DIM = 128;
    // 承诺的生成元
    private static final BigInteger g = new BigInteger("56544564");

    private static final BigInteger h = new BigInteger("237684576");
    public  boolean verify(ProofData proofData) {
        int pass = 1;
        System.out.println("G:" + g);
        System.out.println("h:" + h);



        // 获取用户身份信息
        String id = proofData.getId();
        String time = proofData.getTime();
        List<String> cmtOldJson = proofData.getOldcmt(); // 有点问题

        List<String> cmtNewJson = proofData.getProof().getEmb();
        List<String> cmtEqOldJson = proofData.getProof().getCmt().getCmtEqOldJson();
        List<String> cmtEqNewJson = proofData.getProof().getCmt().getCmtEqNewJson();
        List<String> cmtMultJson = proofData.getProof().getCmt().getCmtMultJson();
        List<String> aJson = proofData.getProof().getA();

        List<String> zJson = proofData.getProof().getZ();
        List<String> pJson = proofData.getProof().getP();

        // 打印数据
        System.out.println("ID: " + id);
        System.out.println("Time: " + time);
        System.out.println("cmtOldJson: " + cmtOldJson);
        System.out.println("cmtNewJson: " + cmtNewJson);
        System.out.println("cmtEqOldJson: " + cmtEqOldJson);
        System.out.println("cmtEqNewJson: " + cmtEqNewJson);
        System.out.println("cmtMultJson: " + cmtMultJson);
        System.out.println("aJson: " + aJson);
        System.out.println("zJson: " + zJson);
        System.out.println("pJson: " + pJson);


        // 将字符串时间转换为Date对象
        Date tStart = new Date(time);
        Date tEnd = new Date();
        System.out.println("Start Time: " + tStart);
        System.out.println("End Time: " + tEnd);
        System.out.println("Time Difference (ms): " + (tEnd.getTime() - tStart.getTime()));

        // 计算时间差
        long timeDifference = tEnd.getTime() - tStart.getTime();

        // 时间有效性判断

        if (timeDifference <= 3600000000L) {
            pass = pass;
        } else {
            pass = 0;
        }


        // 自行生成挑战e
        Map<String, Object> challenge = new LinkedHashMap<>();
        challenge.put("cmt_old_json", cmtOldJson);
        challenge.put("cmt_new_json", cmtNewJson);
        challenge.put("cmt_eq_old_json", cmtEqOldJson);
        challenge.put("cmt_eq_new_json", cmtEqNewJson);
        challenge.put("cmt_mult_json", cmtMultJson);
        challenge.put("id", id);
        challenge.put("t", time);
        System.out.println("Challenge: " + challenge);
        BigInteger e;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String test = objectMapper.writeValueAsString(challenge);
            String hash = DigestUtils.sha256Hex(test.getBytes("UTF-8"));
            System.out.println("Hash: " + hash);
            e = new BigInteger(hash, 16).mod(BigInteger.valueOf(5000));
            System.out.println("e: " + e);
            // 这里可以继续使用e进行后续的验证逻辑
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        // 数据整理
        BigInteger[] cmtOld = new BigInteger[DIM];
        BigInteger[] cmtNew = new BigInteger[DIM];
        BigInteger[] cmtEqOld = new BigInteger[DIM];
        BigInteger[] cmtEqNew = new BigInteger[DIM];
        BigInteger[] cmtMult = new BigInteger[DIM];
        BigInteger[] z1 = new BigInteger[DIM];
        BigInteger[] z2 = new BigInteger[DIM];
        BigInteger[] z3 = new BigInteger[DIM];
        BigInteger[] z4 = new BigInteger[DIM];
        BigInteger[] z5 = new BigInteger[DIM];
        BigInteger[] z6 = new BigInteger[DIM];
        BigInteger[] z7 = new BigInteger[DIM];
        BigInteger[] p1 = new BigInteger[DIM];
        BigInteger[] p2 = new BigInteger[DIM];
        BigInteger[] p3 = new BigInteger[DIM];
//你尝试将一个以"0x"开头的十六进制字符串直接转换为BigInteger，而没有指定基数为16。为了正确解析这些十六进制字符串，你需要去掉"0x"前缀并指定基数为16。
        BigInteger a1 = new BigInteger(aJson.get(0).substring(2), 16);
        BigInteger a2 = new BigInteger(aJson.get(1).substring(2), 16);

        // 打印BigInteger值
        System.out.println("a1: " + a1);
        System.out.println("a2: " + a2);

        for (int i = 0; i < DIM; i++) {
            cmtOld[i] = new BigInteger(cmtOldJson.get(i).substring(2), 16);
            cmtNew[i] = new BigInteger(cmtNewJson.get(i).substring(2), 16);
            cmtEqOld[i] = new BigInteger(cmtEqOldJson.get(i).substring(2), 16);
            cmtEqNew[i] = new BigInteger(cmtEqNewJson.get(i).substring(2), 16);
            cmtMult[i] = new BigInteger(cmtMultJson.get(i).substring(2), 16);
            z1[i] = new BigInteger(zJson.get(i).substring(2), 16);
            z2[i] = new BigInteger(zJson.get(DIM + i).substring(2), 16);
            z3[i] = new BigInteger(zJson.get(2 * DIM + i).substring(2), 16);
            z4[i] = new BigInteger(zJson.get(3 * DIM + i).substring(2), 16);
            z5[i] = new BigInteger(zJson.get(4 * DIM + i).substring(2), 16);
            z6[i] = new BigInteger(zJson.get(5 * DIM + i).substring(2), 16);
            z7[i] = new BigInteger(zJson.get(6 * DIM + i).substring(2), 16);
            p1[i] = new BigInteger(pJson.get(i).substring(2), 16);
            p2[i] = new BigInteger(pJson.get(DIM + i).substring(2), 16);
            p3[i] = new BigInteger(pJson.get(2 * DIM + i).substring(2), 16);
        }

        // 打印整个BigInteger数组
        System.out.println("cmtOld: " + Arrays.toString(cmtOld));
        System.out.println("cmtNew: " + Arrays.toString(cmtNew));
        System.out.println("cmtEqOld: " + Arrays.toString(cmtEqOld));
        System.out.println("cmtEqNew: " + Arrays.toString(cmtEqNew));
        System.out.println("cmtMult: " + Arrays.toString(cmtMult));
        System.out.println("z1: " + Arrays.toString(z1));
        System.out.println("z2: " + Arrays.toString(z2));
        System.out.println("z3: " + Arrays.toString(z3));
        System.out.println("z4: " + Arrays.toString(z4));
        System.out.println("z5: " + Arrays.toString(z5));
        System.out.println("z6: " + Arrays.toString(z6));
        System.out.println("z7: " + Arrays.toString(z7));
        System.out.println("p1: " + Arrays.toString(p1));
        System.out.println("p2: " + Arrays.toString(p2));
        System.out.println("p3: " + Arrays.toString(p3));



        // 验证乘法同态的准确性
        Pedersen code = new Pedersen(g, h);
        BigInteger[] d1 = new BigInteger[DIM];
        BigInteger[] d2 = new BigInteger[DIM];
        BigInteger[] d3 = new BigInteger[DIM];
        BigInteger[] d4 = new BigInteger[DIM];
        BigInteger[] d5 = new BigInteger[DIM];

        for (int i = 0; i < DIM; i++) {
            d1[i] = code.commitment(z1[i], z2[i]);
            Pedersen f = new Pedersen(cmtOld[i], h);
            d2[i] = f.commitment(z1[i], z5[i]);
            d3[i] = code.commitment(z3[i], z4[i]);
            Pedersen ff = new Pedersen(cmtNew[i], h);
            d4[i] = ff.commitment(z3[i], z6[i]);
            d5[i] = f.commitment(z3[i], z7[i]);
        }

        // 打印整个BigInteger数组
        System.out.println("d1: " + Arrays.toString(d1));
        System.out.println("d2: " + Arrays.toString(d2));
        System.out.println("d3: " + Arrays.toString(d3));
        System.out.println("d4: " + Arrays.toString(d4));
        System.out.println("d5: " + Arrays.toString(d5));




        // 注册用生物特征编码的平方项的准确性验证
        Homomorphic hp = new Homomorphic();
        for (int i = 0; i < DIM; i++) {
            if (d1[i].equals(hp.Add(a1, hp.Pow(cmtOld[i], e))) && d2[i].equals(hp.Add(p1[i], hp.Pow(cmtEqOld[i], e)))) {
                pass = pass;
            } else {
                pass = 0;
            }
        }

        // 验证用生物特征编码的平方项的准确性验证
        for (int i = 0; i < DIM; i++) {
            if (d3[i].equals(hp.Add(a2, hp.Pow(cmtNew[i], e))) && d4[i].equals(hp.Add(p2[i], hp.Pow(cmtEqNew[i], e)))) {
                pass = pass;
            } else {
                pass = 0;

            }
        }

        // 不同生物特征编码的乘积项的准确性验证
        for (int i = 0; i < DIM; i++) {
            if (d5[i].equals(hp.Add(p3[i], hp.Pow(cmtMult[i], e)))) {
                pass = pass;
            } else {
                pass = 0;
            }
        }
        // 自行计算生物特征间欧式距离的承诺
        BigInteger sumOld = BigInteger.ONE;
        BigInteger sumNew = BigInteger.ONE;
        BigInteger sumMult = BigInteger.ONE;
        for (int i = 0; i < DIM; i++) {
            sumOld = hp.Add(sumOld, cmtEqOld[i]);
            sumNew = hp.Add(sumNew, cmtEqNew[i]);
            sumMult = hp.Add(sumMult, cmtMult[i]);
        }
// 打印BigInteger变量
        System.out.println("sumOld: " + sumOld);
        System.out.println("sumNew: " + sumNew);
        System.out.println("sumMult: " + sumMult);



        BigInteger cmtSum = hp.Add(hp.Add(sumOld, sumNew), hp.Pow(hp.Add(sumMult, sumMult), new BigInteger("67108857")));






        return pass==1;
    }
    //测试用

    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // 读取 proof.json 文件并转换为 ProofData 对象
            ProofData proofData = objectMapper.readValue(new File("C:\\example/proof.json"), ProofData.class);


            Verify verifier = new Verify();
            boolean result = verifier.verify(proofData);
            System.out.println("Verification result: " + result);
            // 继续处理其他数据

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
