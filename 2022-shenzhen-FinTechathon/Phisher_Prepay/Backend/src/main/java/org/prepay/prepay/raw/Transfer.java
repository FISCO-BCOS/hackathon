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
import org.fisco.bcos.sdk.abi.datatypes.Event;
import org.fisco.bcos.sdk.abi.datatypes.Function;
import org.fisco.bcos.sdk.abi.datatypes.Type;
import org.fisco.bcos.sdk.abi.datatypes.Utf8String;
import org.fisco.bcos.sdk.abi.datatypes.generated.Int256;
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
public class Transfer extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b50604051608080610b09833981018060405261002f919081019061014f565b836000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555082600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555081600260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555080600360006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550505050506101d2565b600061014782516101b2565b905092915050565b6000806000806080858703121561016557600080fd5b60006101738782880161013b565b94505060206101848782880161013b565b93505060406101958782880161013b565b92505060606101a68782880161013b565b91505092959194509250565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b610928806101e16000396000f30060806040526004361061006d576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806309090a511461007257806327895bb91461009d578063364dab10146100c6578063693bd2d0146100f1578063d2f05b3f1461011c575b600080fd5b34801561007e57600080fd5b50610087610147565b6040516100949190610727565b60405180910390f35b3480156100a957600080fd5b506100c460048036036100bf91908101906105fc565b61016d565b005b3480156100d257600080fd5b506100db6104e4565b6040516100e89190610727565b60405180910390f35b3480156100fd57600080fd5b5061010661050a565b6040516101139190610727565b60405180910390f35b34801561012857600080fd5b5061013161052f565b60405161013e9190610727565b60405180910390f35b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16632fa91cd087846040518363ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004016101e7929190610742565b602060405180830381600087803b15801561020157600080fd5b505af1158015610215573d6000803e3d6000fd5b505050506040513d601f19601f8201168201806040525061023991908101906105d3565b50600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16631864e31786846040518363ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004016102b3929190610742565b602060405180830381600087803b1580156102cd57600080fd5b505af11580156102e1573d6000803e3d6000fd5b505050506040513d601f19601f8201168201806040525061030591908101906105d3565b9050600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16632fa91cd086836040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401610380929190610742565b602060405180830381600087803b15801561039a57600080fd5b505af11580156103ae573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052506103d291908101906105d3565b507fb356e94ac4cc238af606cb19bd9bb7bc3c9ba63b812b5bbf842db556dcfd157986868460405161040693929190610772565b60405180910390a1600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166339b314c48588866040518463ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401610489939291906107b7565b602060405180830381600087803b1580156104a357600080fd5b505af11580156104b7573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052506104db91908101906105d3565b50505050505050565b600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60006105618235610891565b905092915050565b60006105758251610891565b905092915050565b600082601f830112151561059057600080fd5b81356105a361059e82610830565b610803565b915080825260208301602083018583830111156105bf57600080fd5b6105ca83828461089b565b50505092915050565b6000602082840312156105e557600080fd5b60006105f384828501610569565b91505092915050565b600080600080600060a0868803121561061457600080fd5b600086013567ffffffffffffffff81111561062e57600080fd5b61063a8882890161057d565b955050602086013567ffffffffffffffff81111561065757600080fd5b6106638882890161057d565b945050604086013567ffffffffffffffff81111561068057600080fd5b61068c8882890161057d565b935050606086013567ffffffffffffffff8111156106a957600080fd5b6106b58882890161057d565b92505060806106c688828901610555565b9150509295509295909350565b6106dc81610867565b82525050565b6106eb81610887565b82525050565b60006106fc8261085c565b8084526107108160208601602086016108aa565b610719816108dd565b602085010191505092915050565b600060208201905061073c60008301846106d3565b92915050565b6000604082019050818103600083015261075c81856106f1565b905061076b60208301846106e2565b9392505050565b6000606082019050818103600083015261078c81866106f1565b905081810360208301526107a081856106f1565b90506107af60408301846106e2565b949350505050565b600060608201905081810360008301526107d181866106f1565b905081810360208301526107e581856106f1565b905081810360408301526107f981846106f1565b9050949350505050565b6000604051905081810181811067ffffffffffffffff8211171561082657600080fd5b8060405250919050565b600067ffffffffffffffff82111561084757600080fd5b601f19601f8301169050602081019050919050565b600081519050919050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b6000819050919050565b6000819050919050565b82818337600083830152505050565b60005b838110156108c85780820151818401526020810190506108ad565b838111156108d7576000848401525b50505050565b6000601f19601f83011690509190505600a265627a7a72305820e3dc33286a0618ceb8ce0aa03e2c1aca8055f2128d001f19f9d7f541f12a0f576c6578706572696d656e74616cf50037"};

    public static final String BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {};

    public static final String SM_BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"constant\":true,\"inputs\":[],\"name\":\"shopContract\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_cardID\",\"type\":\"string\"},{\"name\":\"_shopID\",\"type\":\"string\"},{\"name\":\"_serviceID\",\"type\":\"string\"},{\"name\":\"_record\",\"type\":\"string\"},{\"name\":\"_value\",\"type\":\"int256\"}],\"name\":\"transfer\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"serviceContract\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"cardContract\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"leverageContract\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"name\":\"_cardContract\",\"type\":\"address\"},{\"name\":\"_shopContract\",\"type\":\"address\"},{\"name\":\"_leverageContract\",\"type\":\"address\"},{\"name\":\"_serviceContract\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"cardID\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"shopID\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"Value\",\"type\":\"int256\"}],\"name\":\"TransferLog\",\"type\":\"event\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_SHOPCONTRACT = "shopContract";

    public static final String FUNC_TRANSFER = "transfer";

    public static final String FUNC_SERVICECONTRACT = "serviceContract";

    public static final String FUNC_CARDCONTRACT = "cardContract";

    public static final String FUNC_LEVERAGECONTRACT = "leverageContract";

    public static final Event TRANSFERLOG_EVENT = new Event("TransferLog", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Int256>() {}));
    ;

    protected Transfer(String contractAddress, Client client, CryptoKeyPair credential) {
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

    public TransactionReceipt transfer(String _cardID, String _shopID, String _serviceID, String _record, BigInteger _value) {
        final Function function = new Function(
                FUNC_TRANSFER, 
                Arrays.<Type>asList(new Utf8String(_cardID),
                new Utf8String(_shopID),
                new Utf8String(_serviceID),
                new Utf8String(_record),
                new Int256(_value)),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void transfer(String _cardID, String _shopID, String _serviceID, String _record, BigInteger _value, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_TRANSFER, 
                Arrays.<Type>asList(new Utf8String(_cardID),
                new Utf8String(_shopID),
                new Utf8String(_serviceID),
                new Utf8String(_record),
                new Int256(_value)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForTransfer(String _cardID, String _shopID, String _serviceID, String _record, BigInteger _value) {
        final Function function = new Function(
                FUNC_TRANSFER, 
                Arrays.<Type>asList(new Utf8String(_cardID),
                new Utf8String(_shopID),
                new Utf8String(_serviceID),
                new Utf8String(_record),
                new Int256(_value)),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple5<String, String, String, String, BigInteger> getTransferInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_TRANSFER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Int256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple5<String, String, String, String, BigInteger>(

                (String) results.get(0).getValue(), 
                (String) results.get(1).getValue(), 
                (String) results.get(2).getValue(), 
                (String) results.get(3).getValue(), 
                (BigInteger) results.get(4).getValue()
                );
    }

    public String serviceContract() throws ContractException {
        final Function function = new Function(FUNC_SERVICECONTRACT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeCallWithSingleValueReturn(function, String.class);
    }

    public String cardContract() throws ContractException {
        final Function function = new Function(FUNC_CARDCONTRACT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeCallWithSingleValueReturn(function, String.class);
    }

    public String leverageContract() throws ContractException {
        final Function function = new Function(FUNC_LEVERAGECONTRACT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeCallWithSingleValueReturn(function, String.class);
    }

    public List<TransferLogEventResponse> getTransferLogEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFERLOG_EVENT, transactionReceipt);
        ArrayList<TransferLogEventResponse> responses = new ArrayList<TransferLogEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            TransferLogEventResponse typedResponse = new TransferLogEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.cardID = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.shopID = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.Value = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeTransferLogEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(TRANSFERLOG_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeTransferLogEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(TRANSFERLOG_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public static Transfer load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new Transfer(contractAddress, client, credential);
    }

    public static Transfer deploy(Client client, CryptoKeyPair credential, String _cardContract, String _shopContract, String _leverageContract, String _serviceContract) throws ContractException {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(_cardContract),
                new Address(_shopContract),
                new Address(_leverageContract),
                new Address(_serviceContract)));
        return deploy(Transfer.class, client, credential, getBinary(client.getCryptoSuite()), encodedConstructor);
    }

    public static class TransferLogEventResponse {
        public TransactionReceipt.Logs log;

        public String cardID;

        public String shopID;

        public BigInteger Value;
    }
}
