pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;

/*
 电池合约，包含操作记录
 @author cmgun
*/
contract BatteryContract {
    struct TraceData {
        // 操作者地址
        address addr;
        // 电池状态
        int16 status;
        // 操作时间
        uint timestamp;
        // 备注信息
        string remark;
    }
    // 电池id
    string _id;
    // 批次号
    string _batchNo;
    // 电池状态 1-待安全审查,2-正常,3-待回收,4-回收中,5-梯次利用,6-拆解
    int16 _status;
    // 电池信息
    string _info;
    // 流转记录
    TraceData[] _traceData;
    // 当前所属方
    address _owner;

    modifier onlyOwner() {
        require(msg.sender == _owner, "Not current owner!");
        _;
    }

    event newStatus(address addr, int16 status, uint timestamp, string remark);

    // 初始化
    constructor(string id, string batchNo, string info) public {
        _id = id;
        _batchNo = batchNo;
        _info = info;
        _status = 1;
        _owner = msg.sender;
        _traceData.push(TraceData({addr:msg.sender, status:1, timestamp:now, remark:"电池生产"}));
        emit newStatus(msg.sender, 1, now, "create");
    }

    // 安全审查
    function safeCheck(string remark) public {
        _status = 2;
        _traceData.push(TraceData({addr:msg.sender, status:2, timestamp:now, remark:remark}));
        emit newStatus(msg.sender, 2, now, remark);
    }

    // 归属流转
    function transfer(address to, string remark, int16 status) public onlyOwner {
        require(_status != 6, "current status don't allow to transfer");
        _owner = to;
        _traceData.push(TraceData({addr:msg.sender, status:_status, timestamp:now, remark: remark}));
        emit newStatus(msg.sender, status, now, remark);
    }

    // 电池拆解，只有拆解企业可以操作
    function endLife(string remark) public onlyOwner {
        _traceData.push(TraceData({addr:msg.sender, status:6, timestamp:now, remark: remark}));
        emit newStatus(msg.sender, 6, now, remark);
    }

    // 查询电池状态
    function getStatus() public view returns(int16) {
        return _status;
    }

    // 查询溯源记录
    function getTraceInfo() public view returns(TraceData[] memory _data) {
        return _traceData;
    }
}
