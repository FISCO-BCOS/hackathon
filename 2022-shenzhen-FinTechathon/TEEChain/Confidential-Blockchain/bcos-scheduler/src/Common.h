#pragma once

#include <bcos-framework/dispatcher/SchedulerTypeDef.h>
#include <cstdint>
#include <tuple>
#define SCHEDULER_LOG(LEVEL) BCOS_LOG(LEVEL) << LOG_BADGE("SCHEDULER")
#define SCHEDULER_BLK_LOG(LEVEL, NUMBER) \
    BCOS_LOG(LEVEL) << LOG_BADGE("SCHEDULER") << BLOCK_NUMBER(NUMBER)
namespace bcos::scheduler
{
using ContextID = int64_t;
using Seq = int64_t;
using Context = std::tuple<ContextID, Seq>;
inline const uint64_t TRANSACTION_GAS = 3000000000;

}  // namespace bcos::scheduler