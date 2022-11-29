/**
 * @brief implementation for ConsensusInterface of raft
 * @file RaftImpl.h
*/
#pragma once
#include "../../../bcos-framework/bcos-framework/consensus/ConsensusInterface.h"

namespace bcos
{
namespace consensus
{
class RaftImpl : public ConsensusInterface
{
public:
    using Ptr = std::shared_ptr<RaftImpl>;
    explicit RaftImpl()
    {
        RAFT_LOG(INFO) << LOG_DESC("RaftImpl is coming!");
    }
    virtual ~RaftImpl() { stop(); }

    void start() override;
    void stop() override;

    void asyncSubmitProposal(bool _containSysTxs, bytesConstRef _proposalData,
        bcos::protocol::BlockNumber _proposalIndex, bcos::crypto::HashType const& _proposalHash,
        std::function<void(Error::Ptr)> _onProposalSubmitted) override;

    void asyncGetRaftTerm(std::function<void(Error::Ptr, ViewType)> _onGetView) override;

    void asyncNotifyConsensusMessage(bcos::Error::Ptr _error, std::string const& _id,
        bcos::crypto::NodeIDPtr _nodeID, bytesConstRef _data,
        std::function<void(Error::Ptr _error)> _onRecv) override;

    // the sync module calls this interface to check block
    void asyncCheckBlock(bcos::protocol::Block::Ptr _block,
        std::function<void(Error::Ptr, bool)> _onVerifyFinish) override;

    // the sync module calls this interface to notify new block
    void asyncNotifyNewBlock(bcos::ledger::LedgerConfig::Ptr _ledgerConfig,
        std::function<void(Error::Ptr)> _onRecv) override;

    void notifyHighestSyncingNumber(bcos::protocol::BlockNumber _blockNumber) override;
    void asyncNoteUnSealedTxsSize(
        uint64_t _unsealedTxsSize, std::function<void(Error::Ptr)> _onRecvResponse) override;
    void setLedgerFetcher(bcos::tool::LedgerConfigFetcher::Ptr _ledgerFetcher)
    {
        RAFT_LOG(INFO) << LOG_DESC("setLedgerFetcher!");
    }
    //PBFTEngine::Ptr pbftEngine() { return m_pbftEngine; }

    virtual void init();

    // notify the sealer seal Proposal
    void registerSealProposalNotifier(
        std::function<void(size_t, size_t, size_t, std::function<void(Error::Ptr)>)>
            _sealProposalNotifier)
    {
        RAFT_LOG(INFO) << LOG_DESC("registerSealProposalNotifier!");
    }

    // notify the sealer the latest blockNumber
    void registerStateNotifier(std::function<void(bcos::protocol::BlockNumber)> _stateNotifier)
    {
        RAFT_LOG(INFO) << LOG_DESC("registerStateNotifier!");
    }
    // the sync module notify the consensus module the new block
    void registerNewBlockNotifier(
        std::function<void(bcos::ledger::LedgerConfig::Ptr, std::function<void(Error::Ptr)>)>
            _newBlockNotifier)
    {
        RAFT_LOG(INFO) << LOG_DESC("registerNewBlockNotifier!");
    }

    void registerFaultyDiscriminator(
        std::function<bool(bcos::crypto::NodeIDPtr)> _faultyDiscriminator)
    {
        RAFT_LOG(INFO) << LOG_DESC("registerFaultyDiscriminator!");
    }

    // handler to notify the consensusing proposal index to the sync module
    void registerCommittedProposalNotifier(
        std::function<void(bcos::protocol::BlockNumber, std::function<void(Error::Ptr)>)>
            _committedProposalNotifier)
    {
        RAFT_LOG(INFO) << LOG_DESC("registerCommittedProposalNotifier!");
    }

    // handler to notify the sealer reset the sealing proposals
    void registerSealerResetNotifier(
        std::function<void(std::function<void(Error::Ptr)>)> _sealerResetNotifier)
    {
        RAFT_LOG(INFO) << LOG_DESC("registerSealerResetNotifier!");
    }

    ConsensusNodeList consensusNodeList() const override
    {
        RAFT_LOG(INFO) << LOG_DESC("consensusNodeList!");
        return consensusNodeList();
    }
    uint64_t nodeIndex() const override 
    { 
        RAFT_LOG(INFO) << LOG_DESC("nodeIndex!");
        return 0; 
    }
    void asyncGetConsensusStatus(
        std::function<void(Error::Ptr, std::string)> _onGetConsensusStatus) override;

    void notifyConnectedNodes(bcos::crypto::NodeIDSet const& _connectedNodes,
        std::function<void(Error::Ptr)> _onResponse) override
    {
        RAFT_LOG(INFO) << LOG_DESC("notifyConnectedNodes!");
    }
    virtual void enableAsMasterNode(bool _isMasterNode);

    virtual bool masterNode() const { return m_masterNode.load(); }

    virtual void registerVersionInfoNotification(
        std::function<void(uint32_t _version)> _versionNotification)
    {
        RAFT_LOG(INFO) << LOG_DESC("registerVersionInfoNotification!");
    }

    uint32_t compatibilityVersion() const override
    {
        RAFT_LOG(INFO) << LOG_DESC("compatibilityVersion!");
        return 0;
    }

    void clearExceptionProposalState(bcos::protocol::BlockNumber _number) override
    {
        RAFT_LOG(INFO) << LOG_DESC("clearExceptionProposalState!");
    }

protected:
    // PBFTEngine::Ptr m_pbftEngine;
    // BlockValidator::Ptr m_blockValidator;
    std::atomic_bool m_running = {false};
    std::atomic_bool m_masterNode = {false};
};
}
}