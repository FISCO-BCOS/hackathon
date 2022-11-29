/*
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
 * @file EvenPushTask.h
 * @author: octopus
 * @date 2021-09-01
 */

#pragma once
#include <bcos-boostssl/websocket/WsSession.h>
#include <bcos-cpp-sdk/event/Common.h>
#include <bcos-cpp-sdk/event/EventSubInterface.h>
#include <bcos-cpp-sdk/event/EventSubParams.h>
#include <atomic>

namespace bcos
{
namespace cppsdk
{
namespace event
{
class EventSubTaskState
{
public:
    using Ptr = std::shared_ptr<EventSubTaskState>;
    using ConstPtr = std::shared_ptr<const EventSubTaskState>;

public:
    int64_t currentBlockNumber() const { return m_currentBlockNumber.load(); }
    void setCurrentBlockNumber(int64_t _currentBlockNumber)
    {
        if (_currentBlockNumber > m_currentBlockNumber.load())
        {
            m_currentBlockNumber.store(_currentBlockNumber);
        }
    }

private:
    std::atomic<int64_t> m_currentBlockNumber = -1;
};

class EventSubTask
{
public:
    using Ptr = std::shared_ptr<EventSubTask>;
    EventSubTask() { EVENT_TASK(DEBUG) << LOG_KV("[NEWOBJ][EventSubTask]", this); }
    ~EventSubTask() { EVENT_TASK(DEBUG) << LOG_KV("[DELOBJ][EventSubTask]", this); }

public:
    void setSession(std::shared_ptr<bcos::boostssl::ws::WsSession> _session)
    {
        m_session = _session;
    }
    std::shared_ptr<bcos::boostssl::ws::WsSession> session() const { return m_session; }

    void setId(const std::string& _id) { m_id = _id; }
    std::string id() const { return m_id; }

    void setGroup(const std::string& _group) { m_group = _group; }
    std::string group() const { return m_group; }

    void setParams(std::shared_ptr<const EventSubParams> _params) { m_params = _params; }
    std::shared_ptr<const EventSubParams> params() const { return m_params; }

    void setState(std::shared_ptr<EventSubTaskState> _state) { m_state = _state; }
    std::shared_ptr<EventSubTaskState> state() const { return m_state; }

    void setCallback(Callback _callback) { m_callback = _callback; }
    Callback callback() const { return m_callback; }

private:
    std::string m_id;
    std::string m_group;
    Callback m_callback;
    std::shared_ptr<bcos::boostssl::ws::WsSession> m_session;
    std::shared_ptr<const EventSubParams> m_params;
    std::shared_ptr<EventSubTaskState> m_state;
};

using EventSubTaskPtrs = std::vector<EventSubTask::Ptr>;
}  // namespace event
}  // namespace cppsdk
}  // namespace bcos
