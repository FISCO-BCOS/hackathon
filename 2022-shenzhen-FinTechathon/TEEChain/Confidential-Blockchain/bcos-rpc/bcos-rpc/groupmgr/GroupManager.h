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
 * @brief GroupManager.h
 * @file GroupManager.h
 * @author: yujiechen
 * @date 2021-10-11
 */
#pragma once
#include "NodeService.h"
#include <bcos-tool/NodeConfig.h>
#include <bcos-utilities/Timer.h>
namespace bcos
{
namespace rpc
{
class GroupManager : public std::enable_shared_from_this<GroupManager>
{
public:
    using Ptr = std::shared_ptr<GroupManager>;
    GroupManager(std::string _rpcServiceName, std::string const& _chainID,
        NodeServiceFactory::Ptr _nodeServiceFactory, bcos::tool::NodeConfig::Ptr _nodeConfig)
      : m_rpcServiceName(_rpcServiceName),
        m_chainID(_chainID),
        m_nodeServiceFactory(_nodeServiceFactory),
        m_nodeConfig(_nodeConfig)
    {}
    virtual ~GroupManager() {}
    virtual bool updateGroupInfo(bcos::group::GroupInfo::Ptr _groupInfo);
    virtual void removeGroupNodeList(bcos::group::GroupInfo::Ptr _groupInfo);

    virtual NodeService::Ptr getNodeService(
        std::string_view _groupID, std::string_view _nodeName) const;

    std::string const& chainID() const { return m_chainID; }

    virtual bcos::group::GroupInfo::Ptr getGroupInfo(std::string_view _groupID)
    {
        ReadGuard l(x_nodeServiceList);
        auto it = m_groupInfos.find(_groupID);
        if (it != m_groupInfos.end())
        {
            return it->second;
        }
        return nullptr;
    }

    virtual bcos::group::ChainNodeInfo::Ptr getNodeInfo(
        std::string_view _groupID, std::string_view _nodeName)
    {
        ReadGuard l(x_nodeServiceList);
        auto it = m_groupInfos.find(_groupID);
        if (it == m_groupInfos.end())
        {
            return nullptr;
        }
        auto groupInfo = it->second;

        auto nodeInfos = groupInfo->nodeInfos();
        auto nodeIt = nodeInfos.find(_nodeName);
        return nodeIt->second;
    }

    virtual std::set<std::string> groupList()
    {
        std::set<std::string> groupList;
        ReadGuard l(x_nodeServiceList);
        for (auto const& it : m_groupInfos)
        {
            if (it.second->nodesNum() == 0)
            {
                continue;
            }
            groupList.insert(it.first);
        }
        return groupList;
    }

    virtual std::vector<bcos::group::GroupInfo::Ptr> groupInfoList()
    {
        std::vector<bcos::group::GroupInfo::Ptr> groupList;
        ReadGuard l(x_nodeServiceList);
        for (auto const& it : m_groupInfos)
        {
            groupList.push_back(it.second);
        }
        return groupList;
    }

    virtual void updateGroupBlockInfo(std::string const& _groupID, std::string const& _nodeName,
        bcos::protocol::BlockNumber _blockNumber)
    {
        UpgradableGuard l(x_groupBlockInfos);
        if (m_groupBlockInfos.count(_groupID))
        {
            // expired block
            if (m_groupBlockInfos[_groupID] > _blockNumber)
            {
                return;
            }
            // has already in the m_nodesWithLatestBlockNumber
            if (m_groupBlockInfos[_groupID] == _blockNumber &&
                m_nodesWithLatestBlockNumber.count(_groupID) &&
                m_nodesWithLatestBlockNumber[_groupID].count(_nodeName))
            {
                return;
            }
        }
        UpgradeGuard ul(l);
        bcos::protocol::BlockNumber oldBlockNumber = 0;
        if (m_groupBlockInfos.count(_groupID))
        {
            oldBlockNumber = m_groupBlockInfos[_groupID];
        }
        if (!m_nodesWithLatestBlockNumber.count(_groupID))
        {
            m_nodesWithLatestBlockNumber[_groupID] = std::set<std::string>();
        }
        if (!m_groupBlockInfos.count(_groupID))
        {
            m_groupBlockInfos[_groupID] = _blockNumber;
        }
        // nodes with newer highest block
        if (oldBlockNumber < _blockNumber)
        {
            m_groupBlockInfos[_groupID] = _blockNumber;
            m_nodesWithLatestBlockNumber[_groupID].clear();
        }

        // nodes with the same highest block
        (m_nodesWithLatestBlockNumber[_groupID]).insert(_nodeName);
        BCOS_LOG(DEBUG) << LOG_DESC("updateGroupBlockInfo for receive block notify")
                        << LOG_KV("group", _groupID) << LOG_KV("node", _nodeName)
                        << LOG_KV("block", _blockNumber);
    }

