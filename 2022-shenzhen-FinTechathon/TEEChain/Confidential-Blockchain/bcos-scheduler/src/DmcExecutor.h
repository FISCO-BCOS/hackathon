/*
 *  Copyright (C) 2022 FISCO BCOS.
 *  SPDX-License-Identifier: Apache-2.0
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 * @brief The executor of a contract
 * @file DmcExecutor.h
 * @author: jimmyshi
 * @date: 2022-03-23
 */


#pragma once
#include "DmcStepRecorder.h"
#include "Executive.h"
#include "ExecutivePool.h"
#include "ExecutorManager.h"
#include "GraphKeyLocks.h"
#include <bcos-framework/protocol/Block.h>
#include <tbb/concurrent_set.h>
#include <tbb/concurrent_unordered_map.h>
#include <string>

#define DMC_LOG(LEVEL) SCHEDULER_LOG(LEVEL) << LOG_BADGE("DMC")
//#define DMC_LOG(LEVEL) std::cout << LOG_BADGE("DMC")
namespace bcos::scheduler
{
class DmcExecutor
{
public:
    using MessageHint = bcos::scheduler::ExecutivePool::MessageHint;

    enum Status : int8_t
    {
        ERROR,
        NEED_PREPARE = 1,
        PAUSED = 2,
        FINISHED = 3
    };

    using Ptr = std::shared_ptr<DmcExecutor>;

    DmcExecutor(std::string name, std::string contractAddress, bcos::protocol::Block::Ptr block,
        bcos::executor::ParallelTransactionExecutorInterface::Ptr executor,
        GraphKeyLocks::Ptr keyLocks, bcos::crypto::Hash::Ptr hashImpl,
        DmcStepRecorder::Ptr dmcRecorder)
      : m_name(name),
        m_contractAddress(contractAddress),
        m_block(block),
        m_executor(executor),
        m_keyLocks(keyLocks),
        m_hashImpl(hashImpl),
        m_dmcRecorder(dmcRecorder)
    {}

    void submit(protocol::ExecutionMessage::UniquePtr message, bool withDAG);
    bool prepare();        // return true if has schedule out message
    bool unlockPrepare();  // return true if need to detect deadlock
    void releaseOutdatedLock();
    bool detectLockAndRevert();  // return true if detect a tx and revert

    void go(std::function<void(bcos::Error::UniquePtr, Status)> callback);
    bool hasFinished() { return m_executivePool.empty(); }

    void scheduleIn(ExecutiveState::Ptr executive);

    void setSchedulerOutHandler(std::function<void(ExecutiveState::Ptr)> onSchedulerOut)
    {
        f_onSchedulerOut = std::move(onSchedulerOut);
    };

    void setOnTxFinishedHandler(
        std::function<void(bcos::protocol::ExecutionMessage::UniquePtr)> onTxFinished)
    {
        f_onTxFinished = std::move(onTxFinished);
    }

    void setOnNeedSwitchEventHandler(std::function<void()> onNeedSwitchEvent)
    {
        f_onNeedSwitchEvent = std::move(onNeedSwitchEvent);
    }

    void triggerSwitch()
    {
        if (f_onNeedSwitchEvent)
        {
            f_onNeedSwitchEvent();
        }
    }

    void forEachExecutive(std::function<void(ContextID, ExecutiveState::Ptr)> handler)
    {
        m_executivePool.forEach(
            ExecutivePool::MessageHint::ALL, [handler = std::move(handler)](ContextID contextID,
                                                 ExecutiveState::Ptr executiveState) {
                handler(contextID, executiveState);
                return true;
            });
    }

private:
    MessageHint handleExecutiveMessage(ExecutiveState::Ptr executive);
    void handleExecutiveOutputs(std::vector<bcos::protocol::ExecutionMessage::UniquePtr> outputs);
    void scheduleOut(ExecutiveState::Ptr executiveState);

    void handleCreateMessage(ExecutiveState::Ptr executive);
    std::string newEVMAddress(int64_t blockNumber, int64_t contextID, int64_t seq);
    std::string newEVMAddress(
        const std::string_view& _sender, bytesConstRef _init, u256 const& _salt);

private:
    std::string m_name;
    std::string m_contractAddress;
    bcos::protocol::Block::Ptr m_block;
    bcos::executor::ParallelTransactionExecutorInterface::Ptr m_executor;
    GraphKeyLocks::Ptr m_keyLocks;
    bcos::crypto::Hash::Ptr m_hashImpl;
    DmcStepRecorder::Ptr m_dmcRecorder;
    ExecutivePool m_executivePool;


    mutable SharedMutex x_concurrentLock;

    std::function<void(bcos::protocol::ExecutionMessage::UniquePtr)> f_onTxFinished;
    std::function<void()> f_onNeedSwitchEvent;
    std::function<void(ExecutiveState::Ptr)> f_onSchedulerOut;
};

}  // namespace bcos::scheduler
