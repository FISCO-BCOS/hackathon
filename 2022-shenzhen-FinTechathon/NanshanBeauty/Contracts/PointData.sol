pragma solidity ^0.4.25;

import "./BasicAuth.sol";
import "./DAO.sol";



contract PointData is BasicAuth {

    mapping(address => uint256) private _point;
    mapping(address => uint256) private _freezePoint;
    mapping(address => bool) private _accounts;

    // 电池生产商、车企积分总额
    uint256 public _totalAmount1;
    // 回收商积分总额
    uint256 public _totalAmount2;

    event LogSend( address indexed from, address indexed to, uint256 value);

    // 积分转移
    function transfer(address _from, address _to, uint256 _value) public returns(uint, uint, uint, uint) {
        require(_to != 0x0); // address can't be null
        require(_point[_from] >= _value);
        require(_point[_to] + _value > _point[_to]);

        uint previousBalances = _point[_from] + _point[_to];
        // 操作前from账户积分
        uint fromBeforePoint = _point[_from];
        // 操作前to账户积分
        uint toBeforePoint = _point[_to];
        _point[_from] -= _value;
        _point[_to] += _value;
        emit LogSend(_from, _to, _value);
        // 操作后from账户积分
        uint fromAfterPoint = _point[_from];
        // 操作后to账户积分
        uint toAfterPoint = _point[_to];

        assert(_point[_from] + _point[_to] == previousBalances);
        return (fromBeforePoint, toBeforePoint, fromAfterPoint, toAfterPoint);
    }

    // 冻结积分
    function freeze(address _addr, uint256 _value) public returns(uint, uint) {
        require(_point[_addr] >= _value);
        uint beforePoint = _point[_addr];
        _freezePoint[_addr] += _value;
        _point[_addr] -= _value;
        uint afterPoint = _point[_addr];
        return (beforePoint, afterPoint);
    }

    // 解冻积分
    function unFreeze(address _addr, uint256 _value) public returns(uint, uint) {
        require(_freezePoint[_addr] >= _value);
        uint beforePoint = _point[_addr];
        _freezePoint[_addr] -= _value;
        _point[_addr] += _value;
        uint afterPoint = _point[_addr];
        return (beforePoint, afterPoint);
    }

    // 从冻结积分中转移到指定账户
    function unFreezeAndTransfer(address _from, address _to, uint256 _value) public returns(uint, uint) {
        require(_freezePoint[_from] >= _value);
        uint beforePoint = _point[_to];
        _freezePoint[_from] -= _value;
        _point[_to] += _value;
        uint afterPoint = _point[_to];
        return (beforePoint, afterPoint);
    }

    function setPoint(address a, uint256 value) public returns (bool) {
        _point[a] = value;
        return true;
    }

    function setAccount(address a, bool b) public returns (bool) {
        _accounts[a] = b;
        return true;
    }

    function setTotalAmount1(uint256 amount) public returns (bool) {
        _totalAmount1 = amount;
        return true;
    }

    function setTotalAmount2(uint256 amount) public returns (bool) {
        _totalAmount2 = amount;
        return true;
    }

    function getTotalAmount1() public returns (uint256) {
        return _totalAmount1;
    }

    function getTotalAmount2() public returns (uint256) {
        return _totalAmount1;
    }

    function getAccountInfo(address account) public view returns (bool, uint256) {
        return (_accounts[account], _point[account]);
    }

    function hasAccount(address account) public view returns(bool) {
        return _accounts[account];
    }

    function getPoint(address account) public view returns (uint256) {
        return _point[account];
    }
}
