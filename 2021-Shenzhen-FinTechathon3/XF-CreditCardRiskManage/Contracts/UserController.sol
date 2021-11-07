pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;
import "./SafeRole.sol";
import "./Whitelisted.sol";
import "./LibSafeMath.sol";
contract UserController is Whitelisted{
    using SafeRole for SafeRole.Role;
    using LibSafeMath for uint256;

    event LogSend(address indexed from, address indexed to, uint256 value, int256 marks);
    event LogRepay(address account, int256 marks);
    event LogDonation(int256 marks);

    modifier checkDishonest(address account) {// 检测是否信用分过低

        // 逾期检测，如果超过一定天
        uint256 time = _whitelisteds.time[account];
        uint256 baseMark = _whitelisteds.baseBeliefMarks[account];
        int256 dynamicMark = _whitelisteds.dynamicBeliefMarks[account];
        int256 mark = int256(baseMark)+dynamicMark;
         if(_whitelisteds.balance[account] < _whitelisteds.quota[msg.sender]) {
            if(mark >= 2500) {
                require(time + 60 * 1 seconds > block.timestamp, "Controller: Your dishonesty behavior restricts your use, PLEASE REPAY");
            } else {
                require(time + 45 * 1 seconds > block.timestamp, "Controller: Your dishonesty behavior restricts your use, PLEASE REPAY");
            }
        }
        _;
    }

    modifier accountExist(address addr) {
    	require(isWhitelisted(addr), "Controller: Acccount does not exist.");
    	_;
    }


    function transfer(address toAddress, uint256 value) accountExist(msg.sender) accountExist(toAddress) checkDishonest(msg.sender)
        public returns(bool b, uint256 balanceOfFrom, uint256 balanceOfTo) {
            checkOutOfDate();

            uint topValue;
            int256 marks = int(_whitelisteds.baseBeliefMarks[msg.sender])+_whitelisteds.dynamicBeliefMarks[msg.sender];
            //check qualifications
            if(marks <= 1500) {
                topValue = 2500;
            } else if(marks <= 3000){
                topValue = 5000;
            } else {
                topValue = _whitelisteds.quota[msg.sender];
            }
            require(value <= topValue, "UserController: Your intention is greater than your ability");
            // transfer money

            transferMoney(value, toAddress);
            b = true;
            balanceOfFrom = _whitelisteds.balance[msg.sender];
            balanceOfTo = _whitelisteds.balance[toAddress];
            // update the beliefMarks
            transferMark(value, msg.sender);
            emit LogSend(msg.sender, toAddress, value, marks);
    }



    function transferMoney(uint256 value, address account) internal {
        _whitelisteds.balance[msg.sender] = _whitelisteds.balance[msg.sender].sub(value);
        _whitelisteds.balance[account] = _whitelisteds.balance[account].add(value);
    }

    function transferMark(uint256 value, address account) internal {
        uint256 percent = _whitelisteds.percent[account];
        percent = percent + (value % 100);
        _whitelisteds.dynamicBeliefMarks[account] = _whitelisteds.dynamicBeliefMarks[account] + (int(value) / 100);
        if(percent >= 100) {
            percent = percent - 100;
            _whitelisteds.dynamicBeliefMarks[account] = _whitelisteds.dynamicBeliefMarks[account] + 1;
        }
        _whitelisteds.percent[account] = percent;
    }

    function repayNum(address account, uint256 value)  public returns (bool) {
	// 用户还款
        if (!_whitelisteds.has(account)){
            return false;
        }

        uint256 month = 30;
        if(_whitelisteds.balance[account] > _whitelisteds.quota[account] || value < 0) {
            return false;
        }

        int256 beliefMarks = _whitelisteds.dynamicBeliefMarks[account];
        if(_whitelisteds.balance[account] + value > _whitelisteds.quota[account]) {
            _whitelisteds.balance[account] = _whitelisteds.quota[account];
        } else {
            _whitelisteds.balance[account] = _whitelisteds.balance[account] + value;
        }


        if(block.timestamp > _whitelisteds.time[account] + month * 1 seconds){
            if(_whitelisteds.balance[account] >= _whitelisteds.quota[account]) {
                beliefMarks += 10;
            } else {
                beliefMarks = beliefMarks + (int(value) / 1000);
            }
        } else {
            if(_whitelisteds.balance[account] >= _whitelisteds.quota[account]) {
                beliefMarks += 100;
            } else {
                beliefMarks = beliefMarks + (int(value) / 100);
            }
        }
        _whitelisteds.dynamicBeliefMarks[account] = beliefMarks;
        // update repay time
        if(_whitelisteds.balance[account] >= _whitelisteds.quota[account]) {
            _whitelisteds.time[account] = block.timestamp;
        }
        // update deductWeeks
        _whitelisteds.deduct[account] = 0;
        _whitelisteds.timeLog[account].push(now);
        _whitelisteds.eventLog[account].push(2);
        _whitelisteds.markLog[account].push(int(_whitelisteds.baseBeliefMarks[account])+_whitelisteds.dynamicBeliefMarks[account]);
        emit LogRepay(account, beliefMarks);
        return true;
    }

    function multiTrans(address account, uint256 values) public returns(bool) {
        //多次重复取钱
        uint lastTime = _whitelisteds.transferTime[account];
        if (now < lastTime + 5000) {
            _whitelisteds.dynamicBeliefMarks[account] -= 100;
        }
        _whitelisteds.balance[account] -= values;

        _whitelisteds.transferTime[account] = block.timestamp;
        _whitelisteds.timeLog[account].push(now);
        _whitelisteds.eventLog[account].push(3);
        _whitelisteds.markLog[account].push(int(_whitelisteds.baseBeliefMarks[account])+_whitelisteds.dynamicBeliefMarks[account]);
        return true;
    }


    function multiTransInt(address account, uint values) public returns(bool) {
	// 多次重复取整数钱款
        uint lastTime = _whitelisteds.transferTime[account];
        _whitelisteds.balance[account] -= values;
        if (now < lastTime + 5000 && values % 100 == 0) {
            _whitelisteds.dynamicBeliefMarks[account] -= 200;
        }
        _whitelisteds.transferTime[account] = now;
        _whitelisteds.timeLog[account].push(now);
        _whitelisteds.eventLog[account].push(4);
        _whitelisteds.markLog[account].push(int(_whitelisteds.baseBeliefMarks[account])+_whitelisteds.dynamicBeliefMarks[account]);
        return true;
    }

    function donation(address account, uint256 value) accountExist(account) checkDishonest(account) public returns (bool b){
	// 用户捐款
        if(_whitelisteds.balance[account] <= value){
            return false;
        }
        int256 beliefMarks = _whitelisteds.dynamicBeliefMarks[account];
        uint256 percent = _whitelisteds.percent[account];

        _whitelisteds.balance[account] = _whitelisteds.balance[account] - value;

        beliefMarks = beliefMarks + (int(value) / 100);
        percent = percent + (value % 100);
        if(percent >= 100) {
            percent = percent - 100;
            beliefMarks = beliefMarks + 1;
        }
        _whitelisteds.percent[account] = percent;
        _whitelisteds.dynamicBeliefMarks[account] = beliefMarks;
        _whitelisteds.timeLog[account].push(now);
        _whitelisteds.eventLog[account].push(1);
        _whitelisteds.markLog[account].push(int(_whitelisteds.baseBeliefMarks[account])+_whitelisteds.dynamicBeliefMarks[account]);
        emit LogDonation(beliefMarks);
        b = true;
    }

    function checkOutOfDate(address account) internal returns (bool b) {
	// 检测是否逾期欠款
        if(_whitelisteds.balance[account] >= _whitelisteds.quota[account]) {
            return false;
        }
        // judge out of date
        uint256 _isDeduct = _whitelisteds.deduct[account];
        uint256 time = _whitelisteds.time[account];
        if(block.timestamp <= time + 30000) {
            return false;
        }
        //check deducted?
        if((block.timestamp - time - 30000) / 7000 <= _isDeduct) {
            return false;
        }
        uint256 _weeks;
        //caculate weeks
        _weeks = (block.timestamp - time - 30000) / 7000 - _isDeduct;
        _whitelisteds.dynamicBeliefMarks[account] = _whitelisteds.dynamicBeliefMarks[account] - 70 * int(_weeks);
        /*@@
            If there is no need to repay, it is 0.
            Call the function before the end of the second week after the first week. Weeks is 1.
            At this time, deduct the payment and set isDeduct to 1, so that this week will not be deducted again until the next week.
            If the first week and the third week are tested, the difference of _weeks becomes 2
        */
        _isDeduct = (block.timestamp - time - 30000) / 7000;
        _whitelisteds.deduct[msg.sender] = _isDeduct;
        b = true;
    }

    function _showRepayDate(address account) public view returns (uint256) {
	//返回还款的时间戳
        uint256 date = _whitelisteds.time[account] + 30000;
        return date;
    }

    function _showOverdraft(address account) public view returns (uint256) {
	// 返回欠款额度
        if(_whitelisteds.balance[account] == _whitelisteds.quota[account]) {
            return 0;
        } else {
            uint256 overdraft = _whitelisteds.quota[account].sub(_whitelisteds.balance[account]);

            return overdraft;
        }
    }

    function _showBalance(address account) public view returns(uint256) {
	//返回剩余额度
        uint256 balance = _whitelisteds.balance[account];
        return balance;
    }

    function updateBaseMark(address account) public returns (bool) {
        checkOutOfDate(account);
        //更新基础值
        uint256 overdraft = _whitelisteds.quota[account] - _whitelisteds.balance[account];
        uint256 time = _whitelisteds.checkBaseTime[account];
        if(time + 30000 <= block.timestamp){
            int256 marks = _whitelisteds.dynamicBeliefMarks[account] + int(_whitelisteds.baseBeliefMarks[account]);
            _whitelisteds.quota[msg.sender] = (uint256(marks) / 500) * 1000;
            _whitelisteds.balance[msg.sender] = (int(_whitelisteds.quota[account]) - int(overdraft) > 0 ? _whitelisteds.quota[account] - overdraft : 0);
            _whitelisteds.checkBaseTime[account] = block.timestamp;
            _whitelisteds.dynamicBeliefMarks[account] = 0;
            _whitelisteds.baseBeliefMarks[account] = uint256(marks);
            return true;
        } else{
            return false;
        }
    }

    function _showBase(address account) public view returns(uint256) {
        return _whitelisteds.baseBeliefMarks[account];
    }

    function _showLog(address account) public view returns(uint256[], uint256[], int256[]) {
	// 返回log
        return(_whitelisteds.timeLog[account], _whitelisteds.eventLog[account], _whitelisteds.markLog[account]);
    }

    function _showMarks(address account) public returns (int256) {
	// 返回信用分
        if(!_whitelisteds.has(account)) {
            return -1;
        }
        return (int(_whitelisteds.baseBeliefMarks[account])+_whitelisteds.dynamicBeliefMarks[account]);
    }
}