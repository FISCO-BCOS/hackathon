pragma solidity ^ 0.4.4;



import "./RoleController.sol";

/**
 * @title GovernmentData
 * Authority Issuer data contract.
 */

contract GovernmentData{

    // Error codes
    uint constant private RETURN_CODE_SUCCESS = 0;
    uint constant private RETURN_CODE_FAILURE_ALREADY_EXISTS = 500201;
    uint constant private RETURN_CODE_FAILURE_NOT_EXIST = 500202;
    uint constant private RETURN_CODE_NAME_ALREADY_EXISTS = 500203;
    uint constant private RETURN_CODE_UNRECOGNIZED = 500204;

    struct Government {
        // [0]: name, [1]: desc, [2-11]: extra string
        bytes32[16] attribBytes32;
        // [0]: create date, [1]: update date, [2-11]: extra int
        // [15]: flag for recognition status (0: unrecognized, 1: recognized)
        int[16] attribInt;
        bytes accValue;
    }

    mapping(address => Government) private governmentMap;
    address[] private governmentArray;
    mapping(bytes32 => address) private uniqueNameMap;

    RoleController private roleController;

    // Constructor
    function GovernmentData(address addr) public {
        roleController = RoleController(addr);
    }

    function isGovernment(
        address addr
    )
        public
        constant
        returns(bool)
    {
        if (!roleController.checkRole(addr, roleController.ROLE_GOVERNMENT())) {
            return false;
        }
        if (governmentMap[addr].attribBytes32[0] == bytes32(0)) {
            return false;
        }
        return true;
    }

    function addGovernmentFromAddress(
        address addr,
        bytes32[16] attribBytes32,
        int[16] attribInt,
        bytes accValue
    )
        public
        returns(uint)
    {
        if (governmentMap[addr].attribBytes32[0] != bytes32(0)) {
            return RETURN_CODE_FAILURE_ALREADY_EXISTS;
        }
        if (isNameDuplicate(attribBytes32[0])) {
            return RETURN_CODE_NAME_ALREADY_EXISTS;
        }

        // Actual Role must be granted by calling recognizeGovernment()
        // roleController.addRole(addr, roleController.ROLE_GOVERNMENT());

        Government memory government = Government(attribBytes32, attribInt, accValue);
        governmentMap[addr] = government;
        governmentArray.push(addr);
        uniqueNameMap[attribBytes32[0]] = addr;
        return RETURN_CODE_SUCCESS;
    }

    function recognizeGovernment(address addr) public returns(uint) {
        if (!roleController.checkPermission(tx.origin, roleController.MODIFY_GOVERNMENT())) {
            return roleController.RETURN_CODE_FAILURE_NO_PERMISSION();
        }
        if (governmentMap[addr].attribBytes32[0] == bytes32(0)) {
            return RETURN_CODE_FAILURE_NOT_EXIST;
        }
        // Set role and flag
        roleController.addRole(addr, roleController.ROLE_GOVERNMENT());
        governmentMap[addr].attribInt[15] = int(1);
        return RETURN_CODE_SUCCESS;
    }

    function deRecognizeGovernment(address addr) public returns(uint) {
        if (!roleController.checkPermission(tx.origin, roleController.MODIFY_GOVERNMENT())) {
            return roleController.RETURN_CODE_FAILURE_NO_PERMISSION();
        }
        // Remove role and flag
        roleController.removeRole(addr, roleController.ROLE_GOVERNMENT());
        governmentMap[addr].attribInt[15] = int(0);
        return RETURN_CODE_SUCCESS;
    }

    function deleteGovernmentFromAddress(
        address addr
    )
        public
        returns(uint)
    {
        if (governmentMap[addr].attribBytes32[0] == bytes32(0)) {
            return RETURN_CODE_FAILURE_NOT_EXIST;
        }
        if (!roleController.checkPermission(tx.origin, roleController.MODIFY_GOVERNMENT())) {
            return roleController.RETURN_CODE_FAILURE_NO_PERMISSION();
        }
        roleController.removeRole(addr, roleController.ROLE_GOVERNMENT());
        uniqueNameMap[governmentMap[addr].attribBytes32[0]] = address(0x0);
        delete governmentMap[addr];
        uint datasetLength = governmentArray.length;
        for (uint index = 0; index < datasetLength; index++) {
            if (governmentArray[index] == addr) {
                break;
            }
        }
        if (index != datasetLength - 1) {
            governmentArray[index] = governmentArray[datasetLength - 1];
        }
        delete governmentArray[datasetLength - 1];
        governmentArray.length--;
        return RETURN_CODE_SUCCESS;
    }

    function getDatasetLength()
        public
        constant
        returns(uint)
    {
        return governmentArray.length;
    }

    function getGovernmentFromIndex(
        uint index
    )
        public
        constant
        returns(address)
    {
        return governmentArray[index];
    }

    function getGovernmentInfoNonAccValue(
        address addr
    )
        public
        constant
        returns(bytes32[16], int[16])
    {
        bytes32[16] memory allBytes32;
        int[16] memory allInt;
        for (uint index = 0; index < 16; index++) {
            allBytes32[index] = governmentMap[addr].attribBytes32[index];
            allInt[index] = governmentMap[addr].attribInt[index];
        }
        return (allBytes32, allInt);
    }

    function getGovernmentInfoAccValue(
        address addr
    )
        public
        constant
        returns(bytes)
    {
        return governmentMap[addr].accValue;
    }

    function isNameDuplicate(
        bytes32 name
    )
        public
        constant
        returns(bool)
    {
        if (uniqueNameMap[name] == address(0x0)) {
            return false;
        }
        return true;
    }

    function getAddressFromName(
        bytes32 name
    )
        public
        constant
        returns(address)
    {
        return uniqueNameMap[name];
    }
}