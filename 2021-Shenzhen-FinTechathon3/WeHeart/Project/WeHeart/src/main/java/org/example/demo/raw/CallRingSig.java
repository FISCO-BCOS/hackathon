import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.abi.FunctionReturnDecoder;
import org.fisco.bcos.sdk.abi.TypeReference;
import org.fisco.bcos.sdk.abi.datatypes.Bool;
import org.fisco.bcos.sdk.abi.datatypes.Function;
import org.fisco.bcos.sdk.abi.datatypes.Type;
import org.fisco.bcos.sdk.abi.datatypes.Utf8String;
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
public class CallRingSig extends Contract {
    public static final String[] BINARY_ARRAY = {"60806040526000600660006101000a81548160ff02191690831515021790555034801561002b57600080fd5b506150056000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550610aba8061007d6000396000f300608060405260043610610083576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680630c9ce52f146100885780630d03b8be146100b45780632e982602146100dd57806332af2edb14610106578063392f6ccc146101315780634090ff6d1461015a57806373da567d14610197575b600080fd5b34801561009457600080fd5b5061009d6101c0565b6040516100ab92919061090e565b60405180910390f35b3480156100c057600080fd5b506100db60048036036100d69190810190610752565b61035f565b005b3480156100e957600080fd5b5061010460048036036100ff9190810190610711565b610391565b005b34801561011257600080fd5b5061011b6103ab565b60405161012891906108ec565b60405180910390f35b34801561013d57600080fd5b5061015860048036036101539190810190610711565b6104a5565b005b34801561016657600080fd5b50610181600480360361017c91908101906107be565b6104bf565b60405161018e91906108d1565b60405180910390f35b3480156101a357600080fd5b506101be60048036036101b99190810190610711565b6105bf565b005b60608060011515600660009054906101000a900460ff16151514151561021b576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161021290610991565b60405180910390fd5b60026003818054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156102b45780601f10610289576101008083540402835291602001916102b4565b820191906000526020600020905b81548152906001019060200180831161029757829003601f168201915b50505050509150808054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103505780601f1061032557610100808354040283529160200191610350565b820191906000526020600020905b81548152906001019060200180831161033357829003601f168201915b50505050509050915091509091565b81600290805190602001906103759291906105d9565b50806003908051906020019061038c9291906105d9565b505050565b80600590805190602001906103a79291906105d9565b5050565b606060011515600660009054906101000a900460ff161515141515610405576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016103fc90610991565b60405180910390fd5b60058054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561049b5780601f106104705761010080835404028352916020019161049b565b820191906000526020600020905b81548152906001019060200180831161047e57829003601f168201915b5050505050905090565b80600190805190602001906104bb9291906105d9565b5050565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16634090ff6d8585856040518463ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040161053b93929190610945565b602060405180830381600087803b15801561055557600080fd5b505af1158015610569573d6000803e3d6000fd5b505050506040513d601f19601f8201168201806040525061058d91908101906106e8565b600660006101000a81548160ff021916908315150217905550600660009054906101000a900460ff1690509392505050565b80600490805190602001906105d59291906105d9565b5050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061061a57805160ff1916838001178555610648565b82800160010185558215610648579182015b8281111561064757825182559160200191906001019061062c565b5b5090506106559190610659565b5090565b61067b91905b8082111561067757600081600090555060010161065f565b5090565b90565b600061068a8251610a21565b905092915050565b600082601f83011215156106a557600080fd5b81356106b86106b3826109de565b6109b1565b915080825260208301602083018583830111156106d457600080fd5b6106df838284610a2d565b50505092915050565b6000602082840312156106fa57600080fd5b60006107088482850161067e565b91505092915050565b60006020828403121561072357600080fd5b600082013567ffffffffffffffff81111561073d57600080fd5b61074984828501610692565b91505092915050565b6000806040838503121561076557600080fd5b600083013567ffffffffffffffff81111561077f57600080fd5b61078b85828601610692565b925050602083013567ffffffffffffffff8111156107a857600080fd5b6107b485828601610692565b9150509250929050565b6000806000606084860312156107d357600080fd5b600084013567ffffffffffffffff8111156107ed57600080fd5b6107f986828701610692565b935050602084013567ffffffffffffffff81111561081657600080fd5b61082286828701610692565b925050604084013567ffffffffffffffff81111561083f57600080fd5b61084b86828701610692565b9150509250925092565b61085e81610a15565b82525050565b600061086f82610a0a565b808452610883816020860160208601610a3c565b61088c81610a6f565b602085010191505092915050565b6000601682527f494e56414c49442052494e475349474e415455524521000000000000000000006020830152604082019050919050565b60006020820190506108e66000830184610855565b92915050565b600060208201905081810360008301526109068184610864565b905092915050565b600060408201905081810360008301526109288185610864565b9050818103602083015261093c8184610864565b90509392505050565b6000606082019050818103600083015261095f8186610864565b905081810360208301526109738185610864565b905081810360408301526109878184610864565b9050949350505050565b600060208201905081810360008301526109aa8161089a565b9050919050565b6000604051905081810181811067ffffffffffffffff821117156109d457600080fd5b8060405250919050565b600067ffffffffffffffff8211156109f557600080fd5b601f19601f8301169050602081019050919050565b600081519050919050565b60008115159050919050565b60008115159050919050565b82818337600083830152505050565b60005b83811015610a5a578082015181840152602081019050610a3f565b83811115610a69576000848401525b50505050565b6000601f19601f83011690509190505600a265627a7a723058203c332823fc5d6205924371e473b11d19bd529867917fffaa8108dbd003a5c3d66c6578706572696d656e74616cf50037"};

