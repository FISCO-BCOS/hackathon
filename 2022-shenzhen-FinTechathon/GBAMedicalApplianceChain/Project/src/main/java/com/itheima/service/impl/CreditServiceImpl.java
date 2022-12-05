package com.itheima.service.impl;

import com.itheima.contract.Credit;
import com.itheima.contract.client.CreditClient;
import com.itheima.domain.CreditAgent;
import com.itheima.service.CreditService;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class CreditServiceImpl implements CreditService {


    @Override
    public boolean sendCredit(CreditAgent creditAgent) throws Exception {
        CreditClient creditClient = new CreditClient();
        creditClient.initialize();
        boolean b = creditClient.sendCredit(creditAgent.getAddressTo(), creditAgent.getValue(), null);
        return b;
    }

    @Override
    public int getBalance() throws Exception {
        CreditClient creditClient = new CreditClient();
        creditClient.initialize();
        return creditClient.getBalance();
    }



}
