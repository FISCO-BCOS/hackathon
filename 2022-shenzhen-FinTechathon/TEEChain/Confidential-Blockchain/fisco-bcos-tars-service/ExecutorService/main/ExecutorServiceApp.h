/**
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
 * @brief Application for the ExecutorService
 * @file ExecutorServiceApp.h
 * @author: yujiechen
 * @date 2022-5-10
 */
#pragma once
#include "libinitializer/ProtocolInitializer.h"
#include <bcos-framework/dispatcher/SchedulerInterface.h>
#include <bcos-framework/executor/ParallelTransactionExecutorInterface.h>
#include <bcos-framework/txpool/TxPoolInterface.h>
#include <bcos-tool/NodeConfig.h>
#include <bcos-utilities/BoostLogInitializer.h>
#include <bcos-utilities/Timer.h>
#include <servant/Application.h>

#define EXECUTOR_SERVICE_LOG(LEVEL) BCOS_LOG(LEVEL) << LOG_BADGE("ExecutorServiceApp")

namespace bcostars
{
class ExecutorServiceApp : public tars::Application
{
public:
    ExecutorServiceApp() = default;
    ~ExecutorServiceApp() override {}
    void initialize() override;
    void destroyApp() override
    {
        // stop executor
        if (m_executor)
        {
            m_executor->stop();
        }
    }

protected:
    virtual void createAndInitExecutor();
    virtual void registerExecutor();

private:
    std::string m_iniConfigPath;
    std::string m_genesisConfigPath;
    bcos::BoostLogInitializer::Ptr m_logInitializer;

    bcos::tool::NodeConfig::Ptr m_nodeConfig;
    bcos::initializer::ProtocolInitializer::Ptr m_protocolInitializer;

    bcos::scheduler::SchedulerInterface::Ptr m_scheduler;
    bcos::executor::ParallelTransactionExecutorInterface::Ptr m_executor;
    bcos::txpool::TxPoolInterface::Ptr m_txpool;
    std::string m_executorName;
    std::shared_ptr<bcos::Timer> m_timer;
    bool m_registerExecutorSuccess = false;
};
}  // namespace bcostars