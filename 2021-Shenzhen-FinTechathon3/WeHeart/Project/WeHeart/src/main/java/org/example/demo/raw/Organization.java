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
import org.fisco.bcos.sdk.abi.datatypes.DynamicArray;
import org.fisco.bcos.sdk.abi.datatypes.Event;
import org.fisco.bcos.sdk.abi.datatypes.Function;
import org.fisco.bcos.sdk.abi.datatypes.Type;
import org.fisco.bcos.sdk.abi.datatypes.Utf8String;
import org.fisco.bcos.sdk.abi.datatypes.generated.Uint256;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple2;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple3;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple4;
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
public class Organization extends Contract {
    public static final String[] BINARY_ARRAY = {"60806040523480156200001157600080fd5b50604051604080620022738339810180604052620000339190810190620000d2565b816000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555080600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550505062000133565b6000620000ca825162000113565b905092915050565b60008060408385031215620000e657600080fd5b6000620000f685828601620000bc565b92505060206200010985828601620000bc565b9150509250929050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b61213080620001436000396000f300608060405260043610610099576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168063070679441461009e57806315dfd15e146100dc5780631e9dc28d1461011a578063282183351461015757806339cd65fc14610183578063a171ead0146101c1578063cbb845b6146101ea578063cc769f9114610229578063d790aa7214610252575b600080fd5b3480156100aa57600080fd5b506100c560048036036100c091908101906117eb565b610293565b6040516100d3929190611c7c565b60405180910390f35b3480156100e857600080fd5b5061010360048036036100fe91908101906117eb565b61046f565b604051610111929190611cb3565b60405180910390f35b34801561012657600080fd5b50610141600480360361013c9190810190611908565b610548565b60405161014e9190611d58565b60405180910390f35b34801561016357600080fd5b5061016c61066c565b60405161017a929190611cea565b60405180910390f35b34801561018f57600080fd5b506101aa60048036036101a591908101906117eb565b610744565b6040516101b8929190611d21565b60405180910390f35b3480156101cd57600080fd5b506101e860048036036101e391908101906119b3565b610a89565b005b3480156101f657600080fd5b50610211600480360361020c91908101906117eb565b610c5b565b60405161022093929190611d8e565b60405180910390f35b34801561023557600080fd5b50610250600480360361024b91908101906118b4565b610e0f565b005b34801561025e57600080fd5b50610279600480360361027491908101906117eb565b6110b6565b60405161028a959493929190611df5565b60405180910390f35b6060806000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16634a176ba5846040518263ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040161030c9190611dda565b602060405180830381600087803b15801561032657600080fd5b505af115801561033a573d6000803e3d6000fd5b505050506040513d601f19601f8201168201806040525061035e9190810190611554565b3373ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff1614151561039857600080fd5b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166307067944856040518263ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040161040e9190611dda565b600060405180830381600087803b15801561042857600080fd5b505af115801561043c573d6000803e3d6000fd5b505050506040513d6000823e3d601f19601f82011682018060405250610465919081019061157d565b9250925050915091565b6060806000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166315dfd15e846040518263ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004016104e89190611dda565b600060405180830381600087803b15801561050257600080fd5b505af1158015610516573d6000803e3d6000fd5b505050506040513d6000823e3d601f19601f8201168201806040525061053f91908101906115e9565b91509150915091565b6000808511151561055857600080fd5b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663b5f279b633878787876040518663ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004016105d6959493929190611c14565b602060405180830381600087803b1580156105f057600080fd5b505af1158015610604573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052506106289190810190611702565b507fbf7da4e38e3e3110a90c46fae847d186f46759318da7d3256520492264d7c4ee33858760405161065c93929190611bd6565b60405180910390a1949350505050565b6060806000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16633d5a85d1336040518263ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004016106e59190611bbb565b600060405180830381600087803b1580156106ff57600080fd5b505af1158015610713573d6000803e3d6000fd5b505050506040513d6000823e3d601f19601f8201168201806040525061073c9190810190611696565b915091509091565b606080600080600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663a174ce59866040518263ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004016107c19190611dda565b602060405180830381600087803b1580156107db57600080fd5b505af11580156107ef573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052506108139190810190611814565b9150600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663dffc093e86846040518363ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040161088e929190611e78565b602060405180830381600087803b1580156108a857600080fd5b505af11580156108bc573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052506108e0919081019061172b565b9050600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663602bf460826040518263ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004016109599190611d73565b600060405180830381600087803b15801561097357600080fd5b505af1158015610987573d6000803e3d6000fd5b505050506040513d6000823e3d601f19601f820116820180604052506109b09190810190611655565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16638f7fa294836040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401610a279190611d73565b600060405180830381600087803b158015610a4157600080fd5b505af1158015610a55573d6000803e3d6000fd5b505050506040513d6000823e3d601f19601f82011682018060405250610a7e9190810190611655565b935093505050915091565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16634a176ba5836040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401610aff9190611dda565b602060405180830381600087803b158015610b1957600080fd5b505af1158015610b2d573d6000803e3d6000fd5b505050506040513d601f19601f82011682018060405250610b519190810190611554565b3373ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff16141515610b8b57600080fd5b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663a171ead084846040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401610c03929190611e78565b602060405180830381600087803b158015610c1d57600080fd5b505af1158015610c31573d6000803e3d6000fd5b505050506040513d601f19601f82011682018060405250610c559190810190611702565b50505050565b60608060606000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16636cf351ec856040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401610cd69190611dda565b602060405180830381600087803b158015610cf057600080fd5b505af1158015610d04573d6000803e3d6000fd5b505050506040513d601f19601f82011682018060405250610d289190810190611702565b801515610d3457600080fd5b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663cbb845b6866040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401610daa9190611dda565b600060405180830381600087803b158015610dc457600080fd5b505af1158015610dd8573d6000803e3d6000fd5b505050506040513d6000823e3d601f19601f82011682018060405250610e019190810190611754565b935093509350509193909250565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16634a176ba5836040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401610e859190611dda565b602060405180830381600087803b158015610e9f57600080fd5b505af1158015610eb3573d6000803e3d6000fd5b505050506040513d60","1f19601f82011682018060405250610ed79190810190611554565b3373ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff16141515610f1157600080fd5b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16636cf351ec846040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401610f879190611dda565b602060405180830381600087803b158015610fa157600080fd5b505af1158015610fb5573d6000803e3d6000fd5b505050506040513d601f19601f82011682018060405250610fd99190810190611702565b801515610fe557600080fd5b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16631943a9f185856040518363ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040161105d929190611e48565b602060405180830381600087803b15801561107757600080fd5b505af115801561108b573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052506110af9190810190611702565b5050505050565b60008060008060008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16636cf351ec876040518263ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004016111349190611dda565b602060405180830381600087803b15801561114e57600080fd5b505af1158015611162573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052506111869190810190611702565b80151561119257600080fd5b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663d790aa72886040518263ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004016112089190611dda565b60a060405180830381600087803b15801561122257600080fd5b505af1158015611236573d6000803e3d6000fd5b505050506040513d601f19601f8201168201806040525061125a919081019061183d565b955095509550955095505091939590929450565b600061127a8251612063565b905092915050565b600082601f830112151561129557600080fd5b81516112a86112a382611ece565b611ea1565b915081818352602084019350602081019050838560208402820111156112cd57600080fd5b60005b838110156112fd57816112e3888261126e565b8452602084019350602083019250506001810190506112d0565b5050505092915050565b600082601f830112151561131a57600080fd5b815161132d61132882611ef6565b611ea1565b9150818183526020840193506020810190508360005b838110156113735781518601611359888261142a565b845260208401935060208301925050600181019050611343565b5050505092915050565b600082601f830112151561139057600080fd5b81516113a361139e82611f1e565b611ea1565b915081818352602084019350602081019050838560208402820111156113c857600080fd5b60005b838110156113f857816113de8882611540565b8452602084019350602083019250506001810190506113cb565b5050505092915050565b600061140e8251612083565b905092915050565b6000611422825161208f565b905092915050565b600082601f830112151561143d57600080fd5b815161145061144b82611f46565b611ea1565b9150808252602083016020830185838301111561146c57600080fd5b6114778382846120b2565b50505092915050565b600082601f830112151561149357600080fd5b81356114a66114a182611f72565b611ea1565b915080825260208301602083018583830111156114c257600080fd5b6114cd8382846120a3565b50505092915050565b600082601f83011215156114e957600080fd5b81516114fc6114f782611f72565b611ea1565b9150808252602083016020830185838301111561151857600080fd5b6115238382846120b2565b50505092915050565b60006115388235612099565b905092915050565b600061154c8251612099565b905092915050565b60006020828403121561156657600080fd5b60006115748482850161126e565b91505092915050565b6000806040838503121561159057600080fd5b600083015167ffffffffffffffff8111156115aa57600080fd5b6115b685828601611282565b925050602083015167ffffffffffffffff8111156115d357600080fd5b6115df8582860161137d565b9150509250929050565b600080604083850312156115fc57600080fd5b600083015167ffffffffffffffff81111561161657600080fd5b61162285828601611307565b925050602083015167ffffffffffffffff81111561163f57600080fd5b61164b8582860161137d565b9150509250929050565b60006020828403121561166757600080fd5b600082015167ffffffffffffffff81111561168157600080fd5b61168d8482850161137d565b91505092915050565b600080604083850312156116a957600080fd5b600083015167ffffffffffffffff8111156116c357600080fd5b6116cf8582860161137d565b925050602083015167ffffffffffffffff8111156116ec57600080fd5b6116f885828601611307565b9150509250929050565b60006020828403121561171457600080fd5b600061172284828501611402565b91505092915050565b60006020828403121561173d57600080fd5b600061174b84828501611416565b91505092915050565b60008060006060848603121561176957600080fd5b600084015167ffffffffffffffff81111561178357600080fd5b61178f868287016114d6565b935050602084015167ffffffffffffffff8111156117ac57600080fd5b6117b8868287016114d6565b925050604084015167ffffffffffffffff8111156117d557600080fd5b6117e1868287016114d6565b9150509250925092565b6000602082840312156117fd57600080fd5b600061180b8482850161152c565b91505092915050565b60006020828403121561182657600080fd5b600061183484828501611540565b91505092915050565b600080600080600060a0868803121561185557600080fd5b600061186388828901611540565b95505060206118748882890161126e565b945050604061188588828901611540565b935050606061189688828901611540565b92505060806118a788828901611540565b9150509295509295909350565b600080604083850312156118c757600080fd5b60006118d58582860161152c565b925050602083013567ffffffffffffffff8111156118f257600080fd5b6118fe85828601611480565b9150509250929050565b6000806000806080858703121561191e57600080fd5b600061192c8782880161152c565b945050602085013567ffffffffffffffff81111561194957600080fd5b61195587828801611480565b935050604085013567ffffffffffffffff81111561197257600080fd5b61197e87828801611480565b925050606085013567ffffffffffffffff81111561199b57600080fd5b6119a787828801611480565b91505092959194509250565b600080604083850312156119c657600080fd5b60006119d48582860161152c565b92505060206119e58582860161152c565b9150509250929050565b6119f881612023565b82525050565b6000611a0982611fc5565b808452602084019350611a1b83611f9e565b60005b82811015611a4d57611a318683516119ef565b611a3a82611ffc565b9150602086019550600181019050611a1e565b50849250505092915050565b6000611a6482611fd0565b80845260208401935083602082028501611a7d85611fab565b60005b84811015611ab6578383038852611a98838351611b76565b9250611aa382612009565b9150602088019750600181019050611a80565b508196508694505050505092915050565b6000611ad282611fdb565b808452602084019350611ae483611fb8565b60005b82811015611b1657611afa868351611bac565b611b0382612016565b9150602086019550600181019050611ae7565b50849250505092915050565b611b2b81612043565b82525050565b611b3a8161204f565b82525050565b6000611b4b82611ff1565b808452611b5f8160208601602086016120b2565b611b68816120e5565b602085010191505092915050565b6000611b8182611fe6565b808452611b958160208601602086016120b2565b611b9e816120e5565b602085010191505092915050565b611bb581612059565b82525050565b6000602082019050611bd060008301846119ef565b92915050565b6000606082019050611beb60008301866119ef565b8181036020830152611bfd8185611b40565b9050611c0c6040830184611bac565b949350505050565b600060a082019050611c2960008301886119ef565b611c366020830187611bac565b8181036040830152611c488186611b40565b90508181036060830152611c5c8185611b40565b90508181036080830152611c708184611b40565b90509695505050505050565b60006040820190508181036000830152611c9681856119fe565b90508181036020830152611caa8184611ac7565b90509392505050565b60006040820190508181036000830152611ccd8185611a59565b90508181036020830152611ce18184611ac7565b90509392505050565b60006040820190508181036000830152611d048185611ac7565b90508181036020830152611d188184611a59565b90509392505050565b60006040820190508181036000830152611d3b8185611ac7565b90508181036020830152611d4f8184611ac7565b90509392505050565b6000602082019050611d6d6000830184611b22565b92915050565b6000602082019050611d886000830184611b31565b92915050565b60006060820190508181036000830152611da88186611b40565b90508181036020830152611dbc8185611b40565b90508181036040830152611dd08184611b40565b9050949350505050565b6000602082019050611def6000830184611bac565b92915050565b600060a082019050611e0a6000830188611bac565b611e1760208301876119ef565b611e246040830186611bac565b611e316060830185611bac565b611e3e6080830184611bac565b9695505050505050565b6000604082019050611e5d6000830185611bac565b8181036020830152611e6f8184611b40565b90509392505050565b6000604082019050611e8d6000830185611bac565b611e9a6020830184611bac565b9392505050565b6000604051905081810181811067ffffffffffffffff8211171561","1ec457600080fd5b8060405250919050565b600067ffffffffffffffff821115611ee557600080fd5b602082029050602081019050919050565b600067ffffffffffffffff821115611f0d57600080fd5b602082029050602081019050919050565b600067ffffffffffffffff821115611f3557600080fd5b602082029050602081019050919050565b600067ffffffffffffffff821115611f5d57600080fd5b601f19601f8301169050602081019050919050565b600067ffffffffffffffff821115611f8957600080fd5b601f19601f8301169050602081019050919050565b6000602082019050919050565b6000602082019050919050565b6000602082019050919050565b600081519050919050565b600081519050919050565b600081519050919050565b600081519050919050565b600081519050919050565b6000602082019050919050565b6000602082019050919050565b6000602082019050919050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b60008115159050919050565b6000819050919050565b6000819050919050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b60008115159050919050565b6000819050919050565b6000819050919050565b82818337600083830152505050565b60005b838110156120d05780820151818401526020810190506120b5565b838111156120df576000848401525b50505050565b6000601f19601f83011690509190505600a265627a7a723058206b1fa22138b4c4457cda20a58054d10062939d9637e5c975ffad9f5a0825761d6c6578706572696d656e74616cf50037"};

