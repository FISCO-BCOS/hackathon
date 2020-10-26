package org.fisco.bcos.contract;

import org.fisco.bcos.channel.client.TransactionSucCallback;
import org.fisco.bcos.channel.event.filter.EventLogPushWithDecodeCallback;
import org.fisco.bcos.web3j.abi.EventEncoder;
import org.fisco.bcos.web3j.abi.FunctionEncoder;
import org.fisco.bcos.web3j.abi.FunctionReturnDecoder;
import org.fisco.bcos.web3j.abi.TypeReference;
import org.fisco.bcos.web3j.abi.datatypes.*;
import org.fisco.bcos.web3j.abi.datatypes.generated.Uint256;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.crypto.EncryptType;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.RemoteCall;
import org.fisco.bcos.web3j.protocol.core.methods.response.Log;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tuples.generated.Tuple1;
import org.fisco.bcos.web3j.tuples.generated.Tuple4;
import org.fisco.bcos.web3j.tx.Contract;
import org.fisco.bcos.web3j.tx.TransactionManager;
import org.fisco.bcos.web3j.tx.gas.ContractGasProvider;
import org.fisco.bcos.web3j.tx.txdecode.TransactionDecoder;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

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
public class DAO extends Contract {
    public static final String[] BINARY_ARRAY = {"60806040526000600860006101000a81548160ff0219169083151502179055506000600860016101000a81548160ff02191690831515021790555034801561004657600080fd5b50604051610e90380380610e90833981018060405281019080805182019291906020018051820192919060200180519060200190929190805190602001909291908051906020019092919050505084600160000190805190602001906100ad92919061016d565b50836001800190805190602001906100c692919061016d565b5033600160030160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550826001600201819055506201518082024201600681905550806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505050505050610212565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106101ae57805160ff19168380011785556101dc565b828001600101855582156101dc579182015b828111156101db5782518255916020019190600101906101c0565b5b5090506101e991906101ed565b5090565b61020f91905b8082111561020b5760008160009055506001016101f3565b5090565b90565b610c6f806102216000396000f30060806040526004361061008e576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806301cb3b201461009357806329dcb0cf146100c25780635d58a1f5146100ed57806370a08231146101445780637b3e5e7b1461019b578063978e1427146101c6578063c290d691146102fc578063fd6b7ef814610341575b600080fd5b34801561009f57600080fd5b506100a8610358565b604051808215151515815260200191505060405180910390f35b3480156100ce57600080fd5b506100d7610454565b6040518082815260200191505060405180910390f35b3480156100f957600080fd5b5061010261045a565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561015057600080fd5b50610185600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061047f565b6040518082815260200191505060405180910390f35b3480156101a757600080fd5b506101b0610497565b6040518082815260200191505060405180910390f35b3480156101d257600080fd5b506101db61049d565b6040518080602001806020018581526020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001838103835287818151815260200191508051906020019080838360005b8381101561025757808201518184015260208101905061023c565b50505050905090810190601f1680156102845780820380516001836020036101000a031916815260200191505b50838103825286818151815260200191508051906020019080838360005b838110156102bd5780820151818401526020810190506102a2565b50505050905090810190601f1680156102ea5780820380516001836020036101000a031916815260200191505b50965050505050505060405180910390f35b34801561030857600080fd5b506103276004803603810190808035906020019092919050505061060b565b604051808215151515815260200191505060405180910390f35b34801561034d57600080fd5b50610356610839565b005b60006006544210151561045157600160020154600554101515610423576001600860006101000a81548160ff0219169083151502179055507fec3f991caf7857d61663fd1bba1739e04abd4781238508cde554bb849d790c85600160030160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16600554604051808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019250505060405180910390a15b6001600860016101000a81548160ff021916908315150217905550600860009054906101000a900460ff1690505b90565b60065481565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60076020528060005260406000206000915090505481565b60055481565b6001806000018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156105375780601f1061050c57610100808354040283529160200191610537565b820191906000526020600020905b81548152906001019060200180831161051a57829003601f168201915b505050505090806001018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156105d55780601f106105aa576101008083540402835291602001916105d5565b820191906000526020600020905b8154815290600101906020018083116105b857829003601f168201915b5050505050908060020154908060030160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905084565b60006006544210151561061d57600080fd5b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166323b872dd3330856040518463ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019350505050602060405180830381600087803b15801561071557600080fd5b505af1158015610729573d6000803e3d6000fd5b505050506040513d602081101561073f57600080fd5b8101908080519060200190929190505050151561075b57600080fd5b81600760003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008282540192505081905550816005600082825401925050819055507fe842aea7a5f1b01049d752008c53c52890b1a6daf660cf39e8eec506112bbdf633836001604051808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200183815260200182151515158152602001935050505060405180910390a160019050919050565b600060065442101515610c4057600860009054906101000a900460ff1615801561086f5750600860019054906101000a900460ff165b15610acb57600760003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205490506000600760003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055506000811115610aca576000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16632ccb1b3033836040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200182815260200192505050602060405180830381600087803b1580156109c857600080fd5b505af11580156109dc573d6000803e3d6000fd5b505050506040513d60208110156109f257600080fd5b81019080805190602001909291905050505080600760003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600082825403925050819055507fe842aea7a5f1b01049d752008c53c52890b1a6daf660cf39e8eec506112bbdf633826000604051808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200183815260200182151515158152602001935050505060405180910390a15b5b600860009054906101000a900460ff168015610b3757503373ffffffffffffffffffffffffffffffffffffffff16600160030160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16145b15610c3f576000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16632ccb1b30336005546040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200182815260200192505050602060405180830381600087803b158015610c0257600080fd5b505af1158015610c16573d6000803e3d6000fd5b505050506040513d6020811015610c2c57600080fd5b8101908080519060200190929190505050505b5b505600a165627a7a723058204178f8d40ee0361610f131fd0a0a13775b48fb09a1eb2cba43e540a136a85ab30029"};

