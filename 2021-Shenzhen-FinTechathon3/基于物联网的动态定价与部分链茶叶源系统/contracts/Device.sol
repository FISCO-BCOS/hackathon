pragma solidity >=0.4.22 <0.7.0;

import "./Roles.sol";

contract Device 
{

    using Roles for Roles.Role;

    event DeviceAdded(address indexed account);
    event DeviceRemoved(address indexed account);

    Roles.Role private _devices;

    constructor (address device) public 
    {
        _addDevice(device);

    }

    modifier onlyDevice() 
    {

        require(isDevice(msg.sender), "DeviceRole: caller does not have the device role");
        _;
    }

    function isDevice(address account) public view returns (bool)
    {
        return _devices.has(account);
    }

    function addDevice(address account) public onlyDevice
    {
        _addDevice(account);
    }

    function renounceDevice() public 
    {
        _removeDevice(msg.sender);
    }

    function _addDevice(address account) internal 
    {
        _devices.add(account);
        emit DeviceAdded(account);
    }

    function _removeDevice(address account) internal 
    {
        _devices.remove(account);
        emit DeviceRemoved(account);
    }
    
}