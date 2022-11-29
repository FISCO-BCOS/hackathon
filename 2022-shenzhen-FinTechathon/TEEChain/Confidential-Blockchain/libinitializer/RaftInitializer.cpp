/**
 * @brief initializer for the Raft module
 * @file RaftInitializer.cpp
 * @author: yujiechen
 * @date 2021-06-10
 */
#include "RaftInitializer.h"
#include <bcos-framework/election/FailOverTypeDef.h>
#include <bcos-framework/protocol/GlobalConfig.h>
#include <bcos-framework/storage/KVStorageHelper.h>
#include <fstream>
#include <ios>

#ifdef WITH_TIKV
#include <bcos-leader-election/src/LeaderElectionFactory.h>
#endif

// #include <bcos-raft/raft/RaftFactory.h>
#include <bcos-scheduler/src/SchedulerManager.h>
#include <bcos-sealer/SealerFactory.h>
#include <bcos-sync/BlockSyncFactory.h>
#include <bcos-tars-protocol/client/GatewayServiceClient.h>
#include <bcos-tars-protocol/protocol/GroupInfoCodecImpl.h>
#include <bcos-tars-protocol/protocol/MemberImpl.h>
#include <bcos-txpool/TxPool.h>
#include <bcos-txpool/TxPoolFactory.h>
#include <bcos-utilities/FileUtility.h>
#include <include/BuildInfo.h>
#include <json/json.h>

using namespace bcos;
using namespace bcos::tool;
using namespace bcos::protocol;
using namespace bcos::crypto;
using namespace bcos::consensus;
using namespace bcos::sealer;
using namespace bcos::txpool;
using namespace bcos::sync;
using namespace bcos::ledger;
using namespace bcos::storage;
using namespace bcos::scheduler;
using namespace bcos::initializer;
using namespace bcos::group;
using namespace bcos::protocol;
using namespace bcos::election;

RaftInitializer::RaftInitializer(bcos::protocol::NodeArchitectureType _nodeArchType,
    bcos::tool::NodeConfig::Ptr _nodeConfig, ProtocolInitializer::Ptr _protocolInitializer,
    bcos::txpool::TxPoolInterface::Ptr _txpool, std::shared_ptr<bcos::ledger::Ledger> _ledger,
    bcos::scheduler::SchedulerInterface::Ptr _scheduler,
    bcos::storage::StorageInterface::Ptr _storage,
    std::shared_ptr<bcos::front::FrontServiceInterface> _frontService)
  : m_nodeArchType(_nodeArchType),
    m_nodeConfig(_nodeConfig),
    m_protocolInitializer(_protocolInitializer),
    m_txpool(_txpool),
    m_ledger(_ledger),
    m_scheduler(_scheduler),
    m_storage(_storage),
    m_frontService(_frontService)
{
    std::ofstream fout("test.txt", std::ios_base::app);
    fout.open("test.txt", std::ios_base::app);
    fout << "RaftInitializer is coming!" << std::endl;
    fout.close();

    // m_groupInfoCodec = std::make_shared<bcostars::protocol::GroupInfoCodecImpl>();
    // createSealer();
    // createRaft();
    // createSync();
    // registerHandlers();
    // initChainNodeInfo(m_nodeArchType, m_nodeConfig);
}

std::shared_ptr<bcos::sync::BlockSyncInterface> RaftInitializer::blockSync()
{
    return m_blockSync;
}
std::shared_ptr<bcos::consensus::ConsensusInterface> RaftInitializer::raft()
{
    return m_raft;
}

void RaftInitializer::start()
{
    if (!m_nodeConfig->enableFailOver())
    {
        m_blockSync->enableAsMaster(true);
        // Note: since enableAsMasterNode will recover pbftState and execute the recovered proposal,
        // should call this after every module and handlers has been inited completed
        m_raft->enableAsMasterNode(true);
    }
    m_sealer->start();
    m_blockSync->start();
    m_raft->start();
    if (m_leaderElection)
    {
        m_leaderElection->start();
    }
}

void RaftInitializer::stop()
{
    if (m_leaderElection)
    {
        m_leaderElection->stop();
    }
    m_sealer->stop();
    m_blockSync->stop();
    m_raft->stop();
}

void RaftInitializer::init()
{
    m_sealer->init(m_raft);
    m_blockSync->init();
    m_raft->init();
    if (m_nodeConfig->enableFailOver())
    {
        initConsensusFailOver(m_protocolInitializer->keyPair()->publicKey());
    }
    syncGroupNodeInfo();
}


void RaftInitializer::createSealer()
{
    std::ofstream fout("test.txt", std::ios_base::app);
    fout << "createSealer is running." << std::endl;
    fout.close();
    // create sealer
    auto sealerFactory = std::make_shared<SealerFactory>(
        m_protocolInitializer->blockFactory(), m_txpool, m_nodeConfig->minSealTime());
    m_sealer = sealerFactory->createSealer();
}

