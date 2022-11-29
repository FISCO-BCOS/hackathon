#pragma once
#include "bcos-framework/protocol/Block.h"
#include "bcos-framework/protocol/TransactionReceiptFactory.h"
#include "bcos-protocol/TransactionSubmitResultFactoryImpl.h"
#include "bcos-scheduler/src/BlockExecutive.h"
#include "bcos-scheduler/src/BlockExecutiveFactory.h"
#include "bcos-scheduler/src/Common.h"
#include "bcos-scheduler/src/SerialBlockExecutive.h"
#include "bcos-scheduler/test/mock/MockBlockExecutive.h"
#include <bcos-crypto/interfaces/crypto/CommonType.h>
#include <bcos-framework/protocol/BlockFactory.h>
#include <bcos-framework/txpool/TxPoolInterface.h>

using namespace bcos;
using namespace bcos::scheduler;
using namespace bcos::protocol;

namespace bcos::test
{
class MockBlockExecutiveFactory : public bcos::scheduler::BlockExecutiveFactory
{
public:
    using Ptr = std::shared_ptr<MockBlockExecutiveFactory>;
    MockBlockExecutiveFactory(bool isSerialExecute) : BlockExecutiveFactory(isSerialExecute) {}
    virtual ~MockBlockExecutiveFactory() {}

    std::shared_ptr<BlockExecutive> build(bcos::protocol::Block::Ptr block,
        SchedulerImpl* scheduler, size_t startContextID,
        bcos::protocol::TransactionSubmitResultFactory::Ptr transactionSubmitResultFactory,
        bool staticCall, bcos::protocol::BlockFactory::Ptr _blockFactory,
        bcos::txpool::TxPoolInterface::Ptr _txPool) override
    {
        if (m_isSerialExecute)
        {
            SCHEDULER_LOG(DEBUG) << "-----building MockSerialBlockExecutive-----";
            auto serialBlockExecutive = std::make_shared<SerialBlockExecutive>(block, scheduler,
                startContextID, transactionSubmitResultFactory, staticCall, _blockFactory, _txPool);
            return serialBlockExecutive;
        }
        else
        {
            SCHEDULER_LOG(DEBUG) << "-----building MockBlockExecutive-----";
            auto blockExecutive = std::make_shared<MockBlockExecutive>(block, scheduler,
                startContextID, transactionSubmitResultFactory, staticCall, _blockFactory, _txPool);
            return blockExecutive;
        }
    }

    std::shared_ptr<BlockExecutive> build(bcos::protocol::Block::Ptr block,
        SchedulerImpl* scheduler, size_t startContextID,
        bcos::protocol::TransactionSubmitResultFactory::Ptr transactionSubmitResultFactory,
        bool staticCall, bcos::protocol::BlockFactory::Ptr _blockFactory,
        bcos::txpool::TxPoolInterface::Ptr _txPool, uint64_t _gasLimit, bool _syncBlock) override
    {
        if (m_isSerialExecute)
        {
            SCHEDULER_LOG(DEBUG) << "-----building MockSerialBlockExecutive-----"
                                 << LOG_KV("serialExecuteFlag", m_isSerialExecute);
            auto serialBlockExecutive = std::make_shared<SerialBlockExecutive>(block, scheduler,
                startContextID, transactionSubmitResultFactory, staticCall, _blockFactory, _txPool,
                _gasLimit, _syncBlock);
            return serialBlockExecutive;
        }
        else
        {
            SCHEDULER_LOG(DEBUG) << "-----building MockBlockExecutive-----"
                                 << LOG_KV("serialExecuteFlag", m_isSerialExecute);
            auto blockExecutive = std::make_shared<MockBlockExecutive>(block, scheduler,
                startContextID, transactionSubmitResultFactory, staticCall, _blockFactory, _txPool,
                _gasLimit, _syncBlock);
            return blockExecutive;
        }
    }

private:
    bool m_isSerialExecute = false;
};
}  // namespace bcos::test