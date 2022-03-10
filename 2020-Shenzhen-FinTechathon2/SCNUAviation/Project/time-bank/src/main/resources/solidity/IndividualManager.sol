pragma solidity ^0.4.25;

contract IndividualManager {

    struct Individual {
      address individualAddress;  // individual address in the blockchain
      string name;  // individual name
      string realid;  // individual identity in the real world
      uint score;
      uint index;  // individual index
      bool isDeleted; // flag for deleted
    }

    mapping(address => Individual) public individuals;
    address[] public individualAddresses;

    // individual is exited or not
    function IsExitAddress(address _enterAddress)
      public
      constant
      returns(bool isExited)
    {
      if(individualAddresses.length == 0) return false;
      return (individualAddresses[individuals[_enterAddress].index] == _enterAddress);
    }

    // add a new individual
    function AddIndividual(
      address _entAddress,
      string _name,
      string _realid)
      public
      returns(uint index)
    {
      require(IsExitAddress(_entAddress) == true, "Individual is not exited");
      individualAddresses.push(_entAddress);
      individuals[_entAddress].individualAddress = _entAddress;
      individuals[_entAddress].name = _name;
      individuals[_entAddress].realid = _realid;
      individuals[_entAddress].index = individualAddresses.length - 1;
      individuals[_entAddress].isDeleted = false;
      return individualAddresses.length - 1;
    }

    // update an individual
    function UpdateIndividual(address _entAddress, string key, string value1, uint value2)
      public
      returns(bool success)
    {
      require(IsExitAddress(_entAddress) == true, "Individual is not exited");
      if(keccak256(abi.encodePacked(key)) == keccak256(abi.encodePacked("name"))) {
          individuals[_entAddress].name = value1;
          return true;
      }
      if(keccak256(abi.encodePacked(key)) == keccak256(abi.encodePacked("realid"))) {
          individuals[_entAddress].realid = value1;
          return true;
      }
      if(keccak256(abi.encodePacked(key)) == keccak256(abi.encodePacked("score"))) {
        individuals[_entAddress].score = value2;
        return true;
      }
      return false;
    }

    // delete an individual
    function DeleteIndividual(address _entAddress) public returns (bool isSuccess) {
      if(!IsExitAddress(_entAddress)) return false;
      individuals[_entAddress].isDeleted = true;
      return true;
    }

    // find an individual by address
    function GetIndividual(address _entAddress)
      public
      constant
      returns(string name, string realid, uint score, uint index)
    {
      require(IsExitAddress(_entAddress) == true, "Individual is not exited");
      return(
        individuals[_entAddress].name,
        individuals[_entAddress].realid,
        individuals[_entAddress].score,
        individuals[_entAddress].index);
    }

    // find an individual by index
    function GetIndividualByIndex(uint index)
      public
      constant
      returns(address entAddress, string name, string realid)
    {
      require(index < 0 || index >= individualAddresses.length, "index is out of range");
      return(
        individualAddresses[index],
        individuals[individualAddresses[index]].name,
        individuals[individualAddresses[index]].realid);
    }

    // returns the count of the individual
    function GetIndividualCount() public constant returns(uint count) {
      return individualAddresses.length;
    }

    // check an individual had been deleted or not
    function IsDeleted(address _entAddress) public constant returns(bool isDeleted) {
      require(IsExitAddress(_entAddress) == true, "Individual is not exited");
      return individuals[_entAddress].isDeleted;
    }


}