pragma solidity >=0.4.22 <0.7.0;

import "./Roles.sol";

contract Planter       //种植环节
{
    using Roles for Roles.Role;

    event PlanterAdded(address indexed account);
    event PlanterRemoved(address indexed account);

    Roles.Role private _planters;

    constructor (address planter) public 
    {
        _addPlanter(planter);
    }

    modifier onlyPlanter() 
    {
        require(isPlanter(msg.sender), "PlanterRole: caller does not have the Planter role");
        _;
    }

    function isPlanter(address account) public view returns (bool) 
    {
        return _planters.has(account);
    }

    function addPlanter(address account) public onlyPlanter 
    {
        _addPlanter(account);
    }

    function renouncePlanter() public 
    {
        _removePlanter(msg.sender);
    }

    function _addPlanter(address account) internal 
    {
        _planters.add(account);
        emit PlanterAdded(account);
    }

    function _removePlanter(address account) internal 
    {
        _planters.remove(account);
        emit PlanterRemoved(account);
    }
}