package org.fisco.bcos.contract;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.fisco.bcos.channel.client.TransactionSucCallback;
import org.fisco.bcos.channel.event.filter.EventLogPushWithDecodeCallback;
import org.fisco.bcos.web3j.abi.EventEncoder;
import org.fisco.bcos.web3j.abi.FunctionEncoder;
import org.fisco.bcos.web3j.abi.FunctionReturnDecoder;
import org.fisco.bcos.web3j.abi.TypeReference;
import org.fisco.bcos.web3j.abi.datatypes.Address;
import org.fisco.bcos.web3j.abi.datatypes.Bool;
import org.fisco.bcos.web3j.abi.datatypes.Event;
import org.fisco.bcos.web3j.abi.datatypes.Function;
import org.fisco.bcos.web3j.abi.datatypes.Type;
import org.fisco.bcos.web3j.abi.datatypes.Utf8String;
import org.fisco.bcos.web3j.abi.datatypes.generated.Uint256;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.crypto.EncryptType;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.RemoteCall;
import org.fisco.bcos.web3j.protocol.core.methods.response.Log;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tuples.generated.Tuple1;
import org.fisco.bcos.web3j.tuples.generated.Tuple2;
import org.fisco.bcos.web3j.tuples.generated.Tuple6;
import org.fisco.bcos.web3j.tx.Contract;
import org.fisco.bcos.web3j.tx.TransactionManager;
import org.fisco.bcos.web3j.tx.gas.ContractGasProvider;
import org.fisco.bcos.web3j.tx.txdecode.TransactionDecoder;

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
public class P2PTime extends Contract {
    public static final String[] BINARY_ARRAY = {"60806040523480156200001157600080fd5b5060405162001208380380620012088339810180604052810190808051906020019092919080518201929190602001805190602001909291908051820192919060200180518201929190505050846000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555033600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508360046000019080519060200190620000fa92919062000160565b508260046001018190555081600460020190805190602001906200012092919062000160565b5042600460030181905550806004800190805190602001906200014592919062000160565b5062093a80420160046005018190555050505050506200020f565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10620001a357805160ff1916838001178555620001d4565b82800160010185558215620001d4579182015b82811115620001d3578251825591602001919060010190620001b6565b5b509050620001e39190620001e7565b5090565b6200020c91905b8082111562000208576000816000905550600101620001ee565b5090565b90565b610fe9806200021f6000396000f3006080604052600436106100af576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806305c2e9f1146100b4578063075d47821461010b57806335bcdc4d1461012257806371457b811461029f5780638da5cb5b146102f65780639f0059201461034d5780639fe8a3df14610390578063a53e7e39146103a7578063c19d93fb146103d4578063db595b3b146103ff578063ffb1c28514610416575b600080fd5b3480156100c057600080fd5b506100c961044f565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561011757600080fd5b50610120610475565b005b34801561012e57600080fd5b5061013761065d565b6040518080602001878152602001806020018681526020018060200185815260200184810384528a818151815260200191508051906020019080838360005b83811015610191578082015181840152602081019050610176565b50505050905090810190601f1680156101be5780820380516001836020036101000a031916815260200191505b50848103835288818151815260200191508051906020019080838360005b838110156101f75780820151818401526020810190506101dc565b50505050905090810190601f1680156102245780820380516001836020036101000a031916815260200191505b50848103825286818151815260200191508051906020019080838360005b8381101561025d578082015181840152602081019050610242565b50505050905090810190601f16801561028a5780820380516001836020036101000a031916815260200191505b50995050505050505050505060405180910390f35b3480156102ab57600080fd5b506102b461084f565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561030257600080fd5b5061030b610875565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561035957600080fd5b5061038e600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061089b565b005b34801561039c57600080fd5b506103a5610982565b005b3480156103b357600080fd5b506103d260048036038101908080359060200190929190505050610b2a565b005b3480156103e057600080fd5b506103e9610d24565b6040518082815260200191505060405180910390f35b34801561040b57600080fd5b50610414610d2a565b005b34801561042257600080fd5b5061044d60048036038101908080359060200190929190803515159060200190929190505050610dd4565b005b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156104d157600080fd5b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166323b872dd33306004600101546040518463ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019350505050602060405180830381600087803b1580156105ce57600080fd5b505af11580156105e2573d6000803e3d6000fd5b505050506040513d60208110156105f857600080fd5b8101908080519060200190929190505050151561061457600080fd5b6000600a5414151561062557600080fd5b6001600a8190555060007f1574fcf0f1caf8105cc347b04f4c1b4e85e21a6495eb72d54bef9b144f1a72d960405160405180910390a2565b6004806000018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156106f75780601f106106cc576101008083540402835291602001916106f7565b820191906000526020600020905b8154815290600101906020018083116106da57829003601f168201915b505050505090806001015490806002018054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561079b5780601f106107705761010080835404028352916020019161079b565b820191906000526020600020905b81548152906001019060200180831161077e57829003601f168201915b505050505090806003015490806004018054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561083f5780601f106108145761010080835404028352916020019161083f565b820191906000526020600020905b81548152906001019060200180831161082257829003601f168201915b5050505050908060050154905086565b600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156108f757600080fd5b6002600a5414151561090857600080fd5b80600360006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506003600a8190555060027f1574fcf0f1caf8105cc347b04f4c1b4e85e21a6495eb72d54bef9b144f1a72d960405160405180910390a250565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156109de57600080fd5b6003600a541015156109ef57600080fd5b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16632ccb1b30600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff166004600101546040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200182815260200192505050602060405180830381600087803b158015610ada57600080fd5b505af1158015610aee573d6000803e3d6000fd5b505050506040513d6020811015610b0457600080fd5b81019080805190602001909291905050501515610b2057600080fd5b6008600a81905550565b6004600a541480610b3d57506005600a54145b1515610b4857600080fd5b600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610ba457600080fd5b80600b819055506004600a541415610cea576000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16632ccb1b30600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff166004600101546040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200182815260200192505050602060405180830381600087803b158015610ca157600080fd5b505af1158015610cb5573d6000803e3d6000fd5b505050506040513d6020811015610ccb57600080fd5b8101908080519060200190929190505050506006600a81905550610cf3565b6007600a819055505b60047f1574fcf0f1caf8105cc347b04f4c1b4e85e21a6495eb72d54bef9b144f1a72d960405160405180910390a250565b600a5481565b6001600a541480610d3d57506002600a54145b1515610d4857600080fd5b42600460050154101515610d5b57600080fd5b33600260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506002600a8190555060017f1574fcf0f1caf8105cc347b04f4c1b4e85e21a6495eb72d54bef9b144f1a72d960405160405180910390a2565b600160009054906101000a90","0473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610e3057600080fd5b6003600a54141515610e4157600080fd5b81600c819055508015610e5b576004600a81905550610f8b565b6005600a819055506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16632ccb1b30600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff166004600101546040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200182815260200192505050602060405180830381600087803b158015610f4e57600080fd5b505af1158015610f62573d6000803e3d6000fd5b505050506040513d6020811015610f7857600080fd5b8101908080519060200190929190505050505b60037f1574fcf0f1caf8105cc347b04f4c1b4e85e21a6495eb72d54bef9b144f1a72d960405160405180910390a250505600a165627a7a72305820ebeec4165db60e4d71f96d4704e50d015b215f8be45018741148dff942c66bb30029"};

