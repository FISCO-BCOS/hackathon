package com.brecycle.contract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.abi.FunctionReturnDecoder;
import org.fisco.bcos.sdk.abi.TypeReference;
import org.fisco.bcos.sdk.abi.datatypes.Address;
import org.fisco.bcos.sdk.abi.datatypes.Bool;
import org.fisco.bcos.sdk.abi.datatypes.Event;
import org.fisco.bcos.sdk.abi.datatypes.Function;
import org.fisco.bcos.sdk.abi.datatypes.Type;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.contract.Contract;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.eventsub.EventCallback;
import org.fisco.bcos.sdk.model.CryptoType;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.model.callback.TransactionCallback;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class DAO extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b506105c0806100206000396000f300608060405260043610610057576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806350435fa41461005c578063d82a6e06146100b7578063f912fbca146100fa575b600080fd5b34801561006857600080fd5b5061009d600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061013d565b604051808215151515815260200191505060405180910390f35b3480156100c357600080fd5b506100f8600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610237565b005b34801561010657600080fd5b5061013b600480360381019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506103b6565b005b60008073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff16141515156101e3576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260208152602001807f44414f3a206163636f756e7420697320746865207a65726f206164647265737381525060200191505060405180910390fd5b600160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff169050919050565b6102408161013d565b1515156102b5576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601d8152602001807f44414f3a206163636f756e7420616c72656164792068617320726f6c6500000081525060200191505060405180910390fd5b60018060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff02191690831515021790555060008060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055507fc251ff2b0da987a4cc8f48505264a6637c9d9d0f04a579d164aecc63963ee82281604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390a150565b6103bf3361013d565b1515610459576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260268152602001807f44414f3a2063616c6c657220646f6573206e6f7420686176652074686520444181526020017f4f20726f6c65000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b6104628161013d565b15156104d6576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601f8152602001807f44414f3a206163636f756e7420646f6573206e6f74206861766520726f6c650081525060200191505060405180910390fd5b6000600160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff0219169083151502179055507fac29b2a542253bf4ca97f2427146ea9e117dfafc4c76a839c32641462d7ebef781604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390a1505600a165627a7a72305820e1938d936374898a75b797ebe9181bbf8290e4e2186fdb460b0bdd0cafa409820029"};

    public static final String BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"608060405234801561001057600080fd5b506105c0806100206000396000f300608060405260043610610057576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806309f755001461005c57806315ef93bd146100b7578063e5ab4caf146100fa575b600080fd5b34801561006857600080fd5b5061009d600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061013d565b604051808215151515815260200191505060405180910390f35b3480156100c357600080fd5b506100f8600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610237565b005b34801561010657600080fd5b5061013b600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610415565b005b60008073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff16141515156101e3576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260208152602001807f44414f3a206163636f756e7420697320746865207a65726f206164647265737381525060200191505060405180910390fd5b600160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff169050919050565b6102403361013d565b15156102da576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260268152602001807f44414f3a2063616c6c657220646f6573206e6f7420686176652074686520444181526020017f4f20726f6c65000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b6102e38161013d565b1515610357576040517fc703cb1200000000000000000000000000000000000000000000000000000000815260040180806020018281038252601f8152602001807f44414f3a206163636f756e7420646f6573206e6f74206861766520726f6c650081525060200191505060405180910390fd5b6000600160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff0219169083151502179055507f30d105dac8d320783e5c6674ae6a6b5f06aa97608dd2a18734cbb4b1451e082081604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390a150565b61041e8161013d565b151515610493576040517fc703cb1200000000000000000000000000000000000000000000000000000000815260040180806020018281038252601d8152602001807f44414f3a206163636f756e7420616c72656164792068617320726f6c6500000081525060200191505060405180910390fd5b60018060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff02191690831515021790555060008060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055507fba110a7316ab7ef948aa8ca7b01f1348309663a7bac8aba3ba10fccd16ced6f381604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390a1505600a165627a7a72305820783f2a7b74caffe9c78742479edf880d4a1847d67b320625ac00b876be0830fb0029"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"constant\":true,\"inputs\":[{\"name\":\"account\",\"type\":\"address\"}],\"name\":\"isDAO\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"account\",\"type\":\"address\"}],\"name\":\"addDAO\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"account\",\"type\":\"address\"}],\"name\":\"removeDAO\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"account\",\"type\":\"address\"}],\"name\":\"DAOAdded\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"account\",\"type\":\"address\"}],\"name\":\"DAORemoved\",\"type\":\"event\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_ISDAO = "isDAO";

    public static final String FUNC_ADDDAO = "addDAO";

    public static final String FUNC_REMOVEDAO = "removeDAO";

    public static final Event DAOADDED_EVENT = new Event("DAOAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    public static final Event DAOREMOVED_EVENT = new Event("DAORemoved", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    protected DAO(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public Boolean isDAO(String account) throws ContractException {
        final Function function = new Function(FUNC_ISDAO, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeCallWithSingleValueReturn(function, Boolean.class);
    }

    public TransactionReceipt addDAO(String account) {
        final Function function = new Function(
                FUNC_ADDDAO, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] addDAO(String account, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_ADDDAO, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(account)), 
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForAddDAO(String account) {
        final Function function = new Function(
                FUNC_ADDDAO, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(account)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple1<String> getAddDAOInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_ADDDAO, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<String>(

                (String) results.get(0).getValue()
                );
    }

    public TransactionReceipt removeDAO(String account) {
        final Function function = new Function(
                FUNC_REMOVEDAO, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] removeDAO(String account, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_REMOVEDAO, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(account)), 
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForRemoveDAO(String account) {
        final Function function = new Function(
                FUNC_REMOVEDAO, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(account)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple1<String> getRemoveDAOInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_REMOVEDAO, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<String>(

                (String) results.get(0).getValue()
                );
    }

    public List<DAOAddedEventResponse> getDAOAddedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(DAOADDED_EVENT, transactionReceipt);
        ArrayList<DAOAddedEventResponse> responses = new ArrayList<DAOAddedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            DAOAddedEventResponse typedResponse = new DAOAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeDAOAddedEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(DAOADDED_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeDAOAddedEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(DAOADDED_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public List<DAORemovedEventResponse> getDAORemovedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(DAOREMOVED_EVENT, transactionReceipt);
        ArrayList<DAORemovedEventResponse> responses = new ArrayList<DAORemovedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            DAORemovedEventResponse typedResponse = new DAORemovedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeDAORemovedEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(DAOREMOVED_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeDAORemovedEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(DAOREMOVED_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public static DAO load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new DAO(contractAddress, client, credential);
    }

    public static DAO deploy(Client client, CryptoKeyPair credential) throws ContractException {
        return deploy(DAO.class, client, credential, getBinary(client.getCryptoSuite()), "");
    }

    public static class DAOAddedEventResponse {
        public TransactionReceipt.Logs log;

        public String account;
    }

    public static class DAORemovedEventResponse {
        public TransactionReceipt.Logs log;

        public String account;
    }
}
