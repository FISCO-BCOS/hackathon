import "./TokenTime.sol";

pragma solidity ^0.4.20;

contract P2PTime {
    TokenTime tokenTime;
    address public owner;
    address public candidate_volunteer;
    address public volunteer;
    string public title;
    uint256 public price;
    string public description;
    uint256 public state;
    uint public deadline;
    uint owner_score;
    uint volunteer_score;


    event Transaction_msg(uint256 indexed t);

    constructor(
                address _tokenTime,
                string _title,
                uint256 _price,
                string _description) public {
        tokenTime = TokenTime(_tokenTime);
        owner = msg.sender;
        title = _title;
        price = _price;
        description = _description;
        deadline = now + 7 days;
    }

    function publish() public {
        require(msg.sender == owner);
        require(tokenTime.transferFrom(msg.sender, this, price));
        require(state == 0);
        state = 1;
        emit Transaction_msg(0); // send the msg to web3 if the function run to this point, which mean the function execute successfully
    }

    function apply() public {
        require(state == 1 || state == 2);
        require(deadline < now);
        candidate_volunteer = msg.sender;
        state = 2;
        emit Transaction_msg(1);
    }

    function accept(adress _volunteer) public {
        require(msg.sender == owner);
        require(state == 2);
        volunteer = _volunteer;
        state = 3;
        emit Transaction_msg(2);
    }

    function assess_volunteer(uint score, bool finished) public {
        require(msg.sender == owner);
        require(state == 3);
        volunteer_score = score;
        if(finished) {
            state = 4;
        }else{
            state = 5;
            tokenSewage.transferTo(owner, sewage_token);
        }

        emit Transaction_msg(3);
    }

    function assess_owner(uint score) public {
        require(state == 4 || state == 5);
        require(msg.sender == volunteer);
        owner_score = score;
        if(state == 4) {
            tokenTime.transferTo(volunteer, price);
            state = 6;
        }else
            state = 7;

        emit Transaction_msg(4);
    }

    function interrupt_sell() public {
        require(msg.sender == owner);
        require(state < 3);
        require(tokenSewage.transferTo(owner, sewage_token));
        state = 8;
    }

}