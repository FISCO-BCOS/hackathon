pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;

import "./PointData.sol";
import "./BasicAuth.sol";
import "./DAO.sol";
import "./LibMath.sol";

contract PointController {
    using LibMath for uint256;

    struct PointLog {
        // 操作者地址
        address addr;
        // 操作时间
        uint timestamp;
        // 操作前积分
        uint256 beforePoint;
        // 操作后积分
        uint256 afterPoint;
        // 备注
        string remark;
    }

    PointData _pointData;
    DAO _dao;
    mapping(address => PointLog[]) private _pointLog;

    event LogRegister(address account);
    event LogUnregister(address account);
    event TransferEvent(address from, address to, uint amt);
    event FreezeEvent(address from, uint amt);
    event UnFreezeEvent(address from, uint amt);


    modifier accountExist(address addr) {
        require(_pointData.hasAccount(addr)==true && addr != address(0), "Only existed account!");
        _;
    }

    modifier accountNotExist() {
        require(_pointData.hasAccount(msg.sender)==false, "Account already existed!");
        _;
    }

    modifier canUnregister() {
        require(_pointData.hasAccount(msg.sender)==true && _pointData.getPoint(msg.sender) == 0 , "Can't unregister!");
        _;
    }

    modifier checkAccount(address sender) {
        require(msg.sender != sender && sender != address(0), "Can't transfer to illegal address!");
        _;
    }

    modifier onlyDAO() {
        require(_dao.isDAO(msg.sender), "IssuerRole: caller does not have the Issuer role");
        _;
    }


    constructor() public {
        _pointData = new PointData();
        _dao = new DAO();
    }

    //注册模块：消费者、生产者（初始化积分后期由DAO转账）
    function register() public accountNotExist {
        _pointData.setAccount(msg.sender, true);
        // init point
        _pointData.setPoint(msg.sender, 0);
        emit LogRegister(msg.sender);
    }

    // 目前没有注销功能
    function unregister() public canUnregister {
        _pointData.setAccount(msg.sender, false);
        emit LogUnregister(msg.sender);
    }

    // 每年的初始积分
    function initPoint(uint256 value1, uint256 value2) public onlyDAO {
        uint amt1 = _pointData.getTotalAmount1() + value1;
        uint amt2 = _pointData.getTotalAmount2() + value2;
        _pointData.setTotalAmount1(amt1);
        _pointData.setTotalAmount2(amt2);
        uint totalPoint = amt1 + amt2;
        uint beforePoint = _pointData.getPoint(msg.sender);
        uint afterPoint = totalPoint + beforePoint;
        // 初始化积分放在dao账户之中
        _pointData.setPoint(msg.sender, afterPoint);
        _pointLog[msg.sender].push(PointLog({addr:msg.sender, timestamp:now, beforePoint: beforePoint, afterPoint: afterPoint, remark: "积分初始化"}));
    }

    function getTotalAmount1() public onlyDAO view returns(uint256) {
        return _pointData.getTotalAmount1();
    }

    function getTotalAmount2() public onlyDAO view returns(uint256) {
        return _pointData.getTotalAmount2();
    }

    // 添加DAO账户
    function addDAO(address account) public {
        _dao.addDAO(account);
        _pointData.setAccount(account, true);
        _pointData.setPoint(account, 0);
    }

    //信息查询：地址（账户）是否存在、返回账户信息
    function isRegistered(address addr) public view returns (bool) {
        return _pointData.hasAccount(addr);
    }

    // 查询指定账户积分
    function getPoint(address addr) public view returns (uint256) {
        return _pointData.getPoint(addr);
    }

    //纳税：向DAO、积分派发：给与消费者、回收企业积分
    //企业缴纳积分
    function pay(address DAOaddr, uint256 point) public {
        // 操作前from账户积分
        uint fromBeforePoint;
        // 操作前to账户积分
        uint toBeforePoint;
        // 操作后from账户积分
        uint fromAfterPoint;
        // 操作后to账户积分
        uint toAfterPoint;
        (fromBeforePoint, toBeforePoint, fromAfterPoint, toAfterPoint) = _pointData.transfer(msg.sender, DAOaddr, point);
        emit TransferEvent(msg.sender, DAOaddr, point);
        _pointLog[msg.sender].push(PointLog({addr:msg.sender, timestamp:now, beforePoint: fromBeforePoint, afterPoint: fromAfterPoint, remark: "积分缴纳"}));
        _pointLog[DAOaddr].push(PointLog({addr:DAOaddr, timestamp:now, beforePoint: toBeforePoint, afterPoint: toAfterPoint, remark: "积分缴纳"}));
    }

    // DAO向目标账户转移积分
    function daoTransfer(address to, uint256 point) onlyDAO public {
        // 操作前from账户积分
        uint fromBeforePoint;
        // 操作前to账户积分
        uint toBeforePoint;
        // 操作后from账户积分
        uint fromAfterPoint;
        // 操作后to账户积分
        uint toAfterPoint;
        (fromBeforePoint, toBeforePoint, fromAfterPoint, toAfterPoint) = _pointData.transfer(msg.sender, to, point);
        emit TransferEvent(msg.sender, to, point);
        _pointLog[msg.sender].push(PointLog({addr:msg.sender, timestamp:now, beforePoint: fromBeforePoint, afterPoint: fromAfterPoint, remark: "DAO派发"}));
        _pointLog[to].push(PointLog({addr:to, timestamp:now, beforePoint: toBeforePoint, afterPoint: toAfterPoint, remark: "DAO派发"}));
    }

    // 普通账户之间转移积分
    function transfer(address to, uint256 point) public {
        // 操作前from账户积分
        uint fromBeforePoint;
        // 操作前to账户积分
        uint toBeforePoint;
        // 操作后from账户积分
        uint fromAfterPoint;
        // 操作后to账户积分
        uint toAfterPoint;
        (fromBeforePoint, toBeforePoint, fromAfterPoint, toAfterPoint) = _pointData.transfer(msg.sender, to, point);
        emit TransferEvent(msg.sender, to, point);
        _pointLog[msg.sender].push(PointLog({addr:msg.sender, timestamp:now, beforePoint: fromBeforePoint, afterPoint: fromAfterPoint, remark: "普通交易"}));
        _pointLog[to].push(PointLog({addr:msg.sender, timestamp:now, beforePoint: toBeforePoint, afterPoint: toAfterPoint, remark: "普通交易"}));
    }

    // 积分冻结
    function freeze(uint256 point) public {
        uint beforePoint;
        uint afterPoint;
        (beforePoint, afterPoint) = _pointData.freeze(msg.sender, point);
        emit FreezeEvent(msg.sender, point);
        _pointLog[msg.sender].push(PointLog({addr:msg.sender, timestamp:now, beforePoint: beforePoint, afterPoint: afterPoint, remark: "交易积分冻结"}));
    }

    // 从冻结积分中转移积分
    function unFreezeAndTransfer(address to, uint256 point) public {
        uint beforePoint;
        uint afterPoint;
        (beforePoint, afterPoint) = _pointData.unFreezeAndTransfer(msg.sender, to, point);
        emit TransferEvent(msg.sender, to, point);
        _pointLog[msg.sender].push(PointLog({addr:msg.sender, timestamp:now, beforePoint: beforePoint, afterPoint: afterPoint, remark: "交易积分转移"}));
    }

    function unFreeze(uint256 point) public {
        uint beforePoint;
        uint afterPoint;
        (beforePoint, afterPoint) = _pointData.unFreeze(msg.sender, point);
        emit UnFreezeEvent(msg.sender, point);
        _pointLog[msg.sender].push(PointLog({addr:msg.sender, timestamp:now, beforePoint: beforePoint, afterPoint: afterPoint, remark: "交易积分解冻"}));
    }

    function getPointLog(address account) public view returns(PointLog[] memory _data) {
        return _pointLog[account];
    }
}
