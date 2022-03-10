// 此合约用于对经过信用评估之后的用户进行信用分评估
pragma solidity >=0.4.25 <0.9.0;
pragma experimental ABIEncoderV2;
import "./SafeRole.sol";
import "./WhitelistAdminRole.sol";

contract Whitelisted is WhitelistAdminRole {
    using SafeRole for SafeRole.Role;

    event WhitelistedAdded(address indexed account);
    event WhitelistedRemoved(address indexed account);
    event LogRegister(address account);
    event LogUnregister(address account);

    SafeRole.Role internal _whitelisteds;

    constructor() public {
        // initialize
        _whitelisteds.baseBeliefMarks[msg.sender] = 0;

    }

    modifier onlyWhitelisted() {
        require(isWhitelisted(msg.sender), "WhitelistedRole: caller does not have the Whitelisted role");
        _;
    }

    function isWhitelisted(address account) public view returns (bool) {
        return _whitelisteds.has(account);
    }

    function addWhitelisted(address account, uint isAbleRepay, uint age, uint marriage, uint education, uint salary, uint houseProperty, uint carProperty) public returns(bool) {    //通过继承获得的函数修饰器
        if(isWhitelisted(account)){
            return (false);
        }
        _whitelisteds.baseBeliefMarks[account] = isAbleRepay*1000 + age/5*20 + marriage*200 + education*200 + salary*10 + houseProperty*500 + carProperty*100;

        if (_whitelisteds.baseBeliefMarks[account] < 1500) {
            return (false);
        }
        _addWhitelisted(account);
        _whitelisteds.percent[account] = 0;
        _whitelisteds.balance[account] = (_whitelisteds.baseBeliefMarks[account] / 500) * 1000;
        _whitelisteds.quota[account] = (_whitelisteds.baseBeliefMarks[account] / 500) * 1000;
        _whitelisteds.time[account] = now;
        _whitelisteds.deduct[account] = 0;
        _whitelisteds.checkBaseTime[account] = now;
        _whitelisteds.transferTime[account] = now;
        _whitelisteds.timeLog[account].push(now);
        _whitelisteds.eventLog[account].push(0);
        _whitelisteds.markLog[account].push(int256(_whitelisteds.baseBeliefMarks[account]));
        return true;
    }

    function removeWhitelisted(address account) public onlyWhitelistAdmin {
        require((int(_whitelisteds.baseBeliefMarks[account]) + _whitelisteds.dynamicBeliefMarks[account]) > 0, "WhitelistedRole: The account's beliefMarks is too low to logout.");
        _removeWhitelisted(account);
    }

    function renounceWhitelisted() public {
        _removeWhitelisted(msg.sender);
    }

    function _addWhitelisted(address account) internal {
        _whitelisteds.add(account);
        emit WhitelistedAdded(account);
    }

    function _removeWhitelisted(address account) internal {
        _whitelisteds.remove(account);
        emit WhitelistedRemoved(account);
    }
}