package com.media.bcos.contract;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.fisco.bcos.channel.client.TransactionSucCallback;
import org.fisco.bcos.channel.event.filter.EventLogPushWithDecodeCallback;
import org.fisco.bcos.web3j.abi.EventEncoder;
import org.fisco.bcos.web3j.abi.FunctionReturnDecoder;
import org.fisco.bcos.web3j.abi.TypeReference;
import org.fisco.bcos.web3j.abi.datatypes.Event;
import org.fisco.bcos.web3j.abi.datatypes.Function;
import org.fisco.bcos.web3j.abi.datatypes.Type;
import org.fisco.bcos.web3j.abi.datatypes.Utf8String;
import org.fisco.bcos.web3j.abi.datatypes.generated.Uint64;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.crypto.EncryptType;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.RemoteCall;
import org.fisco.bcos.web3j.protocol.core.methods.response.Log;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tuples.generated.Tuple4;
import org.fisco.bcos.web3j.tuples.generated.Tuple5;
import org.fisco.bcos.web3j.tx.Contract;
import org.fisco.bcos.web3j.tx.TransactionManager;
import org.fisco.bcos.web3j.tx.gas.ContractGasProvider;
import org.fisco.bcos.web3j.tx.txdecode.TransactionDecoder;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.fisco.bcos.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version none.
 */
