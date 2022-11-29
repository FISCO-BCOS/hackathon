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
 * @brief base class for sync config
 * @file SyncConfig.h
 * @author: yujiechen
 * @date 2021-05-25
 */
#pragma once
#include <bcos-crypto/interfaces/crypto/KeyInterface.h>
#include <bcos-framework/consensus/ConsensusNodeInterface.h>
#include <bcos-framework/protocol/ProtocolTypeDef.h>
namespace bcos
{
namespace sync
{
class SyncConfig
{
public:
    using Ptr = std::shared_ptr<SyncConfig>;
    explicit SyncConfig(bcos::crypto::NodeIDPtr _nodeId)
      : m_nodeId(_nodeId),
        m_consensusNodeList(std::make_shared<bcos::consensus::ConsensusNodeList>()),
        m_observerNodeList(std::make_shared<bcos::consensus::ConsensusNodeList>()),
        m_nodeList(std::make_shared<bcos::crypto::NodeIDSet>()),
        m_connectedNodeList(std::make_shared<bcos::crypto::NodeIDSet>())
    {}
    virtual ~SyncConfig() {}

    bcos::crypto::NodeIDPtr nodeID() { return m_nodeId; }
    // Note: copy here to ensure thread-safe
    virtual bcos::consensus::ConsensusNodeList consensusNodeList()
    {
        ReadGuard l(x_consensusNodeList);
        return *m_consensusNodeList;
    }
    // Note: only when the consensusNodeList or observerNodeList changed will call this interface
    // for perf consideration
    virtual void setConsensusNodeList(bcos::consensus::ConsensusNodeList const& _consensusNodeList)
    {
        {
            WriteGuard l(x_consensusNodeList);
            *m_consensusNodeList = _consensusNodeList;
        }
        updateNodeList();
    }

    // Note: only when the consensusNodeList or observerNodeList changed will call this interface
    // for perf consideration
    virtual void setConsensusNodeList(bcos::consensus::ConsensusNodeList&& _consensusNodeList)
    {
        {
            WriteGuard l(x_consensusNodeList);
            *m_consensusNodeList = std::move(_consensusNodeList);
        }
        updateNodeList();
    }

    // Note: only when the consensusNodeList or observerNodeList changed will call this interface
    // for perf consideration
    virtual void setObserverList(bcos::consensus::ConsensusNodeList const& _observerNodeList)
    {
        {
            WriteGuard l(x_observerNodeList);
            *m_observerNodeList = _observerNodeList;
        }
        updateNodeList();
    }

    virtual bcos::consensus::ConsensusNodeList observerNodeList()
    {
        ReadGuard l(x_observerNodeList);
        return *m_observerNodeList;
    }

    // Note: copy here to remove multithreading issues
    virtual bcos::crypto::NodeIDSet connectedNodeList()
    {
        ReadGuard l(x_connectedNodeList);
        return *m_connectedNodeList;
    }

    virtual void setConnectedNodeList(bcos::crypto::NodeIDSet const& _connectedNodeList)
    {
        WriteGuard l(x_connectedNodeList);
        *m_connectedNodeList = _connectedNodeList;
    }

    virtual void setConnectedNodeList(bcos::crypto::NodeIDSet&& _connectedNodeList)
    {
        WriteGuard l(x_connectedNodeList);
        *m_connectedNodeList = std::move(_connectedNodeList);
    }

    virtual bool existsInGroup()
    {
        ReadGuard l(x_nodeList);
        return m_nodeList->count(m_nodeId);
    }


    virtual bool existsInGroup(bcos::crypto::NodeIDPtr _nodeId)
    {
        ReadGuard l(x_nodeList);
        return m_nodeList->count(_nodeId);
    }

    virtual bool connected(bcos::crypto::NodeIDPtr _nodeId)
    {
        ReadGuard l(x_connectedNodeList);
        return m_connectedNodeList->count(_nodeId);
    }

    bcos::crypto::NodeIDSet groupNodeList()
    {
        ReadGuard l(x_nodeList);
        return *m_nodeList;
    }

    virtual void notifyConnectedNodes(bcos::crypto::NodeIDSet const& _connectedNodes,
        std::function<void(Error::Ptr)> _onRecvResponse)
    {
        setConnectedNodeList(_connectedNodes);
        if (!_onRecvResponse)
        {
            return;
        }
        _onRecvResponse(nullptr);
    }

private:
    void updateNodeList()
    {
        auto nodeList = consensusNodeList() + observerNodeList();
        WriteGuard l(x_nodeList);
        m_nodeList->clear();
        for (auto node : nodeList)
        {
            m_nodeList->insert(node->nodeID());
        }
    }

protected:
    bcos::crypto::NodeIDPtr m_nodeId;
    bcos::consensus::ConsensusNodeListPtr m_consensusNodeList;
    mutable SharedMutex x_consensusNodeList;

    bcos::consensus::ConsensusNodeListPtr m_observerNodeList;
    SharedMutex x_observerNodeList;

    bcos::crypto::NodeIDSetPtr m_nodeList;
    mutable SharedMutex x_nodeList;

    bcos::crypto::NodeIDSetPtr m_connectedNodeList;
    mutable SharedMutex x_connectedNodeList;
};
}  // namespace sync
}  // namespace bcos
