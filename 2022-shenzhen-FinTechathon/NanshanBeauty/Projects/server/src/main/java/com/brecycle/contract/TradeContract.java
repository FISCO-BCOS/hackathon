package com.brecycle.contract;

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
import org.fisco.bcos.sdk.abi.datatypes.generated.Int16;
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
public class TradeContract extends Contract {
    public static final String[] BINARY_ARRAY = {"60806040523480156200001157600080fd5b506040516200101438038062001014833981018060405262000037919081019062000238565b336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555083600290805190602001906200008f92919062000115565b506001600360006101000a81548161ffff021916908360010b61ffff160217905550826004819055508160058190555062015180810242016007819055507fab7a8a9304245c885855f1bef2fc37dcdeed967745bdd93e4624bb7aa6c0bb2e33600142604051620001039392919062000327565b60405180910390a1505050506200045f565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200015857805160ff191683800117855562000189565b8280016001018555821562000189579182015b82811115620001885782518255916020019190600101906200016b565b5b5090506200019891906200019c565b5090565b620001c191905b80821115620001bd576000816000905550600101620001a3565b5090565b90565b600082601f8301121515620001d857600080fd5b8151620001ef620001e982620003a7565b62000379565b915080825260208301602083018583830111156200020c57600080fd5b6200021983828462000429565b50505092915050565b60006200023082516200040b565b905092915050565b600080600080608085870312156200024f57600080fd5b600085015167ffffffffffffffff8111156200026a57600080fd5b6200027887828801620001c4565b94505060206200028b8782880162000222565b93505060406200029e8782880162000222565b9250506060620002b18782880162000222565b91505092959194509250565b620002c881620003d4565b82525050565b620002d98162000415565b82525050565b6000600682527f63726561746500000000000000000000000000000000000000000000000000006020830152604082019050919050565b620003218162000401565b82525050565b60006080820190506200033e6000830186620002bd565b6200034d6020830185620002ce565b6200035c604083018462000316565b81810360608301526200036f81620002df565b9050949350505050565b6000604051905081810181811067ffffffffffffffff821117156200039d57600080fd5b8060405250919050565b600067ffffffffffffffff821115620003bf57600080fd5b601f19601f8301169050602081019050919050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b60008160010b9050919050565b6000819050919050565b6000819050919050565b60006200042282620003f4565b9050919050565b60005b83811015620004495780820151818401526020810190506200042c565b8381111562000459576000848401525b50505050565b610ba5806200046f6000396000f30060806040526004361061006d576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806342aa264f14610072578063454a2ab3146100af578063553df021146100ec578063bbfe4dc614610118578063bc6d909414610141575b600080fd5b34801561007e57600080fd5b50610099600480360361009491908101906106af565b610158565b6040516100a69190610abf565b60405180910390f35b3480156100bb57600080fd5b506100d660048036036100d191908101906106d8565b610170565b6040516100e39190610a44565b60405180910390f35b3480156100f857600080fd5b5061010161037c565b60405161010f929190610a1b565b60405180910390f35b34801561012457600080fd5b5061013f600480360361013a91908101906106af565b610508565b005b34801561014d57600080fd5b50610156610598565b005b600b6020528060005260406000206000915090505481565b600060045482101515156101b9576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016101b090610a9f565b60405180910390fd5b600a548210151515610200576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016101f790610a5f565b60405180910390fd5b33600960006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555081600a8190555081600b60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081905550600554600a54101515610372576002600360006101000a81548161ffff021916908360010b61ffff160217905550600960009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550600a54600681905550426008819055507fab7a8a9304245c885855f1bef2fc37dcdeed967745bdd93e4624bb7aa6c0bb2e33600242604051610361939291906108f3565b60405180910390a160019050610377565b600090505b919050565b6000806000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610410576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161040790610a7f565b60405180910390fd5b600960009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550600a5490506002600360006101000a81548161ffff021916908360010b61ffff160217905550426008819055507fab7a8a9304245c885855f1bef2fc37dcdeed967745bdd93e4624bb7aa6c0bb2e336002426040516104d493929190610987565b60405180910390a1600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681915091509091565b80600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550426008819055506005546006819055507fab7a8a9304245c885855f1bef2fc37dcdeed967745bdd93e4624bb7aa6c0bb2e3360024260405161058d9392919061093d565b60405180910390a150565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610629576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161062090610a7f565b60405180910390fd5b60038060006101000a81548161ffff021916908360010b61ffff1602179055507fab7a8a9304245c885855f1bef2fc37dcdeed967745bdd93e4624bb7aa6c0bb2e3360034260405161067d939291906109d1565b60405180910390a1565b60006106938235610b1d565b905092915050565b60006106a78235610b3d565b905092915050565b6000602082840312156106c157600080fd5b60006106cf84828501610687565b91505092915050565b6000602082840312156106ea57600080fd5b60006106f88482850161069b565b91505092915050565b61070a81610ada565b82525050565b61071981610afa565b82525050565b61072881610b47565b82525050565b61073781610b59565b82525050565b6000601282527f6d617463682065787065637420707269636500000000000000000000000000006020830152604082019050919050565b6000601f82527f546865726520616c726561647920686173206120686967686572206269642e006020830152604082019050919050565b6000600382527f656e6400000000000000000000000000000000000000000000000000000000006020830152604082019050919050565b6000600a82527f7461726765744465616c000000000000000000000000000000000000000000006020830152604082019050919050565b6000600b82527f4e6f742073656c6c6572210000000000000000000000000000000000000000006020830152604082019050919050565b6000600482527f6465616c000000000000000000000000000000000000000000000000000000006020830152604082019050919050565b6000602482527f7072696365206e65656420746f20686967686572207468616e206c6f7765737460208301527f416d742e000000000000000000000000000000000000000000000000000000006040830152606082019050919050565b6108ed81610b13565b82525050565b60006080820190506109086000830186610701565b610915602083018561071f565b61092260408301846108e4565b81810360608301526109338161073d565b9050949350505050565b60006080820190506109526000830186610701565b61095f602083018561071f565b61096c60408301846108e4565b818103606083015261097d816107e2565b9050949350505050565b600060808201905061099c6000830186610701565b6109a9602083018561071f565b6109b660408301846108e4565b81810360608301526109c781610850565b9050949350505050565b60006080820190506109e66000830186610701565b6109f3602083018561072e565b610a0060408301846108e4565b8181036060830152610a11816107ab565b9050949350505050565b6000604082019050610a306000830185610701565b610a3d60208301846108e4565b9392505050565b6000602082019050610a596000830184610710565b92915050565b60006020820190508181036000830152610a7881610774565b9050919050565b60006020820190508181036000830152610a9881610819565b9050919050565b60006020820190508181036000830152610ab881610887565b9050919050565b6000602082019050610ad460008301846108e4565b92915050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b60008115159050919050565b60008160010b9050919050565b6000819050919050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b6000819050919050565b6000610b5282610b06565b9050919050565b6000610b6482610b06565b90509190505600a265627a7a7230582046c2af8f953e908846c8d83be5ba37b048fa74e2aefe350f11c6020b","13f8bd976c6578706572696d656e74616cf50037"};

