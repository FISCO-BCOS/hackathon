pragma solidity ^0.4.20;
pragma experimental ABIEncoderV2;

import "./ServiceHistory.sol";
import "./Refund.sol";

contract Complaint {
    address public serviceContract;
    address public refundContract;

    constructor(address _serviceContract, address _refundContract) public {
        serviceContract = _serviceContract;
        refundContract = _refundContract;
    }

    function complaint(
        string _serviceID,
        string _content,
        string _cardID,
        uint _now
    ) public {

        ServiceHistory(serviceContract).changeComplaint(_serviceID, _content);
        Refund(refundContract).refund(_cardID, _now);
    }
}
