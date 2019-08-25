package bcosClient;

import contract.CreditCert;
import contract.CreditCert.*;
import entity.*;
import org.fisco.bcos.channel.client.Service;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.crypto.Keys;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.channel.ChannelEthereumService;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tuples.generated.*;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class CreditCertClient {

    static Logger logger = LoggerFactory.getLogger(CreditCertClient.class);

    private Web3j web3j;

    private Credentials credentials;

    //合约地址,填写已经部署的合约地址
//    public String contractaddress = "0x0e6191a1f760ed5adb3a384b2d906b5475541a64";
//    public String contractaddress = "0xfdd31ce4eafc4063d3c34c75bcff9ff9f8955429";
//    public String contractaddress = "0xa749c62747c82d9543374f38dd41422b35baf131";
    public String contractaddress = "0x522e764439e99ac84ed14855639929f7a8e95117";

    public CreditCertClient() {
        try {
            initialize();
        }catch (Exception e){
            logger.debug("initialize failed");
        }
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

    public Web3j getWeb3j() {
        return web3j;
    }

    public void recordCreditCertAddr(String address) throws FileNotFoundException, IOException {
        Properties prop = new Properties();
        prop.setProperty("address", address);
        final Resource contractResource = new ClassPathResource("contract.properties");
        FileOutputStream fileOutputStream = new FileOutputStream(contractResource.getFile());
        prop.store(fileOutputStream, "contract address");
        contractaddress = address;
    }

    public String loadCreditCertAddr() throws Exception {
        // load CreditCert contact address from contract.properties
        if(contractaddress == null) {
            Properties prop = new Properties();
            final Resource contractResource = new ClassPathResource("contract.properties");
            prop.load(contractResource.getInputStream());

            String contractAddress = prop.getProperty("address");
            if (contractAddress == null || contractAddress.trim().equals("")) {
                throw new Exception(" load CreditCert contract address failed, please deploy it first. ");
            }
            logger.info(" load CreditCert address from contract.properties, address is {}", contractAddress);
            return contractAddress;
        } else
            return contractaddress;
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
        Credentials credentials = Credentials.create(Keys.createEcKeyPair());

        setCredentials(credentials);
        setWeb3j(web3j);

        logger.debug(" web3j is " + web3j + " ,credentials is " + credentials);
    }

    private static BigInteger gasPrice = new BigInteger("30000000");
    private static BigInteger gasLimit = new BigInteger("30000000");

    public String deployCreditCertAndRecordAddr() {

        try {
            CreditCert creditCert = CreditCert.deploy(web3j, credentials, new StaticGasProvider(gasPrice, gasLimit)).send();
            System.out.println(" deploy CreditCert success, contract address is " + creditCert.getContractAddress());
            recordCreditCertAddr(creditCert.getContractAddress());
            return creditCert.getContractAddress();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            System.out.println(" deploy CreditCert contract failed, error message is  " + e.getMessage());
        }
        return null;
    }

    public int stuInfoInit(long stuId, String stuName, String usName, int usLevel, String major, String extInfo, long time, int grade){
        try {
            String contractAddress = loadCreditCertAddr();
            CreditCert creditCert = CreditCert.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
            logger.debug("Student init!");
            TransactionReceipt receipt = creditCert.stuInfoInit(new BigInteger(stuId + ""), stuName, usName, new BigInteger(usLevel + ""),
                    major, extInfo,
                    new BigInteger(time + ""), new BigInteger( grade + "")).send();
            List<StuInfoInitEventEventResponse> response = creditCert.getStuInfoInitEventEvents(receipt);
            if (!response.isEmpty()) {
                if (response.get(0).stuID.longValue() == stuId){
                    System.out.printf(" Init student info success => stuID: %s, stuName: %s \n", response.get(0).stuID, response.get(0).stuName);
                    return 0;
                } else {
                    System.out.printf(" Init student info failed\n");
                }
            } else {
                System.out.println(" event log not found, maybe transaction not exec. ");
            }
        } catch (Exception e) {
            logger.error(" init student info exception, error message is {}", e.getMessage());
            System.out.printf(" init student info failed, error message is %s\n", e.getMessage());
        }
        return 0;
    }

    public int stuInfoUpdate(long stuId, String stuName, String usName, int usLevel, String major, String extInfo, long time, int grade){
        try {
            String contractAddress = loadCreditCertAddr();
            CreditCert creditCert = CreditCert.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
            logger.debug("Student update!");
            TransactionReceipt receipt = creditCert.stuInfoUpdate(new BigInteger(stuId + ""), stuName, usName, new BigInteger(usLevel + ""),
                    major, extInfo,
                    new BigInteger(time + ""), new BigInteger( grade + "")).send();
//            List<StuInfoUpdateEventEventResponse> response = creditCert.getStuInfoUpdateEventEvents(receipt);
//            if (!response.isEmpty()) {
//                if (response.get(0).stuID.longValue() == stuId){
//                    System.out.printf(" Update student info success => stuID: %s, stuName: %s \n", response.get(0).stuID, response.get(0).stuName);
//                    return 0;
//                } else {
//                    System.out.printf(" Update student info failed\n");
//                }
//            } else {
//                System.out.println(" event log not found, maybe transaction not exec. ");
//            }
        } catch (Exception e) {
            logger.error(" Update student info exception, error message is {}", e.getMessage());
            System.out.printf(" Update student info failed, error message is %s\n", e.getMessage());
        }
        return 0;
    }

    public Student stuInfoQuery(long stuId){
        Student student = null;
        try {
            String contractAddress = loadCreditCertAddr();
            CreditCert creditCert = CreditCert.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
            logger.debug("Student query!");
            Tuple8<BigInteger, String, String, BigInteger, String, String, BigInteger, BigInteger> result =
                    creditCert.stuInfoQuery(new BigInteger(stuId + "")).send();
            if(result.getValue1().compareTo(new BigInteger(stuId + "")) == 0){
                student = new Student(result.getValue1().longValue(), result.getValue2() , result.getValue3(), result.getValue4().intValue(),
                        result.getValue5(), result.getValue6(), result.getValue7().longValue(), result.getValue8().intValue());
                logger.info("Query successful!");
                return student;
            }else {
                logger.error("Query student info failed.");
            }

        } catch (Exception e) {
            logger.error(" Query student info exception, error message is {}", e.getMessage());
            System.out.printf(" Query student info failed, error message is %s\n", e.getMessage());
        }
        return student;
    }

    public int stuGradeRecord(long stuId, String stuName, int grade, int averageGrade, int obligatoryCredit,
                              int optionalCredit, String extInfo, long recordTime){
        try {
            String contractAddress = loadCreditCertAddr();
            CreditCert creditCert = CreditCert.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
            logger.debug("Grades recode!");
            TransactionReceipt receipt = creditCert.stuGradeRecord(new BigInteger(stuId + ""), stuName, new BigInteger(grade + ""),
                    new BigInteger(averageGrade + ""), new BigInteger(obligatoryCredit + ""), new BigInteger(optionalCredit + ""),extInfo,
                    new BigInteger(recordTime + "")).send();
//            List<StuGradeRecordEventEventResponse> response = creditCert.getStuGradeRecordEventEvents(receipt);
//            System.out.print(receipt);
//            if (!response.isEmpty()) {
//                if (response.get(0).stuID.longValue() == stuId){
//                    System.out.printf(" Recode grade success => stuID: %s, grade: %s \n", response.get(0).stuID, response.get(0).averageGrade);
//                    return 0;
//                } else {
//                    System.out.printf(" Recode grade failed\n");
//                }
//            } else {
//                System.out.println(" event log not found, maybe transaction not exec. ");
//            }
        } catch (Exception e) {
            logger.error(" Recode grade  exception, error message is {}", e.getMessage());
            System.out.printf(" Recode grade  failed, error message is %s\n", e.getMessage());
        }
        return 0;
    }

    public int stuGradeUpdate(long stuId, String stuName, int grade, int averageGrade, int obligatoryCredit,
                              int optionalCredit, String extInfo, long recordTime){
        try {
            String contractAddress = loadCreditCertAddr();
            CreditCert creditCert = CreditCert.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
            logger.debug("Grades update!");
            TransactionReceipt receipt = creditCert.stuGradeUpdate(new BigInteger(stuId + ""), stuName, new BigInteger(grade + ""),
                    new BigInteger(averageGrade + ""), new BigInteger(obligatoryCredit + ""), new BigInteger(optionalCredit + ""),extInfo,
                    new BigInteger(recordTime + "")).send();
//            List<StuGradeUpdateEventEventResponse> response = creditCert.getStuGradeUpdateEventEvents(receipt);
//            if (!response.isEmpty()) {
//                if (response.get(0).stuID.intValue() == stuId){
//                    System.out.printf(" Update grade success => stuID: %s, grade: %s \n", response.get(0).stuID, response.get(0).averageGrade);
//                    return 0;
//                } else {
//                    System.out.printf(" Update grade failed\n");
//                }
//            } else {
//                System.out.println(" event log not found, maybe transaction not exec. ");
//            }
        } catch (Exception e) {
            logger.error(" Update grade  exception, error message is {}", e.getMessage());
            System.out.printf(" Update grade  failed, error message is %s\n", e.getMessage());
        }
        return 0;
    }

    public Grades stuGradeQuery(long stuId, int grade){
        System.out.println(stuId);
        System.out.println(grade);
        Grades grades = null;
        try {
            String contractAddress = loadCreditCertAddr();
            CreditCert creditCert = CreditCert.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
            logger.debug("Grades query!");
            Tuple6<BigInteger, String, BigInteger, BigInteger, String, BigInteger> result =
                    creditCert.stuGradeQuery(new BigInteger(stuId + ""), new BigInteger(grade + "")).send();
//            if(result.getValue1().compareTo(new BigInteger(stuId + "")) == 0 && result.getValue3().compareTo(new BigInteger(grade + "")) == 0){
                grades = new Grades(result.getValue1().longValue(), result.getValue2() , result.getValue3().intValue(), result.getValue4().intValue(),
                        result.getValue5(), result.getValue6().longValue());
                logger.info("Query successful!");
                return grades;
//            }else {
//                logger.error("Query grades info failed.");
//            }

        } catch (Exception e) {
            logger.error(" Query grades info exception, error message is {}", e.getMessage());
            System.out.printf(" Query grades info failed, error message is %s\n", e.getMessage());
        }
        return grades;
    }

    public int activityRegister(int actID, String actName, String sponsor, String status, String extInfo, long registerTime){
        try {
            String contractAddress = loadCreditCertAddr();
            CreditCert creditCert = CreditCert.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
            logger.debug("Activity register!");
            TransactionReceipt receipt = creditCert.activityRegister(new BigInteger(actID + ""), actName, sponsor, status, extInfo,
                    new BigInteger(registerTime + "")).send();
//            List<ActivityRegisterEventEventResponse> response = creditCert.getActivityRegisterEventEvents(receipt);
//            if (!response.isEmpty()) {
//                if (response.get(0).actID.intValue() == actID){
//                    System.out.printf(" Activity register success => actID: %s, actName: %s \n", response.get(0).actID, response.get(0).actName);
//                    return 0;
//                } else {
//                    System.out.printf(" Activity register failed\n");
//                }
//            } else {
//                System.out.println(" event log not found, maybe transaction not exec. ");
//            }
        } catch (Exception e) {
            logger.error(" Activity register exception, error message is {}", e.getMessage());
            System.out.printf(" Activity register failed, error message is %s\n", e.getMessage());
        }
        return 0;
    }

    public int activityInfoUpdate(int actID, String actName, String sponsor, String status, String extInfo, long updateTime){
        try {
            String contractAddress = loadCreditCertAddr();
            CreditCert creditCert = CreditCert.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
            logger.debug("Activity update!");
            TransactionReceipt receipt = creditCert.activityInfoUpdate(new BigInteger(actID + ""), actName, sponsor, status, extInfo,
                    new BigInteger(updateTime + "")).send();
//            List<ActivityInfoUpdateEventEventResponse> response = creditCert.getActivityInfoUpdateEventEvents(receipt);
//            if (!response.isEmpty()) {
//                if (response.get(0).actID.intValue() == actID){
//                    System.out.printf(" Activity update success => actID: %s, actName: %s \n", response.get(0).actID, response.get(0).actName);
//                    return 0;
//                } else {
//                    System.out.printf(" Activity update failed\n");
//                }
//            } else {
//                System.out.println(" event log not found, maybe transaction not exec. ");
//            }
        } catch (Exception e) {
            logger.error(" Activity update exception, error message is {}", e.getMessage());
            System.out.printf(" Activity update failed, error message is %s\n", e.getMessage());
        }
        return 0;
    }

    public Activity actInfoQuery (int actId){
        Activity activity = null;
        try {
            String contractAddress = loadCreditCertAddr();
            CreditCert creditCert = CreditCert.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
            logger.debug("Activity query!");
            Tuple6<BigInteger, String, String, String, String, BigInteger> result =
                    creditCert.actInfoQuery(new BigInteger(actId + "")).send();
            if(result.getValue1().compareTo(new BigInteger(actId + "")) == 0){
                activity = new Activity(result.getValue1().intValue(), result.getValue2(), result.getValue3(), result.getValue4(),
                        result.getValue5(), result.getValue6().longValue());
                logger.info("Query successful!");
                return activity;
            }else {
                logger.error("Query activity info failed.");
            }

        } catch (Exception e) {
            logger.error(" Query activity info exception, error message is {}", e.getMessage());
            System.out.printf(" Query activity info failed, error message is %s\n", e.getMessage());
        }
        return activity;
    }

    //
    public int activityGradeRecord(int actID, long stuID, String actName, String stuName, String extInfo, long recordTime, String actSignature){
        try {
            String contractAddress = loadCreditCertAddr();
            CreditCert creditCert = CreditCert.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
            logger.debug("Activity grade recode!");
            TransactionReceipt receipt = creditCert.activityGradeRecode(new BigInteger(actID + ""), new BigInteger(stuID + "") ,
                    actName, stuName, extInfo,
                    new BigInteger(recordTime + ""), actSignature).send();
//            List<ActivityGradeRecodeEventEventResponse> response = creditCert.getActivityGradeRecodeEventEvents(receipt);
//            if (!response.isEmpty()) {
//                if (response.get(0).stuID.longValue() == stuID){
//                    System.out.printf(" Activity record success => stuName: %s, actName: %s \n", response.get(0).stuName, response.get(0).actName);
//                    return 0;
//                } else {
//                    System.out.printf(" Activity record failed\n");
//                }
//            } else {
//                System.out.println(" event log not found, maybe transaction not exec. ");
//            }
        } catch (Exception e) {
            logger.error(" Activity record exception, error message is {}", e.getMessage());
            System.out.printf(" Activity record failed, error message is %s\n", e.getMessage());
        }
        return 0;
    }

    public StuActivity stuActQuery(long stuID, int actID){
        StuActivity stuActivity = null;
        try {
            String contractAddress = loadCreditCertAddr();
            CreditCert creditCert = CreditCert.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
            logger.debug("Student activity query!");
            Tuple7<BigInteger, BigInteger, String, String, String, BigInteger, String> result =
                    creditCert.stuActQuery(new BigInteger(stuID + "") , new BigInteger(stuID + "")).send();
            if(result.getValue1().compareTo(new BigInteger( stuID + "")) == 0 && result.getValue2().compareTo(new BigInteger(actID + "")) == 0){
                stuActivity = new StuActivity(result.getValue1().intValue(), result.getValue2().longValue(), result.getValue3(), result.getValue4(),
                        result.getValue5(), result.getValue6().longValue(), result.getValue7());
                logger.info("Query successful!");
                return stuActivity;
            }else {
                logger.error("Query Student activity  failed.");
            }

        } catch (Exception e) {
            logger.error(" Query Student activity exception, error message is {}", e.getMessage());
            System.out.printf(" Query Student activity failed, error message is %s\n", e.getMessage());
        }
        return stuActivity;
    }


    //证书初始化
    public int certInfoInit(long certID, long stuID, String stuName, String usName, String major, String studyTime,
                            String certStatus, String extInfo, long initTime, String certSignature){
        try {
            String contractAddress = loadCreditCertAddr();
            CreditCert creditCert = CreditCert.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
            logger.debug("Certificate init!");
            TransactionReceipt receipt = creditCert.certInfoInit(new BigInteger(certID + ""), new BigInteger(stuID + ""),
                    stuName, usName, major, studyTime, certStatus, extInfo,
                    new BigInteger(initTime + ""), certSignature).send();
//            List<CertInfoInitEventEventResponse> response = creditCert.getCertInfoInitEventEvents(receipt);
//            if (!response.isEmpty()) {
//                if (response.get(0).certID.longValue() == certID){
//                    System.out.printf(" Certificate init success => stuName: %s, certID: %s \n", response.get(0).stuName, response.get(0).certID);
//                    return 0;
//                } else {
//                    System.out.printf(" Certificate init failed\n");
//                }
//            } else {
//                System.out.println(" event log not found, maybe transaction not exec. ");
//            }
        } catch (Exception e) {
            logger.error(" Certificate init exception, error message is {}", e.getMessage());
            System.out.printf(" Certificate init failed, error message is %s\n", e.getMessage());
        }
        return 0;
    }

    //证书状态更新
    public int certInfoUpdate(long certID, long stuID, String stuName, String usName, String major, String studyTime,
                              String certStatus, String extInfo, long updateTime, String certSignature){
        try {
            String contractAddress = loadCreditCertAddr();
            CreditCert creditCert = CreditCert.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
            logger.debug("Certificate update!");
            TransactionReceipt receipt = creditCert.certInfoUpdate(new BigInteger(certID + ""), new BigInteger(stuID + "") ,
                    stuName, usName, major, studyTime, certStatus, extInfo,
                    new BigInteger(updateTime + ""), certSignature).send();
//            List<CertInfoUpdateEventEventResponse> response = creditCert.getCertInfoUpdateEventEvents(receipt);
//            if (!response.isEmpty()) {
//                if (response.get(0).certID.longValue() == certID){
//                    System.out.printf(" Certificate update success => stuName: %s, certID: %s \n", response.get(0).stuName, response.get(0).certID);
//                    return 0;
//                } else {
//                    System.out.printf(" Certificate update failed\n");
//                }
//            } else {
//                System.out.println(" event log not found, maybe transaction not exec. ");
//            }
        } catch (Exception e) {
            logger.error(" Certificate update exception, error message is {}", e.getMessage());
            System.out.printf(" Certificate update failed, error message is %s\n", e.getMessage());
        }
        return 0;
    }

    public Cert stuCertQuery(long certID){
        Cert cert = null;
        try {
            String contractAddress = loadCreditCertAddr();
            CreditCert creditCert = CreditCert.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
            logger.debug("Student cert query!");
            Tuple9<BigInteger, BigInteger, String, String, String, String, String, String, BigInteger> result =
                    creditCert.stuCertQuery(new BigInteger(certID + "")).send();
            if(result.getValue1().compareTo(new BigInteger( certID + "")) == 0){
                cert = new Cert(result.getValue1().longValue(), result.getValue2().longValue(), result.getValue3(), result.getValue4(),
                        result.getValue5(), result.getValue6(), result.getValue7(), result.getValue8(), result.getValue9().longValue());
                logger.info("Query successful!");
                return cert;
            }else {
                logger.error("Query Student cert failed.");
            }

        } catch (Exception e) {
            logger.error(" Query Student cert exception, error message is {}", e.getMessage());
            System.out.printf(" Query Student cert failed, error message is %s\n", e.getMessage());
        }
        return cert;
    }

    public AllCredit allCreditQuery(long stuID){
        AllCredit allCredit = null;
        try {
            String contractAddress = loadCreditCertAddr();
            CreditCert creditCert = CreditCert.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
            logger.debug("Student cert query!");
            Tuple3<BigInteger,BigInteger,BigInteger> result = creditCert.allCreditQuery(new BigInteger(stuID+"")).send();
            allCredit = new AllCredit(result.getValue1().longValue(),result.getValue2().longValue(),result.getValue3().longValue());
            logger.info("Query successful!");
        }catch (Exception e){
            logger.error(" allCreditQueryt exception, error message is {}", e.getMessage());
            System.out.printf(" allCreditQuery failed, error message is %s\n", e.getMessage());
        }
        return allCredit;
    }

    public String certSignatureQueryk(long certID){
        String certSignature = "";
        try {
            String contractAddress = loadCreditCertAddr();
            CreditCert creditCert = CreditCert.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
            logger.debug("Student cert query!");
            Tuple2<String,String> result = creditCert.certSignatureQuery(new BigInteger(certID+"")).send();
            certSignature = result.getValue1();
        }catch (Exception e){
            logger.error(" allCreditQueryt exception, error message is {}", e.getMessage());
            System.out.printf(" allCreditQuery failed, error message is %s\n", e.getMessage());
        }
        return certSignature;
    }

    public Evaluation creditEvaluation(long stuID){
        Evaluation evaluation = null;
        try {
            String contractAddress = loadCreditCertAddr();
            CreditCert creditCert = CreditCert.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
            logger.debug("Student cert query!");
            Tuple4<BigInteger,BigInteger,BigInteger,BigInteger> result = creditCert.creditEvaluation(new BigInteger(stuID+"")).send();
//            System.out.println(result.getValue1().longValue());
//            System.out.println(result.getValue2().longValue());
//            System.out.println(result.getValue3().longValue());
//            System.out.println(result.getValue4().longValue());
            evaluation = new Evaluation(result.getValue1().longValue(),result.getValue2().longValue(),result.getValue3().longValue(),
                    result.getValue4().longValue());

        }catch (Exception e){
            logger.error(" allCreditQueryt exception, error message is {}", e.getMessage());
            System.out.printf(" allCreditQuery failed, error message is %s\n", e.getMessage());
        }
        return evaluation;
    }

    public static void Usage() {
        System.out.println("error parameter!\n");
        System.exit(0);
    }

//    public static void main(String[] args) {
//        CreditCertClient  creditCertClient = new CreditCertClient();
//        System.out.println(creditCertClient.deployCreditCertAndRecordAddr());
//    }

}
