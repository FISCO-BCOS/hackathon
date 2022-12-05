// SPDX-License-Identifier: MIT
   pragma solidity ^0.6.10;
   pragma experimental ABIEncoderV2;
   
   contract Permission{
           address  public owner;
           mapping (address => uint) public Role;
           constructor () public{
                  owner = msg.sender;
          }
  
          modifier onlyOwner(){
                  require(msg.sender == owner,"Only owner can call this.");
                  _;
          }
  
          function enterpriseRegister(address account) public onlyOwner{
                  Role[account] = 111;
          }
  
          function govermentRegister(address account) public onlyOwner{
                  Role[account] = 222;
          }
  
          function auditRegister(address account) public onlyOwner{
                  Role[account] = 333;
          }
  
          function getPermission(address account) public returns(uint ){
                return Role[account];
          }
          
}