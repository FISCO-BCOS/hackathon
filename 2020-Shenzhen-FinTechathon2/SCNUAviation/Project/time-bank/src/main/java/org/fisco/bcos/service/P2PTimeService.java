package org.fisco.bcos.service;

import org.fisco.bcos.constants.GasConstants;
import org.fisco.bcos.contract.P2PTime;
import org.fisco.bcos.model.P2PInfo;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.tuples.generated.Tuple6;
import org.fisco.bcos.web3j.tx.gas.StaticGasProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service("p2pTimeService")
public class P2PTimeService {

    @Autowired
    private Web3j web3j;
    private P2PTime p2pTime;

    public String deploy(
            Credentials credentials,
            String _tokenTime,
            String title,
            BigInteger price,
            String sort,
            String description
            ) throws Exception {
        P2PTime p2pTime =
                P2PTime.deploy(
                        web3j,
                        credentials,
                        new StaticGasProvider(
                                GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT),
                        _tokenTime,
                        title,
                        price,
                        sort,
                        description
                ).send();

        if (p2pTime != null)
            return p2pTime.getContractAddress();
        return "P2PTime deploy failed";
    }

    public String load(Credentials credentials, String contractAddress) throws Exception {
        this.p2pTime =
                P2PTime.load(
                        contractAddress,
                        web3j,
                        credentials,
                        new StaticGasProvider(
                                GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT));

        if (this.p2pTime != null)
            return this.p2pTime.getContractAddress();
        return "p2pTime load failed";
    }

    public void publish() throws Exception {
        if (p2pTime != null)
            p2pTime.publish().send();
    }

    public void apply() throws Exception {
        if (p2pTime != null)
            p2pTime.apply().send();
    }

    public void accept(String volunteerAddress) throws Exception {
        if (p2pTime != null)
            p2pTime.accept(volunteerAddress).send();
    }

    public void assess_volunteer(BigInteger score, Boolean finished) throws Exception {
        if (p2pTime != null)
            p2pTime.assess_volunteer(score, finished).send();
    }

    public void assess_owner(BigInteger score) throws Exception {
        if (p2pTime != null)
            p2pTime.assess_owner(score).send();
    }

    public void interrupt_sel() throws Exception {
        if (p2pTime != null)
            p2pTime.interrupt_sell().send();
    }

    public P2PInfo getP2PAllInfo() throws Exception {
        P2PInfo p2pAllInfo = new P2PInfo();
        p2pAllInfo.setOwner(getOwner());
        p2pAllInfo.setVolunteer(getVolunteer());
        Tuple6<String, BigInteger, String, BigInteger, String, BigInteger> p2pInfo = getP2PInfo();
        p2pAllInfo.setTitle(p2pInfo.getValue1());
        p2pAllInfo.setPrice(Integer.parseInt(p2pInfo.getValue2().toString()));
        p2pAllInfo.setSort(p2pInfo.getValue3());
        p2pAllInfo.setPublish_time(Integer.parseInt(p2pInfo.getValue4().toString()));
        p2pAllInfo.setDescription(p2pInfo.getValue5());
        p2pAllInfo.setDeadline(Integer.parseInt(p2pInfo.getValue6().toString()));
        p2pAllInfo.setState(Integer.parseInt(getState().toString()));

        return p2pAllInfo;
    }

    private String getOwner() throws Exception {
        String owner = "";
        if (p2pTime != null)
            owner = p2pTime.owner().send();
        return  owner;
    }

    private String getVolunteer() throws Exception {
        String volunteer = "";
        if (p2pTime != null)
            volunteer = p2pTime.volunteer().send();
        return volunteer;
    }

    private Tuple6<String, BigInteger, String, BigInteger, String, BigInteger> getP2PInfo() throws Exception {
        Tuple6<String, BigInteger, String, BigInteger, String, BigInteger> p2pInfo = null;
        if (p2pTime != null)
            p2pInfo = p2pTime.p2p_info().send();
        return p2pInfo;
    }

    private BigInteger getState() throws Exception {
        BigInteger state = null;
        if (p2pTime != null)
            state = p2pTime.state().send();
        return state;
    }


}
