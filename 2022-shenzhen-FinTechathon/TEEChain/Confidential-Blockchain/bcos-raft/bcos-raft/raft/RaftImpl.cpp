/**
 * @brief implementation for ConsensusInterface of raft
 * @file RaftImpl.cpp
*/
// #include "RaftImpl.h"
#include <bcos-framework/consensus/ConsensusInterface.h>
#include <utilities/Common.h>
#include <json/json.h>

using namespace bcos;
using namespace bcos::consensus;

void RaftImpl::start()
{
    RAFT_LOG(INFO) << LOG_DESC("RaftImpl start!");
}

void RaftImpl::stop()
{
    RAFT_LOG(INFO) << LOG_DESC("RaftImpl stop!");
}

void RaftImpl::asyncSubmitProposal(bool _containSysTxs, bytesConstRef _proposalData,
    bcos::protocol::BlockNumber _proposalIndex, bcos::crypto::HashType const& _proposalHash,
    std::function<void(Error::Ptr)> _onProposalSubmitted)
{
    RAFT_LOG(INFO) << LOG_DESC("asyncSubmitProposal!");
}

void RaftImpl::asyncGetRaftTerm(std::function<void(Error::Ptr, ViewType)> _onGetView)
{
    RAFT_LOG(INFO) << LOG_DESC("asyncGetRaftTerm!");
}

void RaftImpl::asyncNotifyConsensusMessage(bcos::Error::Ptr _error, std::string const& _id,
    bcos::crypto::NodeIDPtr _nodeID, bytesConstRef _data,
    std::function<void(Error::Ptr _error)> _onRecv)
{
    RAFT_LOG(INFO) << LOG_DESC("asyncNotifyConsensusMessage!");
}

// the sync module calls this interface to check block
void RaftImpl::asyncCheckBlock(
    bcos::protocol::Block::Ptr _block, std::function<void(Error::Ptr, bool)> _onVerifyFinish)
{
    RAFT_LOG(INFO) << LOG_DESC("asyncCheckBlock!");
}

// the sync module calls this interface to notify new block
void RaftImpl::asyncNotifyNewBlock(
    bcos::ledger::LedgerConfig::Ptr _ledgerConfig, std::function<void(Error::Ptr)> _onRecv)
{
    RAFT_LOG(INFO) << LOG_DESC("asyncNotifyNewBlock!");
}

void RaftImpl::notifyHighestSyncingNumber(bcos::protocol::BlockNumber _blockNumber)
{
    RAFT_LOG(INFO) << LOG_DESC("notifyHighestSyncingNumber!");
}

void RaftImpl::asyncNoteUnSealedTxsSize(
    uint64_t _unsealedTxsSize, std::function<void(Error::Ptr)> _onRecvResponse)
{
    RAFT_LOG(INFO) << LOG_DESC("asyncNoteUnSealedTxsSize!");
}

void RaftImpl::init()
{
    RAFT_LOG(INFO) << LOG_DESC("init Raft success");
}

void RaftImpl::asyncGetConsensusStatus(
    std::function<void(Error::Ptr, std::string)> _onGetConsensusStatus)
{
    RAFT_LOG(INFO) << LOG_DESC("asyncGetConsensusStatus!");
}

void RaftImpl::enableAsMasterNode(bool _isMasterNode)
{
    RAFT_LOG(INFO) << LOG_DESC("enableAsMasterNode!");
}