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
 * @brief the header of storage
 * @file Storage.h
 * @author: xingqiangbai
 * @date: 2021-04-16
 */
#include "RocksDBStorage.h"
#include "Common.h"
#include "bcos-framework/protocol/ProtocolTypeDef.h"
#include "bcos-framework/storage/Table.h"
#include <bcos-utilities/Error.h>
#include <rocksdb/cleanable.h>
#include <rocksdb/options.h>
#include <rocksdb/slice.h>
#include <tbb/concurrent_vector.h>
#include <tbb/spin_mutex.h>
#include <boost/algorithm/hex.hpp>
#include <csignal>
#include <exception>
#include <future>
#include <optional>

using namespace bcos::storage;
using namespace bcos::protocol;
using namespace rocksdb;
using namespace std;

#define STORAGE_ROCKSDB_LOG(LEVEL) BCOS_LOG(LEVEL) << "[STORAGE-RocksDB]"

RocksDBStorage::RocksDBStorage(std::unique_ptr<rocksdb::DB, std::function<void(rocksdb::DB*)>>&& db,
    const bcos::security::DataEncryptInterface::Ptr dataEncryption)
  : m_db(std::move(db)), m_dataEncryption(dataEncryption)
{
    m_writeBatch = std::make_shared<WriteBatch>();
}

void RocksDBStorage::asyncGetPrimaryKeys(std::string_view _table,
    const std::optional<Condition const>& _condition,
    std::function<void(Error::UniquePtr, std::vector<std::string>)> _callback)
{
    auto start = utcTime();
    std::vector<std::string> result;

    std::string keyPrefix;
    keyPrefix = string(_table) + TABLE_KEY_SPLIT;

    ReadOptions read_options;
    read_options.total_order_seek = true;
    auto iter = m_db->NewIterator(read_options);

    // FIXME: check performance and add limit of primary keys
    for (iter->Seek(keyPrefix); iter->Valid() && iter->key().starts_with(keyPrefix); iter->Next())
    {
        size_t start = keyPrefix.size();
        if (!_condition || _condition->isValid(std::string_view(
                               iter->key().data() + start, iter->key().size() - start)))
        {  // filter by condition, the key need
           // remove TABLE_PREFIX
            result.emplace_back(iter->key().ToString().substr(start));
        }
    }
    delete iter;
    auto end = utcTime();
    _callback(nullptr, std::move(result));
    STORAGE_ROCKSDB_LOG(TRACE) << LOG_DESC("asyncGetPrimaryKeys") << LOG_KV("table", _table)
                               << LOG_KV("count", result.size())
                               << LOG_KV("read time(ms)", end - start)
                               << LOG_KV("callback time(ms)", utcTime() - end);
}

