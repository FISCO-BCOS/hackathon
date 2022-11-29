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
 * @brief Protocol for all the modules
 * @file Protocol.h
 * @author: yujiechen
 * @date 2021-04-21
 */
#pragma once
#include <boost/algorithm/string.hpp>
#include <limits>
#include <memory>
#include <optional>
#include <ostream>
#include <string>

namespace bcos
{
namespace protocol
{
// Note: both MessageExtFieldFlag and NodeType occupy the ext fields
enum MessageExtFieldFlag : uint32_t
{
    Response = 0x0001,
};
enum NodeType : uint32_t
{
    None = 0x0,
    CONSENSUS_NODE = 0x2,
    OBSERVER_NODE = 0x4,
    NODE_OUTSIDE_GROUP = 0x8,
};

enum NodeArchitectureType
{
    AIR = 0,
    PRO = 1,
    MAX = 2,
    LIGHT
};

enum MessageType
{
    HANDESHAKE = 0x100,         // 256
    BLOCK_NOTIFY = 0x101,       // 257
    RPC_REQUEST = 0x102,        // 258
    GROUP_NOTIFY = 0x103,       // 259
    EVENT_SUBSCRIBE = 0x120,    // 288
    EVENT_UNSUBSCRIBE = 0x121,  // 289
    EVENT_LOG_PUSH = 0x122,     // 290
};

enum ModuleID
{
    PBFT = 1000,
    Raft = 1001,
    BlockSync = 2000,
    TxsSync = 2001,
    ConsTxsSync = 2002,
    AMOP = 3000,

    LIGHTNODE_GETBLOCK = 4000,
    LIGHTNODE_GETTRANSACTIONS,
    LIGHTNODE_GETRECEIPTS,
    LIGHTNODE_GETSTATUS,
    LIGHTNODE_SENDTRANSACTION,
    LIGHTNODE_CALL,
    LIGHTNODE_END = 4999
};
enum ProtocolModuleID : uint32_t
{
    NodeService = 0x0,
    GatewayService = 0x1,
    RpcService = 0x2,
    ExecutorService = 0x3,
    MAX_PROTOCOL_MODULE = ExecutorService,
};
enum ProtocolVersion : uint32_t
{
    V0 = 0,
    V1 = 1,
};
enum class Version : uint32_t
{
    V3_0_VERSION = 0x03000000,
    RC4_VERSION = 4,
    MIN_VERSION = RC4_VERSION,
    MAX_VERSION = V3_0_VERSION,
};
const std::string RC4_VERSION_STR = "3.0.0-rc4";
const std::string V3_0_VERSION_STR = "3.0.0";

const std::string RC_VERSION_PREFIX = "3.0.0-rc";

const Version DEFAULT_VERSION = bcos::protocol::Version::RC4_VERSION;
const uint8_t MAX_MAJOR_VERSION = std::numeric_limits<uint8_t>::max();
const uint8_t MIN_MAJOR_VERSION = 3;

inline std::ostream& operator<<(std::ostream& _out, bcos::protocol::Version const& _version)
{
    switch (_version)
    {
    case bcos::protocol::Version::RC4_VERSION:
        _out << RC4_VERSION_STR;
        break;
    case bcos::protocol::Version::V3_0_VERSION:
        _out << V3_0_VERSION_STR;
        break;
    default:
        _out << "Unknown";
        break;
    }
    return _out;
}
inline std::ostream& operator<<(std::ostream& _out, NodeType const& _nodeType)
{
    switch (_nodeType)
    {
    case NodeType::None:
        _out << "None";
        break;
    case NodeType::CONSENSUS_NODE:
        _out << "CONSENSUS_NODE";
        break;
    case NodeType::OBSERVER_NODE:
        _out << "OBSERVER_NODE";
        break;
    case NodeType::NODE_OUTSIDE_GROUP:
        _out << "NODE_OUTSIDE_GROUP";
        break;
    default:
        _out << "Unknown";
        break;
    }
    return _out;
}

inline std::optional<ModuleID> stringToModuleID(const std::string& _moduleName)
{
    if (boost::iequals(_moduleName, "raft"))
    {
        return bcos::protocol::ModuleID::Raft;
    }
    else if (boost::iequals(_moduleName, "pbft"))
    {
        return bcos::protocol::ModuleID::PBFT;
    }
    else if (boost::iequals(_moduleName, "amop"))
    {
        return bcos::protocol::ModuleID::AMOP;
    }
    else if (boost::iequals(_moduleName, "block_sync"))
    {
        return bcos::protocol::ModuleID::BlockSync;
    }
    else if (boost::iequals(_moduleName, "txs_sync"))
    {
        return bcos::protocol::ModuleID::TxsSync;
    }
    else if (boost::iequals(_moduleName, "cons_txs_sync"))
    {
        return bcos::protocol::ModuleID::ConsTxsSync;
    }
    else if (boost::iequals(_moduleName, "light_node"))
    {
        return bcos::protocol::ModuleID::LIGHTNODE_GETBLOCK;
    }
    else
    {
        return std::nullopt;
    }
}

inline std::string moduleIDToString(ModuleID _moduleID)
{
    switch (_moduleID)
    {
    case ModuleID::PBFT:
        return "pbft";
    case ModuleID::Raft:
        return "raft";
    case ModuleID::BlockSync:
        return "block_sync";
    case ModuleID::TxsSync:
        return "txs_sync";
    case ModuleID::ConsTxsSync:
        return "cons_txs_sync";
    case ModuleID::AMOP:
        return "amop";
    case ModuleID::LIGHTNODE_GETBLOCK:
        return "light_node";
    default:
        return "unrecognized module";
    };
}


}  // namespace protocol
}  // namespace bcos
