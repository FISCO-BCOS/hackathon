package org.prepay.prepay.raw;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.abi.FunctionEncoder;
import org.fisco.bcos.sdk.abi.FunctionReturnDecoder;
import org.fisco.bcos.sdk.abi.TypeReference;
import org.fisco.bcos.sdk.abi.datatypes.Address;
import org.fisco.bcos.sdk.abi.datatypes.Function;
import org.fisco.bcos.sdk.abi.datatypes.Type;
import org.fisco.bcos.sdk.abi.datatypes.Utf8String;
import org.fisco.bcos.sdk.abi.datatypes.generated.Int256;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple2;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.contract.Contract;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.CryptoType;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.model.callback.TransactionCallback;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class Leverage extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b50604051602080610593833981018060405261002f9190810190610089565b806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550506100d2565b600061008182516100b2565b905092915050565b60006020828403121561009b57600080fd5b60006100a984828501610075565b91505092915050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b6104b2806100e16000396000f30060806040526004361061004c576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806309090a51146100515780631864e3171461007c575b600080fd5b34801561005d57600080fd5b506100666100b9565b6040516100739190610335565b60405180910390f35b34801561008857600080fd5b506100a3600480360361009e919081019061028d565b6100de565b6040516100b09190610350565b60405180910390f35b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60008060008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663c10cab9c866040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401610159919061036b565b6040805180830381600087803b15801561017257600080fd5b505af1158015610186573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052506101aa9190810190610251565b9050809250506127108260640160648602028115156101c557fe5b059050809250505092915050565b60006101df823561041b565b905092915050565b60006101f3825161041b565b905092915050565b600082601f830112151561020e57600080fd5b813561022161021c826103ba565b61038d565b9150808252602083016020830185838301111561023d57600080fd5b610248838284610425565b50505092915050565b6000806040838503121561026457600080fd5b6000610272858286016101e7565b9250506020610283858286016101e7565b9150509250929050565b600080604083850312156102a057600080fd5b600083013567ffffffffffffffff8111156102ba57600080fd5b6102c6858286016101fb565b92505060206102d7858286016101d3565b9150509250929050565b6102ea816103f1565b82525050565b6102f981610411565b82525050565b600061030a826103e6565b80845261031e816020860160208601610434565b61032781610467565b602085010191505092915050565b600060208201905061034a60008301846102e1565b92915050565b600060208201905061036560008301846102f0565b92915050565b6000602082019050818103600083015261038581846102ff565b905092915050565b6000604051905081810181811067ffffffffffffffff821117156103b057600080fd5b8060405250919050565b600067ffffffffffffffff8211156103d157600080fd5b601f19601f8301169050602081019050919050565b600081519050919050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b6000819050919050565b6000819050919050565b82818337600083830152505050565b60005b83811015610452578082015181840152602081019050610437565b83811115610461576000848401525b50505050565b6000601f19601f83011690509190505600a265627a7a7230582091db92d8d9bc7863ea097a30f36b443ab68aa2752095eea81af497f55dcb6c1f6c6578706572696d656e74616cf50037"};

    public static final String BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {};

    public static final String SM_BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"constant\":true,\"inputs\":[],\"name\":\"shopContract\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_shopID\",\"type\":\"string\"},{\"name\":\"_value\",\"type\":\"int256\"}],\"name\":\"Calculate\",\"outputs\":[{\"name\":\"\",\"type\":\"int256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"name\":\"_shopContract\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_SHOPCONTRACT = "shopContract";

    public static final String FUNC_CALCULATE = "Calculate";

    protected Leverage(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public String shopContract() throws ContractException {
        final Function function = new Function(FUNC_SHOPCONTRACT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeCallWithSingleValueReturn(function, String.class);
    }

    public TransactionReceipt Calculate(String _shopID, BigInteger _value) {
        final Function function = new Function(
                FUNC_CALCULATE, 
                Arrays.<Type>asList(new Utf8String(_shopID),
                new Int256(_value)),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void Calculate(String _shopID, BigInteger _value, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_CALCULATE, 
                Arrays.<Type>asList(new Utf8String(_shopID),
                new Int256(_value)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForCalculate(String _shopID, BigInteger _value) {
        final Function function = new Function(
                FUNC_CALCULATE, 
                Arrays.<Type>asList(new Utf8String(_shopID),
                new Int256(_value)),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple2<String, BigInteger> getCalculateInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_CALCULATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Int256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple2<String, BigInteger>(

                (String) results.get(0).getValue(), 
                (BigInteger) results.get(1).getValue()
                );
    }

    public Tuple1<BigInteger> getCalculateOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_CALCULATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Int256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<BigInteger>(

                (BigInteger) results.get(0).getValue()
                );
    }

    public static Leverage load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new Leverage(contractAddress, client, credential);
    }

    public static Leverage deploy(Client client, CryptoKeyPair credential, String _shopContract) throws ContractException {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(_shopContract)));
        return deploy(Leverage.class, client, credential, getBinary(client.getCryptoSuite()), encodedConstructor);
    }
}
