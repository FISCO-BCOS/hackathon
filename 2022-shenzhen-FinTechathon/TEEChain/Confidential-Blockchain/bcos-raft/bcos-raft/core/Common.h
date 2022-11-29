/**
 * @file Common.h
*/
#pragma once

#define CONSENSUS_LOG(LEVEL) BCOS_LOG(LEVEL) << LOG_BADGE("CONSENSUS") << LOG_BADGE("Core")
namespace bcos
{
namespace consensus
{
const IndexType NON_CONSENSUS_NODE = (IndexType)(-1);
DERIVE_BCOS_EXCEPTION(InitConsensusException);
}  // namespace consensus
}  // namespace bcos