    public static final String BINARY = String.join("", BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"constant\":false,\"inputs\":[],\"name\":\"checkGoalReached\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"deadline\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"tokenTime\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"\",\"type\":\"address\"}],\"name\":\"balanceOf\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"amountRaised\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"dao_info\",\"outputs\":[{\"name\":\"title\",\"type\":\"string\"},{\"name\":\"description\",\"type\":\"string\"},{\"name\":\"fundingGoal\",\"type\":\"uint256\"},{\"name\":\"beneficiary\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"timeToken\",\"type\":\"uint256\"}],\"name\":\"pay\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"safeWithdrawal\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"name\":\"_title\",\"type\":\"string\"},{\"name\":\"_description\",\"type\":\"string\"},{\"name\":\"fundingGoalInToken\",\"type\":\"uint256\"},{\"name\":\"durationInDays\",\"type\":\"uint256\"},{\"name\":\"addressOfToken\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"recipient\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"totalAmountRaised\",\"type\":\"uint256\"}],\"name\":\"GoalReached\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"backer\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"amount\",\"type\":\"uint256\"},{\"indexed\":false,\"name\":\"isContribution\",\"type\":\"bool\"}],\"name\":\"FundTransfer\",\"type\":\"event\"}]"};

    public static final String ABI = String.join("", ABI_ARRAY);

    public static final TransactionDecoder transactionDecoder = new TransactionDecoder(ABI, BINARY);