    public static final String BINARY = String.join("", BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"constant\":true,\"inputs\":[],\"name\":\"candidate_volunteer\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"publish\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"p2p_info\",\"outputs\":[{\"name\":\"title\",\"type\":\"string\"},{\"name\":\"price\",\"type\":\"uint256\"},{\"name\":\"sort\",\"type\":\"string\"},{\"name\":\"publish_time\",\"type\":\"uint256\"},{\"name\":\"description\",\"type\":\"string\"},{\"name\":\"deadline\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"volunteer\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"owner\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_volunteer\",\"type\":\"address\"}],\"name\":\"accept\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"interrupt_sell\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"score\",\"type\":\"uint256\"}],\"name\":\"assess_owner\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"state\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"apply\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"score\",\"type\":\"uint256\"},{\"name\":\"finished\",\"type\":\"bool\"}],\"name\":\"assess_volunteer\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"name\":\"_tokenTime\",\"type\":\"address\"},{\"name\":\"_title\",\"type\":\"string\"},{\"name\":\"_price\",\"type\":\"uint256\"},{\"name\":\"_sort\",\"type\":\"string\"},{\"name\":\"_description\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"name\":\"t\",\"type\":\"uint256\"}],\"name\":\"Transaction_msg\",\"type\":\"event\"}]"};

