// 此合约用于银行系统管理员注册
pragma solidity >=0.4.25 <0.9.0;

import "./SafeRole.sol";


contract WhitelistAdminRole {
    using SafeRole for SafeRole.Role;

    event WhitelistAdminAdded(address indexed account);
    event WhitelistAdminRemoved(address indexed account);

    SafeRole.Role private _whitelistAdmins;

    constructor() public {
        _addWhitelistAdmin(msg.sender);
    }

    modifier onlyWhitelistAdmin() {
        require(isWhitelistAdmin(msg.sender), "WhitelistAdminRole: caller does not have the WhitelistAdmin role");
        _;
    }

    function isWhitelistAdmin(address account) public view returns (bool) {
        return _whitelistAdmins.has(account);
    }

    function addWhitelistAdmin(address account) public onlyWhitelistAdmin returns (bool) {
        if (!isWhitelistAdmin(msg.sender)){
            return false;
        }
        return _addWhitelistAdmin(account);
    }

    function renounceWhitelistAdmin() public {
        _removeWhitelistAdmin(msg.sender);
    }

    function _addWhitelistAdmin(address account) internal returns(bool) {
        bool result = _whitelistAdmins.add(account);
        emit WhitelistAdminAdded(account);
        return result;
    }

    function _removeWhitelistAdmin(address account) internal {
        _whitelistAdmins.remove(account);
        emit WhitelistAdminRemoved(account);
    }
}