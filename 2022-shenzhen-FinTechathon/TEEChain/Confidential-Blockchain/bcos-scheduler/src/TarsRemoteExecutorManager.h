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
 * @brief Manager remote executor
 * @file TarsRemoteExecutorManager.h
 * @author: jimmyshi
 * @date: 2022-05-25
 */
#pragma once
#include "bcos-scheduler/src/ExecutorManager.h"
#include <bcos-framework/protocol/ServiceDesc.h>
#include <bcos-utilities/Worker.h>
#include <atomic>


namespace bcos::scheduler
{
class TarsRemoteExecutorManager : public ExecutorManager, Worker
{
public:
    using Ptr = std::shared_ptr<TarsRemoteExecutorManager>;
    using EndPointSet = std::shared_ptr<std::set<std::pair<std::string, uint16_t>>>;

    TarsRemoteExecutorManager(std::string executorServiceName)
      : Worker("TarsRemoteExecutorManager", 5000)  // ms
    {
        if (executorServiceName.empty())
        {
            return;
        }

        m_executorServiceName = executorServiceName + "." + bcos::protocol::EXECUTOR_SERVANT_NAME;

        EXECUTOR_MANAGER_LOG(INFO) << "Initialize " << threadName() << " "
                                   << LOG_KV("executorServiceName", m_executorServiceName);
    }

    virtual ~TarsRemoteExecutorManager() { stopWorking(); };

    void start()
    {
        EXECUTOR_MANAGER_LOG(INFO) << "Start" << threadName() << " "
                                   << LOG_KV("executorServiceName", m_executorServiceName);
        waitForExecutorConnection();
        startWorking();
    }

    void waitForExecutorConnection();

    void setRemoteExecutorChangeHandler(std::function<void()> handler)
    {
        m_onRemoteExecutorChange = std::move(handler);
    }

    void executeWorker() override;

    void refresh(bool needNotifyChange = true);

    void update(EndPointSet endPointMap, bool needNotifyChange = true);

    bool empty() { return size() == 0; }

    bool checkAllExecutorSeq();

    void stop() override
    {
        EXECUTOR_MANAGER_LOG(INFO) << "Try to stop TarsRemoteExecutorManager";
        if (isWorking())
        {
            stopWorking();
        }
        ExecutorManager::stop();
    }

private:
    std::function<void()> m_onRemoteExecutorChange;
    std::string m_executorServiceName;

    boost::condition_variable m_signalled;
    boost::mutex x_signalled;
    uint8_t m_waitingExecutorMaxRetryTimes = 20;

    EndPointSet m_endPointSet = std::make_shared<std::set<std::pair<std::string, uint16_t>>>();
    std::map<std::string, int64_t> m_executor2Seq;
};
}  // namespace bcos::scheduler
