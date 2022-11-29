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
 * @brief host context
 * @file HostContext.h
 * @author: xingqiangbai
 * @date: 2021-05-24
 */

#pragma once

#include "../Common.h"
#include "bcos-framework/protocol/BlockHeader.h"
#include "bcos-framework/storage/Table.h"
#include <evmc/evmc.h>
#include <evmc/helpers.h>
#include <evmc/instructions.h>
#include <atomic>
#include <functional>
#include <map>
#include <memory>

namespace bcos
{
namespace executor
{
class TransactionExecutive;

class HostContext : public evmc_host_context
{
public:
    using UniquePtr = std::unique_ptr<HostContext>;
    using UniqueConstPtr = std::unique_ptr<const HostContext>;

    /// Full constructor.
    HostContext(CallParameters::UniquePtr callParameters,
        std::shared_ptr<TransactionExecutive> executive, std::string tableName);
    ~HostContext(){
        // auto total = utcTimeUs() - m_startTime;
        // EXECUTIVE_LOG(DEBUG) << LOG_DESC("TxExecution time(us)") << LOG_KV("total", total)
        //                      << LOG_KV("storageTimeProportion",
        //                             (m_getTimeUsed + m_setTimeUsed) / (double)total)
        //                      << LOG_KV("get", m_getTimeUsed) << LOG_KV("set", m_setTimeUsed);
    };

    HostContext(HostContext const&) = delete;
    HostContext& operator=(HostContext const&) = delete;

    std::string get(const std::string_view& _key);

    void set(const std::string_view& _key, std::string _value);

    bool registerAsset(const std::string& _assetName, const std::string_view& _issuer,
        bool _fungible, uint64_t _total, const std::string& _description);
    bool issueFungibleAsset(
        const std::string_view& _to, const std::string& _assetName, uint64_t _amount);
    uint64_t issueNotFungibleAsset(
        const std::string_view& _to, const std::string& _assetName, const std::string& _uri);
    std::string getNotFungibleAssetInfo(
        const std::string_view& _owner, const std::string& _assetName, uint64_t _id);
    bool transferAsset(const std::string_view& _to, const std::string& _assetName,
        uint64_t _amountOrID, bool _fromSelf);

    // if NFT return counts, else return value
    uint64_t getAssetBanlance(const std::string_view& _account, const std::string& _assetName);

    std::vector<uint64_t> getNotFungibleAssetIDs(
        const std::string_view& _account, const std::string& _assetName);

    /// Read storage location.
    u256 store(const u256& _n);

    /// Write a value in storage.
    void setStore(const u256& _n, const u256& _v);

    /// Create a new contract.
    evmc_result externalRequest(const evmc_message* _msg);

    evmc_result callBuiltInPrecompiled(
        std::unique_ptr<CallParameters> const& _request, bool _isEvmPrecompiled);

    bool setCode(bytes code);

    void setCodeAndAbi(bytes code, std::string abi);

    size_t codeSizeAt(const std::string_view& _a);

    h256 codeHashAt(const std::string_view& _a);

    /// Does the account exist?
    bool exists(const std::string_view&) { return true; }

    /// Return the EVM gas-price schedule for this execution context.
    VMSchedule const& vmSchedule() const;

    /// Hash of a block if within the last 256 blocks, or h256() otherwise.
    h256 blockHash() const;
    int64_t blockNumber() const;
    int64_t timestamp() const;
    int64_t blockGasLimit() const
    {
        return 3000000000;  // TODO: add config
    }

    bool isPermitted();

    /// Revert any changes made (by any of the other calls).
    void log(h256s&& _topics, bytesConstRef _data);

    /// ------ get interfaces related to HostContext------
    std::string_view myAddress() const;
    std::string_view caller() const { return m_callParameters->senderAddress; }
    std::string_view origin() const { return m_callParameters->origin; }
    std::string_view codeAddress() const { return m_callParameters->codeAddress; }
    bytesConstRef data() const { return ref(m_callParameters->data); }
    std::optional<storage::Entry> code();
    bool isCodeHasPrefix(std::string_view _prefix) const;
    h256 codeHash();
    u256 salt() const { return m_salt; }
    SubState& sub() { return m_sub; }
    bool isCreate() const { return m_callParameters->create; }
    bool staticCall() const { return m_callParameters->staticCall; }
    int64_t gas() const { return m_callParameters->gas; }

    CallParameters::UniquePtr&& takeCallParameters() { return std::move(m_callParameters); }

    static crypto::Hash::Ptr hashImpl() { return GlobalHashImpl::g_hashImpl; }

    uint64_t getStorageTimeUsed() { return m_getTimeUsed; }
    uint64_t setStorageTimeUsed() { return m_setTimeUsed; }

    bool isWasm();

private:
    void depositFungibleAsset(
        const std::string_view& _to, const std::string& _assetName, uint64_t _amount);
    void depositNotFungibleAsset(const std::string_view& _to, const std::string& _assetName,
        uint64_t _assetID, const std::string& _uri);

    CallParameters::UniquePtr m_callParameters;
    std::shared_ptr<TransactionExecutive> m_executive;
    std::string m_tableName;

    u256 m_salt;     ///< Values used in new address construction by CREATE2
    SubState m_sub;  ///< Sub-band VM state (suicides, refund counter, logs).

    std::list<CallParameters::UniquePtr> m_responseStore;
    std::atomic_uint64_t m_getTimeUsed = {0};  // microsecond
    std::atomic_uint64_t m_setTimeUsed = {0};  // microsecond
    std::atomic_uint64_t m_startTime = {0};    // microsecond
};

}  // namespace executor
}  // namespace bcos
