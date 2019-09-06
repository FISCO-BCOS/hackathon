import "./TokenSewage.sol";
pragma solidity ^0.4.25;

contract AuctionSewage{
    TokenSewage tokenSewage;
    uint256 public ident;
    address public owner;
    uint256 public auction_token;
    uint public auction_end;
    address public highestBidder;
    uint256 public highestBid;
    bool ended;
    uint256 public deadline;
    uint256 public bank_serials_number;

    event HighestBidIncreased(address bidder, uint amount);
    event AuctionEnded(address winner, uint amount);

    constructor() public{
        owner = msg.sender;
    }

    function start_auction(uint256 _auction_token, uint _biddingTime) public {
        require(msg.sender == owner);
        require(tokenSewage.transferFrom(owner, this, _auction_token));
        auction_token = _auction_token;
        auction_end = now + _biddingTime;
    }

    function bid(uint256 value) public{
        require(now <= auction_end, "Auction already ended.");
        require(value > highestBid, "There already is a higher bid.");

        highestBidder = msg.sender;
        highestBid = value;
        emit HighestBidIncreased(msg.sender, value);
    }


    function auctionEnd() public {
        require(now >= auction_end, "Auction not yet ended.");
        require(!ended, "auctionEnd has already been called.");
        ended = true;
        deadline = now + 7 days;
        emit AuctionEnded(highestBidder, highestBid);
    }

    function pay(uint256 _bank_serials_number) public {
        require(msg.sender == highestBidder && ended);
        require(now < deadline);
        require(_bank_serials_number > 0);
        bank_serials_number = _bank_serials_number;
        require(tokenSewage.transferTo(highestBidder, auction_token));
    }

    function refund() public {
        require(msg.sender == owner);
        require(highestBid == 0);
        uint256 amount = tokenSewage.balanceOf(this);
        require(amount > 0);
        require(tokenSewage.transferTo(msg.sender, amount));
    }
}