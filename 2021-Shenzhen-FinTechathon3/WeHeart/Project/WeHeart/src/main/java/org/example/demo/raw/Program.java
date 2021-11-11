import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.abi.FunctionReturnDecoder;
import org.fisco.bcos.sdk.abi.TypeReference;
import org.fisco.bcos.sdk.abi.datatypes.Address;
import org.fisco.bcos.sdk.abi.datatypes.Bool;
import org.fisco.bcos.sdk.abi.datatypes.DynamicArray;
import org.fisco.bcos.sdk.abi.datatypes.Event;
import org.fisco.bcos.sdk.abi.datatypes.Function;
import org.fisco.bcos.sdk.abi.datatypes.Type;
import org.fisco.bcos.sdk.abi.datatypes.Utf8String;
import org.fisco.bcos.sdk.abi.datatypes.generated.Int16;
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
public class Program extends Contract {
    public static final String[] BINARY_ARRAY = {"6080604052600160005534801561001557600080fd5b50611dc7806100256000396000f3006080604052600436106100fc576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168063070679441461010157806315dfd15e1461013f5780631943a9f11461017d5780631b032647146101ba578063233fab66146101f75780633d5a85d1146102205780634a176ba51461025e5780636cf351ec1461029b578063862e6426146102d8578063a171ead014610315578063a3df219614610352578063b09ab87d1461037b578063b5f279b6146103a4578063b87493aa146103e1578063c9248a791461041e578063cbb845b614610447578063d790aa7214610486578063fad23bdc146104c7575b600080fd5b34801561010d57600080fd5b50610128600480360361012391908101906116a1565b610504565b6040516101369291906119fe565b60405180910390f35b34801561014b57600080fd5b50610166600480360361016191908101906116a1565b610616565b604051610174929190611a35565b60405180910390f35b34801561018957600080fd5b506101a4600480360361019f9190810190611719565b6107ac565b6040516101b19190611aa3565b60405180910390f35b3480156101c657600080fd5b506101e160048036036101dc91908101906116a1565b6108cd565b6040516101ee9190611b25565b60405180910390f35b34801561020357600080fd5b5061021e6004803603610219919081019061176d565b6108ed565b005b34801561022c57600080fd5b5061024760048036036102429190810190611552565b610909565b604051610255929190611a6c565b60405180910390f35b34801561026a57600080fd5b50610285600480360361028091908101906116a1565b610ace565b60405161029291906119e3565b60405180910390f35b3480156102a757600080fd5b506102c260048036036102bd91908101906116a1565b610b0e565b6040516102cf9190611aa3565b60405180910390f35b3480156102e457600080fd5b506102ff60048036036102fa91908101906116a1565b610b3b565b60405161030c9190611b25565b60405180910390f35b34801561032157600080fd5b5061033c6004803603610337919081019061176d565b610b58565b6040516103499190611aa3565b60405180910390f35b34801561035e57600080fd5b50610379600480360361037491908101906116ca565b610b7f565b005b34801561038757600080fd5b506103a2600480360361039d91908101906116a1565b610c3e565b005b3480156103b057600080fd5b506103cb60048036036103c691908101906115e2565b610c76565b6040516103d89190611aa3565b60405180910390f35b3480156103ed57600080fd5b50610408600480360361040391908101906116a1565b610ea0565b6040516104159190611b25565b60405180910390f35b34801561042a57600080fd5b506104456004803603610440919081019061157b565b610f03565b005b34801561045357600080fd5b5061046e600480360361046991908101906116a1565b610ff3565b60405161047d93929190611ad9565b60405180910390f35b34801561049257600080fd5b506104ad60048036036104a891908101906116a1565b61124c565b6040516104be959493929190611b40565b60405180910390f35b3480156104d357600080fd5b506104ee60048036036104e991908101906116a1565b61131a565b6040516104fb9190611abe565b60405180910390f35b6060806004600084815260200190815260200160002060000160046000858152602001908152602001600020600101818054806020026020016040519081016040528092919081815260200182805480156105b457602002820191906000526020600020905b8160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001906001019080831161056a575b505050505091508080548060200260200160405190810160405280929190818152602001828054801561060657602002820191906000526020600020905b8154815260200190600101908083116105f2575b5050505050905091509150915091565b606080600060016000858152602001908152602001600020600001805490501415151561064257600080fd5b600160008481526020019081526020016000206000016001600085815260200190815260200160002060010181805480602002602001604051908101604052809291908181526020016000905b8282101561074b578382906000526020600020018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156107375780601f1061070c57610100808354040283529160200191610737565b820191906000526020600020905b81548152906001019060200180831161071a57829003601f168201915b50505050508152602001906001019061068f565b5050505091508080548060200260200160405190810160405280929190818152602001828054801561079c57602002820191906000526020600020905b815481526020019060010190808311610788575b5050505050905091509150915091565b600080600060016000868152602001908152602001600020600001805490509150600160008681526020019081526020016000206001018054905090506001600086815260200190815260200160002060000184908060018154018082558091505090600182039060005260206000200160009091929091909150908051906020019061083a929190611347565b505060016000868152602001908152602001600020600101429080600181540180825580915050906001820390600052602060002001600090919290919091505550808214151561088a57600080fd5b7fc90342a4ea4dec8502f9ac70c2bc2ee8f070f9bf9daaf79fb4eed9d17e581fe7856040516108b99190611b93565b60405180910390a160019250505092915050565b600060036000838152602001908152602001600020600201549050919050565b8060056000848152602001908152602001600020819055505050565b606080600260008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600001600260008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600101818054806020026020016040519081016040528092919081815260200182805480156109db57602002820191906000526020600020905b8154815260200190600101908083116109c7575b5050505050915080805480602002602001604051908101604052809291908181526020016000905b82821015610abf578382906000526020600020018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610aab5780601f10610a8057610100808354040283529160200191610aab565b820191906000526020600020905b815481529060010190602001808311610a8e57829003601f168201915b505050505081526020019060010190610a03565b50505050905091509150915091565b60006003600083815260200190815260200160002060010160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050919050565b60006003600083815260200190815260200160002060070160009054906101000a900460ff169050919050565b600060056000838152602001908152602001600020549050919050565b60008160036000858152602001908152602001600020600201819055506001905092915050565b600460008481526020019081526020016000206000018290806001815401808255809150509060018203906000526020600020016000909192909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505060046000848152602001908152602001600020600101819080600181540180825580915050906001820390600052602060002001600090919290919091505550505050565b60016003600083815260200190815260200160002060010160146101000a81548161ffff021916908360010b61ffff16021790555050565b6000610c806113c7565b600360008054815260200190815260200160002060070160009054906101000a900460ff1615801515610cb257600080fd5b600087111515610cc157600080fd5b6101206040519081016040528060005481526020018973ffffffffffffffffffffffffffffffffffffffff168152602001600060010b81526020018881526020014281526020018781526020018681526020018581526020016001151581525091508160036000805481526020019081526020016000206000820151816000015560208201518160010160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060408201518160010160146101000a81548161ffff021916908360010b61ffff160217905550606082015181600201556080820151816003015560a0820151816004019080519060200190610ddf92919061142f565b5060c0820151816005019080519060200190610dfc92919061142f565b5060e0820151816006019080519060200190610e1992919061142f565b506101008201518160070160006101000a81548160ff021916908315150217905550905050610e4b8860005488610f03565b7fc90342a4ea4dec8502f9ac70c2bc2ee8f070f9bf9daaf79fb4eed9d17e581fe7600054604051610e7c9190611bc1565b60405180910390a16000808154809291906001019190505550505095945050505050565b600060046000838152602001908152602001600020600101805490506004600084815260200190815260200160002060000180549050141515610ee257600080fd5b60046000838152602001908152602001600020600001805490509050919050565b600260008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600001829080600181540180825580915050906001820390600052602060002001600090919290919091505550600260008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206001018190806001815401808255809150509060018203906000526020600020016000909192909190915090","80519060200190610fec929190611347565b5050505050565b60608060606003600085815260200190815260200160002060070160009054906101000a900460ff1680151561102857600080fd5b600360008681526020019081526020016000206004016003600087815260200190815260200160002060050160036000888152602001908152602001600020600601828054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156110ff5780601f106110d4576101008083540402835291602001916110ff565b820191906000526020600020905b8154815290600101906020018083116110e257829003601f168201915b50505050509250818054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561119b5780601f106111705761010080835404028352916020019161119b565b820191906000526020600020905b81548152906001019060200180831161117e57829003601f168201915b50505050509150808054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156112375780601f1061120c57610100808354040283529160200191611237565b820191906000526020600020905b81548152906001019060200180831161121a57829003601f168201915b50505050509050935093509350509193909250565b60008060008060008060006003600089815260200190815260200160002060070160009054906101000a900460ff1680151561128757600080fd5b61129089610ea0565b925061129b89610b3b565b9150600360008a815260200190815260200160002060000154600360008b815260200190815260200160002060010160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16600360008c81526020019081526020016000206002015485859750975097509750975050505091939590929450565b60006003600083815260200190815260200160002060010160149054906101000a900460010b9050919050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061138857805160ff19168380011785556113b6565b828001600101855582156113b6579182015b828111156113b557825182559160200191906001019061139a565b5b5090506113c391906114af565b5090565b6101206040519081016040528060008152602001600073ffffffffffffffffffffffffffffffffffffffff168152602001600060010b815260200160008152602001600081526020016060815260200160608152602001606081526020016000151581525090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061147057805160ff191683800117855561149e565b8280016001018555821561149e579182015b8281111561149d578251825591602001919060010190611482565b5b5090506114ab91906114af565b5090565b6114d191905b808211156114cd5760008160009055506001016114b5565b5090565b90565b60006114e08235611d10565b905092915050565b600082601f83011215156114fb57600080fd5b813561150e61150982611c1c565b611bef565b9150808252602083016020830185838301111561152a57600080fd5b611535838284611d3a565b50505092915050565b600061154a8235611d30565b905092915050565b60006020828403121561156457600080fd5b6000611572848285016114d4565b91505092915050565b60008060006060848603121561159057600080fd5b600061159e868287016114d4565b93505060206115af8682870161153e565b925050604084013567ffffffffffffffff8111156115cc57600080fd5b6115d8868287016114e8565b9150509250925092565b600080600080600060a086880312156115fa57600080fd5b6000611608888289016114d4565b95505060206116198882890161153e565b945050604086013567ffffffffffffffff81111561163657600080fd5b611642888289016114e8565b935050606086013567ffffffffffffffff81111561165f57600080fd5b61166b888289016114e8565b925050608086013567ffffffffffffffff81111561168857600080fd5b611694888289016114e8565b9150509295509295909350565b6000602082840312156116b357600080fd5b60006116c18482850161153e565b91505092915050565b6000806000606084860312156116df57600080fd5b60006116ed8682870161153e565b93505060206116fe868287016114d4565b925050604061170f8682870161153e565b9150509250925092565b6000806040838503121561172c57600080fd5b600061173a8582860161153e565b925050602083013567ffffffffffffffff81111561175757600080fd5b611763858286016114e8565b9150509250929050565b6000806040838503121561178057600080fd5b600061178e8582860161153e565b925050602061179f8582860161153e565b9150509250929050565b6117b281611ccd565b82525050565b60006117c382611c6f565b8084526020840193506117d583611c48565b60005b82811015611807576117eb8683516117a9565b6117f482611ca6565b91506020860195506001810190506117d8565b50849250505092915050565b600061181e82611c7a565b8084526020840193508360208202850161183785611c55565b60005b84811015611870578383038852611852838351611930565b925061185d82611cb3565b915060208801975060018101905061183a565b508196508694505050505092915050565b600061188c82611c85565b80845260208401935061189e83611c62565b60005b828110156118d0576118b48683516119d4565b6118bd82611cc0565b91506020860195506001810190506118a1565b50849250505092915050565b6118e581611ced565b82525050565b6118f481611cf9565b82525050565b600061190582611c9b565b808452611919816020860160208601611d49565b61192281611d7c565b602085010191505092915050565b600061193b82611c90565b80845261194f816020860160208601611d49565b61195881611d7c565b602085010191505092915050565b6000600e82527f4d616b652070726f6772657373210000000000000000000000000000000000006020830152604082019050919050565b6000600c82527f4e65772050726f6772616d2100000000000000000000000000000000000000006020830152604082019050919050565b6119dd81611d06565b82525050565b60006020820190506119f860008301846117a9565b92915050565b60006040820190508181036000830152611a1881856117b8565b90508181036020830152611a2c8184611881565b90509392505050565b60006040820190508181036000830152611a4f8185611813565b90508181036020830152611a638184611881565b90509392505050565b60006040820190508181036000830152611a868185611881565b90508181036020830152611a9a8184611813565b90509392505050565b6000602082019050611ab860008301846118dc565b92915050565b6000602082019050611ad360008301846118eb565b92915050565b60006060820190508181036000830152611af381866118fa565b90508181036020830152611b0781856118fa565b90508181036040830152611b1b81846118fa565b9050949350505050565b6000602082019050611b3a60008301846119d4565b92915050565b600060a082019050611b5560008301886119d4565b611b6260208301876117a9565b611b6f60408301866119d4565b611b7c60608301856119d4565b611b8960808301846119d4565b9695505050505050565b6000604082019050611ba860008301846119d4565b8181036020830152611bb981611966565b905092915050565b6000604082019050611bd660008301846119d4565b8181036020830152611be78161199d565b905092915050565b6000604051905081810181811067ffffffffffffffff82111715611c1257600080fd5b8060405250919050565b600067ffffffffffffffff821115611c3357600080fd5b601f19601f8301169050602081019050919050565b6000602082019050919050565b6000602082019050919050565b6000602082019050919050565b600081519050919050565b600081519050919050565b600081519050919050565b600081519050919050565b600081519050919050565b6000602082019050919050565b6000602082019050919050565b6000602082019050919050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b60008115159050919050565b60008160010b9050919050565b6000819050919050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b6000819050919050565b82818337600083830152505050565b60005b83811015611d67578082015181840152602081019050611d4c565b83811115611d76576000848401525b50505050565b6000601f19601f83011690509190505600a265627a7a723058200430428a8eb520d24e980974c101d98bb84d8be5e15ad5f1d129c3865403975a6c6578706572696d656e74616cf50037"};

