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
 * @brief the state of executive for TransactionFlow run
 * @file ExecutiveState.cpp
 * @author: jimmyshi
 * @date: 2022-03-23
 */

#include "ExecutiveState.h"

using namespace bcos;
using namespace bcos::executor;

CallParameters::UniquePtr ExecutiveState::go()
{
    // init
    if (!m_executive)
    {
        m_executive =
            std::dynamic_pointer_cast<CoroutineTransactionExecutive>(m_executiveFactory->build(
                m_input->codeAddress, m_input->contextID, m_input->seq, true));
    }

    // run
    CallParameters::UniquePtr output;
    switch (m_status)
    {
    case NEED_RUN:
        output = m_executive->start(std::move(m_input));
        break;
    case PAUSED:
        // just ignore, need to set resume params
        EXECUTOR_LOG(FATAL) << "Invalid type";
        assert(false);
        break;
    case NEED_RESUME:
        output = m_executive->resume();
        break;
    case FINISHED:
        // do nothing
        break;
    }

    // update status
    switch (output->type)
    {
    case CallParameters::MESSAGE:
    case CallParameters::KEY_LOCK:
        m_status = PAUSED;
        break;
    case CallParameters::FINISHED:
    case CallParameters::REVERT:
        m_status = FINISHED;
        break;
    }

    // TODO: debug in executive
    // Bug: Must force set contextID here to fix bug.
    // But why output->context & output->seq here always be 0 ?????
    output->contextID = m_contextID;
    output->seq = m_seq;
    return output;
}

void ExecutiveState::setResumeParam(CallParameters::UniquePtr pullParam)
{
    m_status = NEED_RESUME;
    m_executive->setExchangeMessage(std::move(pullParam));
}


void ExecutiveState::appendKeyLocks(std::vector<std::string> keyLocks)
{
    switch (getStatus())
    {
    case NEED_RUN:
        std::copy(keyLocks.begin(), keyLocks.end(), std::back_inserter(m_input->keyLocks));
        break;
    case PAUSED:
    case NEED_RESUME:
        m_executive->appendResumeKeyLocks(std::move(keyLocks));
        break;
    case FINISHED:
        break;
    }
}