package org.fisco.bcos.service;

import org.fisco.bcos.constants.GasConstants;
import org.fisco.bcos.contract.TokenTime;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.tx.gas.StaticGasProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

//要用的时候就deploy一个合约，返回合约地址，存下来
//使用load函数，输入密钥对象和合约地址，加载我们想要调用的合约，然后调用这个Service的函数就可以用该账户对合约发起交易
@Service("tokenTimeService")
public class TokenTimeService {

    @Autowired
    private Web3j web3j;
    private TokenTime tokenTime;

    public String deploy(Credentials credentials) throws Exception {
        TokenTime tokenTime =
                TokenTime.deploy(
                        web3j,
                        credentials,
                        new StaticGasProvider(
                                GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT),
                        new BigInteger("100"),
                        "testName",
                        "testSymbol"
                ).send();

        if (tokenTime != null)
            return tokenTime.getContractAddress();
        return "TokenTime deploy failed";
    }

    public String load(Credentials credentials, String contractAddress) throws Exception {
        this.tokenTime =
                TokenTime.load(
                        contractAddress,
                        web3j,
                        credentials,
                        new StaticGasProvider(
                                GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT));

        if (this.tokenTime != null)
            return this.tokenTime.getContractAddress();
        return "tokenTime load failed";
    }

    public void initialBalance(String[] addresses) throws Exception {
        if (this.tokenTime != null) {
            for(String address : addresses)
                tokenTime.initial_balance(address).send();
        }
    }

    public void transferTo(String to, BigInteger value) throws Exception {
        if (this.tokenTime != null) {
            tokenTime.transferTo(to, value).send();
        }
    }

    public int getBalance(String address) throws Exception {
        int balance=3;
        if (this.tokenTime != null) {
            BigInteger bigInteger=tokenTime.balanceOf(address).send();
            balance = Integer.valueOf(bigInteger.toString());
            //System.out.println("balance:"+balance);
        }
        return balance;
    }

    public int getInitSupply() throws Exception {
        int initSupply=0;
        if (this.tokenTime != null) {
            BigInteger bigInteger=tokenTime.initial_supply().send();
            initSupply = Integer.valueOf(bigInteger.toString());
        }
        return initSupply;
    }

    public void approve(String spender, BigInteger value) throws Exception {
        if (this.tokenTime != null) {
            tokenTime.approve(spender, value).send();
        }
    }


}
