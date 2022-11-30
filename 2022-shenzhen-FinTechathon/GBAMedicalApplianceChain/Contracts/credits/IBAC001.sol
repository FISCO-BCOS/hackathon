pragma solidity ^0.4.24;


interface IBAC001 {

    function totalAmount() public view returns (uint256);

    function balance(address owner) public view returns (uint256);

    function send(address to, uint256 value, bytes data) public ;

    function sendFrom(address from, address to, uint256 value, bytes data) public;

    function allowance(address owner, address spender) public view returns (uint256);

    function approve(address spender, uint256 amount) public returns (bool);

    function destroy(uint256 value, bytes data) public;

    function destroyFrom(address from, uint256 value, bytes data) public;

    function issue(address to, uint256 value, bytes data) public  returns (bool);

    function batchSend(address[] to, uint256[] values, bytes data) public;

    function increaseAllowance(address spender, uint256 addedValue) public  returns (bool);

    function decreaseAllowance(address spender, uint256 subtractedValue) public  returns (bool);

    event Send(address indexed from, address indexed to, uint256 value, bytes data);

    event Approval(address indexed owner, address indexed spender, uint256 value);

}