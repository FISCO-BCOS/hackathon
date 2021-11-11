pragma solidity ^0.4.4;

import "./RoleController.sol";

contract WeIdContract {

    RoleController private roleController;

    mapping(address => uint) changed;

    uint firstBlockNum;

    uint lastBlockNum;
    
    uint weIdCount = 0;

    mapping(uint => uint) blockAfterLink;

    modifier onlyOwner(address identity, address actor) {
        require (actor == identity);
        _;
    }

    bytes32 constant private WEID_KEY_CREATED = "created";
    bytes32 constant private WEID_KEY_AUTHENTICATION = "/weId/auth";

    // Constructor - Role controller is required in delegate calls
    function WeIdContract(
        address roleControllerAddress
    )
        public
    {
        roleController = RoleController(roleControllerAddress);
        firstBlockNum = block.number;
        lastBlockNum = firstBlockNum;
    }

    event WeIdAttributeChanged(
        address indexed identity,
        bytes32 key,
        bytes value,
        uint previousBlock,
        int updated
    );

    event WeIdHistoryEvent(
        address indexed identity,
        uint previousBlock,
        int created
    );

    function getLatestRelatedBlock(
        address identity
    ) 
        public 
        constant 
        returns (uint) 
    {
        return changed[identity];
    }

    function getFirstBlockNum() 
        public 
        constant 
        returns (uint) 
    {
        return firstBlockNum;
    }

    function getLatestBlockNum() 
        public 
        constant 
        returns (uint) 
    {
        return lastBlockNum;
    }

    function getNextBlockNumByBlockNum(uint currentBlockNum) 
        public 
        constant 
        returns (uint) 
    {
        return blockAfterLink[currentBlockNum];
    }

    function getWeIdCount() 
        public 
        constant 
        returns (uint) 
    {
        return weIdCount;
    }

    function createWeId(
        address identity,
        bytes auth,
        bytes created,
        int updated
    )
        public
        onlyOwner(identity, msg.sender)
    {
        WeIdAttributeChanged(identity, WEID_KEY_CREATED, created, changed[identity], updated);
        WeIdAttributeChanged(identity, WEID_KEY_AUTHENTICATION, auth, changed[identity], updated);
        changed[identity] = block.number;
        if (block.number > lastBlockNum) {
            blockAfterLink[lastBlockNum] = block.number;
        }
        WeIdHistoryEvent(identity, lastBlockNum, updated);
        if (block.number > lastBlockNum) {
            lastBlockNum = block.number;
        }
        weIdCount++;
    }

    function delegateCreateWeId(
        address identity,
        bytes auth,
        bytes created,
        int updated
    )
        public
    {
        if (roleController.checkPermission(msg.sender, roleController.MODIFY_GOVERNMENT())) {
            WeIdAttributeChanged(identity, WEID_KEY_CREATED, created, changed[identity], updated);
            WeIdAttributeChanged(identity, WEID_KEY_AUTHENTICATION, auth, changed[identity], updated);
            changed[identity] = block.number;
            if (block.number > lastBlockNum) {
                blockAfterLink[lastBlockNum] = block.number;
            }
            WeIdHistoryEvent(identity, lastBlockNum, updated);
            if (block.number > lastBlockNum) {
                lastBlockNum = block.number;
            }
            weIdCount++;
        }
    }

    function setAttribute(
        address identity, 
        bytes32 key, 
        bytes value, 
        int updated
    ) 
        public 
        onlyOwner(identity, msg.sender)
    {
        WeIdAttributeChanged(identity, key, value, changed[identity], updated);
        changed[identity] = block.number;
    }

    function delegateSetAttribute(
        address identity,
        bytes32 key,
        bytes value,
        int updated
    )
        public
    {
        if (roleController.checkPermission(msg.sender, roleController.MODIFY_GOVERNMENT())) {
            WeIdAttributeChanged(identity, key, value, changed[identity], updated);
            changed[identity] = block.number;
        }
    }

    function isIdentityExist(
        address identity
    ) 
        public 
        constant 
        returns (bool) 
    {
        if (0x0 != identity && 0 != changed[identity]) {
            return true;
    }
        return false;
    }
}