void RocksDBStorage::asyncGetRow(std::string_view _table, std::string_view _key,
    std::function<void(Error::UniquePtr, std::optional<Entry>)> _callback)
{
    try
    {
        if (!isValid(_table, _key))
        {
            STORAGE_ROCKSDB_LOG(WARNING) << LOG_DESC("asyncGetRow empty tableName or key")
                                         << LOG_KV("table", _table) << LOG_KV("key", _key);
            _callback(BCOS_ERROR_UNIQUE_PTR(TableNotExists, "empty tableName or key"), {});
            return;
        }
        auto start = utcTime();
        std::string value;
        auto dbKey = toDBKey(_table, _key);

        auto status = m_db->Get(
            ReadOptions(), m_db->DefaultColumnFamily(), Slice(dbKey.data(), dbKey.size()), &value);

        if (false == value.empty() && nullptr != m_dataEncryption)
            value = m_dataEncryption->decrypt(value);

        if (!status.ok())
        {
            if (status.IsNotFound())
            {
                if (c_fileLogLevel >= TRACE)
                {
                    STORAGE_ROCKSDB_LOG(TRACE) << LOG_DESC("not found") << LOG_KV("table", _table)
                                               << LOG_KV("key", toHex(_key));
                }
                _callback(nullptr, {});
                return;
            }

            std::string errorMessage =
                "RocksDB get failed!, " + boost::lexical_cast<std::string>(status.ToString());
            STORAGE_ROCKSDB_LOG(WARNING)
                << LOG_DESC("asyncGetRow failed") << LOG_KV("table", _table) << LOG_KV("key", _key)
                << LOG_KV("error", errorMessage);
            if (status.getState())
            {
                errorMessage.append(" ").append(status.getState());
            }
            _callback(BCOS_ERROR_UNIQUE_PTR(ReadError, errorMessage), {});

            return;
        }
        auto end = utcTime();
        auto end2 = utcTime();

        std::optional<Entry> entry((Entry()));

        entry->set(std::move(value));

        _callback(nullptr, entry);

        STORAGE_ROCKSDB_LOG(TRACE)
            << LOG_DESC("asyncGetRow") << LOG_KV("table", _table)
            << LOG_KV("key", boost::algorithm::hex_lower(std::string(_key)))
            << LOG_KV("read time(ms)", end - start) << LOG_KV("tableInfo time(ms)", end2 - end)
            << LOG_KV("callback time(ms)", utcTime() - end2);
    }
    catch (const std::exception& e)
    {
        STORAGE_ROCKSDB_LOG(WARNING)
            << LOG_DESC("asyncGetRow exception") << LOG_KV("table", _table)
            << LOG_KV("key", boost::algorithm::hex_lower(std::string(_key)))
            << LOG_KV("exception", boost::diagnostic_information(e));
        _callback(BCOS_ERROR_WITH_PREV_UNIQUE_PTR(UnknownEntryType, "Get row failed!", e), {});
    }
}

void RocksDBStorage::asyncGetRows(std::string_view _table,
    const std::variant<const gsl::span<std::string_view const>, const gsl::span<std::string const>>&
        _keys,
    std::function<void(Error::UniquePtr, std::vector<std::optional<Entry>>)> _callback)
{
    try
    {
        if (!isValid(_table))
        {
            STORAGE_ROCKSDB_LOG(WARNING)
                << LOG_DESC("asyncGetRow empty tableName") << LOG_KV("table", _table);
            _callback(BCOS_ERROR_UNIQUE_PTR(TableNotExists, "empty tableName"), {});
            return;
        }
        auto start = utcTime();
        std::visit(
            [&](auto const& keys) {
                std::vector<std::optional<Entry>> entries(keys.size());

                std::vector<std::string> dbKeys(keys.size());
                std::vector<Slice> slices(keys.size());
                tbb::parallel_for(tbb::blocked_range<size_t>(0, keys.size()),
                    [&](const tbb::blocked_range<size_t>& range) {
                        for (size_t i = range.begin(); i != range.end(); ++i)
                        {
                            dbKeys[i] = toDBKey(_table, keys[i]);
                            slices[i] = Slice(dbKeys[i].data(), dbKeys[i].size());
                        }
                    });

                std::vector<PinnableSlice> values(keys.size());
                std::vector<Status> statusList(keys.size());
                m_db->MultiGet(ReadOptions(), m_db->DefaultColumnFamily(), slices.size(),
                    slices.data(), values.data(), statusList.data());
                auto end = utcTime();
                tbb::parallel_for(tbb::blocked_range<size_t>(0, keys.size()),
                    [&](const tbb::blocked_range<size_t>& range) {
                        for (size_t i = range.begin(); i != range.end(); ++i)
                        {
                            auto& status = statusList[i];
                            auto& value = values[i];

                            if (status.ok())
                            {
                                entries[i] = std::make_optional(Entry());

                                std::string v(value.data(), value.size());

                                // Storage Security
                                if (false == v.empty() && nullptr != m_dataEncryption)
                                    v = m_dataEncryption->decrypt(v);

                                entries[i]->set(std::move(v));
                            }
                            else
                            {
                                if (status.IsNotFound())
                                {
                                    STORAGE_LOG(TRACE)
                                        << "Multi get rows, not found key: " << keys[i];
                                }
                                else if (status.getState())
                                {
                                    STORAGE_LOG(WARNING)
                                        << "Multi get rows error: " << status.getState();
                                }
                                else
                                {
                                    STORAGE_LOG(WARNING)
                                        << "Multi get rows error:" << status.ToString();
                                }
                            }
                        }
                    });
                auto decode = utcTime();
                _callback(nullptr, std::move(entries));
                STORAGE_ROCKSDB_LOG(TRACE)
                    << LOG_DESC("asyncGetRows") << LOG_KV("table", _table)
                    << LOG_KV("count", entries.size()) << LOG_KV("read time(ms)", end - start)
                    << LOG_KV("decode time(ms)", decode - end)
                    << LOG_KV("callback time(ms)", utcTime() - decode);
            },
            _keys);
    }
    catch (const std::exception& e)
    {
        _callback(BCOS_ERROR_WITH_PREV_UNIQUE_PTR(UnknownEntryType, "Get rows failed! ", e), {});
    }
}

