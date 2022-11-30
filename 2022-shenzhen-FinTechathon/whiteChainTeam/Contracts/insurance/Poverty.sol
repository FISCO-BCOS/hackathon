// SPDX-License-Identifier: MIT
pragma solidity <=0.6.10;

import "./Insurance.sol";

contract Poverty{

    event Bind(uint256 indexed Id, address indexed poverty);

    Insurances public insurance;
    constructor (Insurances _insurance) public{
        require(address(_insurance) != address(0), "insurance contract address must be non-null");
        insurance = _insurance;
    }

    //给用户查看的_insurance
    struct _insurance{
        uint256 insuranceId;//
        //address beneficiaryAddress;
        uint compensation;//赔款金额
        bool insuranceStatus;//保险是否理赔
        //bool censorStatues;//审查?
        //uint endTime;
        //bool isExpired;//过期?
        string insuranceUri;//保存的文本=>更多的保险合同的信息
    }

    //保险编号对应一个保险
    //查询
    mapping(uint256 => _insurance) public idToInsurance;


    //绑定以及返回
    function povertyBindInsurance(uint256 _insuranceId) public returns(bool){
        (idToInsurance[_insuranceId].insuranceId, ,idToInsurance[_insuranceId].compensation ,idToInsurance[_insuranceId].insuranceStatus, , ,idToInsurance[_insuranceId].insuranceUri) = insurance.findInsurance(_insuranceId);
        
        emit Bind(_insuranceId, msg.sender);
        return true;
    }


}