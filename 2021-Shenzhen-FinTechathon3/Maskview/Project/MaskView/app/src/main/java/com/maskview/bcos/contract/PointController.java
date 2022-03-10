package com.media.bcos.contract;

import org.fisco.bcos.channel.client.TransactionSucCallback;
import org.fisco.bcos.channel.event.filter.EventLogPushWithDecodeCallback;
import org.fisco.bcos.web3j.abi.EventEncoder;
import org.fisco.bcos.web3j.abi.TypeReference;
import org.fisco.bcos.web3j.abi.datatypes.Event;
import org.fisco.bcos.web3j.abi.datatypes.Function;
import org.fisco.bcos.web3j.abi.datatypes.Type;
import org.fisco.bcos.web3j.abi.datatypes.Utf8String;
import org.fisco.bcos.web3j.abi.datatypes.generated.Uint64;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.RemoteCall;
import org.fisco.bcos.web3j.protocol.core.methods.response.Log;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tx.Contract;
import org.fisco.bcos.web3j.tx.TransactionManager;
import org.fisco.bcos.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
public class PointController extends Contract {
    public static String BINARY = "608060405234801561001057600080fd5b50611358806100206000396000f3006080604052600436106100af576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806314d40706146100b4578063168972461461019657806352f4c26b146101f95780637adccc291461024457806393e66a61146102a35780639b207a6d14610352578063a01e785814610429578063e00c519b14610460578063f3947f3614610523578063f51658631461059a578063fd312973146105f9575b600080fd5b3480156100c057600080fd5b5061011b600480360381019080803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919291929050505061065c565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561015b578082015181840152602081019050610140565b50505050905090810190601f1680156101885780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b3480156101a257600080fd5b506101df600480360381019080803567ffffffffffffffff169060200190929190803567ffffffffffffffff169060200190929190505050610769565b604051808215151515815260200191505060405180910390f35b34801561020557600080fd5b50610242600480360381019080803567ffffffffffffffff169060200190929190803567ffffffffffffffff169060200190929190505050610831565b005b34801561025057600080fd5b50610279600480360381019080803567ffffffffffffffff169060200190929190505050610925565b604051808267ffffffffffffffff1667ffffffffffffffff16815260200191505060405180910390f35b3480156102af57600080fd5b50610350600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050610969565b005b34801561035e57600080fd5b50610427600480360381019080803567ffffffffffffffff169060200190929190803567ffffffffffffffff169060200190929190803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050610af6565b005b34801561043557600080fd5b5061045e600480360381019080803567ffffffffffffffff169060200190929190505050610c3d565b005b34801561046c57600080fd5b50610521600480360381019080803567ffffffffffffffff169060200190929190803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050610d30565b005b34801561052f57600080fd5b50610580600480360381019080803567ffffffffffffffff169060200190929190803567ffffffffffffffff169060200190929190803567ffffffffffffffff169060200190929190505050610e5a565b604051808215151515815260200191505060405180910390f35b3480156105a657600080fd5b506105cf600480360381019080803567ffffffffffffffff169060200190929190505050610f93565b604051808267ffffffffffffffff1667ffffffffffffffff16815260200191505060405180910390f35b34801561060557600080fd5b50610642600480360381019080803567ffffffffffffffff169060200190929190803567ffffffffffffffff169060200190929190505050611116565b604051808215151515815260200191505060405180910390f35b60606001826040518082805190602001908083835b6020831015156106965780518252602082019150602081019050602083039250610671565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390208054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561075d5780601f106107325761010080835404028352916020019161075d565b820191906000526020600020905b81548152906001019060200180831161074057829003601f168201915b50505050509050919050565b6000806000808567ffffffffffffffff1667ffffffffffffffff16815260200190815260200160002060009054906101000a900467ffffffffffffffff1667ffffffffffffffff1610156107c0576000905061082b565b816000808567ffffffffffffffff1667ffffffffffffffff16815260200190815260200160002060008282829054906101000a900467ffffffffffffffff160192506101000a81548167ffffffffffffffff021916908367ffffffffffffffff160217905550600190505b92915050565b600260008367ffffffffffffffff1667ffffffffffffffff16815260200190815260200160002060009054906101000a900467ffffffffffffffff166000808367ffffffffffffffff1667ffffffffffffffff16815260200190815260200160002060008282829054906101000a900467ffffffffffffffff160192506101000a81548167ffffffffffffffff021916908367ffffffffffffffff1602179055506000600260008467ffffffffffffffff1667ffffffffffffffff16815260200190815260200160002060006101000a81548167ffffffffffffffff021916908367ffffffffffffffff1602179055505050565b60008060008367ffffffffffffffff1667ffffffffffffffff16815260200190815260200160002060009054906101000a900467ffffffffffffffff169050919050565b806001836040518082805190602001908083835b6020831015156109a2578051825260208201915060208101905060208303925061097d565b6001836020036101000a038019825116818451168082178552505050505050905001915050908152602001604051809103902090805190602001906109e8929190611287565b507f822b98db943bb7c85984ee746250c98a0e479b1715dfe808a64a98478c826b338282604051808060200180602001838103835285818151815260200191508051906020019080838360005b83811015610a50578082015181840152602081019050610a35565b50505050905090810190601f168015610a7d5780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b83811015610ab6578082015181840152602081019050610a9b565b50505050905090810190601f168015610ae35780820380516001836020036101000a031916815260200191505b5094505050505060405180910390a15050565b7f915aed83219ca25921951087fd888357d30cf329c7ff4620be08ea9c1fa2fe2a84848484604051808567ffffffffffffffff1667ffffffffffffffff1681526020018467ffffffffffffffff1667ffffffffffffffff1681526020018060200180602001838103835285818151815260200191508051906020019080838360005b83811015610b93578082015181840152602081019050610b78565b50505050905090810190601f168015610bc05780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b83811015610bf9578082015181840152602081019050610bde565b50505050905090810190601f168015610c265780820380516001836020036101000a031916815260200191505b50965050505050505060405180910390a150505050565b600260008267ffffffffffffffff1667ffffffffffffffff16815260200190815260200160002060009054906101000a900467ffffffffffffffff166000808367ffffffffffffffff1667ffffffffffffffff16815260200190815260200160002060008282829054906101000a900467ffffffffffffffff160192506101000a81548167ffffffffffffffff021916908367ffffffffffffffff1602179055506000600260008367ffffffffffffffff1667ffffffffffffffff16815260200190815260200160002060006101000a81548167ffffffffffffffff021916908367ffffffffffffffff16021790555050565b7f4ab3b787fb56bf02b6aa996529b3bea3d746db498252e774b810437f189734cf838383604051808467ffffffffffffffff1667ffffffffffffffff1681526020018060200180602001838103835285818151815260200191508051906020019080838360005b83811015610db2578082015181840152602081019050610d97565b50505050905090810190601f168015610ddf5780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b83811015610e18578082015181840152602081019050610dfd565b50505050905090810190601f168015610e455780820380516001836020036101000a031916815260200191505b509550505050505060405180910390a1505050565b60008167ffffffffffffffff166000808667ffffffffffffffff1667ffffffffffffffff16815260200190815260200160002060009054906101000a900467ffffffffffffffff1667ffffffffffffffff161015610ebb5760009050610f8c565b816000808667ffffffffffffffff1667ffffffffffffffff16815260200190815260200160002060008282829054906101000a900467ffffffffffffffff160392506101000a81548167ffffffffffffffff021916908367ffffffffffffffff160217905550816000808567ffffffffffffffff1667ffffffffffffffff16815260200190815260200160002060008282829054906101000a900467ffffffffffffffff160192506101000a81548167ffffffffffffffff021916908367ffffffffffffffff160217905550600190505b9392505050565b60006101f46000808467ffffffffffffffff1667ffffffffffffffff16815260200190815260200160002060006101000a81548167ffffffffffffffff021916908367ffffffffffffffff1602179055506000600260008467ffffffffffffffff1667ffffffffffffffff16815260200190815260200160002060006101000a81548167ffffffffffffffff021916908367ffffffffffffffff1602179055507f308701f0829bc567fb2ef1d2ac0c547a58d1b0fd241168fe4c74890f1cd685a0826000808567ffffffffffffffff1667ffffffffffffffff16815260200190815260200160002060009054906101000a900467ffffffffffffffff16604051808367ffffffffffffffff1667ffffffffffffffff1681526020018267ffffffffffffffff1667ffffffffffffffff1681526020019250505060405180910390a16000808367ffffffffffffffff1667ffffffffffffffff16815260200190815260200160002060009054906101000a900467ffffffffffffffff169050919050565b60008167ffffffffffffffff166000808567ffffffffffffffff1667ffffffffffffffff16815260200190815260200160002060009054906101000a900467ffffffffffffffff1667ffffffffffffffff1610806111ba57506000600260008567ffffffffffffffff1667ffffffffffffffff16815260200190815260200160002060009054906101000a900467ffffffffffffffff1667ffffffffffffffff1614155b156111c85760009050611281565b816000808567ffffffffffffffff1667ffffffffffffffff16815260200190815260200160002060008282829054906101000a900467ffffffffffffffff160392506101000a81548167ffffffffffffffff021916908367ffffffffffffffff16021790555081600260008567ffffffffffffffff1667ffffffffffffffff16815260200190815260200160002060006101000a81548167ffffffffffffffff021916908367ffffffffffffffff160217905550600190505b92915050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106112c857805160ff19168380011785556112f6565b828001600101855582156112f6579182015b828111156112f55782518255916020019190600101906112da565b5b5090506113039190611307565b5090565b61132991905b8082111561132557600081600090555060010161130d565b5090565b905600a165627a7a723058200b87f85492d95bffe22e415595aaf6b39c525c1789fb2bed8a9b8a40653b71ef0029";