void RocksDBStorage::asyncSetRow(std::string_view _table, std::string_view _key, Entry _entry,
    std::function<void(Error::UniquePtr)> _callback)
{
    try
    {
        if (!isValid(_table, _key))
        {
            STORAGE_ROCKSDB_LOG(WARNING) << LOG_DESC("asyncGetRow empty tableName or key")
                                         << LOG_KV("table", _table) << LOG_KV("key", _key);
            _callback(BCOS_ERROR_UNIQUE_PTR(TableNotExists, "empty tableName or key"));
            return;
        }
        auto dbKey = toDBKey(_table, _key);
        WriteOptions options;
        rocksdb::Status status;
        if (_entry.status() == Entry::DELETED)
        {
            STORAGE_ROCKSDB_LOG(TRACE)
                << LOG_DESC("asyncSetRow delete") << LOG_KV("table", _table)
                << LOG_KV("key", boost::algorithm::hex_lower(std::string(_key)));
            status = m_db->Delete(options, dbKey);
        }
        else
        {
            STORAGE_ROCKSDB_LOG(TRACE)
                << LOG_DESC("asyncSetRow") << LOG_KV("table", _table)
                << LOG_KV("key", boost::algorithm::hex_lower(std::string(_key)));

            std::string value(_entry.get().data(), _entry.get().size());

            // Storage Security
            if (false == value.empty() && nullptr != m_dataEncryption)
                value = m_dataEncryption->encrypt(value);

            status = m_db->Put(options, dbKey, std::move(value));
        }

        if (!status.ok())
        {
            checkStatus(status);
            std::string errorMessage = "Set row failed! " + status.ToString();
            if (status.getState())
            {
                errorMessage.append(" ").append(status.getState());
            }
            _callback(BCOS_ERROR_UNIQUE_PTR(WriteError, errorMessage));
            return;
        }

        _callback(nullptr);
    }
    catch (const std::exception& e)
    {
        _callback(BCOS_ERROR_WITH_PREV_UNIQUE_PTR(UnknownEntryType, "Set row failed! ", e));
    }
}

