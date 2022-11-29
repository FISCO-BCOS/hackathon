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
 * @file Common.h
 * @author: yujiechen
 * @date 2021-04-12
 */
#pragma once
#include <bcos-framework/Common.h>
#include <bcos-framework/consensus/ConsensusTypeDef.h>
#include <bcos-utilities/Exceptions.h>

#define CONSENSUS_LOG(LEVEL) BCOS_LOG(LEVEL) << LOG_BADGE("CONSENSUS") << LOG_BADGE("Core")
namespace bcos
{
namespace consensus
{
const IndexType NON_CONSENSUS_NODE = (IndexType)(-1);
DERIVE_BCOS_EXCEPTION(InitConsensusException);
}  // namespace consensus
}  // namespace bcos