    public static final String ABI = String.join("", ABI_ARRAY);

    public static final TransactionDecoder transactionDecoder = new TransactionDecoder(ABI, BINARY);

    public static final String[] SM_BINARY_ARRAY = {"60806040523480156200001157600080fd5b5060405162001208380380620012088339810180604052810190808051906020019092919080518201929190602001805190602001909291908051820192919060200180518201929190505050846000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555033600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508360046000019080519060200190620000fa92919062000160565b508260046001018190555081600460020190805190602001906200012092919062000160565b5042600460030181905550806004800190805190602001906200014592919062000160565b5062093a80420160046005018190555050505050506200020f565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10620001a357805160ff1916838001178555620001d4565b82800160010185558215620001d4579182015b82811115620001d3578251825591602001919060010190620001b6565b5b509050620001e39190620001e7565b5090565b6200020c91905b8082111562000208576000816000905550600101620001ee565b5090565b90565b610fe9806200021f6000396000f3006080604052600436106100af576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806313c9760f146100b457806317d79dd61461010b5780631acc4bf0146101225780635089e2c81461013957806376f3014f1461019057806384d42bab1461030d57806386cb367e14610324578063b0d0a0d11461035d578063ce9b58c714610388578063ee176ebc146103cb578063f06f4eab14610422575b600080fd5b3480156100c057600080fd5b506100c961044f565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561011757600080fd5b50610120610475565b005b34801561012e57600080fd5b5061013761061d565b005b34801561014557600080fd5b5061014e610805565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561019c57600080fd5b506101a561082b565b6040518080602001878152602001806020018681526020018060200185815260200184810384528a818151815260200191508051906020019080838360005b838110156101ff5780820151818401526020810190506101e4565b50505050905090810190601f16801561022c5780820380516001836020036101000a031916815260200191505b50848103835288818151815260200191508051906020019080838360005b8381101561026557808201518184015260208101905061024a565b50505050905090810190601f1680156102925780820380516001836020036101000a031916815260200191505b50848103825286818151815260200191508051906020019080838360005b838110156102cb5780820151818401526020810190506102b0565b50505050905090810190601f1680156102f85780820380516001836020036101000a031916815260200191505b50995050505050505050505060405180910390f35b34801561031957600080fd5b50610322610a1d565b005b34801561033057600080fd5b5061035b60048036038101908080359060200190929190803515159060200190929190505050610ac7565b005b34801561036957600080fd5b50610372610cb0565b6040518082815260200191505060405180910390f35b34801561039457600080fd5b506103c9600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610cb6565b005b3480156103d757600080fd5b506103e0610d9d565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561042e57600080fd5b5061044d60048036038101908080359060200190929190505050610dc3565b005b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156104d157600080fd5b6003600a541015156104e257600080fd5b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663de4df429600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff166004600101546040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200182815260200192505050602060405180830381600087803b1580156105cd57600080fd5b505af11580156105e1573d6000803e3d6000fd5b505050506040513d60208110156105f757600080fd5b8101908080519060200190929190505050151561061357600080fd5b6008600a81905550565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561067957600080fd5b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663ad8a973133306004600101546040518463ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019350505050602060405180830381600087803b15801561077657600080fd5b505af115801561078a573d6000803e3d6000fd5b505050506040513d60208110156107a057600080fd5b810190808051906020019092919050505015156107bc57600080fd5b6000600a541415156107cd57600080fd5b6001600a8190555060007f9b1865a3943c1e2360ea06691b9cdd68ed2e2cfa5fd80cc20a9099981f0c0e4d60405160405180910390a2565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b6004806000018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156108c55780601f1061089a576101008083540402835291602001916108c5565b820191906000526020600020905b8154815290600101906020018083116108a857829003601f168201915b505050505090806001015490806002018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156109695780601f1061093e57610100808354040283529160200191610969565b820191906000526020600020905b81548152906001019060200180831161094c57829003601f168201915b505050505090806003015490806004018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610a0d5780601f106109e257610100808354040283529160200191610a0d565b820191906000526020600020905b8154815290600101906020018083116109f057829003601f168201915b5050505050908060050154905086565b6001600a541480610a3057506002600a54145b1515610a3b57600080fd5b42600460050154101515610a4e57600080fd5b33600260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506002600a8190555060017f9b1865a3943c1e2360ea06691b9cdd68ed2e2cfa5fd80cc20a9099981f0c0e4d60405160405180910390a2565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610b2357600080fd5b6003600a54141515610b3457600080fd5b81600c819055508015610b4e576004600a81905550610c7e565b6005600a819055506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663de4df429600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff166004600101546040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200182815260200192505050602060405180830381600087803b158015610c4157600080fd5b505af1158015610c55573d6000803e3d6000fd5b505050506040513d6020811015610c6b57600080fd5b8101908080519060200190929190505050505b60037f9b1865a3943c1e2360ea06691b9cdd68ed2e2cfa5fd80cc20a9099981f0c0e4d60405160405180910390a25050565b600a5481565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610d1257600080fd5b6002600a54141515610d2357600080fd5b80600360006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506003600a8190555060027f9b1865a3943c1e2360ea06691b9cdd68ed2e2cfa5fd80cc20a9099981f0c0e4d60405160405180910390a250565b600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b6004600a541480610dd657506005600a54145b1515610de157600080fd","5b600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610e3d57600080fd5b80600b819055506004600a541415610f83576000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663de4df429600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff166004600101546040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200182815260200192505050602060405180830381600087803b158015610f3a57600080fd5b505af1158015610f4e573d6000803e3d6000fd5b505050506040513d6020811015610f6457600080fd5b8101908080519060200190929190505050506006600a81905550610f8c565b6007600a819055505b60047f9b1865a3943c1e2360ea06691b9cdd68ed2e2cfa5fd80cc20a9099981f0c0e4d60405160405180910390a2505600a165627a7a72305820ea77a4cf29bd73af330811407386f49c440e0cce3ec390d9ee089e8b8db3963d0029"};