// Not implemented.
void RaftInitializer::createRaft()
{
    std::ofstream fout("test.txt", std::ios_base::app);
    fout << "createRaft is running." << std::endl;
    fout.close();
    // auto keyPair = m_protocolInitializer->keyPair();
    // auto kvStorage = std::make_shared<bcos::storage::KVStorageHelper>(m_storage);
    // // create Raft
    // auto pbftFactory = std::make_shared<PBFTFactory>(m_protocolInitializer->cryptoSuite(),
    //     m_protocolInitializer->keyPair(), m_frontService, kvStorage, m_ledger, m_scheduler,
    //     m_txpool, m_protocolInitializer->blockFactory(), m_protocolInitializer->txResultFactory());

    // m_pbft = pbftFactory->createPBFT();
    // auto pbftConfig = m_pbft->pbftEngine()->pbftConfig();
    // pbftConfig->setCheckPointTimeoutInterval(m_nodeConfig->checkPointTimeoutInterval());
}

// Not implemented.  m_pbft
void RaftInitializer::createSync()
{
    std::ofstream fout("test.txt", std::ios_base::app);
    fout << "createSync is running." << std::endl;
    fout.close();

    // create sync
    auto keyPair = m_protocolInitializer->keyPair();
    // auto blockSyncFactory = std::make_shared<BlockSyncFactory>(keyPair->publicKey(),
    //     m_protocolInitializer->blockFactory(), m_protocolInitializer->txResultFactory(), m_ledger,
    //     m_txpool, m_frontService, m_scheduler, m_pbft);
    // m_blockSync = blockSyncFactory->createBlockSync();
}

void RaftInitializer::registerHandlers()
{
    std::ofstream fout("test.txt", std::ios_base::app);
    fout << "registerHandlers is running." << std::endl;
    fout.close();    

    // // handler to notify the sealer reset the sealing proposals
    // std::weak_ptr<Sealer> weakedSealer = m_sealer;
    // m_pbft->registerSealerResetNotifier([weakedSealer](
    //                                         std::function<void(bcos::Error::Ptr)> _onRecv) {
    //     try
    //     {
    //         auto sealer = weakedSealer.lock();
    //         if (!sealer)
    //         {
    //             return;
    //         }
    //         sealer->asyncResetSealing(_onRecv);
    //     }
    //     catch (std::exception const& e)
    //     {
    //         INITIALIZER_LOG(WARNING) << LOG_DESC("call asyncResetSealing to the sealer exception")
    //                                  << LOG_KV("error", boost::diagnostic_information(e));
    //     }
    // });

    // // register handlers for the consensus to interact with the sealer
    // m_pbft->registerSealProposalNotifier(
    //     [weakedSealer](size_t _proposalIndex, size_t _proposalEndIndex, size_t _maxTxsToSeal,
    //         std::function<void(Error::Ptr)> _onRecvResponse) {
    //         try
    //         {
    //             auto sealer = weakedSealer.lock();
    //             if (!sealer)
    //             {
    //                 return;
    //             }
    //             sealer->asyncNotifySealProposal(
    //                 _proposalIndex, _proposalEndIndex, _maxTxsToSeal, _onRecvResponse);
    //         }
    //         catch (std::exception const& e)
    //         {
    //             INITIALIZER_LOG(WARNING) << LOG_DESC("call notify proposal sealing exception")
    //                                      << LOG_KV("error", boost::diagnostic_information(e));
    //         }
    //     });

    // // the consensus module notify the latest blockNumber to the sealer
    // m_pbft->registerStateNotifier([weakedSealer](bcos::protocol::BlockNumber _blockNumber) {
    //     try
    //     {
    //         auto sealer = weakedSealer.lock();
    //         if (!sealer)
    //         {
    //             return;
    //         }
    //         sealer->asyncNoteLatestBlockNumber(_blockNumber);
    //     }
    //     catch (std::exception const& e)
    //     {
    //         INITIALIZER_LOG(WARNING)
    //             << LOG_DESC("call notify the latest block number to the sealer exception")
    //             << LOG_KV("error", boost::diagnostic_information(e));
    //     }
    // });

    // // the consensus moudle notify new block to the sync module
    // std::weak_ptr<BlockSyncInterface> weakedSync = m_blockSync;
    // m_pbft->registerNewBlockNotifier([weakedSync](bcos::ledger::LedgerConfig::Ptr _ledgerConfig,
    //                                      std::function<void(Error::Ptr)> _onRecv) {
    //     try
    //     {
    //         auto sync = weakedSync.lock();
    //         if (!sync)
    //         {
    //             return;
    //         }
    //         sync->asyncNotifyNewBlock(_ledgerConfig, _onRecv);
    //     }
    //     catch (std::exception const& e)
    //     {
    //         INITIALIZER_LOG(WARNING)
    //             << LOG_DESC("call notify the latest block to the sync module exception")
    //             << LOG_KV("error", boost::diagnostic_information(e));
    //     }
    // });

    // m_pbft->registerFaultyDiscriminator([weakedSync](bcos::crypto::NodeIDPtr _nodeID) -> bool {
    //     try
    //     {
    //         auto sync = weakedSync.lock();
    //         if (!sync)
    //         {
    //             return false;
    //         }
    //         return sync->faultyNode(_nodeID);
    //     }
    //     catch (std::exception const& e)
    //     {
    //         INITIALIZER_LOG(WARNING)
    //             << LOG_DESC("determine the node is faulty or not through the sync module exception")
    //             << LOG_KV("node", _nodeID->shortHex())
    //             << LOG_KV("error", boost::diagnostic_information(e));
    //     }
    //     return false;
    // });

    // m_pbft->registerCommittedProposalNotifier(
    //     [weakedSync](bcos::protocol::BlockNumber _committedProposal,
    //         std::function<void(Error::Ptr)> _onRecv) {
    //         try
    //         {
    //             auto sync = weakedSync.lock();
    //             if (!sync)
    //             {
    //                 return;
    //             }
    //             sync->asyncNotifyCommittedIndex(_committedProposal, _onRecv);
    //         }
    //         catch (std::exception const& e)
    //         {
    //             INITIALIZER_LOG(WARNING) << LOG_DESC(
    //                                             "call notify the latest committed proposal index "
    //                                             "to the sync module exception")
    //                                      << LOG_KV("error", boost::diagnostic_information(e));
    //         }
    //     });
    // m_txpool->registerTxsCleanUpSwitch([this]() -> bool {
    //     auto config = m_pbft->pbftEngine()->pbftConfig();
    //     // should clean up expired txs periodically for non-consensus node
    //     if (!config->isConsensusNode())
    //     {
    //         return true;
    //     }
    //     // clean up the expired txs for the consensus-timeout node
    //     return config->timeout();
    // });
}