@SuppressWarnings("unchecked")
public class TransactionData extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b5061078f806100206000396000f30060806040526004361061004c576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806360551e58146100515780639cec515014610184575b600080fd5b34801561005d57600080fd5b506100b8600480360381019080803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919291929050505061026f565b604051808567ffffffffffffffff1667ffffffffffffffff1681526020018467ffffffffffffffff1667ffffffffffffffff1681526020018367ffffffffffffffff1667ffffffffffffffff16815260200180602001828103825283818151815260200191508051906020019080838360005b8381101561014657808201518184015260208101905061012b565b50505050905090810190601f1680156101735780820380516001836020036101000a031916815260200191505b509550505050505060405180910390f35b34801561019057600080fd5b5061026d600480360381019080803567ffffffffffffffff169060200190929190803567ffffffffffffffff169060200190929190803567ffffffffffffffff169060200190929190803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192905050506103df565b005b60008060006060600080866040518082805190602001908083835b6020831015156102af578051825260208201915060208101905060208303925061028a565b6001836020036101000a038019825116818451168082178552505050505050905001915050908152602001604051809103902090508060000160009054906101000a900467ffffffffffffffff168160000160089054906101000a900467ffffffffffffffff168260000160109054906101000a900467ffffffffffffffff1683600101808054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103c85780601f1061039d576101008083540402835291602001916103c8565b820191906000526020600020905b8154815290600101906020018083116103ab57829003601f168201915b505050505090509450945094509450509193509193565b60a0604051908101604052808667ffffffffffffffff1681526020018567ffffffffffffffff1681526020018467ffffffffffffffff168152602001838152602001828152506000826040518082805190602001908083835b60208310151561045d5780518252602082019150602081019050602083039250610438565b6001836020036101000a038019825116818451168082178552505050505050905001915050908152602001604051809103902060008201518160000160006101000a81548167ffffffffffffffff021916908367ffffffffffffffff16021790555060208201518160000160086101000a81548167ffffffffffffffff021916908367ffffffffffffffff16021790555060408201518160000160106101000a81548167ffffffffffffffff021916908367ffffffffffffffff16021790555060608201518160010190805190602001906105399291906106be565b5060808201518160020190805190602001906105569291906106be565b509050507fc188aae0e306f1fbc813ea3b71b686dcb17c60e0e2bc827cbcd5e6e823aa60ff8585858585604051808667ffffffffffffffff1667ffffffffffffffff1681526020018567ffffffffffffffff1667ffffffffffffffff1681526020018467ffffffffffffffff1667ffffffffffffffff1681526020018060200180602001838103835285818151815260200191508051906020019080838360005b838110156106125780820151818401526020810190506105f7565b50505050905090810190601f16801561063f5780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b8381101561067857808201518184015260208101905061065d565b50505050905090810190601f1680156106a55780820380516001836020036101000a031916815260200191505b5097505050505050505060405180910390a15050505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106106ff57805160ff191683800117855561072d565b8280016001018555821561072d579182015b8281111561072c578251825591602001919060010190610711565b5b50905061073a919061073e565b5090565b61076091905b8082111561075c576000816000905550600101610744565b5090565b905600a165627a7a72305820d0b7e463bf34a5e573f41be618021cc93ab27a9c5a4f8d1d1a53556daed956270029"};

    public static final String BINARY = String.join("", BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"constant\":true,\"inputs\":[{\"name\":\"transactionId\",\"type\":\"string\"}],\"name\":\"queryRecord\",\"outputs\":[{\"name\":\"seller\",\"type\":\"uint64\"},{\"name\":\"buyer\",\"type\":\"uint64\"},{\"name\":\"price\",\"type\":\"uint64\"},{\"name\":\"hash\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"seller\",\"type\":\"uint64\"},{\"name\":\"buyer\",\"type\":\"uint64\"},{\"name\":\"price\",\"type\":\"uint64\"},{\"name\":\"hash\",\"type\":\"string\"},{\"name\":\"transactionId\",\"type\":\"string\"}],\"name\":\"TransactionRecord\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"seller\",\"type\":\"uint64\"},{\"indexed\":false,\"name\":\"buyer\",\"type\":\"uint64\"},{\"indexed\":false,\"name\":\"price\",\"type\":\"uint64\"},{\"indexed\":false,\"name\":\"hash\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"transactionId\",\"type\":\"string\"}],\"name\":\"record\",\"type\":\"event\"}]"};

    public static final String ABI = String.join("", ABI_ARRAY);

    public static final TransactionDecoder transactionDecoder = new TransactionDecoder(ABI, BINARY);

    public static final String[] SM_BINARY_ARRAY = {"608060405234801561001057600080fd5b5061078e806100206000396000f30060806040526004361061004b576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168062929aa61461005057806317af74881461013b575b600080fd5b34801561005c57600080fd5b50610139600480360381019080803567ffffffffffffffff169060200190929190803567ffffffffffffffff169060200190929190803567ffffffffffffffff169060200190929190803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919291929050505061026e565b005b34801561014757600080fd5b506101a2600480360381019080803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919291929050505061054d565b604051808567ffffffffffffffff1667ffffffffffffffff1681526020018467ffffffffffffffff1667ffffffffffffffff1681526020018367ffffffffffffffff1667ffffffffffffffff16815260200180602001828103825283818151815260200191508051906020019080838360005b83811015610230578082015181840152602081019050610215565b50505050905090810190601f16801561025d5780820380516001836020036101000a031916815260200191505b509550505050505060405180910390f35b60a0604051908101604052808667ffffffffffffffff1681526020018567ffffffffffffffff1681526020018467ffffffffffffffff168152602001838152602001828152506000826040518082805190602001908083835b6020831015156102ec57805182526020820191506020810190506020830392506102c7565b6001836020036101000a038019825116818451168082178552505050505050905001915050908152602001604051809103902060008201518160000160006101000a81548167ffffffffffffffff021916908367ffffffffffffffff16021790555060208201518160000160086101000a81548167ffffffffffffffff021916908367ffffffffffffffff16021790555060408201518160000160106101000a81548167ffffffffffffffff021916908367ffffffffffffffff16021790555060608201518160010190805190602001906103c89291906106bd565b5060808201518160020190805190602001906103e59291906106bd565b509050507f99c43e3387ebd32cbc6262563652a1e60d432ccf7ad8d096fdf0a99013900b318585858585604051808667ffffffffffffffff1667ffffffffffffffff1681526020018567ffffffffffffffff1667ffffffffffffffff1681526020018467ffffffffffffffff1667ffffffffffffffff1681526020018060200180602001838103835285818151815260200191508051906020019080838360005b838110156104a1578082015181840152602081019050610486565b50505050905090810190601f1680156104ce5780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b838110156105075780820151818401526020810190506104ec565b50505050905090810190601f1680156105345780820380516001836020036101000a031916815260200191505b5097505050505050505060405180910390a15050505050565b60008060006060600080866040518082805190602001908083835b60208310151561058d5780518252602082019150602081019050602083039250610568565b6001836020036101000a038019825116818451168082178552505050505050905001915050908152602001604051809103902090508060000160009054906101000a900467ffffffffffffffff168160000160089054906101000a900467ffffffffffffffff168260000160109054906101000a900467ffffffffffffffff1683600101808054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156106a65780601f1061067b576101008083540402835291602001916106a6565b820191906000526020600020905b81548152906001019060200180831161068957829003601f168201915b505050505090509450945094509450509193509193565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106106fe57805160ff191683800117855561072c565b8280016001018555821561072c579182015b8281111561072b578251825591602001919060010190610710565b5b509050610739919061073d565b5090565b61075f91905b8082111561075b576000816000905550600101610743565b5090565b905600a165627a7a723058207c251444140c64cd2663c7f074c4aad27b915d017ab2685d60264894d09605560029"};

    public static final String SM_BINARY = String.join("", SM_BINARY_ARRAY);

    public static final String FUNC_QUERYRECORD = "queryRecord";

    public static final String FUNC_TRANSACTIONRECORD = "TransactionRecord";

    public static final Event RECORD_EVENT = new Event("record",
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint64>() {}, new TypeReference<Uint64>() {}, new TypeReference<Uint64>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

    @Deprecated
    protected TransactionData(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(getBinary(), contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected TransactionData(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(getBinary(), contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected TransactionData(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(getBinary(), contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected TransactionData(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(getBinary(), contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static String getBinary() {
        return (EncryptType.encryptType == EncryptType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public static TransactionDecoder getTransactionDecoder() {
        return transactionDecoder;
    }

    public RemoteCall<Tuple4<BigInteger, BigInteger, BigInteger, String>> queryRecord(String transactionId) {
        final Function function = new Function(FUNC_QUERYRECORD,
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Utf8String(transactionId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint64>() {}, new TypeReference<Uint64>() {}, new TypeReference<Uint64>() {}, new TypeReference<Utf8String>() {}));
        return new RemoteCall<Tuple4<BigInteger, BigInteger, BigInteger, String>>(
                new Callable<Tuple4<BigInteger, BigInteger, BigInteger, String>>() {
                    @Override
                    public Tuple4<BigInteger, BigInteger, BigInteger, String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple4<BigInteger, BigInteger, BigInteger, String>(
                                (BigInteger) results.get(0).getValue(),
                                (BigInteger) results.get(1).getValue(),
                                (BigInteger) results.get(2).getValue(),
                                (String) results.get(3).getValue());
                    }
                });
    }

    public RemoteCall<TransactionReceipt> TransactionRecord(BigInteger seller, BigInteger buyer, BigInteger price, String hash, String transactionId) {
        final Function function = new Function(
                FUNC_TRANSACTIONRECORD,
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Uint64(seller),
                        new org.fisco.bcos.web3j.abi.datatypes.generated.Uint64(buyer),
                        new org.fisco.bcos.web3j.abi.datatypes.generated.Uint64(price),
                        new org.fisco.bcos.web3j.abi.datatypes.Utf8String(hash),
                        new org.fisco.bcos.web3j.abi.datatypes.Utf8String(transactionId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void TransactionRecord(BigInteger seller, BigInteger buyer, BigInteger price, String hash, String transactionId, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_TRANSACTIONRECORD,
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Uint64(seller),
                        new org.fisco.bcos.web3j.abi.datatypes.generated.Uint64(buyer),
                        new org.fisco.bcos.web3j.abi.datatypes.generated.Uint64(price),
                        new org.fisco.bcos.web3j.abi.datatypes.Utf8String(hash),
                        new org.fisco.bcos.web3j.abi.datatypes.Utf8String(transactionId)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String TransactionRecordSeq(BigInteger seller, BigInteger buyer, BigInteger price, String hash, String transactionId) {
        final Function function = new Function(
                FUNC_TRANSACTIONRECORD,
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Uint64(seller),
                        new org.fisco.bcos.web3j.abi.datatypes.generated.Uint64(buyer),
                        new org.fisco.bcos.web3j.abi.datatypes.generated.Uint64(price),
                        new org.fisco.bcos.web3j.abi.datatypes.Utf8String(hash),
                        new org.fisco.bcos.web3j.abi.datatypes.Utf8String(transactionId)),
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple5<BigInteger, BigInteger, BigInteger, String, String> getTransactionRecordInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_TRANSACTIONRECORD,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint64>() {}, new TypeReference<Uint64>() {}, new TypeReference<Uint64>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple5<BigInteger, BigInteger, BigInteger, String, String>(

                (BigInteger) results.get(0).getValue(),
                (BigInteger) results.get(1).getValue(),
                (BigInteger) results.get(2).getValue(),
                (String) results.get(3).getValue(),
                (String) results.get(4).getValue()
        );
    }

    public List<RecordEventResponse> getRecordEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(RECORD_EVENT, transactionReceipt);
        ArrayList<RecordEventResponse> responses = new ArrayList<RecordEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            RecordEventResponse typedResponse = new RecordEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.seller = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.buyer = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.price = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.hash = (String) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.transactionId = (String) eventValues.getNonIndexedValues().get(4).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void registerrecordEventLogFilter(String fromBlock, String toBlock, List<String> otherTopics, EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(RECORD_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void registerrecordEventLogFilter(EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(RECORD_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,callback);
    }

    @Deprecated
    public static TransactionData load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new TransactionData(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static TransactionData load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new TransactionData(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static TransactionData load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new TransactionData(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static TransactionData load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new TransactionData(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<TransactionData> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(TransactionData.class, web3j, credentials, contractGasProvider, getBinary(), "");
    }

    @Deprecated
    public static RemoteCall<TransactionData> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(TransactionData.class, web3j, credentials, gasPrice, gasLimit, getBinary(), "");
    }

    public static RemoteCall<TransactionData> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(TransactionData.class, web3j, transactionManager, contractGasProvider, getBinary(), "");
    }

    @Deprecated
    public static RemoteCall<TransactionData> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(TransactionData.class, web3j, transactionManager, gasPrice, gasLimit, getBinary(), "");
    }

    public static class RecordEventResponse {
        public Log log;

        public BigInteger seller;

        public BigInteger buyer;

        public BigInteger price;

        public String hash;

        public String transactionId;
    }
}
