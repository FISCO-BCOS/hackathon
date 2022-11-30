pragma solidity ^0.4.20;
pragma experimental ABIEncoderV2;

import "./PrepaidCard.sol";

contract Refund {
    address public cardContract;

    event RefundInSeven(string cardID, int balance, uint now, bool isIn);
    event RefundNotInSeven(string cardID, int balance, uint now, bool isIn);

    constructor(address _cardContract) public {
        cardContract = _cardContract;
    }

    function refund(string _cardID, uint _now) public returns (bool, int) {
        uint timeStamp;
        int balance;
        bool inSeven;
        (, , , balance, , timeStamp) = PrepaidCard(cardContract).selectByCardID(_cardID);

        if (_now - 7 * 24 * 3600 * 1000 < timeStamp) {
            inSeven = true;
            emit RefundInSeven(_cardID, balance, _now, inSeven);
        } else {
            inSeven = false;
            emit RefundNotInSeven(_cardID, balance, _now, inSeven);
        }

        PrepaidCard(cardContract).changeBalance(_cardID, balance);
        return (inSeven, balance);
    }
}
