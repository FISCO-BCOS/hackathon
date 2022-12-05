pragma solidity ^0.4.25;

contract PublicKeysStore{
    
    mapping(address => bytes) stores;
    
    function PKin(bytes _pk) public{
        require(stores[msg.sender].length == 0);
        stores[msg.sender] = _pk;
    }
    
    function PKout() public returns(bytes){
        require(stores[msg.sender].length != 0);
        return stores[msg.sender];
    }
    
    function PKdestroy() public {
        require(stores[msg.sender].length != 0);
        stores[msg.sender] = "";
    }
}