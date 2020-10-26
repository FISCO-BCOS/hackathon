import "./TokenTime.sol";
import "./DAO.sol";
pragma solidity ^0.4.25;

contract CharityGoods {

    struct Buyer_Info{
        uint256 purchase_number;
        uint256 bank_serials_number;
        uint256 deadline;
        bool flag;
    }

    TokenTime tokenTime;
    address public owner;
    address public dao_address;
    mapping(address => mapping(uint256 => Buyer_Info)) public buyers;
    string public goods_name;
    uint256 public goods_price;
    uint256 public time_token;
    uint256 public goods_number;
    uint256 public remaining_goods_number;
    uint256 public state;
    event Transaction_msg(uint256 indexed t);

    constructor(address _tokenTime, address _dao) public {
        owner = msg.sender;
        tokenTime = TokenTime(_tokenTime);
        dao_address = _dao;
    }

    function sell(
        string _goods_name,
        uint256 _goods_number,
        uint256 _goods_price,
        uint256 _timeToken) public{
        require(msg.sender == owner);
        require(state == 0);
        goods_name = _goods_name;
        goods_price = _goods_price;
        goods_number = _goods_number;
        remaining_goods_number = _goods_number;
        time_token = _timeToken;
        state = 1;
        emit Transaction_msg(0);
    }

    function purchase_phase1(uint256 ledger_id, uint256 _goods_number) public {
        require(state == 1);
        require(remaining_goods_number > 0);
        remaining_goods_number -= _goods_number;
        buyers[msg.sender][ledger_id] = Buyer_Info(_goods_number, 0, now + 7 days, false);
        emit Transaction_msg(0);
    }

    function purchase_phase2(uint256 ledger_id, uint256 _bank_serials_number) public {
        require(state == 1);
        require(buyers[msg.sender][ledger_id].deadline >= now && _bank_serials_number > 0);
        buyers[msg.sender][ledger_id].bank_serials_number = _bank_serials_number;
        uint256 total_time_token = buyers[msg.sender][ledger_id].purchase_number * time_token;
        require(tokenTime.transferFrom(msg.sender, this, total_time_token));
        tokenTime.approve(dao_address, total_time_token);
        DAO dao = DAO(dao_address);
        require(dao.pay(total_time_token));
        emit Transaction_msg(0);
    }

    function interrupt_order(uint256 ledger_id) public {
        require(buyers[msg.sender][ledger_id].deadline >= now);
        remaining_goods_number += buyers[msg.sender][ledger_id].purchase_number;
        buyers[msg.sender][ledger_id].flag = true;
        emit Transaction_msg(0);
    }

    function interrupt_deadline(address _buyer,uint256 ledger_id) public returns(bool) {
        require(state == 1);
        require(msg.sender == owner);
        if(buyers[_buyer][ledger_id].deadline < now){
            remaining_goods_number += buyers[_buyer][ledger_id].purchase_number;
            return true;
        }
        else{
            return false;
        }
    }

    function interrupt_sell() public {
        require(msg.sender == owner);
        state = 2;
        emit Transaction_msg(0);
    }


}