    public static final String SM_BINARY = String.join("", SM_BINARY_ARRAY);

    public static final String FUNC_CANDIDATE_VOLUNTEER = "candidate_volunteer";

    public static final String FUNC_PUBLISH = "publish";

    public static final String FUNC_P2P_INFO = "p2p_info";

    public static final String FUNC_VOLUNTEER = "volunteer";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_ACCEPT = "accept";

    public static final String FUNC_INTERRUPT_SELL = "interrupt_sell";

    public static final String FUNC_ASSESS_OWNER = "assess_owner";

    public static final String FUNC_STATE = "state";

    public static final String FUNC_APPLY = "apply";

    public static final String FUNC_ASSESS_VOLUNTEER = "assess_volunteer";

    public static final Event TRANSACTION_MSG_EVENT = new Event("Transaction_msg", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}));
    ;

    @Deprecated
    protected P2PTime(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(getBinary(), contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected P2PTime(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(getBinary(), contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected P2PTime(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(getBinary(), contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected P2PTime(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(getBinary(), contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static String getBinary() {
        return (EncryptType.encryptType == EncryptType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public static TransactionDecoder getTransactionDecoder() {
        return transactionDecoder;
    }

    public RemoteCall<String> candidate_volunteer() {
        final Function function = new Function(FUNC_CANDIDATE_VOLUNTEER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> publish() {
        final Function function = new Function(
                FUNC_PUBLISH, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void publish(TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_PUBLISH, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String publishSeq() {
        final Function function = new Function(
                FUNC_PUBLISH, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public RemoteCall<Tuple6<String, BigInteger, String, BigInteger, String, BigInteger>> p2p_info() {
        final Function function = new Function(FUNC_P2P_INFO, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}));
        return new RemoteCall<Tuple6<String, BigInteger, String, BigInteger, String, BigInteger>>(
                new Callable<Tuple6<String, BigInteger, String, BigInteger, String, BigInteger>>() {
                    @Override
                    public Tuple6<String, BigInteger, String, BigInteger, String, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple6<String, BigInteger, String, BigInteger, String, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue(), 
                                (String) results.get(4).getValue(), 
                                (BigInteger) results.get(5).getValue());
                    }
                });
    }

    public RemoteCall<String> volunteer() {
        final Function function = new Function(FUNC_VOLUNTEER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> owner() {
        final Function function = new Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> accept(String _volunteer) {
        final Function function = new Function(
                FUNC_ACCEPT, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(_volunteer)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void accept(String _volunteer, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_ACCEPT, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(_volunteer)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String acceptSeq(String _volunteer) {
        final Function function = new Function(
                FUNC_ACCEPT, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(_volunteer)), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple1<String> getAcceptInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_ACCEPT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple1<String>(

                (String) results.get(0).getValue()
                );
    }

    public RemoteCall<TransactionReceipt> interrupt_sell() {
        final Function function = new Function(
                FUNC_INTERRUPT_SELL, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void interrupt_sell(TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_INTERRUPT_SELL, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String interrupt_sellSeq() {
        final Function function = new Function(
                FUNC_INTERRUPT_SELL, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public RemoteCall<TransactionReceipt> assess_owner(BigInteger score) {
        final Function function = new Function(
                FUNC_ASSESS_OWNER, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(score)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void assess_owner(BigInteger score, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_ASSESS_OWNER, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(score)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String assess_ownerSeq(BigInteger score) {
        final Function function = new Function(
                FUNC_ASSESS_OWNER, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(score)), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple1<BigInteger> getAssess_ownerInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_ASSESS_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple1<BigInteger>(

                (BigInteger) results.get(0).getValue()
                );
    }

    public RemoteCall<BigInteger> state() {
        final Function function = new Function(FUNC_STATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> apply() {
        final Function function = new Function(
                FUNC_APPLY, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void apply(TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_APPLY, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String applySeq() {
        final Function function = new Function(
                FUNC_APPLY, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public RemoteCall<TransactionReceipt> assess_volunteer(BigInteger score, Boolean finished) {
        final Function function = new Function(
                FUNC_ASSESS_VOLUNTEER, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(score), 
                new org.fisco.bcos.web3j.abi.datatypes.Bool(finished)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void assess_volunteer(BigInteger score, Boolean finished, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_ASSESS_VOLUNTEER, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(score), 
                new org.fisco.bcos.web3j.abi.datatypes.Bool(finished)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String assess_volunteerSeq(BigInteger score, Boolean finished) {
        final Function function = new Function(
                FUNC_ASSESS_VOLUNTEER, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(score), 
                new org.fisco.bcos.web3j.abi.datatypes.Bool(finished)), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple2<BigInteger, Boolean> getAssess_volunteerInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_ASSESS_VOLUNTEER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple2<BigInteger, Boolean>(

                (BigInteger) results.get(0).getValue(), 
                (Boolean) results.get(1).getValue()
                );
    }

    public List<Transaction_msgEventResponse> getTransaction_msgEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSACTION_MSG_EVENT, transactionReceipt);
        ArrayList<Transaction_msgEventResponse> responses = new ArrayList<Transaction_msgEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            Transaction_msgEventResponse typedResponse = new Transaction_msgEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.t = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void registerTransaction_msgEventLogFilter(String fromBlock, String toBlock, List<String> otherTopics, EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(TRANSACTION_MSG_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void registerTransaction_msgEventLogFilter(EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(TRANSACTION_MSG_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,callback);
    }

    @Deprecated
    public static P2PTime load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new P2PTime(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static P2PTime load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new P2PTime(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static P2PTime load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new P2PTime(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static P2PTime load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new P2PTime(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<P2PTime> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String _tokenTime, String _title, BigInteger _price, String _sort, String _description) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(_tokenTime), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_title), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(_price), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_sort), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_description)));
        return deployRemoteCall(P2PTime.class, web3j, credentials, contractGasProvider, getBinary(), encodedConstructor);
    }

    public static RemoteCall<P2PTime> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String _tokenTime, String _title, BigInteger _price, String _sort, String _description) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(_tokenTime), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_title), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(_price), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_sort), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_description)));
        return deployRemoteCall(P2PTime.class, web3j, transactionManager, contractGasProvider, getBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<P2PTime> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _tokenTime, String _title, BigInteger _price, String _sort, String _description) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(_tokenTime), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_title), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(_price), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_sort), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_description)));
        return deployRemoteCall(P2PTime.class, web3j, credentials, gasPrice, gasLimit, getBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<P2PTime> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _tokenTime, String _title, BigInteger _price, String _sort, String _description) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(_tokenTime), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_title), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(_price), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_sort), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_description)));
        return deployRemoteCall(P2PTime.class, web3j, transactionManager, gasPrice, gasLimit, getBinary(), encodedConstructor);
    }

    public static class Transaction_msgEventResponse {
        public Log log;

        public BigInteger t;
    }
}