void RocksDBStorage::asyncPrepare(const TwoPCParams& param, const TraverseStorageInterface& storage,
    std::function<void(Error::Ptr, uint64_t startTS)> callback)
{
    std::ignore = param;
    try
    {
        STORAGE_ROCKSDB_LOG(INFO) << LOG_DESC("asyncPrepare") << LOG_KV("number", param.number);
        auto start = utcTime();
        {
            tbb::spin_mutex::scoped_lock lock(m_writeBatchMutex);
            if (!m_writeBatch)
            {
                m_writeBatch = std::make_shared<WriteBatch>();
            }
        }
        std::atomic_uint64_t putCount{0};
        std::atomic_uint64_t deleteCount{0};
        atomic_bool isTableValid = true;
        storage.parallelTraverse(true,
            [&](const std::string_view& table, const std::string_view& key, Entry const& entry) {
                if (!isValid(table, key))
                {
                    isTableValid = false;
                    return false;
                }
                auto dbKey = toDBKey(table, key);

                if (entry.status() == Entry::DELETED)
                {
                    if (c_fileLogLevel >= TRACE)
                    {
                        STORAGE_ROCKSDB_LOG(TRACE) << LOG_DESC("delete") << LOG_KV("table", table)
                                                   << LOG_KV("key", toHex(key));
                    }
                    ++deleteCount;
                    tbb::spin_mutex::scoped_lock lock(m_writeBatchMutex);
                    m_writeBatch->Delete(dbKey);
                }
                else
                {
                    if (c_fileLogLevel >= TRACE)
                    {
                        STORAGE_ROCKSDB_LOG(TRACE)
                            << LOG_DESC("write") << LOG_KV("table", table)
                            << LOG_KV("key", toHex(key)) << LOG_KV("size", entry.size());
                    }
                    ++putCount;
                    tbb::spin_mutex::scoped_lock lock(m_writeBatchMutex);

                    std::string value(entry.get().data(), entry.get().size());

                    // Storage security
                    if (false == value.empty() && nullptr != m_dataEncryption)
                        value = m_dataEncryption->encrypt(value);

                    auto status = m_writeBatch->Put(dbKey, std::move(value));
                }
                return true;
            });

        if (!isTableValid)
        {
            {
                tbb::spin_mutex::scoped_lock lock(m_writeBatchMutex);
                m_writeBatch = nullptr;
            }
            STORAGE_ROCKSDB_LOG(ERROR)
                << LOG_DESC("asyncPrepare invalidTable") << LOG_KV("number", param.number);
            callback(BCOS_ERROR_UNIQUE_PTR(TableNotExists, "empty tableName or key"), 0);
            return;
        }
        auto end = utcTime();
        callback(nullptr, 0);
        STORAGE_ROCKSDB_LOG(INFO) << LOG_DESC("asyncPrepare") << LOG_KV("number", param.number)
                                  << LOG_KV("put", putCount) << LOG_KV("delete", deleteCount)
                                  << LOG_KV("startTS", param.timestamp)
                                  << LOG_KV("time(ms)", end - start)
                                  << LOG_KV("callback time(ms)", utcTime() - end);
    }
    catch (const std::exception& e)
    {
        callback(BCOS_ERROR_WITH_PREV_UNIQUE_PTR(UnknownEntryType, "Prepare failed! ", e), 0);
    }
}

void RocksDBStorage::asyncCommit(
    const TwoPCParams& params, std::function<void(Error::Ptr, uint64_t)> callback)
{
    size_t count = 0;
    auto start = utcTime();
    std::ignore = params;
    {
        tbb::spin_mutex::scoped_lock lock(m_writeBatchMutex);
        if (m_writeBatch)
        {
            WriteOptions options;
            // options.sync = true;
            count = m_writeBatch->Count();
            auto status = m_db->Write(options, m_writeBatch.get());
            auto err = checkStatus(status);
            if (err)
            {
                STORAGE_ROCKSDB_LOG(WARNING)
                    << LOG_DESC("asyncCommit failed") << LOG_KV("number", params.number)
                    << LOG_KV("message", err->errorMessage()) << LOG_KV("startTS", params.timestamp)
                    << LOG_KV("time(ms)", utcTime() - start);
                lock.release();
                callback(err, 0);
                return;
            }
            m_writeBatch = nullptr;
        }
    }
    auto end = utcTime();
    callback(nullptr, 0);
    STORAGE_ROCKSDB_LOG(INFO) << LOG_DESC("asyncCommit") << LOG_KV("number", params.number)
                              << LOG_KV("startTS", params.timestamp)
                              << LOG_KV("time(ms)", utcTime() - start)
                              << LOG_KV("callback time(ms)", utcTime() - end)
                              << LOG_KV("count", count);
}