    virtual void registerGroupInfoNotifier(
        std::function<void(bcos::group::GroupInfo::Ptr)> _callback)
    {
        m_groupInfoNotifier = _callback;
    }

    void registerBlockNumberNotifier(
        std::function<void(std::string const&, std::string const&, bcos::protocol::BlockNumber)>
            _blockNumberNotifier)
    {
        m_blockNumberNotifier = _blockNumberNotifier;
    }
    virtual bcos::protocol::BlockNumber getBlockNumberByGroup(const std::string& _groupID);

protected:
    GroupManager(std::string const& _chainID) : m_chainID(_chainID) {}

    bool updateGroupServices(bcos::group::GroupInfo::Ptr _groupInfo, bool _enforceUpdate);
    bool updateNodeService(std::string const& _groupID, bcos::group::ChainNodeInfo::Ptr _nodeInfo,
        bool _enforceUpdate);
    bool shouldRebuildNodeService(
        std::string const& _groupID, bcos::group::ChainNodeInfo::Ptr _nodeInfo);
    virtual NodeService::Ptr selectNode(std::string_view _groupID) const;
    virtual std::string selectNodeByBlockNumber(std::string_view _groupID) const;
    virtual NodeService::Ptr selectNodeRandomly(std::string_view _groupID) const;
    virtual NodeService::Ptr queryNodeService(
        std::string_view _groupID, std::string_view _nodeName) const;

    virtual void initNodeInfo(
        std::string const& _groupID, std::string const& _nodeName, NodeService::Ptr _nodeService);

    virtual void removeGroupBlockInfo(
        std::map<std::string, std::set<std::string>> const& _unreachableNodes);
    virtual void removeUnreachableNodeService(
        std::map<std::string, std::set<std::string>> const& _unreachableNodes);

    bool checkGroupInfo(bcos::group::GroupInfo::Ptr _groupInfo);

protected:
    std::string m_rpcServiceName;
    std::string m_chainID;
    NodeServiceFactory::Ptr m_nodeServiceFactory;

    bcos::tool::NodeConfig::Ptr m_nodeConfig;

    // map between groupID to groupInfo
    std::map<std::string, bcos::group::GroupInfo::Ptr, std::less<>> m_groupInfos;

    // map between nodeName to NodeService
    std::map<std::string, std::map<std::string, NodeService::Ptr, std::less<>>, std::less<>>
        m_nodeServiceList;
    mutable SharedMutex x_nodeServiceList;

    std::map<std::string, std::set<std::string>, std::less<>> m_nodesWithLatestBlockNumber;
    std::map<std::string, bcos::protocol::BlockNumber, std::less<>> m_groupBlockInfos;
    mutable SharedMutex x_groupBlockInfos;

    std::function<void(bcos::group::GroupInfo::Ptr)> m_groupInfoNotifier;

    std::function<void(std::string const&, std::string const&, bcos::protocol::BlockNumber)>
        m_blockNumberNotifier;
};
}  // namespace rpc
}  // namespace bcos
