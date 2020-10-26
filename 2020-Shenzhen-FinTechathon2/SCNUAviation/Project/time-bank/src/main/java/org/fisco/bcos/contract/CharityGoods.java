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
import org.fisco.bcos.web3j.tuples.generated.Tuple2;
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
public class CharityGoods extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b50604051604080611215833981018060405281019080805190602001909291908051906020019092919050505033600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550816000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555080600260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505050611105806101106000396000f3006080604052600436106100db576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680630c0f9db0146100e05780631b33028f1461013757806322ab6448146101c75780634d0ce4f0146101f2578063735d50fa1461027957806389c3bb9f146102b05780638bc0d011146103155780638da5cb5b1461034057806396580290146103975780639fe8a3df146103ce578063a0e6f5d3146103e5578063c19d93fb1461045f578063c2b25ded1461048a578063e3b4e35a146104b7578063f8e24a86146104e2575b600080fd5b3480156100ec57600080fd5b506100f561050d565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561014357600080fd5b5061014c610533565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561018c578082015181840152602081019050610171565b50505050905090810190601f1680156101b95780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b3480156101d357600080fd5b506101dc6105d1565b6040518082815260200191505060405180910390f35b3480156101fe57600080fd5b50610277600480360381019080803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192908035906020019092919080359060200190929190803590602001909291905050506105d7565b005b34801561028557600080fd5b506102ae60048036038101908080359060200190929190803590602001909291905050506106b3565b005b3480156102bc57600080fd5b506102fb600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610b55565b604051808215151515815260200191505060405180910390f35b34801561032157600080fd5b5061032a610c96565b6040518082815260200191505060405180910390f35b34801561034c57600080fd5b50610355610c9c565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b3480156103a357600080fd5b506103cc6004803603810190808035906020019092919080359060200190929190505050610cc2565b005b3480156103da57600080fd5b506103e3610de2565b005b3480156103f157600080fd5b50610430600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610e76565b604051808581526020018481526020018381526020018215151515815260200194505050505060405180910390f35b34801561046b57600080fd5b50610474610ec0565b6040518082815260200191505060405180910390f35b34801561049657600080fd5b506104b560048036038101908080359060200190929190505050610ec6565b005b3480156104c357600080fd5b506104cc611028565b6040518082815260200191505060405180910390f35b3480156104ee57600080fd5b506104f761102e565b6040518082815260200191505060405180910390f35b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60048054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156105c95780601f1061059e576101008083540402835291602001916105c9565b820191906000526020600020905b8154815290600101906020018083116105ac57829003601f168201915b505050505081565b60065481565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561063357600080fd5b600060095414151561064457600080fd5b836004908051906020019061065a929190611034565b5081600581905550826007819055508260088190555080600681905550600160098190555060007f1574fcf0f1caf8105cc347b04f4c1b4e85e21a6495eb72d54bef9b144f1a72d960405160405180910390a250505050565b60008060016009541415156106c757600080fd5b42600360003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000868152602001908152602001600020600201541015801561072a5750600083115b151561073557600080fd5b82600360003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600086815260200190815260200160002060010181905550600654600360003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000868152602001908152602001600020600001540291506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166323b872dd3330856040518463ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019350505050602060405180830381600087803b1580156108df57600080fd5b505af11580156108f3573d6000803e3d6000fd5b505050506040513d602081101561090957600080fd5b8101908080519060200190929190505050151561092557600080fd5b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663095ea7b3600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16846040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200182815260200192505050602060405180830381600087803b158015610a0b57600080fd5b505af1158015610a1f573d6000803e3d6000fd5b505050506040513d6020811015610a3557600080fd5b810190808051906020019092919050505050600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1690508073ffffffffffffffffffffffffffffffffffffffff1663c290d691836040518263ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180828152602001915050602060405180830381600087803b158015610adb57600080fd5b505af1158015610aef573d6000803e3d6000fd5b505050506040513d6020811015610b0557600080fd5b81019080805190602001909291905050501515610b2157600080fd5b60007f1574fcf0f1caf8105cc347b04f4c1b4e85e21a6495eb72d54bef9b144f1a72d960405160405180910390a250505050565b60006001600954141515610b6857600080fd5b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610bc457600080fd5b42600360008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000848152602001908152602001600020600201541015610c8b57600360008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008381526020019081526020016000206000015460086000828254019250508190555060019050610c90565b600090505b92915050565b60055481565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b6001600954141515610cd357600080fd5b6000600854111515610ce457600080fd5b806008600082825403925050819055506080604051908101604052808281526020016000815260200162093a804201815260200160001515815250600360003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600084815260200190815260200160002060008201518160000155602082015181600101556040820151816002015560608201518160030160006101000a81548160ff02191690831515021790555090505060007f1574fcf0f1caf8105cc347b04f4c1b4e85e21a6495eb72d54bef9b144f1a72d960405160405180910390a25050565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610e3e57600080fd5b600260098190555060007f1574fcf0f1caf8105cc347b04f4c1b4e85e21a6495eb72d54bef9b144f1a72d960405160405180910390a2565b6003602052816000526040600020602052806000526040600020600091509150508060000154908060010154908060020154908060030160009054906101000a900460ff16905084565b60095481565b42600360003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffff","ffffffffffffffff16815260200190815260200160002060008381526020019081526020016000206002015410151515610f2857600080fd5b600360003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000828152602001908152602001600020600001546008600082825401925050819055506001600360003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600083815260200190815260200160002060030160006101000a81548160ff02191690831515021790555060007f1574fcf0f1caf8105cc347b04f4c1b4e85e21a6495eb72d54bef9b144f1a72d960405160405180910390a250565b60075481565b60085481565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061107557805160ff19168380011785556110a3565b828001600101855582156110a3579182015b828111156110a2578251825591602001919060010190611087565b5b5090506110b091906110b4565b5090565b6110d691905b808211156110d25760008160009055506001016110ba565b5090565b905600a165627a7a72305820165a255f09ed10e839825e4932ef7d013ef4da68ba17fa196dd3c0aa964b2bfc0029"};

    public static final String BINARY = String.join("", BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"constant\":true,\"inputs\":[],\"name\":\"dao_address\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"goods_name\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"time_token\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_goods_name\",\"type\":\"string\"},{\"name\":\"_goods_number\",\"type\":\"uint256\"},{\"name\":\"_goods_price\",\"type\":\"uint256\"},{\"name\":\"_timeToken\",\"type\":\"uint256\"}],\"name\":\"sell\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"ledger_id\",\"type\":\"uint256\"},{\"name\":\"_bank_serials_number\",\"type\":\"uint256\"}],\"name\":\"purchase_phase2\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_buyer\",\"type\":\"address\"},{\"name\":\"ledger_id\",\"type\":\"uint256\"}],\"name\":\"interrupt_deadline\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"goods_price\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"owner\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"ledger_id\",\"type\":\"uint256\"},{\"name\":\"_goods_number\",\"type\":\"uint256\"}],\"name\":\"purchase_phase1\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"interrupt_sell\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"\",\"type\":\"address\"},{\"name\":\"\",\"type\":\"uint256\"}],\"name\":\"buyers\",\"outputs\":[{\"name\":\"purchase_number\",\"type\":\"uint256\"},{\"name\":\"bank_serials_number\",\"type\":\"uint256\"},{\"name\":\"deadline\",\"type\":\"uint256\"},{\"name\":\"flag\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"state\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"ledger_id\",\"type\":\"uint256\"}],\"name\":\"interrupt_order\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"goods_number\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"remaining_goods_number\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"name\":\"_tokenTime\",\"type\":\"address\"},{\"name\":\"_dao\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"name\":\"t\",\"type\":\"uint256\"}],\"name\":\"Transaction_msg\",\"type\":\"event\"}]"};

    public static final String ABI = String.join("", ABI_ARRAY);

    public static final TransactionDecoder transactionDecoder = new TransactionDecoder(ABI, BINARY);

    public static final String[] SM_BINARY_ARRAY = {"608060405234801561001057600080fd5b50604051604080611215833981018060405281019080805190602001909291908051906020019092919050505033600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550816000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555080600260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505050611105806101106000396000f3006080604052600436106100db576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806306df27ee146100e057806317d79dd6146101455780631c12fdc01461015c5780633503eb2e146101875780634f20f595146101be5780635089e2c814610215578063696c81be1461026c5780636c01dfca146102975780637223646b146102ce5780638fed1c7c14610355578063b0d0a0d114610382578063d0e8f2a8146103ad578063d5c726df1461043d578063e0302e76146104b7578063ffff3bac146104e2575b600080fd5b3480156100ec57600080fd5b5061012b600480360381019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291908035906020019092919050505061050d565b604051808215151515815260200191505060405180910390f35b34801561015157600080fd5b5061015a61064e565b005b34801561016857600080fd5b506101716106e2565b6040518082815260200191505060405180910390f35b34801561019357600080fd5b506101bc60048036038101908080359060200190929190803590602001909291905050506106e8565b005b3480156101ca57600080fd5b506101d3610b8a565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561022157600080fd5b5061022a610bb0565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561027857600080fd5b50610281610bd6565b6040518082815260200191505060405180910390f35b3480156102a357600080fd5b506102cc6004803603810190808035906020019092919080359060200190929190505050610bdc565b005b3480156102da57600080fd5b50610353600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001909291908035906020019092919080359060200190929190505050610cfc565b005b34801561036157600080fd5b5061038060048036038101908080359060200190929190505050610dd8565b005b34801561038e57600080fd5b50610397610f3a565b6040518082815260200191505060405180910390f35b3480156103b957600080fd5b506103c2610f40565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156104025780820151818401526020810190506103e7565b50505050905090810190601f16801561042f5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561044957600080fd5b50610488600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610fde565b604051808581526020018481526020018381526020018215151515815260200194505050505060405180910390f35b3480156104c357600080fd5b506104cc611028565b6040518082815260200191505060405180910390f35b3480156104ee57600080fd5b506104f761102e565b6040518082815260200191505060405180910390f35b6000600160095414151561052057600080fd5b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561057c57600080fd5b42600360008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600084815260200190815260200160002060020154101561064357600360008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008381526020019081526020016000206000015460086000828254019250508190555060019050610648565b600090505b92915050565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156106aa57600080fd5b600260098190555060007f9b1865a3943c1e2360ea06691b9cdd68ed2e2cfa5fd80cc20a9099981f0c0e4d60405160405180910390a2565b60065481565b60008060016009541415156106fc57600080fd5b42600360003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000868152602001908152602001600020600201541015801561075f5750600083115b151561076a57600080fd5b82600360003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600086815260200190815260200160002060010181905550600654600360003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000868152602001908152602001600020600001540291506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663ad8a97313330856040518463ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019350505050602060405180830381600087803b15801561091457600080fd5b505af1158015610928573d6000803e3d6000fd5b505050506040513d602081101561093e57600080fd5b8101908080519060200190929190505050151561095a57600080fd5b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16631f2d4860600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16846040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200182815260200192505050602060405180830381600087803b158015610a4057600080fd5b505af1158015610a54573d6000803e3d6000fd5b505050506040513d6020811015610a6a57600080fd5b810190808051906020019092919050505050600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1690508073ffffffffffffffffffffffffffffffffffffffff166301de0846836040518263ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180828152602001915050602060405180830381600087803b158015610b1057600080fd5b505af1158015610b24573d6000803e3d6000fd5b505050506040513d6020811015610b3a57600080fd5b81019080805190602001909291905050501515610b5657600080fd5b60007f9b1865a3943c1e2360ea06691b9cdd68ed2e2cfa5fd80cc20a9099981f0c0e4d60405160405180910390a250505050565b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60075481565b6001600954141515610bed57600080fd5b6000600854111515610bfe57600080fd5b806008600082825403925050819055506080604051908101604052808281526020016000815260200162093a804201815260200160001515815250600360003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600084815260200190815260200160002060008201518160000155602082015181600101556040820151816002015560608201518160030160006101000a81548160ff02191690831515021790555090505060007f9b1865a3943c1e2360ea06691b9cdd68ed2e2cfa5fd80cc20a9099981f0c0e4d60405160405180910390a25050565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610d5857600080fd5b6000600954141515610d6957600080fd5b8360049080519060200190610d7f929190611034565b5081600581905550826007819055508260088190555080600681905550600160098190555060007f9b1865a3943c1e2360ea06691b9cdd68ed2e2cfa5fd80cc20a9099981f0c0e4d60405160405180910390a250505050565b42600360003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008381526020019081526020016000206002015410151515610e3a57600080fd5b600360003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000828152602001908152602001600020600001546008600082825401925050819055506001600360003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000838152602001908152602001600020","60030160006101000a81548160ff02191690831515021790555060007f9b1865a3943c1e2360ea06691b9cdd68ed2e2cfa5fd80cc20a9099981f0c0e4d60405160405180910390a250565b60095481565b60048054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610fd65780601f10610fab57610100808354040283529160200191610fd6565b820191906000526020600020905b815481529060010190602001808311610fb957829003601f168201915b505050505081565b6003602052816000526040600020602052806000526040600020600091509150508060000154908060010154908060020154908060030160009054906101000a900460ff16905084565b60055481565b60085481565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061107557805160ff19168380011785556110a3565b828001600101855582156110a3579182015b828111156110a2578251825591602001919060010190611087565b5b5090506110b091906110b4565b5090565b6110d691905b808211156110d25760008160009055506001016110ba565b5090565b905600a165627a7a723058201c3238567886ffa6978faf22b395f1d82849e938bc7b7d52cf0c8aaf1b2cb2440029"};

    public static final String SM_BINARY = String.join("", SM_BINARY_ARRAY);

    public static final String FUNC_DAO_ADDRESS = "dao_address";

    public static final String FUNC_GOODS_NAME = "goods_name";

    public static final String FUNC_TIME_TOKEN = "time_token";

    public static final String FUNC_SELL = "sell";

    public static final String FUNC_PURCHASE_PHASE2 = "purchase_phase2";

    public static final String FUNC_INTERRUPT_DEADLINE = "interrupt_deadline";

    public static final String FUNC_GOODS_PRICE = "goods_price";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_PURCHASE_PHASE1 = "purchase_phase1";

    public static final String FUNC_INTERRUPT_SELL = "interrupt_sell";

    public static final String FUNC_BUYERS = "buyers";

    public static final String FUNC_STATE = "state";

    public static final String FUNC_INTERRUPT_ORDER = "interrupt_order";

    public static final String FUNC_GOODS_NUMBER = "goods_number";

    public static final String FUNC_REMAINING_GOODS_NUMBER = "remaining_goods_number";

    public static final Event TRANSACTION_MSG_EVENT = new Event("Transaction_msg", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}));
    ;

    @Deprecated
    protected CharityGoods(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(getBinary(), contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected CharityGoods(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(getBinary(), contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected CharityGoods(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(getBinary(), contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected CharityGoods(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(getBinary(), contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static String getBinary() {
        return (EncryptType.encryptType == EncryptType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public static TransactionDecoder getTransactionDecoder() {
        return transactionDecoder;
    }

    public RemoteCall<String> dao_address() {
        final Function function = new Function(FUNC_DAO_ADDRESS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> goods_name() {
        final Function function = new Function(FUNC_GOODS_NAME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<BigInteger> time_token() {
        final Function function = new Function(FUNC_TIME_TOKEN, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> sell(String _goods_name, BigInteger _goods_number, BigInteger _goods_price, BigInteger _timeToken) {
        final Function function = new Function(
                FUNC_SELL, 
                Arrays.<Type>asList(new Utf8String(_goods_name),
                new Uint256(_goods_number),
                new Uint256(_goods_price),
                new Uint256(_timeToken)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void sell(String _goods_name, BigInteger _goods_number, BigInteger _goods_price, BigInteger _timeToken, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_SELL, 
                Arrays.<Type>asList(new Utf8String(_goods_name),
                new Uint256(_goods_number),
                new Uint256(_goods_price),
                new Uint256(_timeToken)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String sellSeq(String _goods_name, BigInteger _goods_number, BigInteger _goods_price, BigInteger _timeToken) {
        final Function function = new Function(
                FUNC_SELL, 
                Arrays.<Type>asList(new Utf8String(_goods_name),
                new Uint256(_goods_number),
                new Uint256(_goods_price),
                new Uint256(_timeToken)),
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple4<String, BigInteger, BigInteger, BigInteger> getSellInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_SELL, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple4<String, BigInteger, BigInteger, BigInteger>(

                (String) results.get(0).getValue(), 
                (BigInteger) results.get(1).getValue(), 
                (BigInteger) results.get(2).getValue(), 
                (BigInteger) results.get(3).getValue()
                );
    }

    public RemoteCall<TransactionReceipt> purchase_phase2(BigInteger ledger_id, BigInteger _bank_serials_number) {
        final Function function = new Function(
                FUNC_PURCHASE_PHASE2, 
                Arrays.<Type>asList(new Uint256(ledger_id),
                new Uint256(_bank_serials_number)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void purchase_phase2(BigInteger ledger_id, BigInteger _bank_serials_number, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_PURCHASE_PHASE2, 
                Arrays.<Type>asList(new Uint256(ledger_id),
                new Uint256(_bank_serials_number)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String purchase_phase2Seq(BigInteger ledger_id, BigInteger _bank_serials_number) {
        final Function function = new Function(
                FUNC_PURCHASE_PHASE2, 
                Arrays.<Type>asList(new Uint256(ledger_id),
                new Uint256(_bank_serials_number)),
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple2<BigInteger, BigInteger> getPurchase_phase2Input(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_PURCHASE_PHASE2, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple2<BigInteger, BigInteger>(

                (BigInteger) results.get(0).getValue(), 
                (BigInteger) results.get(1).getValue()
                );
    }

    public RemoteCall<TransactionReceipt> interrupt_deadline(String _buyer, BigInteger ledger_id) {
        final Function function = new Function(
                FUNC_INTERRUPT_DEADLINE, 
                Arrays.<Type>asList(new Address(_buyer),
                new Uint256(ledger_id)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void interrupt_deadline(String _buyer, BigInteger ledger_id, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_INTERRUPT_DEADLINE, 
                Arrays.<Type>asList(new Address(_buyer),
                new Uint256(ledger_id)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String interrupt_deadlineSeq(String _buyer, BigInteger ledger_id) {
        final Function function = new Function(
                FUNC_INTERRUPT_DEADLINE, 
                Arrays.<Type>asList(new Address(_buyer),
                new Uint256(ledger_id)),
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple2<String, BigInteger> getInterrupt_deadlineInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_INTERRUPT_DEADLINE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple2<String, BigInteger>(

                (String) results.get(0).getValue(), 
                (BigInteger) results.get(1).getValue()
                );
    }

    public Tuple1<Boolean> getInterrupt_deadlineOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_INTERRUPT_DEADLINE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple1<Boolean>(

                (Boolean) results.get(0).getValue()
                );
    }

    public RemoteCall<BigInteger> goods_price() {
        final Function function = new Function(FUNC_GOODS_PRICE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> owner() {
        final Function function = new Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> purchase_phase1(BigInteger ledger_id, BigInteger _goods_number) {
        final Function function = new Function(
                FUNC_PURCHASE_PHASE1, 
                Arrays.<Type>asList(new Uint256(ledger_id),
                new Uint256(_goods_number)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void purchase_phase1(BigInteger ledger_id, BigInteger _goods_number, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_PURCHASE_PHASE1, 
                Arrays.<Type>asList(new Uint256(ledger_id),
                new Uint256(_goods_number)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String purchase_phase1Seq(BigInteger ledger_id, BigInteger _goods_number) {
        final Function function = new Function(
                FUNC_PURCHASE_PHASE1, 
                Arrays.<Type>asList(new Uint256(ledger_id),
                new Uint256(_goods_number)),
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple2<BigInteger, BigInteger> getPurchase_phase1Input(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_PURCHASE_PHASE1, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple2<BigInteger, BigInteger>(

                (BigInteger) results.get(0).getValue(), 
                (BigInteger) results.get(1).getValue()
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

    public RemoteCall<Tuple4<BigInteger, BigInteger, BigInteger, Boolean>> buyers(String param0, BigInteger param1) {
        final Function function = new Function(FUNC_BUYERS, 
                Arrays.<Type>asList(new Address(param0),
                new Uint256(param1)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}));
        return new RemoteCall<Tuple4<BigInteger, BigInteger, BigInteger, Boolean>>(
                new Callable<Tuple4<BigInteger, BigInteger, BigInteger, Boolean>>() {
                    @Override
                    public Tuple4<BigInteger, BigInteger, BigInteger, Boolean> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple4<BigInteger, BigInteger, BigInteger, Boolean>(
                                (BigInteger) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue(), 
                                (Boolean) results.get(3).getValue());
                    }
                });
    }

    public RemoteCall<BigInteger> state() {
        final Function function = new Function(FUNC_STATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> interrupt_order(BigInteger ledger_id) {
        final Function function = new Function(
                FUNC_INTERRUPT_ORDER, 
                Arrays.<Type>asList(new Uint256(ledger_id)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void interrupt_order(BigInteger ledger_id, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_INTERRUPT_ORDER, 
                Arrays.<Type>asList(new Uint256(ledger_id)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String interrupt_orderSeq(BigInteger ledger_id) {
        final Function function = new Function(
                FUNC_INTERRUPT_ORDER, 
                Arrays.<Type>asList(new Uint256(ledger_id)),
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple1<BigInteger> getInterrupt_orderInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_INTERRUPT_ORDER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple1<BigInteger>(

                (BigInteger) results.get(0).getValue()
                );
    }

    public RemoteCall<BigInteger> goods_number() {
        final Function function = new Function(FUNC_GOODS_NUMBER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> remaining_goods_number() {
        final Function function = new Function(FUNC_REMAINING_GOODS_NUMBER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public List<Transaction_msgEventResponse> getTransaction_msgEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSACTION_MSG_EVENT, transactionReceipt);
        ArrayList<Transaction_msgEventResponse> responses = new ArrayList<Transaction_msgEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
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
    public static CharityGoods load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new CharityGoods(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static CharityGoods load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new CharityGoods(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static CharityGoods load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new CharityGoods(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static CharityGoods load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new CharityGoods(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<CharityGoods> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String _tokenTime, String _dao) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(_tokenTime),
                new Address(_dao)));
        return deployRemoteCall(CharityGoods.class, web3j, credentials, contractGasProvider, getBinary(), encodedConstructor);
    }

    public static RemoteCall<CharityGoods> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String _tokenTime, String _dao) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(_tokenTime),
                new Address(_dao)));
        return deployRemoteCall(CharityGoods.class, web3j, transactionManager, contractGasProvider, getBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<CharityGoods> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _tokenTime, String _dao) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(_tokenTime),
                new Address(_dao)));
        return deployRemoteCall(CharityGoods.class, web3j, credentials, gasPrice, gasLimit, getBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<CharityGoods> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _tokenTime, String _dao) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(_tokenTime),
                new Address(_dao)));
        return deployRemoteCall(CharityGoods.class, web3j, transactionManager, gasPrice, gasLimit, getBinary(), encodedConstructor);
    }

    public static class Transaction_msgEventResponse {
        public Log log;

        public BigInteger t;
    }
}