    public static final String ABI = "[{\"constant\":true,\"inputs\":[{\"name\":\"tag\",\"type\":\"string\"}],\"name\":\"queryHash\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"userId\",\"type\":\"uint64\"},{\"name\":\"value\",\"type\":\"uint64\"}],\"name\":\"recharge\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"buyerId\",\"type\":\"uint64\"},{\"name\":\"sellerId\",\"type\":\"uint64\"}],\"name\":\"succeedTrade\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"userId\",\"type\":\"uint64\"}],\"name\":\"queryPoint\",\"outputs\":[{\"name\":\"\",\"type\":\"uint64\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"tag\",\"type\":\"string\"},{\"name\":\"hash\",\"type\":\"string\"}],\"name\":\"putImgOnTheShelf\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"userPhone\",\"type\":\"uint64\"},{\"name\":\"tag\",\"type\":\"uint64\"},{\"name\":\"imgName\",\"type\":\"string\"},{\"name\":\"key\",\"type\":\"string\"}],\"name\":\"recordEvidence\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"userId\",\"type\":\"uint64\"}],\"name\":\"returnPoint\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"userId\",\"type\":\"uint64\"},{\"name\":\"result\",\"type\":\"string\"},{\"name\":\"imgName\",\"type\":\"string\"}],\"name\":\"recordResult\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"buyerId\",\"type\":\"uint64\"},{\"name\":\"sellerId\",\"type\":\"uint64\"},{\"name\":\"value\",\"type\":\"uint64\"}],\"name\":\"normalTrade\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"userId\",\"type\":\"uint64\"}],\"name\":\"register\",\"outputs\":[{\"name\":\"\",\"type\":\"uint64\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"userId\",\"type\":\"uint64\"},{\"name\":\"value\",\"type\":\"uint64\"}],\"name\":\"freezeAccount\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"userId\",\"type\":\"uint64\"},{\"indexed\":false,\"name\":\"value\",\"type\":\"uint64\"}],\"name\":\"registerId\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"tag\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"hash\",\"type\":\"string\"}],\"name\":\"putOnTheShelf\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"userId\",\"type\":\"uint64\"},{\"indexed\":false,\"name\":\"result\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"imgName\",\"type\":\"string\"}],\"name\":\"recordTheResult\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"userPhone\",\"type\":\"uint64\"},{\"indexed\":false,\"name\":\"tag\",\"type\":\"uint64\"},{\"indexed\":false,\"name\":\"imgName\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"key\",\"type\":\"string\"}],\"name\":\"recordTheEvidence\",\"type\":\"event\"}]";

