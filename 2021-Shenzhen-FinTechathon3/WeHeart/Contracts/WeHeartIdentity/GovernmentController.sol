pragma solidity ^ 0.4.4;


import "./GovernmentData.sol";
import "./RoleController.sol";

/**
 * @title GovernmentControllerGovernment
 * Issuer contract manages authority issuer info.
 */

contract GovernmentController{

    GovernmentData private governmentData;
    RoleController private roleController;

    // Event structure to store tx records
    uint constant private OPERATION_ADD = 0;
    uint constant private OPERATION_REMOVE = 1;
    uint constant private EMPTY_ARRAY_SIZE = 1;

    event GovernmentRetLog(uint operation, uint retCode, address addr);

    // Constructor.
    function GovernmentController(
        address governmentDataAddress,
        address roleControllerAddress
    )
        public
    {
        governmentData = GovernmentData(governmentDataAddress);
        roleController = RoleController(roleControllerAddress);
    }

    function addGovernment(
        address addr,
        bytes32[16] attribBytes32,
        int[16] attribInt,
        bytes accValue
    )
        public
    {
        uint result = governmentData.addGovernmentFromAddress(addr, attribBytes32, attribInt, accValue);
        GovernmentRetLog(OPERATION_ADD, result, addr);
    }

    function recognizeGovernment(address addr) public {
        uint result = governmentData.recognizeGovernment(addr);
        GovernmentRetLog(OPERATION_ADD, result, addr);
    }

    function deRecognizeGovernment(address addr) public {
        uint result = governmentData.deRecognizeGovernment(addr);
        GovernmentRetLog(OPERATION_REMOVE, result, addr);
    }

    function removeGovernment(address addr) public {
        if (!roleController.checkPermission(tx.origin, roleController.MODIFY_GOVERNMENT())) {
            GovernmentRetLog(OPERATION_REMOVE, roleController.RETURN_CODE_FAILURE_NO_PERMISSION(), addr);
            return;
        }
        uint result = governmentData.deleteGovernmentFromAddress(addr);
        GovernmentRetLog(OPERATION_REMOVE, result, addr);
    }

    function getTotalIssuer() public constant returns(uint) {
        return governmentData.getDatasetLength();
    }

    function getGovernmentAddressList(
        uint startPos,
        uint num
    )
        public
        constant
        returns(address[])
    {
        uint totalLength = governmentData.getDatasetLength();

        uint dataLength;
        // Calculate actual dataLength
        if (totalLength < startPos) {
            return new address[](EMPTY_ARRAY_SIZE);
        }
     else if (totalLength <= startPos + num) {
      dataLength = totalLength - startPos;
    }
    else {
     dataLength = num;
    }
    
    address[] memory issuerArray = new address[](dataLength);
    for (uint index = 0; index < dataLength; index++) {
        issuerArray[index] = governmentData.getGovernmentFromIndex(startPos + index);
    }
    return issuerArray;
    }
    
    function getGovernmentInfoNonAccValue(
        address addr
    )
        public
        constant
        returns(bytes32[], int[])
    {
        // Due to the current limitations of bcos web3j, return dynamic bytes32 and int array instead.
        bytes32[16] memory allBytes32;
        int[16] memory allInt;
        (allBytes32, allInt) = governmentData.getGovernmentInfoNonAccValue(addr);
        bytes32[] memory finalBytes32 = new bytes32[](16);
        int[] memory finalInt = new int[](16);
        for (uint index = 0; index < 16; index++) {
            finalBytes32[index] = allBytes32[index];
            finalInt[index] = allInt[index];
        }
        return (finalBytes32, finalInt);
    }
    
    function isGovernment(
        address addr
    )
        public
        constant
        returns(bool)
    {
        return governmentData.isGovernment(addr);
    }
    
    function getAddressFromName(
        bytes32 name
    )
        public
        constant
        returns(address)
    {
        return governmentData.getAddressFromName(name);
    }
    
    
    function getGovernmentFromIndex(
        uint index
    )
        public
        constant
        returns(address)
    {
        return governmentData.getGovernmentFromIndex(index);
    }
    
    }