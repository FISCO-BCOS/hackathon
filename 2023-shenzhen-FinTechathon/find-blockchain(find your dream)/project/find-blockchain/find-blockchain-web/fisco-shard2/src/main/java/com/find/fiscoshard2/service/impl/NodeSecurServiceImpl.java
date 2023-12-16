package com.find.fiscoshard2.service.impl;

import com.alibaba.fastjson.JSON;
import com.find.NodeSecurService;
import com.find.fiscoshard2.contract.Asset;
import com.find.fiscoshard2.contract.PerformanceCallback;
import com.find.fiscoshard2.contract.PerformanceCollector;
import com.find.fiscoshard2.dao.FlNodeDao;
import org.apache.dubbo.config.annotation.Service;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.CryptoType;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.json.JSONObject;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class NodeSecurServiceImpl implements NodeSecurService {
    private static Logger logger = LoggerFactory.getLogger(NodeSecurServiceImpl.class);

    public BcosSDK bcosSDK;
    public Client client;
    public CryptoKeyPair cryptoKeyPair;
    public static String contractAddr;
    public PerformanceCollector collector;
    public PerformanceCallback callback;
    private static AtomicLong uniqueID = new AtomicLong(0);
    public Asset asset;

    @Autowired
    private FlNodeDao flNodeDao;
//    public Asset asset;


    public NodeSecurServiceImpl() throws ContractException {
        System.out.println("=====Async initing success=====");
        @SuppressWarnings("resource")
        ApplicationContext context =
                new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        BcosSDK bcosSDK = (BcosSDK) context.getBean("rpbftBcosSDK");
        client = bcosSDK.getClient(1);
        System.out.println(bcosSDK);
        asset =
                Asset.deploy(client, client.getCryptoSuite().getCryptoKeyPair());
        collector = new PerformanceCollector();
    }

    private static PerformanceCallback createCallback(PerformanceCollector collector) {
        PerformanceCallback callback = new PerformanceCallback();
        callback.setTimeout(0);
        callback.setCollector(collector);
        return callback;
    }

    public static long getNextID() {
        return uniqueID.getAndIncrement();
    }

    @Override
    public List<String> Upload_data(String data) {
        System.out.println("进入Upload_data");
        callback = createCallback(collector);
        //发起交易
        JSONObject jsonObject = new JSONObject(data);
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
//        System.out.println(dateFormat.format(date));
        System.out.println(client.getBlockNumber().getBlockNumber().toString());
        asset.register(jsonObject.get("docId").toString() + dateFormat.format(date), BigInteger.valueOf(1), callback);
        System.out.println(client.getBlockNumber().getBlockNumber().toString());
        //System.out.println(result);
        return null;
    }

    @Override
    public List<String> Transaction(List<String> nodeList){
        //初始化一个列表存储结果集
        ArrayList<String> result = new ArrayList<>();
        //for循环对列表中节点进行验证
        for (String i : nodeList) {
            //根据文件加载账户,文件名得改
            try {
                loadPemAccount(client,"G:\\findlab-subq\\个人项目\\find-blockchain\\find-blockchain-web\\pemfile\\node"+ i.charAt(6) +".pem");
                System.out.println("加载账户成功，账户的地址为：" + client.getCryptoSuite().getCryptoKeyPair().getAddress());
                result.add(i);
            } catch (Exception e) {
                System.out.println("加载账户出错，该账户" + i +"不属于安全账户");
                //e.printStackTrace();
            }
            //设置合约的发起账户
            try {
                asset =
                        Asset.deploy(client, client.getCryptoSuite().getCryptoKeyPair());
                //发起交易
                asset.register("pear", BigInteger.valueOf(1));
            } catch (ContractException e) {
                System.out.println("设置合约发起账户出错");
                e.printStackTrace();
            }
        }
        System.out.println(result);
        return result;
    }

    @Override
    public void queryNodeAccount() {

    }

    //批量创建外部账户
    @Override
    public void createNodeAccount(String nodeId) {
        //创建非国密类型的CryptoSuite
        CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);
        //随机生成非国密公私钥对
        CryptoKeyPair cryptoKeyPair = cryptoSuite.getCryptoKeyPair();
        //获取账户地址
        String accountAddress = cryptoKeyPair.getAddress();
        System.out.println(accountAddress);
        System.out.println(cryptoKeyPair.getPemKeyStoreFilePath());
        //保存的文件名需要修改
        saveAccountWithPem(cryptoKeyPair,"G:\\findlab-subq\\个人项目\\find-blockchain\\find-blockchain-web\\pemfile\\node"+ nodeId.substring(6) +".pem");
        //插入账户地址信息
        flNodeDao.modifyAddressById(nodeId,accountAddress);
    }

    @Override
    public List<String> validation(List<String> nodeList) {
        ArrayList<String> resultList = new ArrayList<>();
        for (String nodeId : nodeList) {
            Float value = flNodeDao.getRepValueById(nodeId);
            if (value != null){
                if (value >= 8){
                    resultList.add(nodeId);
                }else if(4 <= value && value < 8){
                    //验证节点是否有账户文件，即通过基于智能合约的安全验证方法去验证
                    TransactionForOne(nodeId,resultList);
                }
            }
        }
        return resultList;
    }

    //通过pem文件加载账户
    public void loadPemAccount(Client client, String pemAccountFilePath)
    {
        // 通过client获取CryptoSuite对象
        CryptoSuite cryptoSuite = client.getCryptoSuite();
        // 加载pem账户文件
        cryptoSuite.loadAccount("pem", pemAccountFilePath, null);
        System.out.println("账户地址为：" + cryptoSuite.getCryptoKeyPair().getAddress());
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

    public void TransactionForOne(String nodeId,List<String> resultList){

        //for循环对列表中节点进行验证
        //根据文件加载账户,文件名得改
        try {
            System.out.println("需要发起交易验证的节点id:" + nodeId);
            loadPemAccount(client,"G:\\findlab-subq\\个人项目\\find-blockchain\\find-blockchain-web\\pemfile\\node"+ nodeId.substring(6) +".pem");
            System.out.println(client.getCryptoSuite().getCryptoKeyPair().getAddress());
            resultList.add(nodeId);
        } catch (Exception e) {
            System.out.println("加载账户出错，该账户不属于安全账户");
            //e.printStackTrace();
        }
        //设置合约的发起账户
        try {
            asset =
                    Asset.deploy(client, client.getCryptoSuite().getCryptoKeyPair());
            //发起交易
            asset.register("pear", BigInteger.valueOf(1));
        } catch (ContractException e) {
            System.out.println("设置合约发起账户出错");
            e.printStackTrace();
        }
    }

}
