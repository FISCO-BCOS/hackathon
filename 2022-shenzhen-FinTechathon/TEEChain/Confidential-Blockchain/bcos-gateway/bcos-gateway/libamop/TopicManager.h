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
 * @file TopicManager.h
 * @author: octopus
 * @date 2021-06-18
 */
#pragma once

#include "fisco-bcos-tars-service/Common/TarsUtils.h"
#include <bcos-crypto/interfaces/crypto/KeyInterface.h>
#include <bcos-framework/rpc/RPCInterface.h>
#include <bcos-gateway/libamop/Common.h>
#include <bcos-gateway/libp2p/P2PInterface.h>
#include <bcos-tars-protocol/client/RpcServiceClient.h>
#include <bcos-utilities/Common.h>
#include <bcos-utilities/Timer.h>
#include <servant/Application.h>
#include <algorithm>
#include <shared_mutex>

namespace bcos
{
namespace amop
{
class TopicManager : public std::enable_shared_from_this<TopicManager>
{
public:
    using Ptr = std::shared_ptr<TopicManager>;
    TopicManager(std::string const& _rpcServiceName, bcos::gateway::P2PInterface::Ptr _network)
    {
        m_rpcServiceName = _rpcServiceName;
        m_network = _network;
    }
    virtual ~TopicManager() {}

    virtual void start() { notifyRpcToSubscribeTopics(); }
    virtual void stop() {}

    uint32_t topicSeq() const { return m_topicSeq; }
    uint32_t incTopicSeq()
    {
        uint32_t topicSeq = ++m_topicSeq;
        return topicSeq;
    }

    /**
     * @brief: parse client sub topics json
     * @param _topicItems: return value, topics
     * @param _json: json
     * @return void
     */
    bool parseSubTopicsJson(const std::string& _json, TopicItems& _topicItems);
    /**
     * @brief: client subscribe topic
     * @param _clientID: client identify, to be defined
     * @param _topicJson: topics subscribe by client
     * @return void
     */
    void subTopic(const std::string& _client, const std::string& _topicJson);
    /**
     * @brief: client subscribe topic
     * @param _clientID: client identify, to be defined
     * @param _topicItems: topics subscribe by client
     * @return void
     */
    void subTopic(const std::string& _client, const TopicItems& _topicItems);
    /**
     * @brief: query topics sub by client
     * @param _clientID: client identify, to be defined
     * @param _topicItems: topics subscribe by client
     * @return bool
     */
    bool queryTopicItemsByClient(const std::string& _client, TopicItems& _topicItems);
    /**
     * @brief: remove all topics subscribed by client
     * @param _clientID: client identify, to be defined
     * @return void
     */
    void removeTopics(const std::string& _client, std::vector<std::string> const& _topicList);

    void removeTopicsByClient(const std::string& _client);
    /**
     * @brief: query topics subscribed by all connected clients
     * @return json string result, include topicSeq and topicItems fields
     */
    std::string queryTopicsSubByClient();
    /**
     * @brief: parse json to fetch topicSeq and topicItems
     * @param _topicSeq: return value, topicSeq
     * @param _topicItems: return value, topics
     * @param _json: json
     * @return void
     */
    bool parseTopicItemsJson(
        uint32_t& _topicSeq, TopicItems& _topicItems, const std::string& _json);
    /**
     * @brief: check if the topicSeq of nodeID changed
     * @param _nodeID: the peer nodeID
     * @param _topicSeq: the topicSeq of the nodeID
     * @return bool: if the nodeID has been changed
     */
    bool checkTopicSeq(bcos::gateway::P2pID const& _nodeID, uint32_t _topicSeq);
    /**
     * @brief: update online nodeIDs, clean up the offline nodeIDs state
     * @param _nodeIDs: the online nodeIDs
     * @return void
     */
    void notifyNodeIDs(const std::vector<bcos::gateway::P2pID>& _nodeIDs);
    /**
     * @brief: update the topicSeq and topicItems of the nodeID's
     * @param _nodeID: nodeID
     * @param _topicSeq: topicSeq
     * @param _topicItems: topicItems
     * @return void
     */
    void updateSeqAndTopicsByNodeID(
        bcos::gateway::P2pID const& _nodeID, uint32_t _topicSeq, const TopicItems& _topicItems);
    /**
     * @brief: find the nodeIDs by topic
     * @param _topic: topic
     * @param _nodeIDs: nodeIDs
     * @return void
     */
    void queryNodeIDsByTopic(const std::string& _topic, std::vector<std::string>& _nodeIDs);
    /**
     * @brief: find clients by topic
     * @param _topic: topic
     * @param _nodeIDs: nodeIDs
     * @return void
     */
    void queryClientsByTopic(const std::string& _topic, std::vector<std::string>& _clients);

    virtual bcos::rpc::RPCInterface::Ptr createAndGetServiceByClient(std::string const& _clientID);

protected:
    virtual void notifyRpcToSubscribeTopics();

    // m_client2TopicItems lock
    mutable std::shared_mutex x_clientTopics;
    // client => TopicItems
    // Note: the clientID is the rpc node endpoint
    std::unordered_map<std::string, TopicItems> m_client2TopicItems;

    // topicSeq
    std::atomic<uint32_t> m_topicSeq{1};

    // nodeID => topicSeq
    std::unordered_map<std::string, uint32_t> m_nodeID2TopicSeq;
    // m_nodeID2TopicSeq lock
    mutable std::shared_mutex x_topics;

    // nodeID => topicItems
    std::unordered_map<std::string, TopicItems> m_nodeID2TopicItems;

    std::map<std::string, bcos::rpc::RPCInterface::Ptr> m_clientInfo;
    mutable SharedMutex x_clientInfo;

    std::string m_rpcServiceName;
    bcos::gateway::P2PInterface::Ptr m_network;
};
}  // namespace amop
}  // namespace bcos
