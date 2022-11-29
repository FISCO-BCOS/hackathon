/**
 * @brief log function and other content need to fill
 * @file common.h
*/
#pragma once
#include <bcos-framework/Common.h>
#include <bcos-utilities/Exceptions.h>
#include <stdint.h>

#define RAFT_LOG(LEVEL) BCOS_LOG(LEVEL) << LOG_BADGE("CONSENSUS") << LOG_BADGE("RAFT")
#define RAFT_STORAGE_LOG(LEVEL) \
    BCOS_LOG(LEVEL) << LOG_BADGE("CONSENSUS") << LOG_BADGE("RAFT") << LOG_BADGE("STORAGE")

namespace bcos
{
namespace consensus
{
enum PacketType : uint32_t
{
    PrePreparePacket = 0x00,
    PreparePacket = 0x01,
    CommitPacket = 0x02,
    ViewChangePacket = 0x03,
    NewViewPacket = 0x04,
    CommittedProposalRequest = 0x5,
    CommittedProposalResponse = 0x6,
    PreparedProposalRequest = 0x7,
    PreparedProposalResponse = 0x8,
    CheckPoint = 0x9,
    RecoverRequest = 0xa,
    RecoverResponse = 0xb,
};
DERIVE_BCOS_EXCEPTION(UnknownPBFTMsgType);
DERIVE_BCOS_EXCEPTION(InitPBFTException);
}
}