    public static final String BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {};

    public static final String SM_BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"constant\":true,\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\"}],\"name\":\"getdevoter\",\"outputs\":[{\"name\":\"\",\"type\":\"address[]\"},{\"name\":\"\",\"type\":\"uint256[]\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\"}],\"name\":\"getprocess\",\"outputs\":[{\"name\":\"\",\"type\":\"string[]\"},{\"name\":\"\",\"type\":\"uint256[]\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\"},{\"name\":\"_describe\",\"type\":\"string\"}],\"name\":\"implement\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\"}],\"name\":\"getvalue\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\"},{\"name\":\"_value\",\"type\":\"uint256\"}],\"name\":\"changetotal\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"addr\",\"type\":\"address\"}],\"name\":\"getprogram\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256[]\"},{\"name\":\"\",\"type\":\"string[]\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\"}],\"name\":\"getaddr\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\"}],\"name\":\"getisexist\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\"}],\"name\":\"gettotal\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\"},{\"name\":\"_value\",\"type\":\"uint256\"}],\"name\":\"changevalue\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\"},{\"name\":\"addr\",\"type\":\"address\"},{\"name\":\"_amount\",\"type\":\"uint256\"}],\"name\":\"adddevoter\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\"}],\"name\":\"changestatus\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"addr\",\"type\":\"address\"},{\"name\":\"totalneed\",\"type\":\"uint256\"},{\"name\":\"title\",\"type\":\"string\"},{\"name\":\"programtype\",\"type\":\"string\"},{\"name\":\"description\",\"type\":\"string\"}],\"name\":\"addProgram\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\"}],\"name\":\"getnumber\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"addr\",\"type\":\"address\"},{\"name\":\"id\",\"type\":\"uint256\"},{\"name\":\"title\",\"type\":\"string\"}],\"name\":\"addTolist\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\"}],\"name\":\"getstringinfor\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"},{\"name\":\"\",\"type\":\"string\"},{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\"}],\"name\":\"getinfor\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"},{\"name\":\"\",\"type\":\"address\"},{\"name\":\"\",\"type\":\"uint256\"},{\"name\":\"\",\"type\":\"uint256\"},{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\"}],\"name\":\"getstatus\",\"outputs\":[{\"name\":\"\",\"type\":\"int16\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"id\",\"type\":\"uint256\"},{\"indexed\":false,\"name\":\"commitmessage\",\"type\":\"string\"}],\"name\":\"Commit\",\"type\":\"event\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_GETDEVOTER = "getdevoter";

    public static final String FUNC_GETPROCESS = "getprocess";

    public static final String FUNC_IMPLEMENT = "implement";

    public static final String FUNC_GETVALUE = "getvalue";

    public static final String FUNC_CHANGETOTAL = "changetotal";

    public static final String FUNC_GETPROGRAM = "getprogram";

    public static final String FUNC_GETADDR = "getaddr";

    public static final String FUNC_GETISEXIST = "getisexist";

    public static final String FUNC_GETTOTAL = "gettotal";

    public static final String FUNC_CHANGEVALUE = "changevalue";

    public static final String FUNC_ADDDEVOTER = "adddevoter";

    public static final String FUNC_CHANGESTATUS = "changestatus";

    public static final String FUNC_ADDPROGRAM = "addProgram";

    public static final String FUNC_GETNUMBER = "getnumber";

    public static final String FUNC_ADDTOLIST = "addTolist";

    public static final String FUNC_GETSTRINGINFOR = "getstringinfor";

    public static final String FUNC_GETINFOR = "getinfor";

    public static final String FUNC_GETSTATUS = "getstatus";

    public static final Event COMMIT_EVENT = new Event("Commit", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
    ;

    protected Program(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public Tuple2<List<String>, List<BigInteger>> getdevoter(BigInteger id) throws ContractException {
        final Function function = new Function(FUNC_GETDEVOTER, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(id)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {}, new TypeReference<DynamicArray<Uint256>>() {}));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple2<List<String>, List<BigInteger>>(
                convertToNative((List<Address>) results.get(0).getValue()), 
                convertToNative((List<Uint256>) results.get(1).getValue()));
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

    public TransactionReceipt implement(BigInteger id, String _describe) {
        final Function function = new Function(
                FUNC_IMPLEMENT, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(id), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(_describe)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void implement(BigInteger id, String _describe, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_IMPLEMENT, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(id), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(_describe)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForImplement(BigInteger id, String _describe) {
        final Function function = new Function(
                FUNC_IMPLEMENT, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(id), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(_describe)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple2<BigInteger, String> getImplementInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_IMPLEMENT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple2<BigInteger, String>(

                (BigInteger) results.get(0).getValue(), 
                (String) results.get(1).getValue()
                );
    }

    public Tuple1<Boolean> getImplementOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_IMPLEMENT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<Boolean>(

                (Boolean) results.get(0).getValue()
                );
    }

    public BigInteger getvalue(BigInteger id) throws ContractException {
        final Function function = new Function(FUNC_GETVALUE, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(id)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public TransactionReceipt changetotal(BigInteger id, BigInteger _value) {
        final Function function = new Function(
                FUNC_CHANGETOTAL, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(id), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void changetotal(BigInteger id, BigInteger _value, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_CHANGETOTAL, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(id), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_value)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForChangetotal(BigInteger id, BigInteger _value) {
        final Function function = new Function(
                FUNC_CHANGETOTAL, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(id), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_value)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple2<BigInteger, BigInteger> getChangetotalInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_CHANGETOTAL, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple2<BigInteger, BigInteger>(

                (BigInteger) results.get(0).getValue(), 
                (BigInteger) results.get(1).getValue()
                );
    }

    public Tuple2<List<BigInteger>, List<String>> getprogram(String addr) throws ContractException {
        final Function function = new Function(FUNC_GETPROGRAM, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(addr)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Uint256>>() {}, new TypeReference<DynamicArray<Utf8String>>() {}));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple2<List<BigInteger>, List<String>>(
                convertToNative((List<Uint256>) results.get(0).getValue()), 
                convertToNative((List<Utf8String>) results.get(1).getValue()));
    }

    public String getaddr(BigInteger id) throws ContractException {
        final Function function = new Function(FUNC_GETADDR, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(id)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeCallWithSingleValueReturn(function, String.class);
    }

    public Boolean getisexist(BigInteger id) throws ContractException {
        final Function function = new Function(FUNC_GETISEXIST, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(id)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeCallWithSingleValueReturn(function, Boolean.class);
    }

    public BigInteger gettotal(BigInteger id) throws ContractException {
        final Function function = new Function(FUNC_GETTOTAL, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(id)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
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

    public Tuple1<Boolean> getChangevalueOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_CHANGEVALUE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<Boolean>(

                (Boolean) results.get(0).getValue()
                );
    }

    public TransactionReceipt adddevoter(BigInteger id, String addr, BigInteger _amount) {
        final Function function = new Function(
                FUNC_ADDDEVOTER, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(id), 
                new org.fisco.bcos.sdk.abi.datatypes.Address(addr), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void adddevoter(BigInteger id, String addr, BigInteger _amount, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_ADDDEVOTER, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(id), 
                new org.fisco.bcos.sdk.abi.datatypes.Address(addr), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_amount)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForAdddevoter(BigInteger id, String addr, BigInteger _amount) {
        final Function function = new Function(
                FUNC_ADDDEVOTER, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(id), 
                new org.fisco.bcos.sdk.abi.datatypes.Address(addr), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_amount)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple3<BigInteger, String, BigInteger> getAdddevoterInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_ADDDEVOTER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple3<BigInteger, String, BigInteger>(

                (BigInteger) results.get(0).getValue(), 
                (String) results.get(1).getValue(), 
                (BigInteger) results.get(2).getValue()
                );
    }

    public TransactionReceipt changestatus(BigInteger id) {
        final Function function = new Function(
                FUNC_CHANGESTATUS, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(id)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void changestatus(BigInteger id, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_CHANGESTATUS, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(id)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForChangestatus(BigInteger id) {
        final Function function = new Function(
                FUNC_CHANGESTATUS, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(id)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple1<BigInteger> getChangestatusInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_CHANGESTATUS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<BigInteger>(

                (BigInteger) results.get(0).getValue()
                );
    }

    public TransactionReceipt addProgram(String addr, BigInteger totalneed, String title, String programtype, String description) {
        final Function function = new Function(
                FUNC_ADDPROGRAM, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(addr), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(totalneed), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(title), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(programtype), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(description)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void addProgram(String addr, BigInteger totalneed, String title, String programtype, String description, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_ADDPROGRAM, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(addr), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(totalneed), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(title), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(programtype), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(description)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForAddProgram(String addr, BigInteger totalneed, String title, String programtype, String description) {
        final Function function = new Function(
                FUNC_ADDPROGRAM, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(addr), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(totalneed), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(title), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(programtype), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(description)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple5<String, BigInteger, String, String, String> getAddProgramInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_ADDPROGRAM, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple5<String, BigInteger, String, String, String>(

                (String) results.get(0).getValue(), 
                (BigInteger) results.get(1).getValue(), 
                (String) results.get(2).getValue(), 
                (String) results.get(3).getValue(), 
                (String) results.get(4).getValue()
                );
    }

    public Tuple1<Boolean> getAddProgramOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_ADDPROGRAM, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<Boolean>(

                (Boolean) results.get(0).getValue()
                );
    }

    public BigInteger getnumber(BigInteger id) throws ContractException {
        final Function function = new Function(FUNC_GETNUMBER, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(id)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public TransactionReceipt addTolist(String addr, BigInteger id, String title) {
        final Function function = new Function(
                FUNC_ADDTOLIST, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(addr), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(id), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(title)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void addTolist(String addr, BigInteger id, String title, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_ADDTOLIST, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(addr), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(id), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(title)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForAddTolist(String addr, BigInteger id, String title) {
        final Function function = new Function(
                FUNC_ADDTOLIST, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(addr), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(id), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(title)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple3<String, BigInteger, String> getAddTolistInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_ADDTOLIST, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple3<String, BigInteger, String>(

                (String) results.get(0).getValue(), 
                (BigInteger) results.get(1).getValue(), 
                (String) results.get(2).getValue()
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

    public BigInteger getstatus(BigInteger id) throws ContractException {
        final Function function = new Function(FUNC_GETSTATUS, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(id)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Int16>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public List<CommitEventResponse> getCommitEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(COMMIT_EVENT, transactionReceipt);
        ArrayList<CommitEventResponse> responses = new ArrayList<CommitEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            CommitEventResponse typedResponse = new CommitEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.id = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.commitmessage = (String) eventValues.getNonIndexedValues().get(1).getValue();
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

    public static Program load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new Program(contractAddress, client, credential);
    }

    public static Program deploy(Client client, CryptoKeyPair credential) throws ContractException {
        return deploy(Program.class, client, credential, getBinary(client.getCryptoSuite()), "");
    }

    public static class CommitEventResponse {
        public TransactionReceipt.Logs log;

        public BigInteger id;

        public String commitmessage;
    }
}
