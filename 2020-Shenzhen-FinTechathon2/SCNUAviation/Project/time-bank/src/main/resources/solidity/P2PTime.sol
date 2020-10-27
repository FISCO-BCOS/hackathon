import "./TokenTime.sol";

pragma solidity ^0.4.20;

contract P2PTime {
    TokenTime tokenTime;
    address public owner;
    address public candidate_volunteer;
    address public volunteer;
    struct P2P_Info{
        string title;
        uint256 price;
        string sort;
        uint publish_time;
        string description;
        uint deadline;
    }
    P2P_Info public p2p_info;
    uint256 public state;
    uint owner_score;
    uint volunteer_score;


    event Transaction_msg(uint256 indexed t);

    constructor(
                address _tokenTime,
                string _title,
                uint256 _price,
                string _sort,
                string _description) public {
        tokenTime = TokenTime(_tokenTime);
        owner = msg.sender;
        p2p_info.title = _title;
        p2p_info.price = _price;
        p2p_info.sort = _sort;
        p2p_info.publish_time = now;
        p2p_info.description = _description;
        p2p_info.deadline = now + 7 days;
    }

    function publish() public {
        require(msg.sender == owner);
        require(tokenTime.transferFrom(msg.sender, this, p2p_info.price));
        require(state == 0);
        state = 1;
        emit Transaction_msg(0); // send the msg to web3 if the function run to this point, which mean the function execute successfully
    }

    function apply() public {
        require(state == 1 || state == 2);
        require(p2p_info.deadline < now);
        candidate_volunteer = msg.sender;
        state = 2;
        emit Transaction_msg(1);
    }

    function accept(address _volunteer) public {
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
            tokenTime.transferTo(owner, p2p_info.price);
        }

        emit Transaction_msg(3);
    }

    function assess_owner(uint score) public {
        require(state == 4 || state == 5);
        require(msg.sender == volunteer);
        owner_score = score;
        if(state == 4) {
            tokenTime.transferTo(volunteer, p2p_info.price);
            state = 6;
        }else
            state = 7;

        emit Transaction_msg(4);
    }

    function interrupt_sell() public {
        require(msg.sender == owner);
        require(state < 3);
        require(tokenTime.transferTo(owner, p2p_info.price));
        state = 8;
    }

}