void RocksDBStorage::asyncRollback(
    const TwoPCParams& params, std::function<void(Error::Ptr)> callback)
{
    auto start = utcTime();

    std::ignore = params;
    {
        tbb::spin_mutex::scoped_lock lock(m_writeBatchMutex);
        m_writeBatch = nullptr;
    }
    auto end = utcTime();
    callback(nullptr);
    STORAGE_ROCKSDB_LOG(INFO) << LOG_DESC("asyncRollback") << LOG_KV("number", params.number)
                              << LOG_KV("startTS", params.timestamp)
                              << LOG_KV("time(ms)", utcTime() - start)
                              << LOG_KV("callback time(ms)", utcTime() - end);
}

bcos::Error::Ptr RocksDBStorage::setRows(
    std::string_view table, std::vector<std::string> keys, std::vector<std::string> values) noexcept
{
    if (table.empty())
    {
        STORAGE_ROCKSDB_LOG(WARNING)
            << LOG_DESC("setRows empty tableName") << LOG_KV("table", table);
        return BCOS_ERROR_PTR(TableNotExists, "empty tableName");
    }
    if (keys.size() != values.size())
    {
        STORAGE_ROCKSDB_LOG(WARNING)
            << LOG_DESC("setRows values size mismatch keys size") << LOG_KV("keys", keys.size())
            << LOG_KV("values", values.size());
        return BCOS_ERROR_PTR(TableNotExists, "setRows values size mismatch keys size");
    }
    if (keys.empty())
    {
        STORAGE_ROCKSDB_LOG(WARNING) << LOG_DESC("setRows empty keys") << LOG_KV("table", table);
        return nullptr;
    }
    std::vector<std::string> realKeys(keys.size());
    std::vector<std::string> encryptedValues;
    encryptedValues.resize(values.size());
    tbb::parallel_for(
        tbb::blocked_range<size_t>(0, keys.size()), [&](const tbb::blocked_range<size_t>& range) {
            for (size_t i = range.begin(); i != range.end(); ++i)
            {
                realKeys[i] = toDBKey(table, keys[i]);
                if (m_dataEncryption)
                {
                    encryptedValues[i] = m_dataEncryption->encrypt(values[i]);
                }
            }
        });
    auto writeBatch = WriteBatch();
    for (size_t i = 0; i < values.size(); ++i)
    {
        // Storage Security
        if (m_dataEncryption)
        {
            writeBatch.Put(std::move(realKeys[i]), std::move(encryptedValues[i]));
        }
        else
        {
            writeBatch.Put(std::move(realKeys[i]), std::move(values[i]));
        }
    }
    WriteOptions options;
    auto status = m_db->Write(options, &writeBatch);
    auto err = checkStatus(status);
    if (err)
    {
        STORAGE_ROCKSDB_LOG(WARNING)
            << LOG_DESC("setRows failed") << LOG_KV("message", err->errorMessage());
        return err;
    }
    return nullptr;
}

bcos::Error::Ptr RocksDBStorage::checkStatus(rocksdb::Status const& status)
{
    if (status.ok() || status.IsNotFound())
    {
        return nullptr;
    }
    std::string errorInfo = "access rocksDB failed, status: " + status.ToString();
    // fatal exception
    if (status.IsIOError() || status.IsCorruption() || status.IsNoSpace() ||
        status.IsNotSupported() || status.IsShutdownInProgress())
    {
        std::raise(SIGTERM);
        STORAGE_ROCKSDB_LOG(ERROR) << LOG_DESC(errorInfo);
        return BCOS_ERROR_PTR(DatabaseError, errorInfo);
    }
    // exception that can be recovered by retry
    // statuses are: Busy, TimedOut, TryAgain, Aborted, MergeInProgress, IsIncomplete, Expired,
    // CompactionToolLarge
    else
    {
        errorInfo = errorInfo + ", please try again!";
        STORAGE_ROCKSDB_LOG(WARNING) << LOG_DESC(errorInfo);
        return BCOS_ERROR_PTR(DatabaseRetryable, errorInfo);
    }
}
