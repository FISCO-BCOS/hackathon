/**
 *  Copyright (C) 2021 FISCO BCOS.
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
 * @brief state machine to execute the transactions
 * @file StateMachine.h
 * @author: yujiechen
 * @date 2021-05-18
 */
#pragma once
#include "../framework/StateMachineInterface.h"
#include <bcos-framework/dispatcher/SchedulerInterface.h>
#include <bcos-framework/protocol/BlockFactory.h>
#include <bcos-utilities/ThreadPool.h>

namespace bcos
{
namespace consensus
{
class StateMachine : public StateMachineInterface
{
public:
    StateMachine(bcos::scheduler::SchedulerInterface::Ptr _scheduler,
        bcos::protocol::BlockFactory::Ptr _blockFactory)
      : m_scheduler(_scheduler), m_blockFactory(_blockFactory)
    {
        // since execute block is serial, only use one thread to decrease the timecost
        m_worker = std::make_shared<ThreadPool>("stateMachine", 1);
        m_schedulerWorker =
            std::make_shared<ThreadPool>("preExec", (std::thread::hardware_concurrency() * 2));
    }

    ~StateMachine() override
    {
        if (m_worker)
        {
            m_worker->stop();
        }
        if (m_schedulerWorker)
        {
            m_schedulerWorker->stop();
        }
    }

    void asyncApply(ssize_t _execTimeout, ProposalInterface::ConstPtr _lastAppliedProposal,
        ProposalInterface::Ptr _proposal, ProposalInterface::Ptr _executedProposal,
        std::function<void(int64_t)> _onExecuteFinished) override;

    void asyncPreApply(
        ProposalInterface::Ptr _proposal, std::function<void(bool)> _onPreApplyFinished) override;

private:
    void apply(ssize_t _execTimeout, ProposalInterface::ConstPtr _lastAppliedProposal,
        ProposalInterface::Ptr _proposal, ProposalInterface::Ptr _executedProposal,
        std::function<void(int64_t)> _onExecuteFinished);

    void preApply(ProposalInterface::Ptr _proposal, std::function<void(bool)> _onPreApplyFinished);

protected:
    bcos::scheduler::SchedulerInterface::Ptr m_scheduler;
    bcos::protocol::BlockFactory::Ptr m_blockFactory;
    bcos::ThreadPool::Ptr m_worker;
    // threadPool used for scheduler preExecuteBlock, since preExecuteBlock may fetch transactions
    // from the txpool, it need to use multiple thread to improve the txs-fetching-speed
    bcos::ThreadPool::Ptr m_schedulerWorker;
};
}  // namespace consensus
}  // namespace bcos