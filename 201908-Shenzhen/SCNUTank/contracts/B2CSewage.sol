import "./TokenSewage.sol";
pragma solidity ^0.4.25;

contract B2CSewage{

    struct Buyer_Info{
        uint256 sewage_token;
        uint256 bank_serials_number;
        uint256 deadline;
        bool flag;
    }

    TokenSewage tokenSewage;
    address public owner;
    mapping(address => mapping(uint256 => Buyer_Info)) public buyers;
    uint256 public sewage_token;
    uint256 public unit_price;
    uint256 public remaining_sewage_token;
    uint256 state;
    event Transaction_msg(uint256 indexed t);

    constructor(address _tokenSewage) public {
        owner = msg.sender;
        tokenSewage = TokenSewage(_tokenSewage);
    }

    function sell(uint256 _sewage_token) public{
        require(msg.sender == owner);
        require(tokenSewage.transferFrom(msg.sender, this, _sewage_token));
        sewage_token = _sewage_token;
        remaining_sewage_token = _sewage_token;
        state = 1;
        emit Transaction_msg(0);
    }

    function purchase_phase1(uint256 ledger_id, uint256 _sewage_token) public{
        require(state == 1);
        require(remaining_sewage_token > 0);
        require(remaining_sewage_token >= _sewage_token);
        remaining_sewage_token -= _sewage_token;
        buyers[msg.sender][ledger_id] = Buyer_Info(_sewage_token, 0, now + 7 days, false);
        emit Transaction_msg(0);
    }

    function purchase_phase2(uint256 ledger_id, uint256 _bank_serials_number) public{
        require(state == 1);
        require(buyers[msg.sender][ledger_id].deadline >= now && _bank_serials_number > 0);
        buyers[msg.sender][ledger_id].bank_serials_number = _bank_serials_number;
        require(tokenSewage.transferTo(msg.sender, buyers[msg.sender][ledger_id].sewage_token));
        emit Transaction_msg(0);
    }

    function interrupt_order(uint256 ledger_id) public{
        require(buyers[msg.sender][ledger_id].deadline >= now);
        remaining_sewage_token += buyers[msg.sender][ledger_id].sewage_token;
        buyers[msg.sender][ledger_id].flag = true;
        emit Transaction_msg(0);
    }

    function interrupt_deadline(address _buyer,uint256 ledger_id) public returns(bool){
        require(state == 1);
        require(msg.sender == owner);
        if(buyers[_buyer][ledger_id].deadline < now){
            remaining_sewage_token += buyers[_buyer][ledger_id].sewage_token;
            return true;
        }
        else{
            return false;
        }
    }

    function interrupt_sell() public{
        require(msg.sender == owner);
        uint256 amount = tokenSewage.balanceOf(this);
        require(tokenSewage.transferTo(msg.sender, amount));
        state = 2;
        emit Transaction_msg(0);
    }
}