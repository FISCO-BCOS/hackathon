import "./TokenSewage.sol";

pragma solidity ^0.4.20;

contract P2PSewage{
    TokenSewage tokenSewage;
    uint256 ident;
    uint public deadline;
    address public owner;
    address public buyer;
    uint256 public sewage_token;
    uint256 public price;
    uint256 public bank_serials_number;
    uint256 state;
    event Transaction_msg(uint256 indexed t);

    constructor(address _tokenSewage, uint256 _ident, uint256 _value) public {
        owner = msg.sender;
        tokenSewage = TokenSewage(_tokenSewage);
        price = _value;
        deadline = now + 7 days;
        ident = _ident;
    }

    function sell(address _buyer, uint256 _sewage_token) public{
        require(msg.sender == owner);
        require(tokenSewage.transferFrom(msg.sender, this, _sewage_token));
        require(state == 0);
        buyer = _buyer;
        sewage_token = _sewage_token;
        state += 1;
        emit Transaction_msg(0); // send the msg to web3 if the function run to this point, which mean the function execute successfully
    }

    function purchase(uint256 _bank_serials_number) public{
        require(msg.sender == buyer);
        require(state == 1 || state == 2);
        require(bank_serials_number != 0);
        require(deadline < now);
        bank_serials_number = _bank_serials_number;
        require(tokenSewage.transferTo(msg.sender, sewage_token));
        state = 2;
        emit Transaction_msg(1);
    }

    function interrupt_sell() public{
        require(msg.sender == owner);
        require(state == 1);
        require(tokenSewage.transferTo(owner, sewage_token));
        state = 3;
    }

}
