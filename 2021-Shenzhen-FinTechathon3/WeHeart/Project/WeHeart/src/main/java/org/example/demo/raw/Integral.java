import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.abi.FunctionReturnDecoder;
import org.fisco.bcos.sdk.abi.TypeReference;
import org.fisco.bcos.sdk.abi.datatypes.Address;
import org.fisco.bcos.sdk.abi.datatypes.DynamicArray;
import org.fisco.bcos.sdk.abi.datatypes.Event;
import org.fisco.bcos.sdk.abi.datatypes.Function;
import org.fisco.bcos.sdk.abi.datatypes.Type;
import org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32;
import org.fisco.bcos.sdk.abi.datatypes.generated.Uint256;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple2;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple3;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple5;
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
public class Integral extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b50610de5806100206000396000f300608060405260043610610078576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168063507cd30f1461007d5780636185287f146100ba578063b0eef500146100e3578063b1c155e814610124578063c907ac8c14610161578063f8b2cb4f1461019e575b600080fd5b34801561008957600080fd5b506100a4600480360361009f9190810190610af8565b6101db565b6040516100b19190610c6c565b60405180910390f35b3480156100c657600080fd5b506100e160048036036100dc9190810190610aa9565b61044d565b005b3480156100ef57600080fd5b5061010a60048036036101059190810190610b34565b6107f3565b60405161011b959493929190610cb0565b60405180910390f35b34801561013057600080fd5b5061014b60048036036101469190810190610b86565b6108e9565b6040516101589190610c6c565b60405180910390f35b34801561016d57600080fd5b5061018860048036036101839190810190610a80565b61094d565b6040516101959190610c4a565b60405180910390f35b3480156101aa57600080fd5b506101c560048036036101c09190810190610a80565b6109e8565b6040516101d29190610d03565b60405180910390f35b6000806000836000808773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054019150816000808773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000208190555061027042436108e9565b9050600160008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081908060018154018082558091505090600182039060005260206000200160009091929091909150906000191690555060a060405190810160405280826000191681526020018673ffffffffffffffffffffffffffffffffffffffff1681526020018673ffffffffffffffffffffffffffffffffffffffff168152602001428152602001858152506002600083600019166000191681526020019081526020016000206000820151816000019060001916905560208201518160010160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060408201518160020160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060608201518160030155608082015181600401559050507f166f573def0e957094f782e6d6990711c3e199643e20ce9381f19d9d51f97832818660405161043a929190610c87565b60405180910390a1809250505092915050565b6000816000808673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020541015151561049c57600080fd5b816000808673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054036000808673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081905550816000808573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054016000808573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055506105ac42436108e9565b9050600160008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819080600181540180825580915050906001820390600052602060002001600090919290919091509060001916905550600160008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081908060018154018082558091505090600182039060005260206000200160009091929091909150906000191690555060a060405190810160405280826000191681526020018573ffffffffffffffffffffffffffffffffffffffff1681526020018473ffffffffffffffffffffffffffffffffffffffff168152602001428152602001838152506002600083600019166000191681526020019081526020016000206000820151816000019060001916905560208201518160010160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060408201518160020160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060608201518160030155608082015181600401559050507f166f573def0e957094f782e6d6990711c3e199643e20ce9381f19d9d51f9783281856040516107e5929190610c87565b60405180910390a150505050565b60008060008060006002600087600019166000191681526020019081526020016000206000015460026000886000191660001916815260200190815260200160002060010160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1660026000896000191660001916815260200190815260200160002060020160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16600260008a6000191660001916815260200190815260200160002060030154600260008b60001916600019168152602001908152602001600020600401549450945094509450945091939590929450565b60006002838360405180838152602001828152602001925050506020604051808303816000865af1158015610922573d6000803e3d6000fd5b5050506040513d601f19601f820116820180604052506109459190810190610b5d565b905092915050565b6060600160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000208054806020026020016040519081016040528092919081815260200182805480156109dc57602002820191906000526020600020905b815460001916815260200190600101908083116109c4575b50505050509050919050565b60008060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020549050919050565b6000610a3c8235610d77565b905092915050565b6000610a508235610d97565b905092915050565b6000610a648251610d97565b905092915050565b6000610a788235610da1565b905092915050565b600060208284031215610a9257600080fd5b6000610aa084828501610a30565b91505092915050565b600080600060608486031215610abe57600080fd5b6000610acc86828701610a30565b9350506020610add86828701610a30565b9250506040610aee86828701610a6c565b9150509250925092565b60008060408385031215610b0b57600080fd5b6000610b1985828601610a30565b9250506020610b2a85828601610a6c565b9150509250929050565b600060208284031215610b4657600080fd5b6000610b5484828501610a44565b91505092915050565b600060208284031215610b6f57600080fd5b6000610b7d84828501610a58565b91505092915050565b60008060408385031215610b9957600080fd5b6000610ba785828601610a6c565b9250506020610bb885828601610a6c565b9150509250929050565b610bcb81610d43565b82525050565b6000610bdc82610d2b565b808452602084019350610bee83610d1e565b60005b82811015610c2057610c04868351610c2c565b610c0d82610d36565b9150602086019550600181019050610bf1565b50849250505092915050565b610c3581610d63565b82525050565b610c4481610d6d565b82525050565b60006020820190508181036000830152610c648184610bd1565b905092915050565b6000602082019050610c816000830184610c2c565b92915050565b6000604082019050610c9c6000830185610c2c565b610ca96020830184610bc2565b9392505050565b600060a082019050610cc56000830188610c2c565b610cd26020830187610bc2565b610cdf6040830186610bc2565b610cec6060830185610c3b565b610cf96080830184610c3b565b9695505050505050565b6000602082019050610d186000830184610c3b565b92915050565b6000602082019050919050565b600081519050919050565b6000602082019050919050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b6000819050919050565b6000819050919050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b6000819050919050565b60008190509190505600a265627a7a72305820633eef9d4ad6fa628e8d126a8e36b248f0aed932faa52fb46bee52418ac84c416c6578706572696d656e74616cf50037"};

    public static final String BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {};

    public static final String SM_BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"constant\":false,\"inputs\":[{\"name\":\"_user\",\"type\":\"address\"},{\"name\":\"_value\",\"type\":\"uint256\"}],\"name\":\"addPoints\",\"outputs\":[{\"name\":\"\",\"type\":\"bytes32\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_from\",\"type\":\"address\"},{\"name\":\"_to\",\"type\":\"address\"},{\"name\":\"_value\",\"type\":\"uint256\"}],\"name\":\"dealPoints\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"txs_id\",\"type\":\"bytes32\"}],\"name\":\"getTxInfor\",\"outputs\":[{\"name\":\"\",\"type\":\"bytes32\"},{\"name\":\"\",\"type\":\"address\"},{\"name\":\"\",\"type\":\"address\"},{\"name\":\"\",\"type\":\"uint256\"},{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"time\",\"type\":\"uint256\"},{\"name\":\"number\",\"type\":\"uint256\"}],\"name\":\"gettxid\",\"outputs\":[{\"name\":\"\",\"type\":\"bytes32\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"addr\",\"type\":\"address\"}],\"name\":\"getTxs\",\"outputs\":[{\"name\":\"\",\"type\":\"bytes32[]\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_user\",\"type\":\"address\"}],\"name\":\"getBalance\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"txid\",\"type\":\"bytes32\"},{\"indexed\":false,\"name\":\"memberOID\",\"type\":\"address\"}],\"name\":\"Instructor\",\"type\":\"event\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_ADDPOINTS = "addPoints";

    public static final String FUNC_DEALPOINTS = "dealPoints";

    public static final String FUNC_GETTXINFOR = "getTxInfor";

    public static final String FUNC_GETTXID = "gettxid";

    public static final String FUNC_GETTXS = "getTxs";

    public static final String FUNC_GETBALANCE = "getBalance";

    public static final Event INSTRUCTOR_EVENT = new Event("Instructor", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Address>() {}));
    ;

    protected Integral(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public TransactionReceipt addPoints(String _user, BigInteger _value) {
        final Function function = new Function(
                FUNC_ADDPOINTS, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(_user), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void addPoints(String _user, BigInteger _value, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_ADDPOINTS, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(_user), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_value)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForAddPoints(String _user, BigInteger _value) {
        final Function function = new Function(
                FUNC_ADDPOINTS, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(_user), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_value)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple2<String, BigInteger> getAddPointsInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_ADDPOINTS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple2<String, BigInteger>(

                (String) results.get(0).getValue(), 
                (BigInteger) results.get(1).getValue()
                );
    }

    public Tuple1<byte[]> getAddPointsOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_ADDPOINTS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<byte[]>(

                (byte[]) results.get(0).getValue()
                );
    }

    public TransactionReceipt dealPoints(String _from, String _to, BigInteger _value) {
        final Function function = new Function(
                FUNC_DEALPOINTS, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(_from), 
                new org.fisco.bcos.sdk.abi.datatypes.Address(_to), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void dealPoints(String _from, String _to, BigInteger _value, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_DEALPOINTS, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(_from), 
                new org.fisco.bcos.sdk.abi.datatypes.Address(_to), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_value)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForDealPoints(String _from, String _to, BigInteger _value) {
        final Function function = new Function(
                FUNC_DEALPOINTS, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(_from), 
                new org.fisco.bcos.sdk.abi.datatypes.Address(_to), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_value)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple3<String, String, BigInteger> getDealPointsInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_DEALPOINTS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple3<String, String, BigInteger>(

                (String) results.get(0).getValue(), 
                (String) results.get(1).getValue(), 
                (BigInteger) results.get(2).getValue()
                );
    }

    public Tuple5<byte[], String, String, BigInteger, BigInteger> getTxInfor(byte[] txs_id) throws ContractException {
        final Function function = new Function(FUNC_GETTXINFOR, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32(txs_id)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple5<byte[], String, String, BigInteger, BigInteger>(
                (byte[]) results.get(0).getValue(), 
                (String) results.get(1).getValue(), 
                (String) results.get(2).getValue(), 
                (BigInteger) results.get(3).getValue(), 
                (BigInteger) results.get(4).getValue());
    }

    public TransactionReceipt gettxid(BigInteger time, BigInteger number) {
        final Function function = new Function(
                FUNC_GETTXID, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(time), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(number)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void gettxid(BigInteger time, BigInteger number, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_GETTXID, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(time), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(number)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForGettxid(BigInteger time, BigInteger number) {
        final Function function = new Function(
                FUNC_GETTXID, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(time), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(number)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple2<BigInteger, BigInteger> getGettxidInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_GETTXID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple2<BigInteger, BigInteger>(

                (BigInteger) results.get(0).getValue(), 
                (BigInteger) results.get(1).getValue()
                );
    }

    public Tuple1<byte[]> getGettxidOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_GETTXID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<byte[]>(

                (byte[]) results.get(0).getValue()
                );
    }

    public List getTxs(String addr) throws ContractException {
        final Function function = new Function(FUNC_GETTXS, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(addr)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Bytes32>>() {}));
        List<Type> result = (List<Type>) executeCallWithSingleValueReturn(function, List.class);
        return convertToNative(result);
    }

    public TransactionReceipt getBalance(String _user) {
        final Function function = new Function(
                FUNC_GETBALANCE, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(_user)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void getBalance(String _user, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_GETBALANCE, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(_user)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForGetBalance(String _user) {
        final Function function = new Function(
                FUNC_GETBALANCE, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(_user)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple1<String> getGetBalanceInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_GETBALANCE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<String>(

                (String) results.get(0).getValue()
                );
    }

    public Tuple1<BigInteger> getGetBalanceOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_GETBALANCE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<BigInteger>(

                (BigInteger) results.get(0).getValue()
                );
    }

    public List<InstructorEventResponse> getInstructorEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(INSTRUCTOR_EVENT, transactionReceipt);
        ArrayList<InstructorEventResponse> responses = new ArrayList<InstructorEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            InstructorEventResponse typedResponse = new InstructorEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.txid = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.memberOID = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeInstructorEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(INSTRUCTOR_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeInstructorEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(INSTRUCTOR_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public static Integral load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new Integral(contractAddress, client, credential);
    }

    public static Integral deploy(Client client, CryptoKeyPair credential) throws ContractException {
        return deploy(Integral.class, client, credential, getBinary(client.getCryptoSuite()), "");
    }

    public static class InstructorEventResponse {
        public TransactionReceipt.Logs log;

        public byte[] txid;

        public String memberOID;
    }
}
