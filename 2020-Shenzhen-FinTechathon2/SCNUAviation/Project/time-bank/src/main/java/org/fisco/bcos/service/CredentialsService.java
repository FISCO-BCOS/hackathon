package org.fisco.bcos.service;

import org.fisco.bcos.autoconfigure.AccountConfig;
import org.fisco.bcos.channel.client.P12Manager;
import org.fisco.bcos.channel.client.PEMManager;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.crypto.ECKeyPair;
import org.fisco.bcos.web3j.crypto.gm.GenCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;


//链上的个人账户，获取密钥对象
//登录需要用户名，暂时就是0x9833...，密码是123456
//对合约操作时，要传入密钥对象Credentials
@Service("credentialsService")
public class CredentialsService {

    private String pemFile;
    private String p12File;
    private String password;
    private static final Logger log = LoggerFactory.getLogger(AccountConfig.class);

    //获取密钥对象，输入用户名+密码
    //可以写入session中
    public Credentials getCredentials(String p12File, String password)
            throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException,
            InvalidKeySpecException, NoSuchProviderException, CertificateException,
            IOException {
//        return loadPemAccount();
         return loadP12Account(p12File, password);
    }

    // load pem account file
    private Credentials loadPemAccount()
            throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException,
            NoSuchProviderException, InvalidKeySpecException, UnrecoverableKeyException {
        log.info("pem accounts : {}", pemFile);
        PEMManager pem = new PEMManager();
        pem.setPemFile("classpath:" + pemFile);
        pem.load();
        ECKeyPair keyPair = pem.getECKeyPair();
        Credentials credentials = GenCredential.create(keyPair.getPrivateKey().toString(16));
        System.out.println(credentials.getAddress());
        return credentials;
    }

    // load p12 account file
    private Credentials loadP12Account(String p12File, String password)
            throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException,
            NoSuchProviderException, InvalidKeySpecException, UnrecoverableKeyException {
        log.info("p12 accounts : {}", p12File);
        P12Manager p12Manager = new P12Manager();
        p12Manager.setP12File("classpath:" + p12File);
        p12Manager.setPassword(password);
        p12Manager.load();
        ECKeyPair keyPair = p12Manager.getECKeyPair();
        Credentials credentials = GenCredential.create(keyPair.getPrivateKey().toString(16));
        System.out.println(credentials.getAddress());
        return credentials;
    }

}
