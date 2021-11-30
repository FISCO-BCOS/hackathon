package com.media.bcos.client;

import com.media.bcos.contract.PointController;
import org.fisco.bcos.channel.client.Service;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.crypto.gm.GenCredential;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.channel.ChannelEthereumService;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tx.gas.StaticGasProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Properties;

public class PointControllerClient {

    private final static String TAG = "PointControllerClient";

    static Logger logger = LoggerFactory.getLogger(PointControllerClient.class);

    private Web3j web3j;

    private Credentials credentials;

    public Web3j getWeb3j() {
        return web3j;
    }

    public void setWeb3j(Web3j web3j) {
        this.web3j = web3j;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public void recordAssetAddr(String address) throws FileNotFoundException, IOException {
        Properties prop = new Properties();
        prop.setProperty("address", address);
        final Resource contractResource = new ClassPathResource("contract.properties");
        FileOutputStream fileOutputStream = new FileOutputStream(contractResource.getFile());
        prop.store(fileOutputStream, "contract address");
    }

    public String loadPointControllerAddr() throws Exception {
        // load PointController contact address from contract.properties

//        String contractAddress = "0x208b710be2cee57469a8b8a8663b869122e59e76";
        String contractAddress = "0x162da7a679bbe9d8015e6d4d32598ecb4f0966a8";
        if (contractAddress == null) {
            Properties prop = new Properties();
            final Resource contractResource = new ClassPathResource("contract.properties");
            prop.load(contractResource.getInputStream());

            contractAddress = prop.getProperty("address");
            if (contractAddress == null || contractAddress.trim().equals("")) {
                throw new Exception(" load PointController contract address failed, please deploy it first. ");
            }
            logger.info(" load PointController address from contract.properties, address is {}", contractAddress);

        }

        return contractAddress;

    }

    public void initialize() throws Exception {

        // init the Service
        @SuppressWarnings("resource")
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        Service service = context.getBean(Service.class);
        service.run();

        ChannelEthereumService channelEthereumService = new ChannelEthereumService();
        channelEthereumService.setChannelService(service);
        Web3j web3j = Web3j.build(channelEthereumService, 1);

        // init Credentials
        Credentials credentials = GenCredential.create();

        setCredentials(credentials);
        setWeb3j(web3j);

        logger.debug(" web3j is " + web3j + " ,credentials is " + credentials);
    }

    private static BigInteger gasPrice = new BigInteger("30000000");
    private static BigInteger gasLimit = new BigInteger("30000000");

    public void deployPointControllerAndRecordAddr() {

        try {
            PointController pointController = PointController.deploy(web3j, credentials, new StaticGasProvider(gasPrice, gasLimit)).send();
            System.out.println(" deploy PointController success, contract address is " + pointController.getContractAddress());

            recordAssetAddr(pointController.getContractAddress());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            System.out.println(" deploy pointController contract failed, error message is  " + e.getMessage());
        }
    }

    public Boolean registerId(Integer userId) {
        try {
            String contractAddress = loadPointControllerAddr();

            PointController pointController = PointController.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
            TransactionReceipt receipt = pointController.register(new BigInteger(userId + "")).send();
            List<PointController.RegisterIdEventResponse> response = pointController.getRegisterIdEvents(receipt);
            if (!response.isEmpty()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            logger.error(" register id exception, error message is {}", e.getMessage());
            System.out.printf(" register id failed, error message is %s\n", e.getMessage());
        }
        return false;
    }

    public int queryPoint(Integer userId) {
        try {
            String contractAddress = loadPointControllerAddr();
            PointController pointController = PointController.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
            BigInteger point = pointController.queryPoint(new BigInteger(userId + "")).send();
            return point.intValue();
        } catch (Exception e) {
            System.out.println(" event log not found, maybe transaction not exec.");
            return -1;
        }
    }

    public boolean recharge(Integer userId, Integer value) {
        try {
            String contractAddress = loadPointControllerAddr();
            PointController pointController = PointController.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
            BigInteger bPoint = pointController.queryPoint(new BigInteger(userId + "")).send();
            pointController.recharge(new BigInteger(userId + ""), new BigInteger(value + "")).send();
            BigInteger aPoint = pointController.queryPoint(new BigInteger(userId + "")).send();
            if (aPoint.intValue() == bPoint.intValue()) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            System.out.println(" event log not found, maybe transaction not exec.");
        }
        return false;
    }

    public boolean putImgOnTheShelf(String tag, String hash) {
        try {
            String contractAddress = loadPointControllerAddr();
            PointController pointController = PointController.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
            TransactionReceipt receipt = pointController.putImgOnTheShelf(tag, hash).send();
            List<PointController.PutOnTheShelfEventResponse> response = pointController.getPutOnTheShelfEvents(receipt);
            if (!response.isEmpty()) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            System.out.println(" event log not found, maybe transaction not exec.");
        }
        return false;
    }

    public String queryHash(String tag) {
        try {
            String contractAddress = loadPointControllerAddr();
            PointController pointController = PointController.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
            String hash = pointController.queryHash(tag).send();
            return hash;
        } catch (Exception e) {
            System.out.println(" event log not found, maybe transaction not exec.");
            return "";
        }
    }

    // 冻结积分
    public String freezeAccount(Integer userId, Integer value) {
        try {
            String contractAddress = loadPointControllerAddr();
            PointController pointController = PointController.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
            Object result = pointController.freezeAccount(new BigInteger(userId + ""), new BigInteger(value + "")).send();
            StringBuilder sResult = new StringBuilder(result.toString());
            int index1 = sResult.indexOf("output='") + 10; // 截取output后的内容并去掉0x
            int index2 = sResult.indexOf("logs") - 3;
            String outPut = sResult.substring(index1, index2);
            int index = Integer.parseInt(outPut, 16);  // 将16进制转化为10进制
            if (index == 1) {
                return "冻结成功";
            } else if (index == 0) {
                return "已经冻结";
            } else {
                return "冻结结果异常";
            }
        } catch (Exception e) {
            System.out.println(" event log not found, maybe transaction not exec.");
            return "冻结结果异常";
        }
    }

    public Boolean returnPoint(Integer userId) {
        try {
            String contractAddress = loadPointControllerAddr();
            PointController pointController = PointController.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
            BigInteger cPoint = pointController.queryPoint(new BigInteger(userId + "")).send();
            pointController.returnPoint(new BigInteger(userId + "")).send();
            BigInteger dPoint = pointController.queryPoint(new BigInteger(userId + "")).send();
            if (cPoint.intValue() == dPoint.intValue()) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            System.out.println(" event log not found, maybe transaction not exec.");
        }
        return false;
    }

    public Boolean succeedTrade(Integer buyerId, Integer sellerId) {
        try {
            String contractAddress = loadPointControllerAddr();
            PointController pointController = PointController.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
            BigInteger cPoint = pointController.queryPoint(new BigInteger(sellerId + "")).send();
            pointController.succeedTrade(new BigInteger(buyerId + ""), new BigInteger(sellerId + "")).send();
            BigInteger dPoint = pointController.queryPoint(new BigInteger(sellerId + "")).send();
            if (cPoint.intValue() == dPoint.intValue()) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            System.out.println(" event log not found, maybe transaction not exec.");
        }
        return false;
    }

    public Boolean normalTrade(int buyerId, int sellerId, int value) {
        try {
            String contractAddress = loadPointControllerAddr();
            PointController pointController = PointController.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
            pointController.normalTrade(new BigInteger(buyerId + ""), new BigInteger(sellerId + ""),
                    new BigInteger(value + "")).send();
            return true;
        } catch (Exception e) {
            System.out.println(" event log not found, maybe transaction not exec.");
        }
        return false;
    }

    // 侵权上链
    public Boolean recordResult(long userId, String result, String imgName) {
        try {
            String contractAddress = loadPointControllerAddr();
            PointController pointController = PointController.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
            TransactionReceipt receipt = pointController.recordResult(new BigInteger(userId + ""), result, imgName).send();
            List<PointController.RecordTheResultEventResponse> response = pointController.getRecordTheResultEvents(receipt);
            if (!response.isEmpty()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            logger.error(" record result exception, error message is {}", e.getMessage());
            System.out.printf(" record result failed, error message is %s\n", e.getMessage());
        }
        return false;
    }

    // 确权
    public Boolean recordEvidence(long userPhone, int tag, String imgName, String key) {
        try {
            String contractAddress = loadPointControllerAddr();
            PointController pointController = PointController.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
            TransactionReceipt receipt = pointController.recordEvidence(new BigInteger(userPhone + ""), new BigInteger(tag + ""), imgName, key).send();
            List<PointController.RecordTheEvidenceEventResponse> response = pointController.getRecordTheEvidenceEvents(receipt);
            /*System.out.println(response);
            if (!response.isEmpty()) {
                return true;
            } else {
                return false;
            }*/
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(" record result exception, error message is {}", e.getMessage());
            System.out.printf(" record result failed, error message is %s\n", e.getMessage());
        }
        return false;
    }

    public static void main(String[] args) throws Exception {

        PointControllerClient pointControllerClient = new PointControllerClient();

        pointControllerClient.initialize();

        //System.out.println(TAG + "   r" + pointControllerClient.registerId(123));

        //pointControllerClient.deployPointControllerAndRecordAddr();

        // 注册
        /*System.out.println(TAG + "   r" + pointControllerClient.registerId(123));
        // 查用户积分
        System.out.println(TAG + "   p" + pointControllerClient.queryPoint(123));
        // 充值
        System.out.println(TAG + "   r" + pointControllerClient.recharge(123, 200));
        //
        System.out.println(TAG + "   p" + pointControllerClient.queryPoint(123));
        // 上架
        System.out.println(TAG + "   put:" + pointControllerClient.putImgOnTheShelf("001", "hhd322432t"));
        // 通过tag查哈希
        System.out.println(TAG + "   que:" + pointControllerClient.queryHash("001"));
        // 冻结买家积分
        System.out.println(TAG + "   F:" + pointControllerClient.freezeAccount(123, 100));

        System.out.println(TAG + "   p" + pointControllerClient.queryPoint(123));
        // 返还积分,传卖方uid
        System.out.println(TAG + "   R: " + pointControllerClient.returnPoint(123));  // 传买方uid
        //
        System.out.println(TAG + "   p" + pointControllerClient.queryPoint(123));

        System.out.println(TAG + "   F:" + pointControllerClient.freezeAccount(123, 100));

        System.out.println(TAG + "   S: " + pointControllerClient.succeedTrade(123, 345));*/

        //System.out.println(TAG + "   que result: " + pointControllerClient.queryHash("sdjkfhdskjhfjds"));

    }

}