    public static final String BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {};

    public static final String SM_BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"constant\":false,\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\"}],\"name\":\"getdevoter\",\"outputs\":[{\"name\":\"\",\"type\":\"address[]\"},{\"name\":\"\",\"type\":\"uint256[]\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\"}],\"name\":\"getprocess\",\"outputs\":[{\"name\":\"\",\"type\":\"string[]\"},{\"name\":\"\",\"type\":\"uint256[]\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"totalneed\",\"type\":\"uint256\"},{\"name\":\"title\",\"type\":\"string\"},{\"name\":\"programType\",\"type\":\"string\"},{\"name\":\"description\",\"type\":\"string\"}],\"name\":\"addprogram\",\"outputs\":[{\"name\":\"success\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"getprogram\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256[]\"},{\"name\":\"\",\"type\":\"string[]\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\"}],\"name\":\"getDAG\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256[]\"},{\"name\":\"\",\"type\":\"uint256[]\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\"},{\"name\":\"_value\",\"type\":\"uint256\"}],\"name\":\"changevalue\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\"}],\"name\":\"getstringinfor\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"},{\"name\":\"\",\"type\":\"string\"},{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\"},{\"name\":\"_describe\",\"type\":\"string\"}],\"name\":\"implementProgram\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\"}],\"name\":\"getinfor\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"},{\"name\":\"\",\"type\":\"address\"},{\"name\":\"\",\"type\":\"uint256\"},{\"name\":\"\",\"type\":\"uint256\"},{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"name\":\"Programaddr\",\"type\":\"address\"},{\"name\":\"DAGaddr\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"_from\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"_title\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"_need\",\"type\":\"uint256\"}],\"name\":\"addprogramlog\",\"type\":\"event\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_GETDEVOTER = "getdevoter";

    public static final String FUNC_GETPROCESS = "getprocess";

    public static final String FUNC_ADDPROGRAM = "addprogram";

    public static final String FUNC_GETPROGRAM = "getprogram";

    public static final String FUNC_GETDAG = "getDAG";

    public static final String FUNC_CHANGEVALUE = "changevalue";

    public static final String FUNC_GETSTRINGINFOR = "getstringinfor";

    public static final String FUNC_IMPLEMENTPROGRAM = "implementProgram";

    public static final String FUNC_GETINFOR = "getinfor";

    public static final Event ADDPROGRAMLOG_EVENT = new Event("addprogramlog", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}));
    ;

    protected Organization(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public TransactionReceipt getdevoter(BigInteger id) {
        final Function function = new Function(
                FUNC_GETDEVOTER, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(id)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void getdevoter(BigInteger id, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_GETDEVOTER, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(id)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForGetdevoter(BigInteger id) {
        final Function function = new Function(
                FUNC_GETDEVOTER, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(id)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple1<BigInteger> getGetdevoterInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_GETDEVOTER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<BigInteger>(

                (BigInteger) results.get(0).getValue()
                );
    }

    public Tuple2<List<String>, List<BigInteger>> getGetdevoterOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_GETDEVOTER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {}, new TypeReference<DynamicArray<Uint256>>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple2<List<String>, List<BigInteger>>(

                convertToNative((List<Address>) results.get(0).getValue()), 
                convertToNative((List<Uint256>) results.get(1).getValue())
                );
    }

    public Tuple2<List<String>, List<BigInteger>> getprocess(BigInteger id) throws ContractException {
        final Function function = new Function(FUNC_GETPROCESS, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(id)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Utf8String>>() {}, new TypeReference<DynamicArray<Uint256>>() {}));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple2<List<String>, List<BigInteger>>(
                convertToNative((List<Utf8String>) results.get(0).getValue()), 
                convertToNative((List<Uint256>) results.get(1).getValue()));
    }

    public TransactionReceipt addprogram(BigInteger totalneed, String title, String programType, String description) {
        final Function function = new Function(
                FUNC_ADDPROGRAM, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(totalneed), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(title), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(programType), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(description)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void addprogram(BigInteger totalneed, String title, String programType, String description, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_ADDPROGRAM, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(totalneed), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(title), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(programType), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(description)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForAddprogram(BigInteger totalneed, String title, String programType, String description) {
        final Function function = new Function(
                FUNC_ADDPROGRAM, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(totalneed), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(title), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(programType), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(description)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple4<BigInteger, String, String, String> getAddprogramInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_ADDPROGRAM, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple4<BigInteger, String, String, String>(

                (BigInteger) results.get(0).getValue(), 
                (String) results.get(1).getValue(), 
                (String) results.get(2).getValue(), 
                (String) results.get(3).getValue()
                );
    }

    public Tuple1<Boolean> getAddprogramOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_ADDPROGRAM, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<Boolean>(

                (Boolean) results.get(0).getValue()
                );
    }

    public TransactionReceipt getprogram() {
        final Function function = new Function(
                FUNC_GETPROGRAM, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void getprogram(TransactionCallback callback) {
        final Function function = new Function(
                FUNC_GETPROGRAM, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForGetprogram() {
        final Function function = new Function(
                FUNC_GETPROGRAM, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple2<List<BigInteger>, List<String>> getGetprogramOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_GETPROGRAM, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Uint256>>() {}, new TypeReference<DynamicArray<Utf8String>>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple2<List<BigInteger>, List<String>>(

                convertToNative((List<Uint256>) results.get(0).getValue()), 
                convertToNative((List<Utf8String>) results.get(1).getValue())
                );
    }

    public TransactionReceipt getDAG(BigInteger id) {
        final Function function = new Function(
                FUNC_GETDAG, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(id)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void getDAG(BigInteger id, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_GETDAG, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(id)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForGetDAG(BigInteger id) {
        final Function function = new Function(
                FUNC_GETDAG, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(id)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple1<BigInteger> getGetDAGInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_GETDAG, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<BigInteger>(

                (BigInteger) results.get(0).getValue()
                );
    }

    public Tuple2<List<BigInteger>, List<BigInteger>> getGetDAGOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_GETDAG, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Uint256>>() {}, new TypeReference<DynamicArray<Uint256>>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple2<List<BigInteger>, List<BigInteger>>(

                convertToNative((List<Uint256>) results.get(0).getValue()), 
                convertToNative((List<Uint256>) results.get(1).getValue())
                );
    }

    public TransactionReceipt changevalue(BigInteger id, BigInteger _value) {
        final Function function = new Function(
                FUNC_CHANGEVALUE, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(id), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void changevalue(BigInteger id, BigInteger _value, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_CHANGEVALUE, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(id), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_value)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForChangevalue(BigInteger id, BigInteger _value) {
        final Function function = new Function(
                FUNC_CHANGEVALUE, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(id), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_value)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple2<BigInteger, BigInteger> getChangevalueInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_CHANGEVALUE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple2<BigInteger, BigInteger>(

                (BigInteger) results.get(0).getValue(), 
                (BigInteger) results.get(1).getValue()
                );
    }

    public Tuple3<String, String, String> getstringinfor(BigInteger id) throws ContractException {
        final Function function = new Function(FUNC_GETSTRINGINFOR, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(id)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple3<String, String, String>(
                (String) results.get(0).getValue(), 
                (String) results.get(1).getValue(), 
                (String) results.get(2).getValue());
    }

    public TransactionReceipt implementProgram(BigInteger id, String _describe) {
        final Function function = new Function(
                FUNC_IMPLEMENTPROGRAM, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(id), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(_describe)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void implementProgram(BigInteger id, String _describe, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_IMPLEMENTPROGRAM, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(id), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(_describe)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForImplementProgram(BigInteger id, String _describe) {
        final Function function = new Function(
                FUNC_IMPLEMENTPROGRAM, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(id), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(_describe)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple2<BigInteger, String> getImplementProgramInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_IMPLEMENTPROGRAM, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple2<BigInteger, String>(

                (BigInteger) results.get(0).getValue(), 
                (String) results.get(1).getValue()
                );
    }

    public Tuple5<BigInteger, String, BigInteger, BigInteger, BigInteger> getinfor(BigInteger id) throws ContractException {
        final Function function = new Function(FUNC_GETINFOR, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(id)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple5<BigInteger, String, BigInteger, BigInteger, BigInteger>(
                (BigInteger) results.get(0).getValue(), 
                (String) results.get(1).getValue(), 
                (BigInteger) results.get(2).getValue(), 
                (BigInteger) results.get(3).getValue(), 
                (BigInteger) results.get(4).getValue());
    }

    public List<AddprogramlogEventResponse> getAddprogramlogEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ADDPROGRAMLOG_EVENT, transactionReceipt);
        ArrayList<AddprogramlogEventResponse> responses = new ArrayList<AddprogramlogEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AddprogramlogEventResponse typedResponse = new AddprogramlogEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._from = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._title = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse._need = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeAddprogramlogEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(ADDPROGRAMLOG_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeAddprogramlogEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(ADDPROGRAMLOG_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public static Organization load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new Organization(contractAddress, client, credential);
    }

    public static Organization deploy(Client client, CryptoKeyPair credential, String Programaddr, String DAGaddr) throws ContractException {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(Programaddr), 
                new org.fisco.bcos.sdk.abi.datatypes.Address(DAGaddr)));
        return deploy(Organization.class, client, credential, getBinary(client.getCryptoSuite()), encodedConstructor);
    }

    public static class AddprogramlogEventResponse {
        public TransactionReceipt.Logs log;

        public String _from;

        public String _title;

        public BigInteger _need;
    }
}
