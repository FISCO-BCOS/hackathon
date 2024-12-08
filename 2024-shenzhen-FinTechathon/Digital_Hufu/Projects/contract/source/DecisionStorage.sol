// SPDX-License-Identifier: MIT
pragma solidity >=0.6.10 <0.8.20;

contract DecisionStorage {
    // 决策记录结构
    struct Decision {
        string evidence;      // 证据
        string nodeID;        // 节点ID
        uint256 timestamp;    // 时间戳
        bool approved;        // 是否批准
    }
    
    // 存储所有决策记录
    Decision[] public decisions;
    
    // 记录决策的事件
    event DecisionRecorded(
        string evidence,
        string nodeID,
        uint256 timestamp,
        bool approved
    );
    
    function recordDecision(string memory evidence, string memory nodeID, bool approved) public returns (bool) {
        Decision memory newDecision = Decision({
            evidence: evidence,
            nodeID: nodeID,
            timestamp: block.timestamp,
            approved: approved
        });
        
        // 存储决策
        decisions.push(newDecision);
        
        // 触发事件
        emit DecisionRecorded(
            newDecision.evidence,
            newDecision.nodeID,
            newDecision.timestamp,
            newDecision.approved
        );
        
        return true;
    }
    
    // 验证证据 可以添加具体的验证逻辑
    function verifyEvidence(string memory evidence) public view returns (bool) {
        // 目前返回true用于测试
        return true;
    }
    
    // 获取决策记录数量
    function getDecisionCount() public view returns (uint256) {
        return decisions.length;
    }
    
    // 获取指定索引的决策记录
    function getDecision(uint256 index) public view returns (
        string memory evidence,
        string memory nodeID,
        uint256 timestamp,
        bool approved
    ) {
        require(index < decisions.length, "Index out of bounds");
        Decision memory decision = decisions[index];
        return (
            decision.evidence,
            decision.nodeID,
            decision.timestamp,
            decision.approved
        );
    }
    
}