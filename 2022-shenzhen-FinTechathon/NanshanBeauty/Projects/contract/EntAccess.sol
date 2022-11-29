pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;

/*
 企业准入合约
 @author cmgun
*/
contract EntAccess {
    // 企业账号地址
    address _enterpriseAddr;
    // 企业名称
    string _name;
    // 统一社会信用代码
    string _idno;
    // 电话
    string _mobile;
    // 联系地址地址
    string _addr;
    // 准入备注
    string _remark;
    // 准入审批方
    address _approvalAddr;
    // 准入时间
    uint _accessTime;

    constructor(address entAddr, string name, string idno, string mobile, string addr, string remark) public {
        _enterpriseAddr = entAddr;
        _name = name;
        _idno = idno;
        _mobile = mobile;
        _addr = addr;
        _remark = remark;
        _approvalAddr = msg.sender;
        _accessTime = now;
    }

    // 获取准入信息
    function getAccessInfo() public view returns(address, string, string, address, uint) {
        return (_enterpriseAddr, _name, _remark, _approvalAddr, _accessTime);
    }
}
