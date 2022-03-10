package com.media.bcos.client;

import com.media.bcos.contract.TransactionData;
import org.fisco.bcos.channel.client.Service;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.crypto.gm.GenCredential;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.channel.ChannelEthereumService;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tuples.generated.Tuple4;
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

public class TransactionDataClient {

    static Logger logger = LoggerFactory.getLogger(TransactionDataClient.class);

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

    public String loadTransactionDataAddr() throws Exception {
        // load PointController contact address from contract.properties

        String contractAddress = "0x2540c39a09b2199037ab2ca1971260de9cfb876b";

        if (contractAddress == null) {
            Properties prop = new Properties();
            final Resource contractResource = new ClassPathResource("contract.properties");
            prop.load(contractResource.getInputStream());

            contractAddress = prop.getProperty("address");
            if (contractAddress == null || contractAddress.trim().equals("")) {
                throw new Exception(" load TransactionData contract address failed, please deploy it first. ");
            }
            logger.info(" load TransactionData address from contract.properties, address is {}", contractAddress);
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

    public void deployTransactionDataAndRecordAddr() {

        try {
            TransactionData transactionData = TransactionData.deploy(web3j, credentials, new StaticGasProvider(gasPrice, gasLimit)).send();
            System.out.println(" deploy TransactionData success, contract address is " + transactionData.getContractAddress());

            recordAssetAddr(transactionData.getContractAddress());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            System.out.println(" deploy TransactionData contract failed, error message is  " + e.getMessage());
        }
    }

    public Boolean TransactionRecord(int sellerId, int buyerId, int price, String hash, String transactionId) {
        try {
            String contractAddress = loadTransactionDataAddr();

            TransactionData transactionData = TransactionData.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
            TransactionReceipt receipt = transactionData.TransactionRecord(new BigInteger(sellerId + ""),
                    new BigInteger(buyerId + ""), new BigInteger(price + ""), hash, transactionId).send();
            List<TransactionData.RecordEventResponse> response = transactionData.getRecordEvents(receipt);
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

    public TRecord queryRecord(String transactionId) {
        try {
            String contractAddress = loadTransactionDataAddr();
            TransactionData transactionData = TransactionData.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
            Tuple4<BigInteger, BigInteger, BigInteger, String> record = transactionData.queryRecord(transactionId).send();
            TRecord tRecord = new TRecord(record.getValue1().intValue(), record.getValue2().intValue(),
                    record.getValue3().intValue(), record.getValue4(), transactionId);
            return tRecord;
        } catch (Exception e) {
            System.out.println(" event log not found, maybe transaction not exec.");
        }
        return null;

    }

    public static void main(String[] args) throws Exception {

        TransactionDataClient transactionDataClient = new TransactionDataClient();

        transactionDataClient.initialize();

        //transactionDataClient.deployTransactionDataAndRecordAddr();

        //System.out.println("r" + transactionDataClient.TransactionRecord(123,456,100,"001","1"));

        System.out.println("q" + transactionDataClient.queryRecord("1").toString());

    }


}