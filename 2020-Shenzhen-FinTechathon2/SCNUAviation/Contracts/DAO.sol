import "./TokenTime.sol";

pragma solidity ^0.4.16;

contract DAO {
    TokenTime public tokenTime;
    struct DAO_Info{
        string title;
        string description;
        uint fundingGoal;
        address beneficiary;
    }
    DAO_Info public dao_info;
    uint public amountRaised;
    uint public deadline;
    mapping(address => uint256) public balanceOf;
    bool fundingGoalReached = false;
    bool crowdSaleClosed = false;

    event GoalReached(address recipient, uint totalAmountRaised);
    event FundTransfer(address backer, uint amount, bool isContribution);

    /**
     * Constructor function
     *
     * Setup the owner
     */
    constructor(
        string _title,
        string _description,
        uint fundingGoalInToken,
        uint durationInDays,
        address addressOfToken
    ) {
        dao_info.title = _title;
        dao_info.description = _description;
        dao_info.beneficiary = msg.sender;
        dao_info.fundingGoal = fundingGoalInToken;
        deadline = now + durationInDays * 1 days;
        tokenTime = TokenTime(addressOfToken);
    }

    /**
     * Fallback function
     *
     * The function without name is the default function that is called whenever anyone sends funds to a contract
     */
    function pay(uint256 timeToken) public returns(bool){
        require(now < deadline);
        require(tokenTime.transferFrom(msg.sender, this, timeToken));
        balanceOf[msg.sender] += timeToken;
        amountRaised += timeToken;
        emit FundTransfer(msg.sender, timeToken, true);
        return true;
    }

    modifier afterDeadline() {
        if (now >= deadline) _;
    }

    /**
     * Check if goal was reached
     *
     * Checks if the goal or time limit has been reached and ends the campaign
     */
    function checkGoalReached() afterDeadline public returns(bool){
        if (amountRaised >= dao_info.fundingGoal){
            fundingGoalReached = true;
            emit GoalReached(dao_info.beneficiary, amountRaised);
        }
        crowdSaleClosed = true;

        return fundingGoalReached;
    }

    /**
     * Withdraw the funds
     *
     * Checks to see if goal or time limit has been reached, and if so, and the funding goal was reached,
     * sends the entire amount to the beneficiary. If goal was not reached, each contributor can withdraw
     * the amount they contributed.
     */
    function safeWithdrawal() afterDeadline public {
        if (!fundingGoalReached && crowdSaleClosed) {
            uint amount = balanceOf[msg.sender];
            balanceOf[msg.sender] = 0;
            if (amount > 0) {
                tokenTime.transferTo(msg.sender, amount);
                balanceOf[msg.sender] -= amount;
                emit FundTransfer(msg.sender, amount, false);
            }
        }
        if (fundingGoalReached && dao_info.beneficiary == msg.sender) {
            tokenTime.transferTo(msg.sender, amountRaised);
        }
    }


}
