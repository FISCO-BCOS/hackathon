pragma solidity ^0.4.25;

contract EnterpriseManager {

  struct Enterprise {
    address enterpriseAddress;  // enterprise address in the blockchain
    string name;  // enterprise name
    string realid;  // enterprise identity in the real world
    uint index;  // enterprise index
    bool isdeleted; // flag for deleted
  }

  mapping(address => Enterprise) public enterprises;
  address[] public enterpriseAddresses;

  // enterprise is exited or not
  function IsExitAddress(address _enterAddress)
    public
    constant
    returns(bool isExited)
  {
    if(enterpriseAddresses.length == 0) return false;
    return (enterpriseAddresses[enterprises[_enterAddress].index] == _enterAddress);
  }

  // add a new enterprise
  function AddEnterprise(
    address _entAddress,
    string _name,
    string _realid)
    public
    returns(uint index)
  {
    require(IsExitAddress(_entAddress) == true, "Enterprise is not exited");
    enterpriseAddresses.push(_entAddress);
    enterprises[_entAddress].enterpriseAddress = _entAddress;
    enterprises[_entAddress].name   = _name;
    enterprises[_entAddress].realid   = _realid;
    enterprises[_entAddress].index     = enterpriseAddresses.length-1;
    enterprises[_entAddress].isdeleted = false;
    return enterpriseAddresses.length-1;
  }

  // update an enterprise
  function UpdateEnterprise(address _entAddress, string key, string value)
    public
    returns(bool success)
  {
    require(IsExitAddress(_entAddress) == true, "Enterprise is not exited");
    if(keccak256(abi.encodePacked(key)) == keccak256(abi.encodePacked("name"))) {
        enterprises[_entAddress].name = value;
        return true;
    }
    if(keccak256(abi.encodePacked(key)) == keccak256(abi.encodePacked("realid"))) {
        enterprises[_entAddress].realid = value;
        return true;
    }
    return false;
  }

  // delete an enterprise
  function DeleteEnterprise(address _entAddress) public returns (bool isSuccess) {
    if(!IsExitAddress(_entAddress)) return false;
    enterprises[_entAddress].isdeleted = true;
    return true;
  }

  // find an enterprise by address
  function GetEnterprise(address _entAddress)
    public
    constant
    returns(string name, string realid, uint index)
  {
    require(IsExitAddress(_entAddress) == true, "Enterprise is not exited");
    return(
      enterprises[_entAddress].name,
      enterprises[_entAddress].realid,
      enterprises[_entAddress].index);
  }

  // find an enterprise by index
  function GetEnterpriseByIndex(uint index)
    public
    constant
    returns(address entAddress, string name, string realid)
  {
    require(index < 0 || index >= enterpriseAddresses.length, "index is out of range");
    return(
      enterpriseAddresses[index],
      enterprises[enterpriseAddresses[index]].name,
      enterprises[enterpriseAddresses[index]].realid);
  }

  // returns the count of the enterpises
  function GetEnterpriseCount()
    public
    constant
    returns(uint count)
  {
    return enterpriseAddresses.length;
  }

  // check an enterprise had been deleted or not
  function IsDeleted(address _entAddress)
    public
    constant
    returns(bool isDeleted)
  {
    require(IsExitAddress(_entAddress) == true, "Enterprise is not exited");
    return enterprises[_entAddress].isdeleted;
  }

}