    public static final String[] SM_BINARY_ARRAY = {"60806040526000600860006101000a81548160ff0219169083151502179055506000600860016101000a81548160ff02191690831515021790555034801561004657600080fd5b50604051610e90380380610e90833981018060405281019080805182019291906020018051820192919060200180519060200190929190805190602001909291908051906020019092919050505084600160000190805190602001906100ad92919061016d565b50836001800190805190602001906100c692919061016d565b5033600160030160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550826001600201819055506201518082024201600681905550806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505050505050610212565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106101ae57805160ff19168380011785556101dc565b828001600101855582156101dc579182015b828111156101db5782518255916020019190600101906101c0565b5b5090506101e991906101ed565b5090565b61020f91905b8082111561020b5760008160009055506001016101f3565b5090565b90565b610c6f806102216000396000f30060806040526004361061008e576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806301de084614610093578063220b22df146100d85780639a2dab9114610107578063bb07c2de1461011e578063c85a3c1c14610254578063cb72354b1461027f578063cc8be70e146102d6578063e766c9f91461032d575b600080fd5b34801561009f57600080fd5b506100be60048036038101908080359060200190929190505050610358565b604051808215151515815260200191505060405180910390f35b3480156100e457600080fd5b506100ed610586565b604051808215151515815260200191505060405180910390f35b34801561011357600080fd5b5061011c610682565b005b34801561012a57600080fd5b50610133610a8c565b6040518080602001806020018581526020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001838103835287818151815260200191508051906020019080838360005b838110156101af578082015181840152602081019050610194565b50505050905090810190601f1680156101dc5780820380516001836020036101000a031916815260200191505b50838103825286818151815260200191508051906020019080838360005b838110156102155780820151818401526020810190506101fa565b50505050905090810190601f1680156102425780820380516001836020036101000a031916815260200191505b50965050505050505060405180910390f35b34801561026057600080fd5b50610269610bfa565b6040518082815260200191505060405180910390f35b34801561028b57600080fd5b50610294610c00565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b3480156102e257600080fd5b50610317600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610c25565b6040518082815260200191505060405180910390f35b34801561033957600080fd5b50610342610c3d565b6040518082815260200191505060405180910390f35b60006006544210151561036a57600080fd5b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663ad8a97313330856040518463ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019350505050602060405180830381600087803b15801561046257600080fd5b505af1158015610476573d6000803e3d6000fd5b505050506040513d602081101561048c57600080fd5b810190808051906020019092919050505015156104a857600080fd5b81600760003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008282540192505081905550816005600082825401925050819055507fe9ec4b587708fe286d802b4a32902ba03b9b00b7b3a42b5cb4c76550d94f288233836001604051808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200183815260200182151515158152602001935050505060405180910390a160019050919050565b60006006544210151561067f57600160020154600554101515610651576001600860006101000a81548160ff0219169083151502179055507f33af1a7d8f74a57ac5c47fc2aae79248da3dac5dc622e07894effd5539982af2600160030160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16600554604051808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019250505060405180910390a15b6001600860016101000a81548160ff021916908315150217905550600860009054906101000a900460ff1690505b90565b600060065442101515610a8957600860009054906101000a900460ff161580156106b85750600860019054906101000a900460ff165b1561091457600760003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205490506000600760003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055506000811115610913576000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663de4df42933836040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200182815260200192505050602060405180830381600087803b15801561081157600080fd5b505af1158015610825573d6000803e3d6000fd5b505050506040513d602081101561083b57600080fd5b81019080805190602001909291905050505080600760003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600082825403925050819055507fe9ec4b587708fe286d802b4a32902ba03b9b00b7b3a42b5cb4c76550d94f288233826000604051808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200183815260200182151515158152602001935050505060405180910390a15b5b600860009054906101000a900460ff16801561098057503373ffffffffffffffffffffffffffffffffffffffff16600160030160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16145b15610a88576000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663de4df429336005546040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200182815260200192505050602060405180830381600087803b158015610a4b57600080fd5b505af1158015610a5f573d6000803e3d6000fd5b505050506040513d6020811015610a7557600080fd5b8101908080519060200190929190505050505b5b50565b6001806000018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610b265780601f10610afb57610100808354040283529160200191610b26565b820191906000526020600020905b815481529060010190602001808311610b0957829003601f168201915b505050505090806001018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610bc45780601f10610b9957610100808354040283529160200191610bc4565b820191906000526020600020905b815481529060010190602001808311610ba757829003601f168201915b5050505050908060020154908060030160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905084565b60055481565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60076020528060005260406000206000915090505481565b600654815600a165627a7a72305820e323106047aa1c02c18529e7ecf406ce42bb1e49c1276ce2cf12cf2ddfdab6d00029"};

    public static final String SM_BINARY = String.join("", SM_BINARY_ARRAY);

    public static final String FUNC_CHECKGOALREACHED = "checkGoalReached";

    public static final String FUNC_DEADLINE = "deadline";

    public static final String FUNC_TOKENTIME = "tokenTime";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_AMOUNTRAISED = "amountRaised";

    public static final String FUNC_DAO_INFO = "dao_info";

    public static final String FUNC_PAY = "pay";

    public static final String FUNC_SAFEWITHDRAWAL = "safeWithdrawal";

