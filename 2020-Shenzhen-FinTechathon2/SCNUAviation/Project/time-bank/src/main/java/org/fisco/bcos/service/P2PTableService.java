package org.fisco.bcos.service;

import org.fisco.bcos.constants.GasConstants;
import org.fisco.bcos.contract.P2PTable;
import org.fisco.bcos.model.P2PEntry;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.tuples.generated.Tuple6;
import org.fisco.bcos.web3j.tx.gas.StaticGasProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class P2PTableService {

    @Autowired
    private Web3j web3j;
    private P2PTable p2pTable;

    public String deploy(Credentials credentials) throws Exception {
        P2PTable _p2pTable =
                P2PTable.deploy(
                        web3j,
                        credentials,
                        new StaticGasProvider(
                                GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT)
                ).send();

        if (_p2pTable != null)
            return _p2pTable.getContractAddress();
        return "P2PTable deploy failed";
    }

    public String load(Credentials credentials, String contractAddress) throws Exception {
        this.p2pTable = P2PTable.load(
                contractAddress,
                web3j,
                credentials,
                new StaticGasProvider(
                        GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT
                        )
                );
        if (this.p2pTable != null)
            return this.p2pTable.getContractAddress();
        return "p2pTable load failed";
    }

    public void create(String tableName) throws Exception {
        if (this.p2pTable != null)
            p2pTable.create(tableName).send();
    }

    public ArrayList<P2PEntry> select(String key, String value) throws Exception {
        Tuple6<
                List<String>,
                List<String>,
                List<String>,
                List<BigInteger>,
                List<String>,
                List<BigInteger>
                > result;
        ArrayList<P2PEntry> p2pEntries = new ArrayList<>();

        if (this.p2pTable != null) {
            result = p2pTable.select(key, value).send();
            for (int i = 0;i < result.getValue1().size(); i++) {
                P2PEntry p2pEntry = new P2PEntry();
                p2pEntry.setP2pAddress(result.getValue1().get(i));
                p2pEntry.setOwnerAddress(result.getValue2().get(i));
                p2pEntry.setTitle(result.getValue3().get(i));
                p2pEntry.setPrice(result.getValue4().get(i));
                p2pEntry.setDescription(result.getValue5().get(i));
                p2pEntry.setState(result.getValue6().get(i));
                p2pEntries.add(p2pEntry);
            }
            return p2pEntries;
        }

        return p2pEntries;
    }

    public void insert(
            String p2pAddress,
            String ownerAddress,
            String title,
            BigInteger price,
            String description,
            BigInteger state) throws Exception {
        if (this.p2pTable != null)
            p2pTable.insert(p2pAddress, ownerAddress, title, price, description, state).send();
    }

    public void update(
            String p2pAddress,
            String volunteerAddress,
            BigInteger state) throws Exception {
        if (p2pTable != null)
            p2pTable.update(p2pAddress, volunteerAddress, state).send();
    }


}
