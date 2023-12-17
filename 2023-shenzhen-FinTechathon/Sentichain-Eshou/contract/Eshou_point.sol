// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;


// 积分合约
contract PointsContract {
    // 定义变量
    mapping(address => uint256) public balances; // 地址（账户）→ 积分余额
    uint256 public accNum;//维护一个变量来记录balances的长度
    address[] public Account;//维护一个记录账户地址的数组，方便后续遍历

    // 定义事件
    event AccountAdded(address indexed account);
    event BalanceAdded(address indexed account, uint256 amount);
    event BalanceReduced(address indexed account, uint256 amount);

    //构造函数
    constructor(){
        accNum=0;
    }

    // 添加新地址
    function addAccount(address account) public {
        require(!isAccountExist(account), "Account already exists"); // 确保地址不存在
        balances[account] = 0; // 初始化积分余额为0
        accNum++;
        Account.push(account);
        emit AccountAdded(account); // 触发事件
    }

    // 增加积分余额
    function addBalance(address account, uint256 amount) public returns (bool) {
        require(isAccountExist(account),"Account does not exist");//检验账本中是否存在该地址
        require(balances[account] + amount >= balances[account], "Invalid amount"); // 防止溢出
        balances[account] += amount; // 增加积分余额
        emit BalanceAdded(account, amount); // 触发事件
        return true;
    }

    // 减少积分余额
    function reduceBalance(address account, uint256 amount) public returns (bool) {
        require(isAccountExist(account),"Account does not exist");//检验账本中是否存在该地址
        require(balances[account] >= amount, "Insufficient balance"); // 确保余额足够
        balances[account] -= amount; // 减少积分余额
        emit BalanceReduced(account, amount); // 触发事件
        return true;
    }
    //查询账本中是否存在某个地址
    function isAccountExist(address account) public view returns (bool) {
    for (uint256 i = 0; i < accNum; i++) {
        if (Account[i] == account) {
            return true;
        }
    }
    return false;
}
    // 查询积分余额
    function getBalance(address account) public view returns (uint256) {
        require(isAccountExist(account), "Account does not exist"); // 校验地址是否存在
        return balances[account]; // 返回指定地址的积分余额
    }
}