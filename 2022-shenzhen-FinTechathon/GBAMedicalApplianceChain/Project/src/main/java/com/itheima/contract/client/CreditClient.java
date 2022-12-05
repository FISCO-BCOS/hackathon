package com.itheima.contract.client;

import lombok.SneakyThrows;
import com.itheima.contract.Credit;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.*;
import java.math.BigInteger;
import java.util.Properties;

public class CreditClient {

    static Logger logger = LoggerFactory.getLogger(CreditClient.class);
    private BcosSDK bcosSDK;
    private Client client;
    private CryptoKeyPair cryptoKeyPair;

    public Credit credit;

    public void initialize() throws Exception {
        @SuppressWarnings("resource")
        ApplicationContext context =
                new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        bcosSDK = context.getBean(BcosSDK.class);
        client = bcosSDK.getClient(1);
        cryptoKeyPair = client.getCryptoSuite().createKeyPair();
        client.getCryptoSuite().setCryptoKeyPair(cryptoKeyPair);
        String contractAddress = loadAddr();
        if(contractAddress == null || contractAddress.trim().equals("")){
            int a = 10000,b=10;
            BigInteger totalAmount = BigInteger.valueOf(a);
            BigInteger minUnit = BigInteger.valueOf(b);
            recordAddr(deployAssetAndRecordAddr("medicalTest","MedicalCredit",minUnit,totalAmount));
        }else {
            credit = Credit.load(contractAddress,client,cryptoKeyPair);
        }


    }

    public String deployAssetAndRecordAddr(String description, String shortName, BigInteger minUnit,BigInteger totalAmount) {
        try {
            credit = Credit.deploy(client, cryptoKeyPair,description,shortName,minUnit,totalAmount);
            System.out.println(
                    " deploy Asset success, contract address is " + credit.getContractAddress());
            return credit.getContractAddress();
            //recordAddr(Credit.getContractAddress());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            System.out.println(" deploy Asset contract failed, error message is  " + e.getMessage());
            return null;
        }
    }

    public void recordAddr(String address) throws FileNotFoundException, IOException {
        Properties prop = new Properties();
        prop.setProperty("address", address);
        prop.store(new FileWriter("contract.properties"),"file comments");
    }

    public String loadAddr() throws Exception {
        // load Credit contact address from contract.properties
        Properties prop = new Properties();
        prop.load(new FileInputStream("contract.properties"));
        String contractAddress = (String) prop.get("address");
        return  contractAddress;
        //if (contractAddress == null || contractAddress.trim().equals("")) {
        //    deployAssetAndRecordAddr();
        //}
        //logger.info(" load Credit address from contract.properties, address is {}", contractAddress);
        //return contractAddress;
    }


    public  int getBalance() throws Exception {
        //String contractAddress = loadAddr();
        //Credit credit = Credit.load(contractAddress,client,cryptoKeyPair);
        BigInteger balance = credit.balance(credit.getCurrentExternalAccountAddress());
        return balance.intValue();
    }

    public boolean sendCredit(String to,BigInteger value,byte[] data) throws Exception {
        try {
            //String contractAddress = loadAddr();
            //Credit credit = Credit.load(contractAddress, client, cryptoKeyPair);
            credit.send(to, value, data);
            return true;
        }catch (Exception e){
            return false;
        }

    }









    //@SneakyThrows
    //public static void main(String[] args) {
    //    CreditClient creditClient = new CreditClient();
    //    creditClient.initialize();
    //    int a = 10000,b=10;
    //    BigInteger totalAmount = BigInteger.valueOf(a);
    //    BigInteger minUnit = BigInteger.valueOf(b);
    //    creditClient.deployAssetAndRecordAddr("medicalTest","MedicalCredit",minUnit,totalAmount);
    //    System.out.println(1);
    //    //creditClient.
    //}

}