    public static final String BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {};

    public static final String SM_BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"constant\":false,\"inputs\":[],\"name\":\"get_target\",\"outputs\":[{\"name\":\"rand_G\",\"type\":\"string\"},{\"name\":\"target_addrG\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"rand_G\",\"type\":\"string\"},{\"name\":\"target_addrG\",\"type\":\"string\"}],\"name\":\"set_target\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"message\",\"type\":\"string\"}],\"name\":\"set_message\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"get_message\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"ring_param_info\",\"type\":\"string\"}],\"name\":\"setup_ring\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"signature\",\"type\":\"string\"},{\"name\":\"message\",\"type\":\"string\"},{\"name\":\"paramInfo\",\"type\":\"string\"}],\"name\":\"ringSigVerify\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"ring_sign\",\"type\":\"string\"}],\"name\":\"ring_sig\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_GET_TARGET = "get_target";

    public static final String FUNC_SET_TARGET = "set_target";

    public static final String FUNC_SET_MESSAGE = "set_message";

    public static final String FUNC_GET_MESSAGE = "get_message";

    public static final String FUNC_SETUP_RING = "setup_ring";

    public static final String FUNC_RINGSIGVERIFY = "ringSigVerify";

    public static final String FUNC_RING_SIG = "ring_sig";

    protected CallRingSig(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public TransactionReceipt get_target() {
        final Function function = new Function(
                FUNC_GET_TARGET, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void get_target(TransactionCallback callback) {
        final Function function = new Function(
                FUNC_GET_TARGET, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForGet_target() {
        final Function function = new Function(
                FUNC_GET_TARGET, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple2<String, String> getGet_targetOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_GET_TARGET, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple2<String, String>(

                (String) results.get(0).getValue(), 
                (String) results.get(1).getValue()
                );
    }

    public TransactionReceipt set_target(String rand_G, String target_addrG) {
        final Function function = new Function(
                FUNC_SET_TARGET, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(rand_G), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(target_addrG)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void set_target(String rand_G, String target_addrG, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_SET_TARGET, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(rand_G), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(target_addrG)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForSet_target(String rand_G, String target_addrG) {
        final Function function = new Function(
                FUNC_SET_TARGET, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(rand_G), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(target_addrG)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple2<String, String> getSet_targetInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_SET_TARGET, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple2<String, String>(

                (String) results.get(0).getValue(), 
                (String) results.get(1).getValue()
                );
    }

    public TransactionReceipt set_message(String message) {
        final Function function = new Function(
                FUNC_SET_MESSAGE, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(message)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void set_message(String message, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_SET_MESSAGE, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(message)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForSet_message(String message) {
        final Function function = new Function(
                FUNC_SET_MESSAGE, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(message)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple1<String> getSet_messageInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_SET_MESSAGE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<String>(

                (String) results.get(0).getValue()
                );
    }

    public TransactionReceipt get_message() {
        final Function function = new Function(
                FUNC_GET_MESSAGE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void get_message(TransactionCallback callback) {
        final Function function = new Function(
                FUNC_GET_MESSAGE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForGet_message() {
        final Function function = new Function(
                FUNC_GET_MESSAGE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple1<String> getGet_messageOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_GET_MESSAGE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<String>(

                (String) results.get(0).getValue()
                );
    }

    public TransactionReceipt setup_ring(String ring_param_info) {
        final Function function = new Function(
                FUNC_SETUP_RING, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(ring_param_info)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void setup_ring(String ring_param_info, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_SETUP_RING, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(ring_param_info)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForSetup_ring(String ring_param_info) {
        final Function function = new Function(
                FUNC_SETUP_RING, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(ring_param_info)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple1<String> getSetup_ringInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_SETUP_RING, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<String>(

                (String) results.get(0).getValue()
                );
    }

    public Boolean ringSigVerify(String signature, String message, String paramInfo) throws ContractException {
        final Function function = new Function(FUNC_RINGSIGVERIFY, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(signature), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(message), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(paramInfo)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeCallWithSingleValueReturn(function, Boolean.class);
    }

    public TransactionReceipt ring_sig(String ring_sign) {
        final Function function = new Function(
                FUNC_RING_SIG, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(ring_sign)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void ring_sig(String ring_sign, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_RING_SIG, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(ring_sign)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForRing_sig(String ring_sign) {
        final Function function = new Function(
                FUNC_RING_SIG, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(ring_sign)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple1<String> getRing_sigInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_RING_SIG, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<String>(

                (String) results.get(0).getValue()
                );
    }

    public static CallRingSig load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new CallRingSig(contractAddress, client, credential);
    }

    public static CallRingSig deploy(Client client, CryptoKeyPair credential) throws ContractException {
        return deploy(CallRingSig.class, client, credential, getBinary(client.getCryptoSuite()), "");
    }
}
