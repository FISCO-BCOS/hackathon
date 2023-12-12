package org.fisco.bcos.upload.contract;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.abi.FunctionReturnDecoder;
import org.fisco.bcos.sdk.abi.TypeReference;
import org.fisco.bcos.sdk.abi.datatypes.Event;
import org.fisco.bcos.sdk.abi.datatypes.Function;
import org.fisco.bcos.sdk.abi.datatypes.Type;
import org.fisco.bcos.sdk.abi.datatypes.Utf8String;
import org.fisco.bcos.sdk.abi.datatypes.generated.Int256;
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
public class HashUpload extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b5061002861002d640100000000026401000000009004565b610185565b600061100190508073ffffffffffffffffffffffffffffffffffffffff166356004b6a6040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808060200180602001806020018481038452600a8152602001807f686173685f7461626c6500000000000000000000000000000000000000000000815250602001848103835260078152602001807f686173685f6964000000000000000000000000000000000000000000000000008152506020018481038252600a8152602001807f686173685f76616c7565000000000000000000000000000000000000000000008152506020019350505050602060405180830381600087803b15801561014657600080fd5b505af115801561015a573d6000803e3d6000fd5b505050506040513d602081101561017057600080fd5b81019080805190602001909291905050505050565b6117a1806101946000396000f300608060405260043610610057576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680639c4712531461005c578063d0e9f2941461011f578063d951707e146101e2575b600080fd5b34801561006857600080fd5b50610109600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192905050506102cb565b6040518082815260200191505060405180910390f35b34801561012b57600080fd5b506101cc600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192905050506108c5565b6040518082815260200191505060405180910390f35b3480156101ee57600080fd5b50610249600480360381019080803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192905050506111b5565b6040518083815260200180602001828103825283818151815260200191508051906020019080838360005b8381101561028f578082015181840152602081019050610274565b50505050905090810190601f1680156102bc5780820380516001836020036101000a031916815260200191505b50935050505060405180910390f35b60008060006060600080600080955060009450602060405190810160405280600081525093506102fa896111b5565b809550819650505060008514151561078157610314611686565b92508273ffffffffffffffffffffffffffffffffffffffff166313db93466040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b15801561037a57600080fd5b505af115801561038e573d6000803e3d6000fd5b505050506040513d60208110156103a457600080fd5b810190808051906020019092919050505091508173ffffffffffffffffffffffffffffffffffffffff1663e942b5168a6040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808060200180602001838103835260078152602001807f686173685f696400000000000000000000000000000000000000000000000000815250602001838103825284818151815260200191508051906020019080838360005b8381101561047757808201518184015260208101905061045c565b50505050905090810190601f1680156104a45780820380516001836020036101000a031916815260200191505b509350505050600060405180830381600087803b1580156104c457600080fd5b505af11580156104d8573d6000803e3d6000fd5b505050508173ffffffffffffffffffffffffffffffffffffffff1663e942b516896040518263ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018080602001806020018381038352600a8152602001807f686173685f76616c756500000000000000000000000000000000000000000000815250602001838103825284818151815260200191508051906020019080838360005b8381101561059c578082015181840152602081019050610581565b50505050905090810190601f1680156105c95780820380516001836020036101000a031916815260200191505b509350505050600060405180830381600087803b1580156105e957600080fd5b505af11580156105fd573d6000803e3d6000fd5b505050508273ffffffffffffffffffffffffffffffffffffffff166331afac368a846040518363ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180806020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828103825284818151815260200191508051906020019080838360005b838110156106bc5780820151818401526020810190506106a1565b50505050905090810190601f1680156106e95780820380516001836020036101000a031916815260200191505b509350505050602060405180830381600087803b15801561070957600080fd5b505af115801561071d573d6000803e3d6000fd5b505050506040513d602081101561073357600080fd5b810190808051906020019092919050505090506001811415610758576000955061077c565b7ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe95505b6107a5565b7fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff95505b7ffa0a1f62761d4abaf92ec5fad9b5959e392dee4439856a01dbafb79c6c608286868a8a604051808481526020018060200180602001838103835285818151815260200191508051906020019080838360005b838110156108135780820151818401526020810190506107f8565b50505050905090810190601f1680156108405780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b8381101561087957808201518184015260208101905061085e565b50505050905090810190601f1680156108a65780820380516001836020036101000a031916815260200191505b509550505050505060405180910390a185965050505050505092915050565b60008060006060600080600080955060009450602060405190810160405280600081525093506108f4896111b5565b8095508196505050600085141515610a42577fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff95507fc66d2fdec13f2ffc68e88037860450d99cd67382fcb61f50e104aba48f43c15a858a8a604051808481526020018060200180602001838103835285818151815260200191508051906020019080838360005b8381101561099757808201518184015260208101905061097c565b50505050905090810190601f1680156109c45780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b838110156109fd5780820151818401526020810190506109e2565b50505050905090810190601f168015610a2a5780820380516001836020036101000a031916815260200191505b509550505050505060405180910390a18596506111a9565b610a4a611686565b92508273ffffffffffffffffffffffffffffffffffffffff166313db93466040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b158015610ab057600080fd5b505af1158015610ac4573d6000803e3d6000fd5b505050506040513d6020811015610ada57600080fd5b810190808051906020019092919050505091508173ffffffffffffffffffffffffffffffffffffffff1663e942b5168a6040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808060200180602001838103835260078152602001807f686173685f696400000000000000000000000000000000000000000000000000815250602001838103825284818151815260200191508051906020019080838360005b83811015610bad578082015181840152602081019050610b92565b50505050905090810190601f168015610bda5780820380516001836020036101000a031916815260200191505b509350505050600060405180830381600087803b158015610bfa57600080fd5b505af1158015610c0e573d6000803e3d6000fd5b505050508173ffffffffffffffffffffffffffffffffffffffff1663e942b516896040518263ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018080602001806020018381038352600a8152602001807f686173685f76616c756500000000000000000000000000000000000000000000815250602001838103825284818151815260200191508051906020019080838360005b83811015610cd2578082015181840152602081019050610cb7565b50505050905090810190601f168015610cff5780820380516001836020036101000a031916815260200191505b509350505050600060405180830381600087803b158015610d1f57600080fd5b505af1158015610d33573d6000803e3d6000fd5b505050508273ffffffffffffffffffffffffffffffffffffffff1663bf2b70a18a848673ffffffffffffffffffffffffffffffffffffffff16637857d7c96040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b158015610db957600080fd5b505af1158015610dcd573d6000803e3d6000fd5b505050506040513d6020811015610de357600080fd5b81019080805190602001909291905050506040518463ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180806020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffff","ffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828103825285818151815260200191508051906020019080838360005b83811015610ec3578082015181840152602081019050610ea8565b50505050905090810190601f168015610ef05780820380516001836020036101000a031916815260200191505b50945050505050602060405180830381600087803b158015610f1157600080fd5b505af1158015610f25573d6000803e3d6000fd5b505050506040513d6020811015610f3b57600080fd5b81019080805190602001909291905050509050600181141515611094577ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe95507fc66d2fdec13f2ffc68e88037860450d99cd67382fcb61f50e104aba48f43c15a858a8a604051808481526020018060200180602001838103835285818151815260200191508051906020019080838360005b83811015610fe9578082015181840152602081019050610fce565b50505050905090810190601f1680156110165780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b8381101561104f578082015181840152602081019050611034565b50505050905090810190601f16801561107c5780820380516001836020036101000a031916815260200191505b509550505050505060405180910390a18596506111a9565b7fc66d2fdec13f2ffc68e88037860450d99cd67382fcb61f50e104aba48f43c15a858a8a604051808481526020018060200180602001838103835285818151815260200191508051906020019080838360005b838110156111025780820151818401526020810190506110e7565b50505050905090810190601f16801561112f5780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b8381101561116857808201518184015260208101905061114d565b50505050905090810190601f1680156111955780820380516001836020036101000a031916815260200191505b509550505050505060405180910390a18596505b50505050505092915050565b60006060600080606060006111c8611686565b93508373ffffffffffffffffffffffffffffffffffffffff1663e8434e39888673ffffffffffffffffffffffffffffffffffffffff16637857d7c96040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b15801561124b57600080fd5b505af115801561125f573d6000803e3d6000fd5b505050506040513d602081101561127557600080fd5b81019080805190602001909291905050506040518363ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180806020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828103825284818151815260200191508051906020019080838360005b83811015611323578082015181840152602081019050611308565b50505050905090810190601f1680156113505780820380516001836020036101000a031916815260200191505b509350505050602060405180830381600087803b15801561137057600080fd5b505af1158015611384573d6000803e3d6000fd5b505050506040513d602081101561139a57600080fd5b81019080805190602001909291905050509250602060405190810160405280600081525091508273ffffffffffffffffffffffffffffffffffffffff1663949d225d6040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b15801561142457600080fd5b505af1158015611438573d6000803e3d6000fd5b505050506040513d602081101561144e57600080fd5b810190808051906020019092919050505060001415611495577fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff828191509550955061167d565b8273ffffffffffffffffffffffffffffffffffffffff1663846719e060006040518263ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180828152602001915050602060405180830381600087803b15801561150557600080fd5b505af1158015611519573d6000803e3d6000fd5b505050506040513d602081101561152f57600080fd5b8101908080519060200190929190505050905060008173ffffffffffffffffffffffffffffffffffffffff16639c981fcb6040518163ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180806020018281038252600a8152602001807f686173685f76616c756500000000000000000000000000000000000000000000815250602001915050600060405180830381600087803b1580156115e457600080fd5b505af11580156115f8573d6000803e3d6000fd5b505050506040513d6000823e3d601f19601f82011682018060405250602081101561162257600080fd5b81019080805164010000000081111561163a57600080fd5b8281019050602081018481111561165057600080fd5b815185600182028301116401000000008211171561166d57600080fd5b5050929190505050819150955095505b50505050915091565b600080600061100191508173ffffffffffffffffffffffffffffffffffffffff1663f23f63c96040518163ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180806020018281038252600a8152602001807f686173685f7461626c6500000000000000000000000000000000000000000000815250602001915050602060405180830381600087803b15801561173057600080fd5b505af1158015611744573d6000803e3d6000fd5b505050506040513d602081101561175a57600080fd5b810190808051906020019092919050505090508092505050905600a165627a7a7230582031405c597e427f4c8c91128f1ce9f705af911f73157e8d3be68e80f0295a227a0029"};

    public static final String BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"608060405234801561001057600080fd5b5061002861002d640100000000026401000000009004565b610185565b600061100190508073ffffffffffffffffffffffffffffffffffffffff1663c92a78016040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808060200180602001806020018481038452600a8152602001807f686173685f7461626c6500000000000000000000000000000000000000000000815250602001848103835260078152602001807f686173685f6964000000000000000000000000000000000000000000000000008152506020018481038252600a8152602001807f686173685f76616c7565000000000000000000000000000000000000000000008152506020019350505050602060405180830381600087803b15801561014657600080fd5b505af115801561015a573d6000803e3d6000fd5b505050506040513d602081101561017057600080fd5b81019080805190602001909291905050505050565b6117a1806101946000396000f300608060405260043610610057576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680636c2786f31461005c578063eca788ee1461011f578063f20b4ed0146101e2575b600080fd5b34801561006857600080fd5b50610109600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192905050506102cb565b6040518082815260200191505060405180910390f35b34801561012b57600080fd5b506101cc600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050610bbb565b6040518082815260200191505060405180910390f35b3480156101ee57600080fd5b50610249600480360381019080803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192905050506111b5565b6040518083815260200180602001828103825283818151815260200191508051906020019080838360005b8381101561028f578082015181840152602081019050610274565b50505050905090810190601f1680156102bc5780820380516001836020036101000a031916815260200191505b50935050505060405180910390f35b60008060006060600080600080955060009450602060405190810160405280600081525093506102fa896111b5565b8095508196505050600085141515610448577fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff95507f9fe4bfe4648aa1b9f99d98156abb00ac4d79f47b5c13758cecf7095c9ed45603858a8a604051808481526020018060200180602001838103835285818151815260200191508051906020019080838360005b8381101561039d578082015181840152602081019050610382565b50505050905090810190601f1680156103ca5780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b838110156104035780820151818401526020810190506103e8565b50505050905090810190601f1680156104305780820380516001836020036101000a031916815260200191505b509550505050505060405180910390a1859650610baf565b610450611686565b92508273ffffffffffffffffffffffffffffffffffffffff16635887ab246040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b1580156104b657600080fd5b505af11580156104ca573d6000803e3d6000fd5b505050506040513d60208110156104e057600080fd5b810190808051906020019092919050505091508173ffffffffffffffffffffffffffffffffffffffff16631a391cb48a6040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808060200180602001838103835260078152602001807f686173685f696400000000000000000000000000000000000000000000000000815250602001838103825284818151815260200191508051906020019080838360005b838110156105b3578082015181840152602081019050610598565b50505050905090810190601f1680156105e05780820380516001836020036101000a031916815260200191505b509350505050600060405180830381600087803b15801561060057600080fd5b505af1158015610614573d6000803e3d6000fd5b505050508173ffffffffffffffffffffffffffffffffffffffff16631a391cb4896040518263ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018080602001806020018381038352600a8152602001807f686173685f76616c756500000000000000000000000000000000000000000000815250602001838103825284818151815260200191508051906020019080838360005b838110156106d85780820151818401526020810190506106bd565b50505050905090810190601f1680156107055780820380516001836020036101000a031916815260200191505b509350505050600060405180830381600087803b15801561072557600080fd5b505af1158015610739573d6000803e3d6000fd5b505050508273ffffffffffffffffffffffffffffffffffffffff1663664b37d68a848673ffffffffffffffffffffffffffffffffffffffff1663c74f8caf6040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b1580156107bf57600080fd5b505af11580156107d3573d6000803e3d6000fd5b505050506040513d60208110156107e957600080fd5b81019080805190602001909291905050506040518463ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180806020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828103825285818151815260200191508051906020019080838360005b838110156108c95780820151818401526020810190506108ae565b50505050905090810190601f1680156108f65780820380516001836020036101000a031916815260200191505b50945050505050602060405180830381600087803b15801561091757600080fd5b505af115801561092b573d6000803e3d6000fd5b505050506040513d602081101561094157600080fd5b81019080805190602001909291905050509050600181141515610a9a577ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe95507f9fe4bfe4648aa1b9f99d98156abb00ac4d79f47b5c13758cecf7095c9ed45603858a8a604051808481526020018060200180602001838103835285818151815260200191508051906020019080838360005b838110156109ef5780820151818401526020810190506109d4565b50505050905090810190601f168015610a1c5780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b83811015610a55578082015181840152602081019050610a3a565b50505050905090810190601f168015610a825780820380516001836020036101000a031916815260200191505b509550505050505060405180910390a1859650610baf565b7f9fe4bfe4648aa1b9f99d98156abb00ac4d79f47b5c13758cecf7095c9ed45603858a8a604051808481526020018060200180602001838103835285818151815260200191508051906020019080838360005b83811015610b08578082015181840152602081019050610aed565b50505050905090810190601f168015610b355780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b83811015610b6e578082015181840152602081019050610b53565b50505050905090810190601f168015610b9b5780820380516001836020036101000a031916815260200191505b509550505050505060405180910390a18596505b50505050505092915050565b6000806000606060008060008095506000945060206040519081016040528060008152509350610bea896111b5565b809550819650505060008514151561107157610c04611686565b92508273ffffffffffffffffffffffffffffffffffffffff16635887ab246040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b158015610c6a57600080fd5b505af1158015610c7e573d6000803e3d6000fd5b505050506040513d6020811015610c9457600080fd5b810190808051906020019092919050505091508173ffffffffffffffffffffffffffffffffffffffff16631a391cb48a6040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808060200180602001838103835260078152602001807f686173685f696400000000000000000000000000000000000000000000000000815250602001838103825284818151815260200191508051906020019080838360005b83811015610d67578082015181840152602081019050610d4c565b50505050905090810190601f168015610d945780820380516001836020036101000a031916815260200191505b509350505050600060405180830381600087803b158015610db457600080fd5b505af1158015610dc8573d6000803e3d6000fd5b505050508173ffffffffffffffffffffffffffffffffffffffff16631a391cb4896040518263ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018080602001806020018381038352600a8152602001807f686173685f76616c7565000000000000000000000000000000000000000000008152506020018381038252848181518152602001915080519060200190","80838360005b83811015610e8c578082015181840152602081019050610e71565b50505050905090810190601f168015610eb95780820380516001836020036101000a031916815260200191505b509350505050600060405180830381600087803b158015610ed957600080fd5b505af1158015610eed573d6000803e3d6000fd5b505050508273ffffffffffffffffffffffffffffffffffffffff16634c6f30c08a846040518363ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180806020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828103825284818151815260200191508051906020019080838360005b83811015610fac578082015181840152602081019050610f91565b50505050905090810190601f168015610fd95780820380516001836020036101000a031916815260200191505b509350505050602060405180830381600087803b158015610ff957600080fd5b505af115801561100d573d6000803e3d6000fd5b505050506040513d602081101561102357600080fd5b810190808051906020019092919050505090506001811415611048576000955061106c565b7ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe95505b611095565b7fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff95505b7f9961298cae60b34a7a0f135ab27247be7349d2e7eb48f2854b98f2d6d8566ead868a8a604051808481526020018060200180602001838103835285818151815260200191508051906020019080838360005b838110156111035780820151818401526020810190506110e8565b50505050905090810190601f1680156111305780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b8381101561116957808201518184015260208101905061114e565b50505050905090810190601f1680156111965780820380516001836020036101000a031916815260200191505b509550505050505060405180910390a185965050505050505092915050565b60006060600080606060006111c8611686565b93508373ffffffffffffffffffffffffffffffffffffffff1663d8ac5957888673ffffffffffffffffffffffffffffffffffffffff1663c74f8caf6040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b15801561124b57600080fd5b505af115801561125f573d6000803e3d6000fd5b505050506040513d602081101561127557600080fd5b81019080805190602001909291905050506040518363ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180806020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828103825284818151815260200191508051906020019080838360005b83811015611323578082015181840152602081019050611308565b50505050905090810190601f1680156113505780820380516001836020036101000a031916815260200191505b509350505050602060405180830381600087803b15801561137057600080fd5b505af1158015611384573d6000803e3d6000fd5b505050506040513d602081101561139a57600080fd5b81019080805190602001909291905050509250602060405190810160405280600081525091508273ffffffffffffffffffffffffffffffffffffffff1663d3e9af5a6040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b15801561142457600080fd5b505af1158015611438573d6000803e3d6000fd5b505050506040513d602081101561144e57600080fd5b810190808051906020019092919050505060001415611495577fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff828191509550955061167d565b8273ffffffffffffffffffffffffffffffffffffffff16633dd2b61460006040518263ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180828152602001915050602060405180830381600087803b15801561150557600080fd5b505af1158015611519573d6000803e3d6000fd5b505050506040513d602081101561152f57600080fd5b8101908080519060200190929190505050905060008173ffffffffffffffffffffffffffffffffffffffff16639bca41e86040518163ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180806020018281038252600a8152602001807f686173685f76616c756500000000000000000000000000000000000000000000815250602001915050600060405180830381600087803b1580156115e457600080fd5b505af11580156115f8573d6000803e3d6000fd5b505050506040513d6000823e3d601f19601f82011682018060405250602081101561162257600080fd5b81019080805164010000000081111561163a57600080fd5b8281019050602081018481111561165057600080fd5b815185600182028301116401000000008211171561166d57600080fd5b5050929190505050819150955095505b50505050915091565b600080600061100191508173ffffffffffffffffffffffffffffffffffffffff166359a48b656040518163ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180806020018281038252600a8152602001807f686173685f7461626c6500000000000000000000000000000000000000000000815250602001915050602060405180830381600087803b15801561173057600080fd5b505af1158015611744573d6000803e3d6000fd5b505050506040513d602081101561175a57600080fd5b810190808051906020019092919050505090508092505050905600a165627a7a7230582085bf73f60a945741901bf748f77e3e0ad259778e7a6de5ad09c4c8bfe2ca4afd0029"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"constant\":false,\"inputs\":[{\"name\":\"hash_id\",\"type\":\"string\"},{\"name\":\"hash_value\",\"type\":\"string\"}],\"name\":\"uploadHash\",\"outputs\":[{\"name\":\"\",\"type\":\"int256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"hash_id\",\"type\":\"string\"},{\"name\":\"hash_value\",\"type\":\"string\"}],\"name\":\"modifyHash\",\"outputs\":[{\"name\":\"\",\"type\":\"int256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"hash_id\",\"type\":\"string\"}],\"name\":\"selectHash\",\"outputs\":[{\"name\":\"\",\"type\":\"int256\"},{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"ret\",\"type\":\"int256\"},{\"indexed\":false,\"name\":\"hash_id\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"hash_value\",\"type\":\"string\"}],\"name\":\"UploadHash\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"ret\",\"type\":\"int256\"},{\"indexed\":false,\"name\":\"hash_id\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"hash_value\",\"type\":\"string\"}],\"name\":\"ModifyHash\",\"type\":\"event\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_UPLOADHASH = "uploadHash";

    public static final String FUNC_MODIFYHASH = "modifyHash";

    public static final String FUNC_SELECTHASH = "selectHash";

    public static final Event UPLOADHASH_EVENT = new Event("UploadHash", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Int256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event MODIFYHASH_EVENT = new Event("ModifyHash", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Int256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

    protected HashUpload(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public TransactionReceipt uploadHash(String hash_id, String hash_value) {
        final Function function = new Function(
                FUNC_UPLOADHASH, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(hash_id), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(hash_value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] uploadHash(String hash_id, String hash_value, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_UPLOADHASH, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(hash_id), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(hash_value)), 
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForUploadHash(String hash_id, String hash_value) {
        final Function function = new Function(
                FUNC_UPLOADHASH, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(hash_id), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(hash_value)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple2<String, String> getUploadHashInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_UPLOADHASH, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple2<String, String>(

                (String) results.get(0).getValue(), 
                (String) results.get(1).getValue()
                );
    }

    public Tuple1<BigInteger> getUploadHashOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_UPLOADHASH, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Int256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<BigInteger>(

                (BigInteger) results.get(0).getValue()
                );
    }

    public TransactionReceipt modifyHash(String hash_id, String hash_value) {
        final Function function = new Function(
                FUNC_MODIFYHASH, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(hash_id), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(hash_value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] modifyHash(String hash_id, String hash_value, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_MODIFYHASH, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(hash_id), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(hash_value)), 
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForModifyHash(String hash_id, String hash_value) {
        final Function function = new Function(
                FUNC_MODIFYHASH, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(hash_id), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(hash_value)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple2<String, String> getModifyHashInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_MODIFYHASH, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple2<String, String>(

                (String) results.get(0).getValue(), 
                (String) results.get(1).getValue()
                );
    }

    public Tuple1<BigInteger> getModifyHashOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_MODIFYHASH, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Int256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<BigInteger>(

                (BigInteger) results.get(0).getValue()
                );
    }

    public Tuple2<BigInteger, String> selectHash(String hash_id) throws ContractException {
        final Function function = new Function(FUNC_SELECTHASH, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(hash_id)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Int256>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple2<BigInteger, String>(
                (BigInteger) results.get(0).getValue(), 
                (String) results.get(1).getValue());
    }

    public List<UploadHashEventResponse> getUploadHashEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(UPLOADHASH_EVENT, transactionReceipt);
        ArrayList<UploadHashEventResponse> responses = new ArrayList<UploadHashEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            UploadHashEventResponse typedResponse = new UploadHashEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.ret = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.hash_id = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.hash_value = (String) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeUploadHashEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(UPLOADHASH_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeUploadHashEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(UPLOADHASH_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public List<ModifyHashEventResponse> getModifyHashEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(MODIFYHASH_EVENT, transactionReceipt);
        ArrayList<ModifyHashEventResponse> responses = new ArrayList<ModifyHashEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ModifyHashEventResponse typedResponse = new ModifyHashEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.ret = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.hash_id = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.hash_value = (String) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeModifyHashEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(MODIFYHASH_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeModifyHashEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(MODIFYHASH_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public static HashUpload load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new HashUpload(contractAddress, client, credential);
    }

    public static HashUpload deploy(Client client, CryptoKeyPair credential) throws ContractException {
        return deploy(HashUpload.class, client, credential, getBinary(client.getCryptoSuite()), "");
    }

    public static class UploadHashEventResponse {
        public TransactionReceipt.Logs log;

        public BigInteger ret;

        public String hash_id;

        public String hash_value;
    }

    public static class ModifyHashEventResponse {
        public TransactionReceipt.Logs log;

        public BigInteger ret;

        public String hash_id;

        public String hash_value;
    }
}