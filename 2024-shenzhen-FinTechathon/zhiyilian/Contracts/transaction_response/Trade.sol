// SPDX-License-Identifier: GPL-3.0
pragma solidity ^0.4.25;

contract ModelTransaction {
    // 交易存证结构
    struct TransactionRecord {
        string modelId; // 模型ID
        address seller; // 模型拥有者
        address buyer; // 模型购买者
        uint256 amount; // 交易金额
        uint256 timestamp; // 交易时间
        mapping(address => bool) votes; // 投票情况
        bool isCompleted; // 是否完成
    }

    // 存储所有交易存证
    mapping(string => TransactionRecord[]) private transactions;

    // 事件
    event TransactionCreated(string modelId, address seller, address buyer, uint256 amount, uint256 timestamp);
    event VoteCompleted(string modelId, uint256 transactionIndex, address voter);
    event TransactionCompleted(string modelId, uint256 transactionIndex);

    // 创建交易存证
    function createTransaction(string _modelId, address _seller, address _buyer, uint256 _amount) public {
        require(_seller != address(0), "Seller address is invalid.");
        require(_buyer != address(0), "Buyer address is invalid.");
        require(_amount > 0, "Transaction amount must be greater than zero.");

        // 创建交易存证
        transactions[_modelId].length++;
        TransactionRecord storage transaction = transactions[_modelId][transactions[_modelId].length - 1];
        transaction.modelId = _modelId;
        transaction.seller = _seller;
        transaction.buyer = _buyer;
        transaction.amount = _amount;
        transaction.timestamp = block.timestamp;
        transaction.isCompleted = false;

        emit TransactionCreated(_modelId, _seller, _buyer, _amount, block.timestamp);
    }

    // 投票确认交易
    function voteForTransaction(string _modelId, uint256 _transactionIndex) public {
        require(_transactionIndex < transactions[_modelId].length, "Transaction does not exist.");
        require(!transactions[_modelId][_transactionIndex].isCompleted, "Transaction is already completed.");
        require(msg.sender == transactions[_modelId][_transactionIndex].seller || msg.sender == transactions[_modelId][_transactionIndex].buyer, "Only seller or buyer can vote.");
        require(!transactions[_modelId][_transactionIndex].votes[msg.sender], "You have already voted.");
        TransactionRecord storage transaction = transactions[_modelId][_transactionIndex];
        
        // 投票
        transaction.votes[msg.sender] = true;

        emit VoteCompleted(_modelId, _transactionIndex, msg.sender);

        // 检查是否双方都投票完成
        if (transaction.votes[transaction.seller] && transaction.votes[transaction.buyer]) {
            transaction.isCompleted = true;
            emit TransactionCompleted(_modelId, _transactionIndex);
        }
    }

    // 获取交易存证详情
    function getTransaction(string _modelId, uint256 _transactionIndex) public view returns (
        string modelId,
        address seller,
        address buyer,
        uint256 amount,
        uint256 timestamp,
        bool isCompleted
    ) {
        require(_transactionIndex < transactions[_modelId].length, "Transaction does not exist.");

        TransactionRecord storage transaction = transactions[_modelId][_transactionIndex];

        return (
            transaction.modelId,
            transaction.seller,
            transaction.buyer,
            transaction.amount,
            transaction.timestamp,
            transaction.isCompleted
        );
    }

    // 获取模型的所有交易
    function getTransactionsByModel(string _modelId) public view returns (
        address[] sellers,
        address[] buyers,
        uint256[] amounts,
        uint256[] timestamps,
        bool[] completions
    ) {
        uint256 count = transactions[_modelId].length;
        sellers = new address[](count);
        buyers = new address[](count);
        amounts = new uint256[](count);
        timestamps = new uint256[](count);
        completions = new bool[](count);

        for (uint256 i = 0; i < count; i++) {
            TransactionRecord storage transaction = transactions[_modelId][i];
            sellers[i] = transaction.seller;
            buyers[i] = transaction.buyer;
            amounts[i] = transaction.amount;
            timestamps[i] = transaction.timestamp;
            completions[i] = transaction.isCompleted;
        }
    }
}
