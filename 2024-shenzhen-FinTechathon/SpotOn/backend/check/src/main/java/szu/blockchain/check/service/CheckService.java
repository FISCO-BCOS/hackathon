package szu.blockchain.check.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import szu.blockchain.check.entity.ProofData;
import szu.blockchain.check.entity.ZkProofData;
import szu.blockchain.check.utils.ReadFile;
import szu.blockchain.check.utils.ToHex;
import szu.blockchain.check.utils.Verify;
import szu.blockchain.check.utils.ZKVerify;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Objects;

@Service
@Component
public class CheckService {
    public boolean  check(ProofData proofData) {


        String jsonFilePath = "C:/check/proof.json";
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // 将实体类转换为JSON文件
            objectMapper.writeValue(new File(jsonFilePath), proofData);
            System.out.println("JSON文件已创建");
        }catch (IOException e){
            e.printStackTrace();
        }
        Verify verify = new Verify();
        boolean result = verify.verify(proofData);
        if (result) {
            return true;
            //并且上链吗
        } else {
            return false;
        }
    }

        public boolean zkcheck(ZkProofData zkProofData) {
        boolean result = false;

        String jsonFilePath = "C:/check/zkproof.json";
         ObjectMapper objectMapper = new ObjectMapper();

        try {
            // 将实体类转换为JSON文件
            objectMapper.writeValue(new File(jsonFilePath), zkProofData);
            System.out.println("JSON文件已创建");
            ZKVerify zkVerify = new ZKVerify();
            result = zkVerify.verify();
            //读取cmt
            ReadFile readFile = new ReadFile();
            HashMap<String, Object> map = readFile.readJsonFileToMap("C:/check/proof.json");

            // 将十进制字符串转换为 BigInteger
            BigInteger cmtDistBigInt = new BigInteger(String.valueOf(map.get("cmt_dist")));

            // 将 BigInteger 转换为十六进制字符串，并确保长度为 16 位
            ToHex toHex = new ToHex();
            String cmtdist = toHex.intToHex(cmtDistBigInt,16);
            System.out.println(cmtdist);
            System.out.println("CMT对比"+zkProofData.getCmt_zk());
            System.out.println("zk验证结果"+result);

            String stringWithQuotes = zkProofData.getCmt_zk();
            String stringWithoutQuotes = stringWithQuotes.replace("\"", "");
            if(result&& Objects.equals(cmtdist, stringWithoutQuotes)){
                System.out.println("zk验证通过");
                return result;

            }
            else {
                return false;
            }



        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }



        }

    }

