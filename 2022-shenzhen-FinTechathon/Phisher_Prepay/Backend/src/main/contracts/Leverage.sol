pragma solidity ^0.4.20;
pragma experimental ABIEncoderV2;

import "./ShopAccounts.sol";

contract Leverage {
    address public shopContract;

    constructor(address _shopContract) {
        shopContract = _shopContract;
    }

    function Calculate(string _shopID, int _value) public returns (int) {
        int leverage;
        (, leverage) = ShopAccounts(shopContract).selectByShopID(_shopID);

        int amount = ((_value * 100) * (100 + leverage)) / 10000;
        return amount;
    }
}
