pragma solidity >=0.4.22 <0.7.0;

import "./Roles.sol";

contract Retailer   //零售信息
{
    using Roles for Roles.Role;

    event RetailerAdded(address indexed account);
    event RetailerRemoved(address indexed account);

    Roles.Role private _retailers;

    constructor (address retailer) internal 
    {
        _addRetailer(retailer);
    }

    modifier onlyRetailer() 
    {
        require(isRetailer(msg.sender), "RetailerRole: caller does not have the Retailer role");
        _;
    }

    function isRetailer(address account) public view returns (bool) 
    {
        return _retailers.has(account);
    }

    function addRetailer(address account) public onlyRetailer 
    {
        _addRetailer(account);
    }

    function renounceRetailer() public 
    {
        _removeRetailer(msg.sender);
    }

    function _addRetailer(address account) internal 
    {
        _retailers.add(account);
        emit RetailerAdded(account);
    }

    function _removeRetailer(address account) internal 
    {
        _retailers.remove(account);
        emit RetailerRemoved(account);
    }

}