package com.find.fiscoshard2;

import com.find.fiscoshard2.dao.FlNodeDao;
import com.find.fiscoshard2.pojo.Account;
import com.find.fiscoshard2.pojo.FlNode;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.CryptoType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;

@SpringBootTest
class FiscoShard2ApplicationTests {

    @Autowired
    FlNodeDao flNodeDao;

    @Test
    void contextLoads() {
    }

    //批量创建3.0版本外部账户
    @Test
    public void createNodeAccount() {
        for (int i = 0; i < 10; i++) {
            //创建非国密类型的CryptoSuite
            CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);
            //随机生成非国密公私钥对
            CryptoKeyPair cryptoKeyPair = cryptoSuite.getCryptoKeyPair();
            //获取账户地址
            String accountAddress = cryptoKeyPair.getAddress();
            System.out.println(accountAddress);
            System.out.println(cryptoKeyPair.getPemKeyStoreFilePath());
            //保存的文件名需要修改
            //saveAccountWithPem(cryptoKeyPair,"/home/su/fisco3.x/account/ecdsa/node"+ i +".pem");
            saveAccountWithPem(cryptoKeyPair,"G:\\findlab-subq\\个人项目\\find-blockchain\\find-blockchain-web\\pemfile\\node"+ i +".pem");
            //生成账户信息
//            Account flNode = new Account("client" + i,accountAddress,8f);
            //插入账户信息
//            flNodeDao.insert(flNode);
        }
    }

    // 将随机生成的账户信息保存在pemFilePath指定的路径
    public void saveAccountWithPem(CryptoKeyPair cryptoKeyPair, String pemFilePath){
        File file = new File(pemFilePath);
        //文件不存在就创建文件
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    System.out.println("File created successfully.");
                } else {
                    System.out.println("File already exists.");
                }
            } catch (IOException e){
                System.out.println("An error occurred while creating the file.");
                e.printStackTrace();
            }
        }
        // 以pem的格式保存账户文件到pemFilePath路径
        cryptoKeyPair.storeKeyPairWithPem(pemFilePath);
    }

}
