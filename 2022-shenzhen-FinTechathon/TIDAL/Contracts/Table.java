package org.fisco.bcos.upload.contract;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.abi.FunctionReturnDecoder;
import org.fisco.bcos.sdk.abi.TypeReference;
import org.fisco.bcos.sdk.abi.datatypes.Address;
import org.fisco.bcos.sdk.abi.datatypes.Function;
import org.fisco.bcos.sdk.abi.datatypes.Type;
import org.fisco.bcos.sdk.abi.datatypes.Utf8String;
import org.fisco.bcos.sdk.abi.datatypes.generated.Int256;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple2;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple3;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.contract.Contract;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.CryptoType;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.model.callback.TransactionCallback;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class Table extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b50610442806100206000396000f300608060405260043610610078576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806313db93461461007d57806328bb2117146100d457806331afac36146101715780637857d7c91461020e578063bf2b70a114610265578063e8434e3914610322575b600080fd5b34801561008957600080fd5b506100926103eb565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b3480156100e057600080fd5b5061015b600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506103f0565b6040518082815260200191505060405180910390f35b34801561017d57600080fd5b506101f8600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506103f8565b6040518082815260200191505060405180910390f35b34801561021a57600080fd5b50610223610400565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561027157600080fd5b5061030c600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610405565b6040518082815260200191505060405180910390f35b34801561032e57600080fd5b506103a9600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061040e565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b600090565b600092915050565b600092915050565b600090565b60009392505050565b6000929150505600a165627a7a72305820616a5c0eafb618cac458bc9639ec46165eba3cd705964d82b3bca3461755fd6f0029"};

    public static final String BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"608060405234801561001057600080fd5b50610442806100206000396000f300608060405260043610610078576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806309ff42f01461007d5780634c6f30c01461011a5780635887ab24146101b7578063664b37d61461020e578063c74f8caf146102cb578063d8ac595714610322575b600080fd5b34801561008957600080fd5b50610104600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506103eb565b6040518082815260200191505060405180910390f35b34801561012657600080fd5b506101a1600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506103f3565b6040518082815260200191505060405180910390f35b3480156101c357600080fd5b506101cc6103fb565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561021a57600080fd5b506102b5600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610400565b6040518082815260200191505060405180910390f35b3480156102d757600080fd5b506102e0610409565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561032e57600080fd5b506103a9600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061040e565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b600092915050565b600092915050565b600090565b60009392505050565b600090565b6000929150505600a165627a7a7230582053e3a8a43b002ca9ba0f15cdacaeacd6d5d6532d87d986c568cc4ab6466e7fb50029"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"constant\":true,\"inputs\":[],\"name\":\"newEntry\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"\",\"type\":\"string\"},{\"name\":\"\",\"type\":\"address\"}],\"name\":\"remove\",\"outputs\":[{\"name\":\"\",\"type\":\"int256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"\",\"type\":\"string\"},{\"name\":\"\",\"type\":\"address\"}],\"name\":\"insert\",\"outputs\":[{\"name\":\"\",\"type\":\"int256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"newCondition\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"\",\"type\":\"string\"},{\"name\":\"\",\"type\":\"address\"},{\"name\":\"\",\"type\":\"address\"}],\"name\":\"update\",\"outputs\":[{\"name\":\"\",\"type\":\"int256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"\",\"type\":\"string\"},{\"name\":\"\",\"type\":\"address\"}],\"name\":\"select\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_NEWENTRY = "newEntry";

    public static final String FUNC_REMOVE = "remove";

    public static final String FUNC_INSERT = "insert";

    public static final String FUNC_NEWCONDITION = "newCondition";

    public static final String FUNC_UPDATE = "update";

    public static final String FUNC_SELECT = "select";

    protected Table(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public String newEntry() throws ContractException {
        final Function function = new Function(FUNC_NEWENTRY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeCallWithSingleValueReturn(function, String.class);
    }

    public TransactionReceipt remove(String param0, String param1) {
        final Function function = new Function(
                FUNC_REMOVE, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(param0), 
                new org.fisco.bcos.sdk.abi.datatypes.Address(param1)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] remove(String param0, String param1, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_REMOVE, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(param0), 
                new org.fisco.bcos.sdk.abi.datatypes.Address(param1)), 
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForRemove(String param0, String param1) {
        final Function function = new Function(
                FUNC_REMOVE, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(param0), 
                new org.fisco.bcos.sdk.abi.datatypes.Address(param1)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple2<String, String> getRemoveInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_REMOVE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple2<String, String>(

                (String) results.get(0).getValue(), 
                (String) results.get(1).getValue()
                );
    }

    public Tuple1<BigInteger> getRemoveOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_REMOVE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Int256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<BigInteger>(

                (BigInteger) results.get(0).getValue()
                );
    }

    public TransactionReceipt insert(String param0, String param1) {
        final Function function = new Function(
                FUNC_INSERT, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(param0), 
                new org.fisco.bcos.sdk.abi.datatypes.Address(param1)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] insert(String param0, String param1, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_INSERT, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(param0), 
                new org.fisco.bcos.sdk.abi.datatypes.Address(param1)), 
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForInsert(String param0, String param1) {
        final Function function = new Function(
                FUNC_INSERT, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(param0), 
                new org.fisco.bcos.sdk.abi.datatypes.Address(param1)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple2<String, String> getInsertInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_INSERT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple2<String, String>(

                (String) results.get(0).getValue(), 
                (String) results.get(1).getValue()
                );
    }

    public Tuple1<BigInteger> getInsertOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_INSERT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Int256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<BigInteger>(

                (BigInteger) results.get(0).getValue()
                );
    }

    public String newCondition() throws ContractException {
        final Function function = new Function(FUNC_NEWCONDITION, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeCallWithSingleValueReturn(function, String.class);
    }

    public TransactionReceipt update(String param0, String param1, String param2) {
        final Function function = new Function(
                FUNC_UPDATE, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(param0), 
                new org.fisco.bcos.sdk.abi.datatypes.Address(param1), 
                new org.fisco.bcos.sdk.abi.datatypes.Address(param2)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] update(String param0, String param1, String param2, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_UPDATE, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(param0), 
                new org.fisco.bcos.sdk.abi.datatypes.Address(param1), 
                new org.fisco.bcos.sdk.abi.datatypes.Address(param2)), 
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForUpdate(String param0, String param1, String param2) {
        final Function function = new Function(
                FUNC_UPDATE, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(param0), 
                new org.fisco.bcos.sdk.abi.datatypes.Address(param1), 
                new org.fisco.bcos.sdk.abi.datatypes.Address(param2)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple3<String, String, String> getUpdateInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_UPDATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple3<String, String, String>(

                (String) results.get(0).getValue(), 
                (String) results.get(1).getValue(), 
                (String) results.get(2).getValue()
                );
    }

    public Tuple1<BigInteger> getUpdateOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_UPDATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Int256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<BigInteger>(

                (BigInteger) results.get(0).getValue()
                );
    }

    public String select(String param0, String param1) throws ContractException {
        final Function function = new Function(FUNC_SELECT, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(param0), 
                new org.fisco.bcos.sdk.abi.datatypes.Address(param1)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeCallWithSingleValueReturn(function, String.class);
    }

    public static Table load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new Table(contractAddress, client, credential);
    }

    public static Table deploy(Client client, CryptoKeyPair credential) throws ContractException {
        return deploy(Table.class, client, credential, getBinary(client.getCryptoSuite()), "");
    }
}
