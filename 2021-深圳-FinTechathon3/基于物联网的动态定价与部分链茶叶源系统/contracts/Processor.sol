pragma solidity >=0.4.22 <0.7.0;

import "./Roles.sol";

contract Processor     //加工环节
{
    using Roles for Roles.Role;

    event ProcessorAdded(address indexed account);
    event ProcessorRemoved(address indexed account);

    Roles.Role private _processors;

    constructor (address  Processor ) public 
    {
        _addProcessor(Processor);
    }

    modifier onlyProcessor() 
    {
        require(isProcessor(msg.sender), "ProcessorRole: caller does not have the Processor role");
        _;
    }

    function isProcessor(address account) public view returns (bool) 
    {
        return _processors.has(account);
    }

    function addProcessor(address account) public onlyProcessor  
    {
        _addProcessor(account);
    }

    function renounceProcessor() public 
    {
        _removeProcessor(msg.sender);
    }

    function _addProcessor(address account) internal 
    {
        _processors.add(account);
        emit ProcessorAdded(account);
    }

    function _removeProcessor(address account) internal 
    {
        _processors.remove(account);
        emit ProcessorRemoved(account);
    }
    
}