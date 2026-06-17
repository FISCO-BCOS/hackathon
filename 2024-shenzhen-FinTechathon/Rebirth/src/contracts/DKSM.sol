// SPDX-License-Identifier: GPL-3.0
pragma solidity ^0.8.16;

contract DKSM {
    // 用户结构体定义
    struct User {
        address user;                 // 用户的以太坊地址
        string ID;                    // 用户的唯一标识符
        string Key;                   // 加密密钥
        string PL;                    // 权限列表
        string lifetime;              // 生命周期
        bool login;                   // 登录状态
        uint256 nonce;                // 随机数，用于防止重放攻击
        uint256 timestamp;            // 时间戳，用于记录注册或操作时间
        mapping(string => Packet) asreq;   // 存储 AS-EX 请求的映射
        mapping(string => Packet) asrep;   // 存储 AS-EX 响应的映射
        mapping(string => Packet) tgsreq;  // 存储 TGS-EX 请求的映射
        mapping(string => Packet) tgsrep;  // 存储 TGS-EX 响应的映射
        mapping(string => Packet) apreq;   // 存储 AP-EX 请求的映射
        mapping(string => Packet) aprep;   // 存储 AP-EX 响应的映射
    }

    // 数据包结构体定义，用于存储消息内容和时间戳
    struct Packet {
        string content;           // 消息内容
        uint256 timestamp;        // 消息的时间戳
    }

    address public owner;           // 合约的所有者地址
    mapping(string => User) users;  // 用户ID到用户信息的映射

    // 构造函数，设置合约所有者
    constructor() {
        owner = msg.sender;
    }

    // 初始化用户信息的函数，仅合约所有者可以调用
    function setup(address iUser, string calldata iID, string calldata iKey, string calldata iPL, string calldata ilifetime) external {
        require(msg.sender == owner);  // 检查调用者是否为合约所有者
        users[iID].user = iUser;       // 设置用户地址
        users[iID].ID = iID;           // 设置用户ID
        users[iID].Key = iKey;         // 设置用户密钥
        users[iID].PL = iPL;           // 设置权限列表
        users[iID].lifetime = ilifetime;// 设置生命周期
        users[iID].nonce = uint256(keccak256(abi.encodePacked(block.timestamp, block.difficulty, block.number)));  // 生成随机数nonce
        users[iID].timestamp = block.timestamp;  // 设置时间戳
        users[iID].login = false;      // 初始化登录状态为false
    }

    // 获取特定用户的nonce值，用于防止重放攻击
    function get_nonce(string calldata iID) external view returns (uint256){
        require(msg.sender == users[iID].user);  // 检查调用者是否为用户本人
        return users[iID].nonce;                 // 返回用户的nonce
    }

    // 内部函数，检查提供的nonce是否与存储的nonce匹配，并递增nonce
    function check_nonce(string calldata iID, uint256 _nonce) internal returns (bool) {
        require(msg.sender == users[iID].user);  // 检查调用者是否为用户本人
        bool Check;
        if (users[iID].nonce == _nonce) {
            Check = true;  // nonce匹配
        } else {
            Check = false; // nonce不匹配
        }
        users[iID].nonce += 1;  // 匹配后递增nonce
        return Check;           // 返回检查结果
    }

    // 返回用户的关键信息，用于验证设置的有效性
    function getInfo(string calldata iID) external view returns (address, string memory, string memory, string memory, string memory) {
        return (users[iID].user, users[iID].ID, users[iID].Key, users[iID].PL, users[iID].lifetime);
    }

    // 客户端设置AS-EX请求
    function C_Set_AS_REQ(string calldata IDc, string calldata IDas, string calldata AS_REQ, uint256 _nonce) external returns (bool) {
        require(msg.sender == users[IDc].user);  // 检查调用者是否为客户端用户
        bool Check;
        if (check_nonce(IDc, _nonce)) {  // 检查nonce
            users[IDc].asreq[IDas].content = AS_REQ;  // 存储AS-EX请求内容
            users[IDc].asreq[IDas].timestamp = block.timestamp;  // 设置时间戳
            Check = true;
        } else {
            Check = false;
        }
        return Check;  // 返回操作结果
    }

    // 认证服务器获取AS-EX请求
    function AS_Get_AS_REQ(string calldata IDc, string calldata IDas) external view returns (string memory, uint256, string memory, string memory) {
        require(msg.sender == users[IDas].user);  // 检查调用者是否为认证服务器用户
        return (users[IDc].asreq[IDas].content, users[IDc].asreq[IDas].timestamp, users[IDc].Key, users[IDas].Key);  // 返回AS-EX请求及相关信息
    }

    // 认证服务器设置AS-EX响应
    function AS_Set_AS_REP(string calldata IDc, string calldata IDas, string calldata AS_REP, uint256 _nonce) external returns (bool) {
        require(msg.sender == users[IDas].user);  // 检查调用者是否为认证服务器用户
        bool Check;
        if (check_nonce(IDas, _nonce)) {  // 检查nonce
            users[IDc].asrep[IDas].content = AS_REP;  // 存储AS-EX响应内容
            users[IDc].asrep[IDas].timestamp = block.timestamp;  // 设置时间戳
            users[IDc].login = true;  // 更新用户登录状态为true
            Check = true;
        } else {
            Check = false;
        }
        return Check;  // 返回操作结果
    }

    // 客户端获取AS-EX响应
    function C_Get_AS_REP(string calldata IDc, string calldata IDas) external view returns (string memory, uint256) {
        require(msg.sender == users[IDc].user);  // 检查调用者是否为客户端用户
        return (users[IDc].asrep[IDas].content, users[IDc].asrep[IDas].timestamp);  // 返回AS-EX响应及时间戳
    }

    //-----------------------------TGS-EX----------------------------
    // 客户端设置TGS-EX请求
    function C_Set_TGS_REQ(string calldata IDc, string calldata IDtgs, string calldata TGS_REQ, uint256 _nonce) external returns (bool) {
        require(msg.sender == users[IDc].user && users[IDc].login == true);  // 检查调用者是否为已登录的客户端用户
        bool Check;
        if (check_nonce(IDc, _nonce)) {  // 检查nonce
            users[IDc].tgsreq[IDtgs].content = TGS_REQ;  // 存储TGS-EX请求内容
            users[IDc].tgsreq[IDtgs].timestamp = block.timestamp;  // 设置时间戳
            Check = true;
        } else {
            Check = false;
        }
        return Check;  // 返回操作结果
    }

    // TGS服务器获取TGS-EX请求
    function TGS_Get_TGS_REQ(string calldata IDc, string calldata IDtgs) external view returns (string memory, uint256, string memory) {
        require(msg.sender == users[IDtgs].user);  // 检查调用者是否为TGS服务器用户
        return (users[IDc].tgsreq[IDtgs].content, users[IDc].tgsreq[IDtgs].timestamp, users[IDtgs].Key);  // 返回TGS-EX请求及相关信息
    }

    // TGS服务器获取服务密钥
    function TGS_Get_CKv(string calldata IDtgs, string calldata IDv) external view returns (string memory) {
        require(msg.sender == users[IDtgs].user);  // 检查调用者是否为TGS服务器用户
        return users[IDv].Key;  // 返回服务密钥
    }

    // TGS服务器设置TGS-EX响应
    function TGS_Set_TGS_REP(string calldata IDc, string calldata IDtgs, string calldata IDv, string calldata TGS_REP, uint256 _nonce) external returns (bool) {
        require(msg.sender == users[IDtgs].user);  // 检查调用者是否为TGS服务器用户
        bool Check;
        if (check_nonce(IDtgs, _nonce)) {  // 检查nonce
            users[IDc].tgsrep[IDv].content = TGS_REP;  // 存储TGS-EX响应内容
            users[IDc].tgsrep[IDv].timestamp = block.timestamp;  // 设置时间戳
            Check = true;
        } else {
            Check = false;
        }
        return Check;  // 返回操作结果
    }

    // 客户端获取TGS-EX响应
    function C_Get_TGS_REP(string calldata IDc, string calldata IDv) external view returns (string memory, uint256) {
        require(msg.sender == users[IDc].user);  // 检查调用者是否为客户端用户
        return (users[IDc].tgsrep[IDv].content, users[IDc].tgsrep[IDv].timestamp);  // 返回TGS-EX响应及时间戳
    }

    //-----------------------------AP-EX----------------------------
    // 客户端设置AP-EX请求
    function C_Set_AP_REQ(string calldata IDc, string calldata IDv, string calldata AP_REQ, uint256 _nonce) external returns (bool) {
        require(msg.sender == users[IDc].user);  // 检查调用者是否为客户端用户
        bool Check;
        if (check_nonce(IDc, _nonce)) {  // 检查nonce
            users[IDc].apreq[IDv].content = AP_REQ;  // 存储AP-EX请求内容
            users[IDc].apreq[IDv].timestamp = block.timestamp;  // 设置时间戳
            Check = true;
        } else {
            Check = false;
        }
        return Check;  // 返回操作结果
    }

    // 服务端获取AP-EX请求
    function S_Get_AP_REQ(string calldata IDc, string calldata IDv) external view returns (string memory, uint256) {
        require(msg.sender == users[IDv].user);  // 检查调用者是否为服务端用户
        return (users[IDc].apreq[IDv].content, users[IDc].apreq[IDv].timestamp);  // 返回AP-EX请求及时间戳
    }

    // 服务端设置AP-EX响应
    function S_Set_AP_REP(string calldata IDc, string calldata IDv, string calldata AP_REP, uint256 _nonce) external returns (bool) {
        require(msg.sender == users[IDv].user);  // 检查调用者是否为服务端用户
        bool Check;
        if (check_nonce(IDv, _nonce)) {  // 检查nonce
            users[IDc].aprep[IDv].content = AP_REP;  // 存储AP-EX响应内容
            users[IDc].aprep[IDv].timestamp = block.timestamp;  // 设置时间戳
            Check = true;
        } else {
            Check = false;
        }
        return Check;  // 返回操作结果
    }

    // 客户端获取AP-EX响应
    function C_Get_AP_REP(string calldata IDc, string calldata IDv) external view returns (string memory, uint256) {
        require(msg.sender == users[IDc].user);  // 检查调用者是否为客户端用户
        return (users[IDc].aprep[IDv].content, users[IDc].aprep[IDv].timestamp);  // 返回AP-EX响应及时间戳
    }
}
