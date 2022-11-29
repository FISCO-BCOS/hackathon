/**
 * @brief initializer for the Raft module
 * @file RaftInitializer.h
 * @author: yujiechen
 * @date 2021-06-10
 */
#pragma once
#include "Common.h"
#include "bcos-framework/rpc/RPCInterface.h"
#include "libinitializer/ProtocolInitializer.h"
#include <bcos-framework/consensus/ConsensusInterface.h>
#include <bcos-framework/dispatcher/SchedulerInterface.h>
#include <bcos-framework/election/LeaderElectionInterface.h>
#include <bcos-framework/front/FrontServiceInterface.h>
#include <bcos-framework/multigroup/GroupInfo.h>
#include <bcos-framework/multigroup/GroupInfoCodec.h>
#include <bcos-framework/protocol/MemberInterface.h>
#include <bcos-framework/sealer/SealerInterface.h>
#include <bcos-framework/storage/StorageInterface.h>
#include <bcos-framework/sync/BlockSyncInterface.h>
#include <bcos-framework/txpool/TxPoolInterface.h>
#include <bcos-ledger/src/libledger/Ledger.h>
#include <fisco-bcos-tars-service/Common/TarsUtils.h>

namespace bcos
{
namespace sealer
{
class Sealer;
}
namespace sync
{
class BlockSync;
}
namespace consensus
{
class RaftImpl;
}

namespace initializer
{
class RaftInitializer : public std::enable_shared_from_this<RaftInitializer>
{
public:
    using Ptr = std::shared_ptr<RaftInitializer>;
    RaftInitializer(bcos::protocol::NodeArchitectureType _nodeArchType,
        bcos::tool::NodeConfig::Ptr _nodeConfig, ProtocolInitializer::Ptr _protocolInitializer,
        bcos::txpool::TxPoolInterface::Ptr _txpool, std::shared_ptr<bcos::ledger::Ledger> _ledger,
        bcos::scheduler::SchedulerInterface::Ptr _scheduler,
        bcos::storage::StorageInterface::Ptr _storage,
        bcos::front::FrontServiceInterface::Ptr _frontService);

    virtual ~RaftInitializer() { stop(); }

    virtual void init();

    virtual void start();
    virtual void stop();

    bcos::txpool::TxPoolInterface::Ptr txpool();
    bcos::sync::BlockSyncInterface::Ptr blockSync();
    bcos::consensus::ConsensusInterface::Ptr raft();
    bcos::sealer::SealerInterface::Ptr sealer();

    bcos::protocol::BlockFactory::Ptr blockFactory()
    {
        return m_protocolInitializer->blockFactory();
    }
    bcos::crypto::KeyFactory::Ptr keyFactory() { return m_protocolInitializer->keyFactory(); }

    bcos::group::GroupInfo::Ptr groupInfo() { return m_groupInfo; }
    bcos::group::ChainNodeInfo::Ptr nodeInfo() { return m_nodeInfo; }
    virtual void onGroupInfoChanged();
    virtual void initNotificationHandlers(bcos::rpc::RPCInterface::Ptr _rpc);

protected:
    virtual void initChainNodeInfo(bcos::protocol::NodeArchitectureType _nodeArchType,
        bcos::tool::NodeConfig::Ptr _nodeConfig);
    virtual void createSealer();
    virtual void createRaft();
    virtual void createSync();
    virtual void registerHandlers();
    std::string generateGenesisConfig(bcos::tool::NodeConfig::Ptr _nodeConfig);
    std::string generateIniConfig(bcos::tool::NodeConfig::Ptr _nodeConfig);

    void syncGroupNodeInfo();
    virtual void initConsensusFailOver(bcos::crypto::KeyInterface::Ptr _nodeID);

protected:
    bcos::protocol::NodeArchitectureType m_nodeArchType;
    bcos::tool::NodeConfig::Ptr m_nodeConfig;
    ProtocolInitializer::Ptr m_protocolInitializer;

    bcos::txpool::TxPoolInterface::Ptr m_txpool;
    // Note: Raft and other modules (except rpc and gateway) access ledger with bcos-ledger SDK
    std::shared_ptr<bcos::ledger::Ledger> m_ledger;
    bcos::scheduler::SchedulerInterface::Ptr m_scheduler;
    bcos::storage::StorageInterface::Ptr m_storage;
    bcos::front::FrontServiceInterface::Ptr m_frontService;

    std::shared_ptr<bcos::sealer::Sealer> m_sealer;
    std::shared_ptr<bcos::sync::BlockSync> m_blockSync;
    std::shared_ptr<bcos::consensus::RaftImpl> m_raft;

    bcos::group::GroupInfo::Ptr m_groupInfo;
    bcos::group::ChainNodeInfo::Ptr m_nodeInfo;

    bcos::group::GroupInfoCodec::Ptr m_groupInfoCodec;
    bcos::protocol::MemberFactoryInterface::Ptr m_memberFactory;
    bcos::election::LeaderElectionInterface::Ptr m_leaderElection;
};
}  // namespace initializer
}  // namespace bcos