    public static final String BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"60806040523480156200001157600080fd5b506040516200101438038062001014833981018060405262000037919081019062000238565b336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555083600290805190602001906200008f92919062000115565b506001600360006101000a81548161ffff021916908360010b61ffff160217905550826004819055508160058190555062015180810242016007819055507fc93a292970984f2ab9b7cbc20ceb6ca38acd20de093bb581eaf9cf11b84604f933600142604051620001039392919062000327565b60405180910390a1505050506200045f565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200015857805160ff191683800117855562000189565b8280016001018555821562000189579182015b82811115620001885782518255916020019190600101906200016b565b5b5090506200019891906200019c565b5090565b620001c191905b80821115620001bd576000816000905550600101620001a3565b5090565b90565b600082601f8301121515620001d857600080fd5b8151620001ef620001e982620003a7565b62000379565b915080825260208301602083018583830111156200020c57600080fd5b6200021983828462000429565b50505092915050565b60006200023082516200040b565b905092915050565b600080600080608085870312156200024f57600080fd5b600085015167ffffffffffffffff8111156200026a57600080fd5b6200027887828801620001c4565b94505060206200028b8782880162000222565b93505060406200029e8782880162000222565b9250506060620002b18782880162000222565b91505092959194509250565b620002c881620003d4565b82525050565b620002d98162000415565b82525050565b6000600682527f63726561746500000000000000000000000000000000000000000000000000006020830152604082019050919050565b620003218162000401565b82525050565b60006080820190506200033e6000830186620002bd565b6200034d6020830185620002ce565b6200035c604083018462000316565b81810360608301526200036f81620002df565b9050949350505050565b6000604051905081810181811067ffffffffffffffff821117156200039d57600080fd5b8060405250919050565b600067ffffffffffffffff821115620003bf57600080fd5b601f19601f8301169050602081019050919050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b60008160010b9050919050565b6000819050919050565b6000819050919050565b60006200042282620003f4565b9050919050565b60005b83811015620004495780820151818401526020810190506200042c565b8381111562000459576000848401525b50505050565b610ba5806200046f6000396000f30060806040526004361061006d576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806347186a591461007257806370cf2ccf1461008957806399e6779a146100c6578063b44f5bd9146100ef578063d9cf81c01461012c575b600080fd5b34801561007e57600080fd5b50610087610158565b005b34801561009557600080fd5b506100b060048036036100ab91908101906106af565b610247565b6040516100bd9190610abf565b60405180910390f35b3480156100d257600080fd5b506100ed60048036036100e891908101906106af565b61025f565b005b3480156100fb57600080fd5b50610116600480360361011191908101906106d8565b6102ef565b6040516101239190610a44565b60405180910390f35b34801561013857600080fd5b506101416104fb565b60405161014f929190610a1b565b60405180910390f35b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156101e9576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004016101e090610a5f565b60405180910390fd5b60038060006101000a81548161ffff021916908360010b61ffff1602179055507fc93a292970984f2ab9b7cbc20ceb6ca38acd20de093bb581eaf9cf11b84604f93360034260405161023d939291906109d1565b60405180910390a1565b600b6020528060005260406000206000915090505481565b80600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550426008819055506005546006819055507fc93a292970984f2ab9b7cbc20ceb6ca38acd20de093bb581eaf9cf11b84604f9336002426040516102e493929190610987565b60405180910390a150565b60006004548210151515610338576040517fc703cb1200000000000000000000000000000000000000000000000000000000815260040161032f90610a7f565b60405180910390fd5b600a54821015151561037f576040517fc703cb1200000000000000000000000000000000000000000000000000000000815260040161037690610a9f565b60405180910390fd5b33600960006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555081600a8190555081600b60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081905550600554600a541015156104f1576002600360006101000a81548161ffff021916908360010b61ffff160217905550600960009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550600a54600681905550426008819055507fc93a292970984f2ab9b7cbc20ceb6ca38acd20de093bb581eaf9cf11b84604f9336002426040516104e0939291906108f3565b60405180910390a1600190506104f6565b600090505b919050565b6000806000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561058f576040517fc703cb1200000000000000000000000000000000000000000000000000000000815260040161058690610a5f565b60405180910390fd5b600960009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550600a5490506002600360006101000a81548161ffff021916908360010b61ffff160217905550426008819055507fc93a292970984f2ab9b7cbc20ceb6ca38acd20de093bb581eaf9cf11b84604f9336002426040516106539392919061093d565b60405180910390a1600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681915091509091565b60006106938235610b1d565b905092915050565b60006106a78235610b3d565b905092915050565b6000602082840312156106c157600080fd5b60006106cf84828501610687565b91505092915050565b6000602082840312156106ea57600080fd5b60006106f88482850161069b565b91505092915050565b61070a81610ada565b82525050565b61071981610afa565b82525050565b61072881610b47565b82525050565b61073781610b59565b82525050565b6000600b82527f4e6f742073656c6c6572210000000000000000000000000000000000000000006020830152604082019050919050565b6000602482527f7072696365206e65656420746f20686967686572207468616e206c6f7765737460208301527f416d742e000000000000000000000000000000000000000000000000000000006040830152606082019050919050565b6000601f82527f546865726520616c726561647920686173206120686967686572206269642e006020830152604082019050919050565b6000600382527f656e6400000000000000000000000000000000000000000000000000000000006020830152604082019050919050565b6000601282527f6d617463682065787065637420707269636500000000000000000000000000006020830152604082019050919050565b6000600482527f6465616c000000000000000000000000000000000000000000000000000000006020830152604082019050919050565b6000600a82527f7461726765744465616c000000000000000000000000000000000000000000006020830152604082019050919050565b6108ed81610b13565b82525050565b60006080820190506109086000830186610701565b610915602083018561071f565b61092260408301846108e4565b81810360608301526109338161083f565b9050949350505050565b60006080820190506109526000830186610701565b61095f602083018561071f565b61096c60408301846108e4565b818103606083015261097d81610876565b9050949350505050565b600060808201905061099c6000830186610701565b6109a9602083018561071f565b6109b660408301846108e4565b81810360608301526109c7816108ad565b9050949350505050565b60006080820190506109e66000830186610701565b6109f3602083018561072e565b610a0060408301846108e4565b8181036060830152610a1181610808565b9050949350505050565b6000604082019050610a306000830185610701565b610a3d60208301846108e4565b9392505050565b6000602082019050610a596000830184610710565b92915050565b60006020820190508181036000830152610a788161073d565b9050919050565b60006020820190508181036000830152610a9881610774565b9050919050565b60006020820190508181036000830152610ab8816107d1565b9050919050565b6000602082019050610ad460008301846108e4565b92915050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b60008115159050919050565b60008160010b9050919050565b6000819050919050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b6000819050919050565b6000610b5282610b06565b9050919050565b6000610b6482610b06565b90509190505600a265627a7a723058205f0117a8b9142cabd894272c319a3bce7fc07a94234eadc06171d003","19c3110f6c6578706572696d656e74616cf50037"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"constant\":true,\"inputs\":[{\"name\":\"\",\"type\":\"address\"}],\"name\":\"_bidLogs\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_price\",\"type\":\"uint256\"}],\"name\":\"bid\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"deal\",\"outputs\":[{\"name\":\"buyer\",\"type\":\"address\"},{\"name\":\"_tradeAmt\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"buyer\",\"type\":\"address\"}],\"name\":\"targetDeal\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"tradeEnd\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"name\":\"info\",\"type\":\"string\"},{\"name\":\"lowestAmt\",\"type\":\"uint256\"},{\"name\":\"expectAmt\",\"type\":\"uint256\"},{\"name\":\"auctionDays\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"addr\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"status\",\"type\":\"int16\"},{\"indexed\":false,\"name\":\"timestamp\",\"type\":\"uint256\"},{\"indexed\":false,\"name\":\"remark\",\"type\":\"string\"}],\"name\":\"newStatus\",\"type\":\"event\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC__BIDLOGS = "_bidLogs";

