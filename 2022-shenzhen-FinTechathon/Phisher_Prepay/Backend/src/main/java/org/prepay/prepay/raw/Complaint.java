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
import org.fisco.bcos.sdk.abi.datatypes.generated.Uint256;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple4;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.contract.Contract;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.CryptoType;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.model.callback.TransactionCallback;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class Complaint extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b506040516040806107ac833981018060405261002f91908101906100cb565b816000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555080600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505050610127565b60006100c38251610107565b905092915050565b600080604083850312156100de57600080fd5b60006100ec858286016100b7565b92505060206100fd858286016100b7565b9150509250929050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b610676806101366000396000f300608060405260043610610057576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168063116b1ebd1461005c578063364dab1014610085578063e2080b56146100b0575b600080fd5b34801561006857600080fd5b50610083600480360361007e91908101906103ba565b6100db565b005b34801561009157600080fd5b5061009a610278565b6040516100a791906104b9565b60405180910390f35b3480156100bc57600080fd5b506100c561029d565b6040516100d291906104b9565b60405180910390f35b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16634bec79ce85856040518363ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004016101539291906104d4565b602060405180830381600087803b15801561016d57600080fd5b505af1158015610181573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052506101a59190810190610391565b50600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663edd9241683836040518363ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040161021f92919061050b565b6040805180830381600087803b15801561023857600080fd5b505af115801561024c573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052506102709190810190610355565b505050505050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60006102cf82516105c9565b905092915050565b60006102e382516105d5565b905092915050565b600082601f83011215156102fe57600080fd5b813561031161030c82610568565b61053b565b9150808252602083016020830185838301111561032d57600080fd5b6103388382846105e9565b50505092915050565b600061034d82356105df565b905092915050565b6000806040838503121561036857600080fd5b6000610376858286016102c3565b9250506020610387858286016102d7565b9150509250929050565b6000602082840312156103a357600080fd5b60006103b1848285016102d7565b91505092915050565b600080600080608085870312156103d057600080fd5b600085013567ffffffffffffffff8111156103ea57600080fd5b6103f6878288016102eb565b945050602085013567ffffffffffffffff81111561041357600080fd5b61041f878288016102eb565b935050604085013567ffffffffffffffff81111561043c57600080fd5b610448878288016102eb565b925050606061045987828801610341565b91505092959194509250565b61046e8161059f565b82525050565b600061047f82610594565b8084526104938160208601602086016105f8565b61049c8161062b565b602085010191505092915050565b6104b3816105bf565b82525050565b60006020820190506104ce6000830184610465565b92915050565b600060408201905081810360008301526104ee8185610474565b905081810360208301526105028184610474565b90509392505050565b600060408201905081810360008301526105258185610474565b905061053460208301846104aa565b9392505050565b6000604051905081810181811067ffffffffffffffff8211171561055e57600080fd5b8060405250919050565b600067ffffffffffffffff82111561057f57600080fd5b601f19601f8301169050602081019050919050565b600081519050919050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b6000819050919050565b60008115159050919050565b6000819050919050565b6000819050919050565b82818337600083830152505050565b60005b838110156106165780820151818401526020810190506105fb565b83811115610625576000848401525b50505050565b6000601f19601f83011690509190505600a265627a7a7230582083737628b6400b096c99ede263302b0e57c83fc2dd4f9e6c88a1cbd97bf6ef186c6578706572696d656e74616cf50037"};

    public static final String BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {};

    public static final String SM_BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"constant\":false,\"inputs\":[{\"name\":\"_serviceID\",\"type\":\"string\"},{\"name\":\"_content\",\"type\":\"string\"},{\"name\":\"_cardID\",\"type\":\"string\"},{\"name\":\"_now\",\"type\":\"uint256\"}],\"name\":\"complaint\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"serviceContract\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"refundContract\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"name\":\"_serviceContract\",\"type\":\"address\"},{\"name\":\"_refundContract\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_COMPLAINT = "complaint";

    public static final String FUNC_SERVICECONTRACT = "serviceContract";

    public static final String FUNC_REFUNDCONTRACT = "refundContract";

    protected Complaint(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public TransactionReceipt complaint(String _serviceID, String _content, String _cardID, BigInteger _now) {
        final Function function = new Function(
                FUNC_COMPLAINT, 
                Arrays.<Type>asList(new Utf8String(_serviceID),
                new Utf8String(_content),
                new Utf8String(_cardID),
                new Uint256(_now)),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void complaint(String _serviceID, String _content, String _cardID, BigInteger _now, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_COMPLAINT, 
                Arrays.<Type>asList(new Utf8String(_serviceID),
                new Utf8String(_content),
                new Utf8String(_cardID),
                new Uint256(_now)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForComplaint(String _serviceID, String _content, String _cardID, BigInteger _now) {
        final Function function = new Function(
                FUNC_COMPLAINT, 
                Arrays.<Type>asList(new Utf8String(_serviceID),
                new Utf8String(_content),
                new Utf8String(_cardID),
                new Uint256(_now)),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple4<String, String, String, BigInteger> getComplaintInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_COMPLAINT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple4<String, String, String, BigInteger>(

                (String) results.get(0).getValue(), 
                (String) results.get(1).getValue(), 
                (String) results.get(2).getValue(), 
                (BigInteger) results.get(3).getValue()
                );
    }

    public String serviceContract() throws ContractException {
        final Function function = new Function(FUNC_SERVICECONTRACT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeCallWithSingleValueReturn(function, String.class);
    }

    public String refundContract() throws ContractException {
        final Function function = new Function(FUNC_REFUNDCONTRACT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeCallWithSingleValueReturn(function, String.class);
    }

    public static Complaint load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new Complaint(contractAddress, client, credential);
    }

    public static Complaint deploy(Client client, CryptoKeyPair credential, String _serviceContract, String _refundContract) throws ContractException {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(_serviceContract),
                new Address(_refundContract)));
        return deploy(Complaint.class, client, credential, getBinary(client.getCryptoSuite()), encodedConstructor);
    }
}
