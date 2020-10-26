package org.fisco.bcos.service;

import org.fisco.bcos.constants.GasConstants;
import org.fisco.bcos.contract.TokenTime;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.tx.gas.StaticGasProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

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

    public void approve(String spender, BigInteger value) throws Exception {
        if (this.tokenTime != null) {
            tokenTime.approve(spender, value).send();
        }
    }


}
