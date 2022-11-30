package org.prepay.prepay.raw;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.abi.FunctionEncoder;
import org.fisco.bcos.sdk.abi.FunctionReturnDecoder;
import org.fisco.bcos.sdk.abi.TypeReference;
import org.fisco.bcos.sdk.abi.datatypes.Address;
import org.fisco.bcos.sdk.abi.datatypes.Bool;
import org.fisco.bcos.sdk.abi.datatypes.Event;
import org.fisco.bcos.sdk.abi.datatypes.Function;
import org.fisco.bcos.sdk.abi.datatypes.Type;
import org.fisco.bcos.sdk.abi.datatypes.Utf8String;
import org.fisco.bcos.sdk.abi.datatypes.generated.Int256;
import org.fisco.bcos.sdk.abi.datatypes.generated.Uint256;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple2;
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
public class Refund extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b506040516020806108ee833981018060405261002f9190810190610089565b806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550506100d2565b600061008182516100b2565b905092915050565b60006020828403121561009b57600080fd5b60006100a984828501610075565b91505092915050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b61080d806100e16000396000f30060806040526004361061004c576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168063693bd2d014610051578063edd924161461007c575b600080fd5b34801561005d57600080fd5b506100666100ba565b60405161007391906105e6565b60405180910390f35b34801561008857600080fd5b506100a3600480360361009e9190810190610520565b6100df565b6040516100b1929190610601565b60405180910390f35b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60008060008060008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663b3dade06886040518263ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040161015d919061062a565b600060405180830381600087803b15801561017757600080fd5b505af115801561018b573d6000803e3d6000fd5b505050506040513d6000823e3d601f19601f820116820180604052506101b4919081019061044f565b909192939450909192935090919250905080945081935050508263240c84008703101561022157600190507f681ecd69674e80ffd2a76ab983988a5d41a9954e443878a7f8d569cad6150d4e87838884604051610214949392919061067c565b60405180910390a1610263565b600090507fc4110a59315b8ffd29359af4d8d471dd9004c8da2d864e68df2657ef97be805e8783888460405161025a949392919061067c565b60405180910390a15b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16632fa91cd088846040518363ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004016102db92919061064c565b602060405180830381600087803b1580156102f557600080fd5b505af1158015610309573d6000803e3d6000fd5b505050506040513d601f19601f8201168201806040525061032d9190810190610426565b508082945094505050509250929050565b600061034a825161076c565b905092915050565b600082601f830112151561036557600080fd5b8135610378610373826106f5565b6106c8565b9150808252602083016020830185838301111561039457600080fd5b61039f838284610780565b50505092915050565b600082601f83011215156103bb57600080fd5b81516103ce6103c9826106f5565b6106c8565b915080825260208301602083018583830111156103ea57600080fd5b6103f583828461078f565b50505092915050565b600061040a8235610776565b905092915050565b600061041e8251610776565b905092915050565b60006020828403121561043857600080fd5b60006104468482850161033e565b91505092915050565b60008060008060008060c0878903121561046857600080fd5b600087015167ffffffffffffffff81111561048257600080fd5b61048e89828a016103a8565b965050602087015167ffffffffffffffff8111156104ab57600080fd5b6104b789828a016103a8565b955050604087015167ffffffffffffffff8111156104d457600080fd5b6104e089828a016103a8565b94505060606104f189828a0161033e565b935050608061050289828a0161033e565b92505060a061051389828a01610412565b9150509295509295509295565b6000806040838503121561053357600080fd5b600083013567ffffffffffffffff81111561054d57600080fd5b61055985828601610352565b925050602061056a858286016103fe565b9150509250929050565b61057d8161072c565b82525050565b61058c8161074c565b82525050565b61059b81610758565b82525050565b60006105ac82610721565b8084526105c081602086016020860161078f565b6105c9816107c2565b602085010191505092915050565b6105e081610762565b82525050565b60006020820190506105fb6000830184610574565b92915050565b60006040820190506106166000830185610583565b6106236020830184610592565b9392505050565b6000602082019050818103600083015261064481846105a1565b905092915050565b6000604082019050818103600083015261066681856105a1565b90506106756020830184610592565b9392505050565b6000608082019050818103600083015261069681876105a1565b90506106a56020830186610592565b6106b260408301856105d7565b6106bf6060830184610583565b95945050505050565b6000604051905081810181811067ffffffffffffffff821117156106eb57600080fd5b8060405250919050565b600067ffffffffffffffff82111561070c57600080fd5b601f19601f8301169050602081019050919050565b600081519050919050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b60008115159050919050565b6000819050919050565b6000819050919050565b6000819050919050565b6000819050919050565b82818337600083830152505050565b60005b838110156107ad578082015181840152602081019050610792565b838111156107bc576000848401525b50505050565b6000601f19601f83011690509190505600a265627a7a723058200169e2eb6f4851fc31bd059bfe1b6dd72499ffb4a08917c8bd1659e07824d8296c6578706572696d656e74616cf50037"};

    public static final String BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {};

    public static final String SM_BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"constant\":true,\"inputs\":[],\"name\":\"cardContract\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_cardID\",\"type\":\"string\"},{\"name\":\"_now\",\"type\":\"uint256\"}],\"name\":\"refund\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"},{\"name\":\"\",\"type\":\"int256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"name\":\"_cardContract\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"cardID\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"balance\",\"type\":\"int256\"},{\"indexed\":false,\"name\":\"now\",\"type\":\"uint256\"},{\"indexed\":false,\"name\":\"isIn\",\"type\":\"bool\"}],\"name\":\"RefundInSeven\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"cardID\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"balance\",\"type\":\"int256\"},{\"indexed\":false,\"name\":\"now\",\"type\":\"uint256\"},{\"indexed\":false,\"name\":\"isIn\",\"type\":\"bool\"}],\"name\":\"RefundNotInSeven\",\"type\":\"event\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_CARDCONTRACT = "cardContract";

    public static final String FUNC_REFUND = "refund";

    public static final Event REFUNDINSEVEN_EVENT = new Event("RefundInSeven", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Int256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}));
    ;

    public static final Event REFUNDNOTINSEVEN_EVENT = new Event("RefundNotInSeven", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Int256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}));
    ;

    protected Refund(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public String cardContract() throws ContractException {
        final Function function = new Function(FUNC_CARDCONTRACT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeCallWithSingleValueReturn(function, String.class);
    }

    public TransactionReceipt refund(String _cardID, BigInteger _now) {
        final Function function = new Function(
                FUNC_REFUND, 
                Arrays.<Type>asList(new Utf8String(_cardID),
                new Uint256(_now)),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void refund(String _cardID, BigInteger _now, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_REFUND, 
                Arrays.<Type>asList(new Utf8String(_cardID),
                new Uint256(_now)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForRefund(String _cardID, BigInteger _now) {
        final Function function = new Function(
                FUNC_REFUND, 
                Arrays.<Type>asList(new Utf8String(_cardID),
                new Uint256(_now)),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple2<String, BigInteger> getRefundInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_REFUND, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple2<String, BigInteger>(

                (String) results.get(0).getValue(), 
                (BigInteger) results.get(1).getValue()
                );
    }

    public Tuple2<Boolean, BigInteger> getRefundOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_REFUND, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}, new TypeReference<Int256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple2<Boolean, BigInteger>(

                (Boolean) results.get(0).getValue(), 
                (BigInteger) results.get(1).getValue()
                );
    }

    public List<RefundInSevenEventResponse> getRefundInSevenEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(REFUNDINSEVEN_EVENT, transactionReceipt);
        ArrayList<RefundInSevenEventResponse> responses = new ArrayList<RefundInSevenEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            RefundInSevenEventResponse typedResponse = new RefundInSevenEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.cardID = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.balance = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.now = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.isIn = (Boolean) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeRefundInSevenEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(REFUNDINSEVEN_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeRefundInSevenEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(REFUNDINSEVEN_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public List<RefundNotInSevenEventResponse> getRefundNotInSevenEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(REFUNDNOTINSEVEN_EVENT, transactionReceipt);
        ArrayList<RefundNotInSevenEventResponse> responses = new ArrayList<RefundNotInSevenEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            RefundNotInSevenEventResponse typedResponse = new RefundNotInSevenEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.cardID = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.balance = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.now = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.isIn = (Boolean) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeRefundNotInSevenEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(REFUNDNOTINSEVEN_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeRefundNotInSevenEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(REFUNDNOTINSEVEN_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public static Refund load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new Refund(contractAddress, client, credential);
    }

    public static Refund deploy(Client client, CryptoKeyPair credential, String _cardContract) throws ContractException {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(_cardContract)));
        return deploy(Refund.class, client, credential, getBinary(client.getCryptoSuite()), encodedConstructor);
    }

    public static class RefundInSevenEventResponse {
        public TransactionReceipt.Logs log;

        public String cardID;

        public BigInteger balance;

        public BigInteger now;

        public Boolean isIn;
    }

    public static class RefundNotInSevenEventResponse {
        public TransactionReceipt.Logs log;

        public String cardID;

        public BigInteger balance;

        public BigInteger now;

        public Boolean isIn;
    }
}
