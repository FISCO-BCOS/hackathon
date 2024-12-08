// SPDX-License-Identifier: GPL-3.0
pragma solidity ^0.4.25;

contract ModelAuthorization {
    // 授权/使用存证结构
    struct AuthorizationRecord {
        string modelId; // 模型ID
        address seller; // 模型授权者
        address buyer; // 模型使用者
        uint256 timestamp; // 授权/使用时间
        bool isAuthorized; // 是否已授权
        bool isUsed; // 是否已使用
    }

    // 存储所有授权/使用存证
    mapping(string => AuthorizationRecord[]) private authorizations;

    // 事件
    event AuthorizationRequested(string modelId, address seller, address buyer, uint256 timestamp);
    event AuthorizationVoted(string modelId, uint256 authorizationIndex, address voter, bool isAuthorization);
    event AuthorizationCompleted(string modelId, uint256 authorizationIndex);

    // 发起模型授权请求
    function requestAuthorization(string _modelId, address _seller, address _buyer) public {
        require(_seller != address(0), "Seller address is invalid.");
        require(_buyer != address(0), "Buyer address is invalid.");

        // 创建授权/使用存证
        authorizations[_modelId].length++;
        AuthorizationRecord storage record = authorizations[_modelId][authorizations[_modelId].length - 1];
        record.modelId = _modelId;
        record.seller = _seller;
        record.buyer = _buyer;
        record.timestamp = block.timestamp;
        record.isAuthorized = false;
        record.isUsed = false;

        emit AuthorizationRequested(_modelId, _seller, _buyer, block.timestamp);
    }

    // 投票授权/使用
    function voteAuthorization(string _modelId, uint256 _authorizationIndex, bool _isAuthorization) public {
        require(_authorizationIndex < authorizations[_modelId].length, "Authorization does not exist.");
        AuthorizationRecord storage record = authorizations[_modelId][_authorizationIndex];
        require(!record.isAuthorized || !record.isUsed, "Authorization is already completed.");
        require(msg.sender == record.seller || msg.sender == record.buyer, "Only seller or buyer can vote.");

        // 卖方授权
        if (msg.sender == record.seller && !record.isAuthorized) {
            record.isAuthorized = _isAuthorization;
            emit AuthorizationVoted(_modelId, _authorizationIndex, msg.sender, _isAuthorization);
        }

        // 买方表示已使用
        if (msg.sender == record.buyer && record.isAuthorized && !record.isUsed) {
            record.isUsed = _isAuthorization;
            emit AuthorizationVoted(_modelId, _authorizationIndex, msg.sender, _isAuthorization);
        }

        // 检查是否双方都已完成
        if (record.isAuthorized && record.isUsed) {
            emit AuthorizationCompleted(_modelId, _authorizationIndex);
        }
    }

    // 获取授权/使用存证详情
    function getAuthorization(string _modelId, uint256 _authorizationIndex) public view returns (
        string modelId,
        address seller,
        address buyer,
        uint256 timestamp,
        bool isAuthorized,
        bool isUsed
    ) {
        require(_authorizationIndex < authorizations[_modelId].length, "Authorization does not exist.");

        AuthorizationRecord storage record = authorizations[_modelId][_authorizationIndex];

        return (
            record.modelId,
            record.seller,
            record.buyer,
            record.timestamp,
            record.isAuthorized,
            record.isUsed
        );
    }

    // 获取模型的所有授权/使用记录
    function getAuthorizationsByModel(string _modelId) public view returns (
        address[] sellers,
        address[] buyers,
        uint256[] timestamps,
        bool[] auths,
        bool[] usages
    ) {
        uint256 count = authorizations[_modelId].length;
        sellers = new address[](count);
        buyers = new address[](count);
        timestamps = new uint256[](count);
        auths = new bool[](count);
        usages = new bool[](count);

        for (uint256 i = 0; i < count; i++) {
            AuthorizationRecord storage record = authorizations[_modelId][i];
            sellers[i] = record.seller;
            buyers[i] = record.buyer;
            timestamps[i] = record.timestamp;
            auths[i] = record.isAuthorized;
            usages[i] = record.isUsed;
        }
    }
}