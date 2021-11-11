import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.abi.FunctionReturnDecoder;
import org.fisco.bcos.sdk.abi.TypeReference;
import org.fisco.bcos.sdk.abi.datatypes.DynamicArray;
import org.fisco.bcos.sdk.abi.datatypes.DynamicBytes;
import org.fisco.bcos.sdk.abi.datatypes.Event;
import org.fisco.bcos.sdk.abi.datatypes.Function;
import org.fisco.bcos.sdk.abi.datatypes.Type;
import org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32;
import org.fisco.bcos.sdk.abi.datatypes.generated.Uint256;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple1;
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
public class DAG extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b50610512806100206000396000f300608060405260043610610062576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168063602bf460146100675780638f7fa294146100a4578063a174ce59146100e1578063dffc093e1461011e575b600080fd5b34801561007357600080fd5b5061008e60048036036100899190810190610303565b61015b565b60405161009b9190610433565b60405180910390f35b3480156100b057600080fd5b506100cb60048036036100c69190810190610303565b6101cd565b6040516100d89190610433565b60405180910390f35b3480156100ed57600080fd5b5061010860048036036101039190810190610355565b610240565b6040516101159190610470565b60405180910390f35b34801561012a57600080fd5b506101456004803603610140919081019061037e565b610263565b6040516101529190610455565b60405180910390f35b606060008083600019166000191681526020019081526020016000208054806020026020016040519081016040528092919081815260200182805480156101c157602002820191906000526020600020905b8154815260200190600101908083116101ad575b50505050509050919050565b606060016000836000191660001916815260200190815260200160002080548060200260200160405190810160405280929190818152602001828054801561023457602002820191906000526020600020905b815481526020019060010190808311610220575b50505050509050919050565b600060058281548110151561025157fe5b90600052602060002001549050919050565b60006002838360405180838152602001828152602001925050506020604051808303816000865af115801561029c573d6000803e3d6000fd5b5050506040513d601f19601f820116820180604052506102bf919081019061032c565b905092915050565b60006102d382356104c4565b905092915050565b60006102e782516104c4565b905092915050565b60006102fb82356104ce565b905092915050565b60006020828403121561031557600080fd5b6000610323848285016102c7565b91505092915050565b60006020828403121561033e57600080fd5b600061034c848285016102db565b91505092915050565b60006020828403121561036757600080fd5b6000610375848285016102ef565b91505092915050565b6000806040838503121561039157600080fd5b600061039f858286016102ef565b92505060206103b0858286016102ef565b9150509250929050565b60006103c582610498565b8084526020840193506103d78361048b565b60005b82811015610409576103ed868351610424565b6103f6826104a3565b91506020860195506001810190506103da565b50849250505092915050565b61041e816104b0565b82525050565b61042d816104ba565b82525050565b6000602082019050818103600083015261044d81846103ba565b905092915050565b600060208201905061046a6000830184610415565b92915050565b60006020820190506104856000830184610424565b92915050565b6000602082019050919050565b600081519050919050565b6000602082019050919050565b6000819050919050565b6000819050919050565b6000819050919050565b60008190509190505600a265627a7a723058204ad12d72f08d9ea06dfe5a9d2969151421426572a330f7c96f41a55f1b985ba16c6578706572696d656e74616cf50037"};

    public static final String BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {};

    public static final String SM_BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"constant\":true,\"inputs\":[{\"name\":\"commitHash\",\"type\":\"bytes32\"}],\"name\":\"getparents\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256[]\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"commitHash\",\"type\":\"bytes32\"}],\"name\":\"getchildren\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256[]\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\"}],\"name\":\"getblocknumber\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\"},{\"name\":\"number\",\"type\":\"uint256\"}],\"name\":\"getCommitHash\",\"outputs\":[{\"name\":\"\",\"type\":\"bytes32\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"Id\",\"type\":\"uint256\"},{\"indexed\":false,\"name\":\"commitMessage\",\"type\":\"bytes\"}],\"name\":\"Commit\",\"type\":\"event\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_GETPARENTS = "getparents";

    public static final String FUNC_GETCHILDREN = "getchildren";

    public static final String FUNC_GETBLOCKNUMBER = "getblocknumber";

    public static final String FUNC_GETCOMMITHASH = "getCommitHash";

    public static final Event COMMIT_EVENT = new Event("Commit", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<DynamicBytes>() {}));
    ;

    protected DAG(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public List getparents(byte[] commitHash) throws ContractException {
        final Function function = new Function(FUNC_GETPARENTS, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32(commitHash)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Uint256>>() {}));
        List<Type> result = (List<Type>) executeCallWithSingleValueReturn(function, List.class);
        return convertToNative(result);
    }

    public List getchildren(byte[] commitHash) throws ContractException {
        final Function function = new Function(FUNC_GETCHILDREN, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32(commitHash)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Uint256>>() {}));
        List<Type> result = (List<Type>) executeCallWithSingleValueReturn(function, List.class);
        return convertToNative(result);
    }

    public BigInteger getblocknumber(BigInteger id) throws ContractException {
        final Function function = new Function(FUNC_GETBLOCKNUMBER, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(id)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public TransactionReceipt getCommitHash(BigInteger id, BigInteger number) {
        final Function function = new Function(
                FUNC_GETCOMMITHASH, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(id), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(number)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void getCommitHash(BigInteger id, BigInteger number, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_GETCOMMITHASH, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(id), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(number)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForGetCommitHash(BigInteger id, BigInteger number) {
        final Function function = new Function(
                FUNC_GETCOMMITHASH, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(id), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(number)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple2<BigInteger, BigInteger> getGetCommitHashInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_GETCOMMITHASH, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple2<BigInteger, BigInteger>(

                (BigInteger) results.get(0).getValue(), 
                (BigInteger) results.get(1).getValue()
                );
    }

    public Tuple1<byte[]> getGetCommitHashOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_GETCOMMITHASH, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<byte[]>(

                (byte[]) results.get(0).getValue()
                );
    }

    public List<CommitEventResponse> getCommitEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(COMMIT_EVENT, transactionReceipt);
        ArrayList<CommitEventResponse> responses = new ArrayList<CommitEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            CommitEventResponse typedResponse = new CommitEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.Id = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.commitMessage = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeCommitEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(COMMIT_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeCommitEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(COMMIT_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public static DAG load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new DAG(contractAddress, client, credential);
    }

    public static DAG deploy(Client client, CryptoKeyPair credential) throws ContractException {
        return deploy(DAG.class, client, credential, getBinary(client.getCryptoSuite()), "");
    }

    public static class CommitEventResponse {
        public TransactionReceipt.Logs log;

        public BigInteger Id;

        public byte[] commitMessage;
    }
}
