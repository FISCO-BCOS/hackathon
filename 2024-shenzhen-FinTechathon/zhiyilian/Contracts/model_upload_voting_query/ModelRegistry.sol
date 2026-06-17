// SPDX-License-Identifier: GPL-3.0
pragma solidity ^0.4.25;

contract ModelRegistry {
    // 数据结构：模型上链提案
    struct ModelProposal {
        string modelId;
        string modelHash;
        string reportHash;
        address owner;
        uint256 votes; // 当前投票数
        bool isApproved; // 是否批准
        mapping(address => bool) voted; // 记录哪些评估机构已投票
    }

    // 模型提案的存储
    mapping(string => ModelProposal) private proposals;

    // 模型上链的存储
    struct Model {
        string modelHash;
        string reportHash;
        address owner;
    }

    mapping(string => Model) private models;

    // 评估机构列表
    mapping(address => bool) public evaluators;

    // 投票阈值
    uint256 public threshold;

    // 合约部署者
    address public admin;

    // 事件
    event ProposalCreated(string modelId, address proposer);
    event VoteCasted(string modelId, address voter, uint256 totalVotes);
    event ModelApproved(string modelId);

    // 构造函数
    constructor(uint256 _threshold) public {
        admin = msg.sender;
        threshold = _threshold;
    }

    // 添加评估机构（仅管理员权限）
    function addEvaluator(address _evaluator) public {
        require(msg.sender == admin, "Only admin can add evaluators.");
        evaluators[_evaluator] = true;
    }

    // 发起模型上链提案
    function createProposal(
        string _modelId,
        string _modelHash,
        string _reportHash,
        address _owner
    ) public {
        require(evaluators[msg.sender], "Only evaluators can create proposals.");
        require(proposals[_modelId].owner == address(0), "Model ID already exists.");

        // 创建提案
        proposals[_modelId] = ModelProposal({
            modelId: _modelId,
            modelHash: _modelHash,
            reportHash: _reportHash,
            owner: _owner,
            votes: 0,
            isApproved: false
        });

        emit ProposalCreated(_modelId, msg.sender);
    }

    // 评估机构为模型提案投票
    function voteForProposal(string _modelId) public {
        require(evaluators[msg.sender], "Only evaluators can vote.");
        ModelProposal storage proposal = proposals[_modelId];
        require(proposal.owner != address(0), "Proposal does not exist.");
        require(!proposal.voted[msg.sender], "You have already voted.");
        require(!proposal.isApproved, "Proposal is already approved.");

        // 投票
        proposal.voted[msg.sender] = true;
        proposal.votes++;

        emit VoteCasted(_modelId, msg.sender, proposal.votes);

        // 检查是否达到阈值
        if (proposal.votes >= threshold) {
            // 批准并上链
            proposal.isApproved = true;
            models[_modelId] = Model({
                modelHash: proposal.modelHash,
                reportHash: proposal.reportHash,
                owner: proposal.owner
            });

            emit ModelApproved(_modelId);
        }
    }

    // 获取提案信息
    function getProposal(string _modelId) public view returns (
        string modelId,
        string modelHash,
        string reportHash,
        address owner,
        uint256 votes,
        bool isApproved
    ) {
        ModelProposal storage proposal = proposals[_modelId];
        require(proposal.owner != address(0), "Proposal does not exist.");
        return (
            proposal.modelId,
            proposal.modelHash,
            proposal.reportHash,
            proposal.owner,
            proposal.votes,
            proposal.isApproved
        );
    }

    // 获取模型上链信息
    function getModel(string _modelId) public view returns (
        string modelHash,
        string reportHash,
        address owner
    ) {
        Model storage model = models[_modelId];
        require(model.owner != address(0), "Model does not exist.");

        return (
            model.modelHash,
            model.reportHash,
            model.owner
        );
    }

    // 更新投票阈值（仅管理员权限）
    function updateThreshold(uint256 _newThreshold) public {
        require(msg.sender == admin, "Only admin can update the threshold.");
        threshold = _newThreshold;
    }
}