    public static final Event GOALREACHED_EVENT = new Event("GoalReached", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event FUNDTRANSFER_EVENT = new Event("FundTransfer", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}));
    ;

    @Deprecated
    protected DAO(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(getBinary(), contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected DAO(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(getBinary(), contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected DAO(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(getBinary(), contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected DAO(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(getBinary(), contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static String getBinary() {
        return (EncryptType.encryptType == EncryptType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public static TransactionDecoder getTransactionDecoder() {
        return transactionDecoder;
    }

    public RemoteCall<TransactionReceipt> checkGoalReached() {
        final Function function = new Function(
                FUNC_CHECKGOALREACHED, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void checkGoalReached(TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_CHECKGOALREACHED, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String checkGoalReachedSeq() {
        final Function function = new Function(
                FUNC_CHECKGOALREACHED, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple1<Boolean> getCheckGoalReachedOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_CHECKGOALREACHED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple1<Boolean>(

                (Boolean) results.get(0).getValue()
                );
    }

    public RemoteCall<BigInteger> deadline() {
        final Function function = new Function(FUNC_DEADLINE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> tokenTime() {
        final Function function = new Function(FUNC_TOKENTIME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<BigInteger> balanceOf(String param0) {
        final Function function = new Function(FUNC_BALANCEOF, 
                Arrays.<Type>asList(new Address(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> amountRaised() {
        final Function function = new Function(FUNC_AMOUNTRAISED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Tuple4<String, String, BigInteger, String>> dao_info() {
        final Function function = new Function(FUNC_DAO_INFO, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}));
        return new RemoteCall<Tuple4<String, String, BigInteger, String>>(
                new Callable<Tuple4<String, String, BigInteger, String>>() {
                    @Override
                    public Tuple4<String, String, BigInteger, String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple4<String, String, BigInteger, String>(
                                (String) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue(), 
                                (String) results.get(3).getValue());
                    }
                });
    }

    public RemoteCall<TransactionReceipt> pay(BigInteger timeToken) {
        final Function function = new Function(
                FUNC_PAY, 
                Arrays.<Type>asList(new Uint256(timeToken)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void pay(BigInteger timeToken, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_PAY, 
                Arrays.<Type>asList(new Uint256(timeToken)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String paySeq(BigInteger timeToken) {
        final Function function = new Function(
                FUNC_PAY, 
                Arrays.<Type>asList(new Uint256(timeToken)),
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple1<BigInteger> getPayInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_PAY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple1<BigInteger>(

                (BigInteger) results.get(0).getValue()
                );
    }

    public Tuple1<Boolean> getPayOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_PAY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple1<Boolean>(

                (Boolean) results.get(0).getValue()
                );
    }

    public RemoteCall<TransactionReceipt> safeWithdrawal() {
        final Function function = new Function(
                FUNC_SAFEWITHDRAWAL, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void safeWithdrawal(TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_SAFEWITHDRAWAL, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String safeWithdrawalSeq() {
        final Function function = new Function(
                FUNC_SAFEWITHDRAWAL, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public List<GoalReachedEventResponse> getGoalReachedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(GOALREACHED_EVENT, transactionReceipt);
        ArrayList<GoalReachedEventResponse> responses = new ArrayList<GoalReachedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            GoalReachedEventResponse typedResponse = new GoalReachedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.recipient = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.totalAmountRaised = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void registerGoalReachedEventLogFilter(String fromBlock, String toBlock, List<String> otherTopics, EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(GOALREACHED_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void registerGoalReachedEventLogFilter(EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(GOALREACHED_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,callback);
    }

    public List<FundTransferEventResponse> getFundTransferEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(FUNDTRANSFER_EVENT, transactionReceipt);
        ArrayList<FundTransferEventResponse> responses = new ArrayList<FundTransferEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            FundTransferEventResponse typedResponse = new FundTransferEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.backer = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.isContribution = (Boolean) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void registerFundTransferEventLogFilter(String fromBlock, String toBlock, List<String> otherTopics, EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(FUNDTRANSFER_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void registerFundTransferEventLogFilter(EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(FUNDTRANSFER_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,callback);
    }

    @Deprecated
    public static DAO load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new DAO(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static DAO load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new DAO(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static DAO load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new DAO(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static DAO load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new DAO(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<DAO> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String _title, String _description, BigInteger fundingGoalInToken, BigInteger durationInDays, String addressOfToken) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Utf8String(_title),
                new Utf8String(_description),
                new Uint256(fundingGoalInToken),
                new Uint256(durationInDays),
                new Address(addressOfToken)));
        return deployRemoteCall(DAO.class, web3j, credentials, contractGasProvider, getBinary(), encodedConstructor);
    }

    public static RemoteCall<DAO> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String _title, String _description, BigInteger fundingGoalInToken, BigInteger durationInDays, String addressOfToken) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Utf8String(_title),
                new Utf8String(_description),
                new Uint256(fundingGoalInToken),
                new Uint256(durationInDays),
                new Address(addressOfToken)));
        return deployRemoteCall(DAO.class, web3j, transactionManager, contractGasProvider, getBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<DAO> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _title, String _description, BigInteger fundingGoalInToken, BigInteger durationInDays, String addressOfToken) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Utf8String(_title),
                new Utf8String(_description),
                new Uint256(fundingGoalInToken),
                new Uint256(durationInDays),
                new Address(addressOfToken)));
        return deployRemoteCall(DAO.class, web3j, credentials, gasPrice, gasLimit, getBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<DAO> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _title, String _description, BigInteger fundingGoalInToken, BigInteger durationInDays, String addressOfToken) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Utf8String(_title),
                new Utf8String(_description),
                new Uint256(fundingGoalInToken),
                new Uint256(durationInDays),
                new Address(addressOfToken)));
        return deployRemoteCall(DAO.class, web3j, transactionManager, gasPrice, gasLimit, getBinary(), encodedConstructor);
    }

    public static class GoalReachedEventResponse {
        public Log log;

        public String recipient;

        public BigInteger totalAmountRaised;
    }

    public static class FundTransferEventResponse {
        public Log log;

        public String backer;

        public BigInteger amount;

        public Boolean isContribution;
    }
}
