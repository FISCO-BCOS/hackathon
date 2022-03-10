package org.fisco.bcos.service;

import org.fisco.bcos.constants.GasConstants;
import org.fisco.bcos.contract.CharityGoods;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.tx.gas.StaticGasProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class CharityGoodsService {
    @Autowired
    private Web3j web3j;
    private CharityGoods charityGoods;

    public String deploy(
            Credentials credentials,
            String tokenTimeAddress,
            String daoAddress
    ) throws Exception {
        CharityGoods charityGoods =
                CharityGoods.deploy(
                        web3j,
                        credentials,
                        new StaticGasProvider(
                                GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT),
                        tokenTimeAddress,
                        daoAddress
                ).send();

        if (charityGoods != null)
            return charityGoods.getContractAddress();
        return "CharityGoods deploy failed";
    }

    public String load(Credentials credentials, String contractAddress) throws Exception {
        this.charityGoods =
                CharityGoods.load(
                        contractAddress,
                        web3j,
                        credentials,
                        new StaticGasProvider(
                                GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT));

        if (this.charityGoods != null)
            return this.charityGoods.getContractAddress();
        return "charityGoods load failed";
    }

    public void sell(
            String goods_name,
            BigInteger goods_number,
            BigInteger goods_price,
            BigInteger timeToken) throws Exception {
        if (this.charityGoods != null)
            charityGoods.sell(
                    goods_name,
                    goods_number,
                    goods_price,
                    timeToken).send();
    }

    public void purchase_phase1(BigInteger ledgerId, BigInteger goodsNumber) throws Exception {
        if (this.charityGoods != null)
            charityGoods.purchase_phase1(ledgerId, goodsNumber).send();
    }

    public void purchase_phase2(BigInteger ledgerId, BigInteger bank_serials_number) throws Exception {
        if (this.charityGoods != null)
            charityGoods.purchase_phase2(ledgerId, bank_serials_number).send();
    }


}
