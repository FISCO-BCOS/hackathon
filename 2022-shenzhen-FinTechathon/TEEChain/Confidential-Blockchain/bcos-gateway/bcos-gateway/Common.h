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
 * @file Common.h
 * @author: octopus
 * @date 2021-05-04
 */
#pragma once
#include "libnetwork/Common.h"

#define GATEWAY_LOG(LEVEL) BCOS_LOG(LEVEL) << "[Gateway][Gateway]"
#define GATEWAY_CONFIG_LOG(LEVEL) BCOS_LOG(LEVEL) << "[Gateway][Config]"
#define GATEWAY_FACTORY_LOG(LEVEL) BCOS_LOG(LEVEL) << "[Gateway][Factory]"
#define NODE_MANAGER_LOG(LEVEL) BCOS_LOG(LEVEL) << "[Gateway][GatewayNodeManager]"
#define ROUTER_LOG(LEVEL) BCOS_LOG(LEVEL) << "[Gateway][Router]"
#define RATELIMIT_LOG(LEVEL) BCOS_LOG(LEVEL) << "[Gateway][RateLimiter]"

namespace bcos
{
namespace gateway
{
enum GroupType : uint16_t
{
    // group with at-least one consensus node
    GROUP_WITH_CONSENSUS_NODE = 0x0,
    // group without consensus node
    GROUP_WITHOUT_CONSENSUS_NODE = 0x1,
    // group without consensus node and observer node
    OUTSIDE_GROUP = 0x2,
};

}  // namespace gateway
}  // namespace bcos