    public static final String FUNC_BID = "bid";

    public static final String FUNC_DEAL = "deal";

    public static final String FUNC_TARGETDEAL = "targetDeal";

    public static final String FUNC_TRADEEND = "tradeEnd";

    public static final Event NEWSTATUS_EVENT = new Event("newStatus", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Int16>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
    ;

    protected TradeContract(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public BigInteger _bidLogs(String param0) throws ContractException {
        final Function function = new Function(FUNC__BIDLOGS, 
                Arrays.<Type>asList(new Address(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public TransactionReceipt bid(BigInteger _price) {
        final Function function = new Function(
                FUNC_BID, 
                Arrays.<Type>asList(new Uint256(_price)),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] bid(BigInteger _price, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_BID, 
                Arrays.<Type>asList(new Uint256(_price)),
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForBid(BigInteger _price) {
        final Function function = new Function(
                FUNC_BID, 
                Arrays.<Type>asList(new Uint256(_price)),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple1<BigInteger> getBidInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_BID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<BigInteger>(

                (BigInteger) results.get(0).getValue()
                );
    }

    public Tuple1<Boolean> getBidOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_BID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<Boolean>(

                (Boolean) results.get(0).getValue()
                );
    }

    public TransactionReceipt deal() {
        final Function function = new Function(
                FUNC_DEAL, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] deal(TransactionCallback callback) {
        final Function function = new Function(
                FUNC_DEAL, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForDeal() {
        final Function function = new Function(
                FUNC_DEAL, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple2<String, BigInteger> getDealOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_DEAL, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple2<String, BigInteger>(

                (String) results.get(0).getValue(), 
                (BigInteger) results.get(1).getValue()
                );
    }

    public TransactionReceipt targetDeal(String buyer) {
        final Function function = new Function(
                FUNC_TARGETDEAL, 
                Arrays.<Type>asList(new Address(buyer)),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] targetDeal(String buyer, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_TARGETDEAL, 
                Arrays.<Type>asList(new Address(buyer)),
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForTargetDeal(String buyer) {
        final Function function = new Function(
                FUNC_TARGETDEAL, 
                Arrays.<Type>asList(new Address(buyer)),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple1<String> getTargetDealInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_TARGETDEAL, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<String>(

                (String) results.get(0).getValue()
                );
    }

    public TransactionReceipt tradeEnd() {
        final Function function = new Function(
                FUNC_TRADEEND, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] tradeEnd(TransactionCallback callback) {
        final Function function = new Function(
                FUNC_TRADEEND, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForTradeEnd() {
        final Function function = new Function(
                FUNC_TRADEEND, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public List<NewStatusEventResponse> getNewStatusEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(NEWSTATUS_EVENT, transactionReceipt);
        ArrayList<NewStatusEventResponse> responses = new ArrayList<NewStatusEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            NewStatusEventResponse typedResponse = new NewStatusEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.addr = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.status = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.remark = (String) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeNewStatusEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(NEWSTATUS_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeNewStatusEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(NEWSTATUS_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public static TradeContract load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new TradeContract(contractAddress, client, credential);
    }

    public static TradeContract deploy(Client client, CryptoKeyPair credential, String info, BigInteger lowestAmt, BigInteger expectAmt, BigInteger auctionDays) throws ContractException {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Utf8String(info),
                new Uint256(lowestAmt),
                new Uint256(expectAmt),
                new Uint256(auctionDays)));
        return deploy(TradeContract.class, client, credential, getBinary(client.getCryptoSuite()), encodedConstructor);
    }

    public static class NewStatusEventResponse {
        public TransactionReceipt.Logs log;

        public String addr;

        public BigInteger status;

        public BigInteger timestamp;

        public String remark;
    }
}