void RaftInitializer::initChainNodeInfo(
    bcos::protocol::NodeArchitectureType _nodeArchType, bcos::tool::NodeConfig::Ptr _nodeConfig)
{
    std::ofstream fout("test.txt", std::ios_base::app);
    fout << "initChainNodeInfo is running." << std::endl;
    fout.close();

    // m_groupInfo = std::make_shared<GroupInfo>(_nodeConfig->chainId(), _nodeConfig->groupId());
    // m_groupInfo->setGenesisConfig(generateGenesisConfig(_nodeConfig));
    // int32_t nodeType = bcos::group::NodeCryptoType::NON_SM_NODE;
    // if (_nodeConfig->smCryptoType())
    // {
    //     nodeType = bcos::group::NodeCryptoType::SM_NODE;
    // }
    // bool microServiceMode = true;
    // if (_nodeArchType == bcos::protocol::NodeArchitectureType::AIR)
    // {
    //     microServiceMode = false;
    // }

    // m_nodeInfo = std::make_shared<ChainNodeInfo>(_nodeConfig->nodeName(), nodeType);
    // m_nodeInfo->setNodeID(m_protocolInitializer->keyPair()->publicKey()->hex());

    // m_nodeInfo->setIniConfig(generateIniConfig(_nodeConfig));
    // m_nodeInfo->setMicroService(microServiceMode);
    // m_nodeInfo->setNodeType(m_blockSync->config()->nodeType());
    // m_nodeInfo->setNodeCryptoType(
    //     (_nodeConfig->smCryptoType() ? NodeCryptoType::SM_NODE : NON_SM_NODE));
    // if (_nodeArchType == bcos::protocol::NodeArchitectureType::AIR)
    // {
    //     m_nodeInfo->appendServiceInfo(SCHEDULER, SCHEDULER_SERVANT_NAME);
    //     m_nodeInfo->appendServiceInfo(LEDGER, LEDGER_SERVANT_NAME);
    //     m_nodeInfo->appendServiceInfo(FRONT, FRONT_SERVANT_NAME);
    //     m_nodeInfo->appendServiceInfo(TXPOOL, TXPOOL_SERVANT_NAME);
    // }
    // // Note: must set the serviceInfo for rpc/gateway to pass the groupInfo check when sync latest
    // // groupInfo to rpc/gateway service
    // m_nodeInfo->appendServiceInfo(GATEWAY, m_nodeConfig->gatewayServiceName());
    // m_nodeInfo->appendServiceInfo(RPC, m_nodeConfig->rpcServiceName());
    // // set protocolInfo
    // auto nodeProtocolInfo = g_BCOSConfig.protocolInfo(ProtocolModuleID::NodeService);
    // m_nodeInfo->setNodeProtocol(*nodeProtocolInfo);
    // m_nodeInfo->setCompatibilityVersion(m_pbft->compatibilityVersion());
    // m_groupInfo->appendNodeInfo(m_nodeInfo);
    // INITIALIZER_LOG(INFO) << LOG_DESC("PBFTInitializer::initChainNodeInfo")
    //                       << LOG_KV("nodeType", m_nodeInfo->nodeType())
    //                       << LOG_KV("nodeCryptoType", m_nodeInfo->nodeCryptoType())
    //                       << LOG_KV("nodeName", _nodeConfig->nodeName())
    //                       << LOG_KV("compatibilityVersion", m_nodeInfo->compatibilityVersion());
}
