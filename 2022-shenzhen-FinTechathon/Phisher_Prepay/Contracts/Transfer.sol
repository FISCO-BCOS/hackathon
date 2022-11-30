pragma solidity ^0.4.20;
pragma experimental ABIEncoderV2;

import "./PrepaidCard.sol";
import "./ShopAccounts.sol";
import "./Leverage.sol";
import "./ServiceHistory.sol";

contract Transfer {
    address public cardContract;
    address public shopContract;
    address public leverageContract;
    address public serviceContract;

    event TransferLog(string cardID, string shopID, int Value);

    constructor(
        address _cardContract,
        address _shopContract,
        address _leverageContract,
        address _serviceContract
    ) public {
        cardContract = _cardContract;
        shopContract = _shopContract;
        leverageContract = _leverageContract;
        serviceContract = _serviceContract;
    }

   function transfer(
        string _cardID,
        string _shopID,
        string _serviceID,
        string _record,
        int _value
    ) public {
        PrepaidCard(cardContract).changeBalance(_cardID, _value);
        
        int leveragedValue = Leverage(leverageContract).Calculate(_shopID, _value);
        ShopAccounts(shopContract).changeBalance(_shopID, leveragedValue);
        
        emit TransferLog(_cardID, _shopID, _value);
        ServiceHistory(serviceContract).createHistory(_serviceID, _cardID, _record);
    }
}