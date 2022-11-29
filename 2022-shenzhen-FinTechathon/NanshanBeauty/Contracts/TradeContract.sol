pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;

/*
 委托交易合约，包含回收委托交易、梯次利用委托交易
 @author cmgun
*/
contract TradeContract {
    // 卖方地址
    address _seller;
    // 买方地址
    address _buyer;
    // 交易信息，json格式存储，根据不同业务存储不同内容
    string _info;
    // 交易状态：1-竞价中，2-交易成功，3-交易撤回或失败
    int16 _status;
    // 最低金额
    uint256 _lowestAmt;
    // 期望金额
    uint256 _expectAmt;
    // 成交金额
    uint256 _tradeAmt;
    // 结束时间
    uint _endTime;
    // 交易时间
    uint _tradeTime;

    // 当前最高价买方地址
    address _highestBuyer;
    // 当前最高价格
    uint256 _highestAmt;

    // 记录参加过竞价的买方
    mapping (address => uint256) public _bidLogs;


    modifier onlyOwner() {
        require(msg.sender == _seller, "Not seller!");
        _;
    }

    event newStatus(address addr, int16 status, uint timestamp, string remark);

    // 初始化
    constructor(string info, uint256 lowestAmt, uint256 expectAmt, uint256 auctionDays) public {
        _seller = msg.sender;
        _info = info;
        _status = 1;
        _lowestAmt = lowestAmt;
        _expectAmt = expectAmt;
        _endTime = now + auctionDays * 1 days;
        emit newStatus(msg.sender, 1, now, "create");
    }

    // 竞价交易，交易结束则返回true，否则返回false
    function bid(uint256 _price) public returns (bool) {
//        require(now <= _endTime, "Auction already ended.");
        require(_price >= _lowestAmt, "price need to higher than lowestAmt.");
        require(_price >= _highestAmt, "There already has a higher bid.");

        _highestBuyer = msg.sender;
        _highestAmt = _price;
        // 记录竞价记录
        _bidLogs[msg.sender] = _price;
        // 达到期望值自动结束交易
        if (_highestAmt >= _expectAmt) {
            _status = 2;
            _buyer = _highestBuyer;
            _tradeAmt = _highestAmt;
            _tradeTime = now;
            emit newStatus(msg.sender, 2, now, "match expect price");
            return true;
        }
        return false;
    }

    // 指定交易
    function targetDeal(address buyer) public {
        _buyer = buyer;
        _tradeTime = now;
        _tradeAmt = _expectAmt;
        emit newStatus(msg.sender, 2, now, "targetDeal");
    }

    // 最终成交
    function deal() public onlyOwner returns(address buyer, uint256 _tradeAmt) {
        // 方便测试，暂时注释该条件
//        require(now >= _endTime, "Auction did not end.");
        if (_highestAmt <= 0) {
            _status = 3;
            return (_buyer, _tradeAmt);
        }
        _buyer = _highestBuyer;
        _tradeAmt = _highestAmt;
        _status = 2;
        _tradeTime = now;
        emit newStatus(msg.sender, 2, now, "deal");
        return (_buyer, _tradeAmt);
    }

    // 交易撤回
    function tradeEnd() public onlyOwner {
        _status = 3;
        emit newStatus(msg.sender, 3, now, "end");
    }
}