    public static final String FUNC_QUERYHASH = "queryHash";

    public static final String FUNC_RECHARGE = "recharge";

    public static final String FUNC_SUCCEEDTRADE = "succeedTrade";

    public static final String FUNC_QUERYPOINT = "queryPoint";

    public static final String FUNC_PUTIMGONTHESHELF = "putImgOnTheShelf";

    public static final String FUNC_RECORDEVIDENCE = "recordEvidence";

    public static final String FUNC_RETURNPOINT = "returnPoint";

    public static final String FUNC_RECORDRESULT = "recordResult";

    public static final String FUNC_NORMALTRADE = "normalTrade";

    public static final String FUNC_REGISTER = "register";

    public static final String FUNC_FREEZEACCOUNT = "freezeAccount";

    public static final Event REGISTERID_EVENT = new Event("registerId", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint64>() {}, new TypeReference<Uint64>() {}));
    ;

    public static final Event PUTONTHESHELF_EVENT = new Event("putOnTheShelf", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event RECORDTHERESULT_EVENT = new Event("recordTheResult", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint64>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event RECORDTHEEVIDENCE_EVENT = new Event("recordTheEvidence", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint64>() {}, new TypeReference<Uint64>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

    @Deprecated
    protected PointController(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected PointController(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected PointController(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected PointController(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<String> queryHash(String tag) {
        final Function function = new Function(FUNC_QUERYHASH, 
                Arrays.<Type>asList(new Utf8String(tag)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> recharge(BigInteger userId, BigInteger value) {
        final Function function = new Function(
                FUNC_RECHARGE, 
                Arrays.<Type>asList(new Uint64(userId),
                new Uint64(value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void recharge(BigInteger userId, BigInteger value, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_RECHARGE, 
                Arrays.<Type>asList(new Uint64(userId),
                new Uint64(value)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String rechargeSeq(BigInteger userId, BigInteger value) {
        final Function function = new Function(
                FUNC_RECHARGE, 
                Arrays.<Type>asList(new Uint64(userId),
                new Uint64(value)),
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public RemoteCall<TransactionReceipt> succeedTrade(BigInteger buyerId, BigInteger sellerId) {
        final Function function = new Function(
                FUNC_SUCCEEDTRADE, 
                Arrays.<Type>asList(new Uint64(buyerId),
                new Uint64(sellerId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void succeedTrade(BigInteger buyerId, BigInteger sellerId, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_SUCCEEDTRADE, 
                Arrays.<Type>asList(new Uint64(buyerId),
                new Uint64(sellerId)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String succeedTradeSeq(BigInteger buyerId, BigInteger sellerId) {
        final Function function = new Function(
                FUNC_SUCCEEDTRADE, 
                Arrays.<Type>asList(new Uint64(buyerId),
                new Uint64(sellerId)),
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public RemoteCall<BigInteger> queryPoint(BigInteger userId) {
        final Function function = new Function(FUNC_QUERYPOINT, 
                Arrays.<Type>asList(new Uint64(userId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint64>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> putImgOnTheShelf(String tag, String hash) {
        final Function function = new Function(
                FUNC_PUTIMGONTHESHELF, 
                Arrays.<Type>asList(new Utf8String(tag),
                new Utf8String(hash)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void putImgOnTheShelf(String tag, String hash, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_PUTIMGONTHESHELF, 
                Arrays.<Type>asList(new Utf8String(tag),
                new Utf8String(hash)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String putImgOnTheShelfSeq(String tag, String hash) {
        final Function function = new Function(
                FUNC_PUTIMGONTHESHELF, 
                Arrays.<Type>asList(new Utf8String(tag),
                new Utf8String(hash)),
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public RemoteCall<TransactionReceipt> recordEvidence(BigInteger userPhone, BigInteger tag, String imgName, String key) {
        final Function function = new Function(
                FUNC_RECORDEVIDENCE, 
                Arrays.<Type>asList(new Uint64(userPhone),
                new Uint64(tag),
                new Utf8String(imgName),
                new Utf8String(key)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void recordEvidence(BigInteger userPhone, BigInteger tag, String imgName, String key, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_RECORDEVIDENCE, 
                Arrays.<Type>asList(new Uint64(userPhone),
                new Uint64(tag),
                new Utf8String(imgName),
                new Utf8String(key)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String recordEvidenceSeq(BigInteger userPhone, BigInteger tag, String imgName, String key) {
        final Function function = new Function(
                FUNC_RECORDEVIDENCE, 
                Arrays.<Type>asList(new Uint64(userPhone),
                new Uint64(tag),
                new Utf8String(imgName),
                new Utf8String(key)),
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public RemoteCall<TransactionReceipt> returnPoint(BigInteger userId) {
        final Function function = new Function(
                FUNC_RETURNPOINT, 
                Arrays.<Type>asList(new Uint64(userId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void returnPoint(BigInteger userId, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_RETURNPOINT, 
                Arrays.<Type>asList(new Uint64(userId)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String returnPointSeq(BigInteger userId) {
        final Function function = new Function(
                FUNC_RETURNPOINT, 
                Arrays.<Type>asList(new Uint64(userId)),
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public RemoteCall<TransactionReceipt> recordResult(BigInteger userId, String result, String imgName) {
        final Function function = new Function(
                FUNC_RECORDRESULT, 
                Arrays.<Type>asList(new Uint64(userId),
                new Utf8String(result),
                new Utf8String(imgName)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void recordResult(BigInteger userId, String result, String imgName, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_RECORDRESULT, 
                Arrays.<Type>asList(new Uint64(userId),
                new Utf8String(result),
                new Utf8String(imgName)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String recordResultSeq(BigInteger userId, String result, String imgName) {
        final Function function = new Function(
                FUNC_RECORDRESULT, 
                Arrays.<Type>asList(new Uint64(userId),
                new Utf8String(result),
                new Utf8String(imgName)),
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public RemoteCall<TransactionReceipt> normalTrade(BigInteger buyerId, BigInteger sellerId, BigInteger value) {
        final Function function = new Function(
                FUNC_NORMALTRADE, 
                Arrays.<Type>asList(new Uint64(buyerId),
                new Uint64(sellerId),
                new Uint64(value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void normalTrade(BigInteger buyerId, BigInteger sellerId, BigInteger value, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_NORMALTRADE, 
                Arrays.<Type>asList(new Uint64(buyerId),
                new Uint64(sellerId),
                new Uint64(value)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String normalTradeSeq(BigInteger buyerId, BigInteger sellerId, BigInteger value) {
        final Function function = new Function(
                FUNC_NORMALTRADE, 
                Arrays.<Type>asList(new Uint64(buyerId),
                new Uint64(sellerId),
                new Uint64(value)),
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public RemoteCall<TransactionReceipt> register(BigInteger userId) {
        final Function function = new Function(
                FUNC_REGISTER, 
                Arrays.<Type>asList(new Uint64(userId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void register(BigInteger userId, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_REGISTER, 
                Arrays.<Type>asList(new Uint64(userId)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String registerSeq(BigInteger userId) {
        final Function function = new Function(
                FUNC_REGISTER, 
                Arrays.<Type>asList(new Uint64(userId)),
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public RemoteCall<TransactionReceipt> freezeAccount(BigInteger userId, BigInteger value) {
        final Function function = new Function(
                FUNC_FREEZEACCOUNT, 
                Arrays.<Type>asList(new Uint64(userId),
                new Uint64(value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void freezeAccount(BigInteger userId, BigInteger value, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_FREEZEACCOUNT, 
                Arrays.<Type>asList(new Uint64(userId),
                new Uint64(value)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String freezeAccountSeq(BigInteger userId, BigInteger value) {
        final Function function = new Function(
                FUNC_FREEZEACCOUNT, 
                Arrays.<Type>asList(new Uint64(userId),
                new Uint64(value)),
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public List<RegisterIdEventResponse> getRegisterIdEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(REGISTERID_EVENT, transactionReceipt);
        ArrayList<RegisterIdEventResponse> responses = new ArrayList<RegisterIdEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            RegisterIdEventResponse typedResponse = new RegisterIdEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.userId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void registerregisterIdEventLogFilter(String fromBlock, String toBlock, List<String> otherTopcs, EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(REGISTERID_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,fromBlock,toBlock,otherTopcs,callback);
    }

    public void registerregisterIdEventLogFilter(EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(REGISTERID_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,callback);
    }

    public List<PutOnTheShelfEventResponse> getPutOnTheShelfEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(PUTONTHESHELF_EVENT, transactionReceipt);
        ArrayList<PutOnTheShelfEventResponse> responses = new ArrayList<PutOnTheShelfEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            PutOnTheShelfEventResponse typedResponse = new PutOnTheShelfEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.tag = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.hash = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void registerputOnTheShelfEventLogFilter(String fromBlock, String toBlock, List<String> otherTopcs, EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(PUTONTHESHELF_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,fromBlock,toBlock,otherTopcs,callback);
    }

    public void registerputOnTheShelfEventLogFilter(EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(PUTONTHESHELF_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,callback);
    }

    public List<RecordTheResultEventResponse> getRecordTheResultEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(RECORDTHERESULT_EVENT, transactionReceipt);
        ArrayList<RecordTheResultEventResponse> responses = new ArrayList<RecordTheResultEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            RecordTheResultEventResponse typedResponse = new RecordTheResultEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.userId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.result = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.imgName = (String) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void registerrecordTheResultEventLogFilter(String fromBlock, String toBlock, List<String> otherTopcs, EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(RECORDTHERESULT_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,fromBlock,toBlock,otherTopcs,callback);
    }

    public void registerrecordTheResultEventLogFilter(EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(RECORDTHERESULT_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,callback);
    }

    public List<RecordTheEvidenceEventResponse> getRecordTheEvidenceEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(RECORDTHEEVIDENCE_EVENT, transactionReceipt);
        ArrayList<RecordTheEvidenceEventResponse> responses = new ArrayList<RecordTheEvidenceEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            RecordTheEvidenceEventResponse typedResponse = new RecordTheEvidenceEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.userPhone = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.tag = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.imgName = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.key = (String) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void registerrecordTheEvidenceEventLogFilter(String fromBlock, String toBlock, List<String> otherTopcs, EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(RECORDTHEEVIDENCE_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,fromBlock,toBlock,otherTopcs,callback);
    }

    public void registerrecordTheEvidenceEventLogFilter(EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(RECORDTHEEVIDENCE_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,callback);
    }

    @Deprecated
    public static PointController load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new PointController(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static PointController load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new PointController(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static PointController load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new PointController(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static PointController load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new PointController(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<PointController> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(PointController.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<PointController> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(PointController.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<PointController> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(PointController.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<PointController> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(PointController.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class RegisterIdEventResponse {
        public Log log;

        public BigInteger userId;

        public BigInteger value;
    }

    public static class PutOnTheShelfEventResponse {
        public Log log;

        public String tag;

        public String hash;
    }

    public static class RecordTheResultEventResponse {
        public Log log;

        public BigInteger userId;

        public String result;

        public String imgName;
    }

    public static class RecordTheEvidenceEventResponse {
        public Log log;

        public BigInteger userPhone;

        public BigInteger tag;

        public String imgName;

        public String key;
    }
}
