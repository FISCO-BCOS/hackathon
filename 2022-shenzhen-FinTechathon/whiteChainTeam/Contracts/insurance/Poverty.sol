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

    struct _insurance{
        uint256 insuranceId;
        uint compensation;
        bool insuranceStatus;
        string insuranceUri;
    }

    mapping(uint256 => _insurance) public idToInsurance;

    function povertyBindInsurance(uint256 _insuranceId) public returns(bool){
        (idToInsurance[_insuranceId].insuranceId, ,idToInsurance[_insuranceId].compensation ,idToInsurance[_insuranceId].insuranceStatus, , ,idToInsurance[_insuranceId].insuranceUri) = insurance.findInsurance(_insuranceId);
        
        emit Bind(_insuranceId, msg.sender);
        return true;
    }


}
