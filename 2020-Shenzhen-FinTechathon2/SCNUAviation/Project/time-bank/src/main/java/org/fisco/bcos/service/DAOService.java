package org.fisco.bcos.service;

import org.fisco.bcos.constants.GasConstants;
import org.fisco.bcos.contract.DAO;
import org.fisco.bcos.model.DAOEntry;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tuples.generated.Tuple1;
import org.fisco.bcos.web3j.tuples.generated.Tuple4;
import org.fisco.bcos.web3j.tx.gas.StaticGasProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class DAOService {

    @Autowired
    private Web3j web3j;
    private DAO dao;

    public String deploy(
            Credentials credentials,
            String title,
            String description,
            BigInteger fundingGoalInToken,
            BigInteger durationInDays,
            String addressOfToken
            ) throws Exception {
        DAO dao =
                DAO.deploy(
                        web3j,
                        credentials,
                        new StaticGasProvider(
                                GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT),
                        title,
                        description,
                        fundingGoalInToken,
                        durationInDays,
                        addressOfToken).send();

        if (dao != null)
            return dao.getContractAddress();
        return "DAO deploy failed";
    }

    public String load(Credentials credentials, String contractAddress) {
        this.dao =
                DAO.load(
                        contractAddress,
                        web3j,
                        credentials,
                        new StaticGasProvider(
                                GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT));

        if (this.dao != null)
            return this.dao.getContractAddress();
        return "dao load failed";
    }

    public void pay(BigInteger timeToken) throws Exception {
        if (dao != null)
            dao.pay(timeToken).send();
    }

    public Boolean checkGoalReached() throws Exception {
        Tuple1<Boolean> tuple1;
        if (dao != null) {
            TransactionReceipt tr = dao.checkGoalReached().send();
            tuple1 = dao.getCheckGoalReachedOutput(tr);
            return tuple1.getValue1();
        }

        return new Boolean("False");

    }

    public void safeWithdrawal() throws Exception {
        if (dao != null)
            dao.safeWithdrawal().send();
    }

    public DAOEntry get_dao_info() throws Exception {
        Tuple4<String, String, BigInteger, String> dao_info = dao.dao_info().send();
        DAOEntry daoEntry = new DAOEntry();
        daoEntry.setTitle(dao_info.getValue1());
        daoEntry.setDescription(dao_info.getValue2());
        daoEntry.setFundingGoal(dao_info.getValue3());
        daoEntry.setBeneficiary(dao_info.getValue4());
        return daoEntry;
    }

    public BigInteger get_amountRaised() throws Exception {
        BigInteger amountRaised;
        amountRaised = dao.amountRaised().send();
        return amountRaised;
    }


}
