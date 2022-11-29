#pragma once

#include "TransactionExecutorFactory.h"
#include "bcos-executor/src/executor/TransactionExecutor.h"
#include "bcos-framework/executor/ExecutionMessage.h"
#include <bcos-framework/executor/ExecuteError.h>
#include <bcos-framework/executor/ParallelTransactionExecutorInterface.h>
#include <bcos-utilities/ThreadPool.h>
#include <thread>

namespace bcos::executor
{
class SwitchExecutorManager : public executor::ParallelTransactionExecutorInterface
{
public:
    const int64_t INIT_SCHEDULER_TERM_ID = 0;
    const int64_t STOPPED_TERM_ID = -1;

    SwitchExecutorManager(bcos::executor::TransactionExecutorFactory::Ptr factory)
      : m_pool("exec", std::thread::hardware_concurrency()), m_factory(factory)
    {
        refreshExecutor(INIT_SCHEDULER_TERM_ID);
    }

    ~SwitchExecutorManager() noexcept override {}

    void refreshExecutor(int64_t schedulerTermId)
    {
        // refresh when receive larger schedulerTermId
        if (schedulerTermId > m_schedulerTermId)
        {
            bcos::executor::TransactionExecutor::Ptr oldExecutor;
            {
                WriteGuard l(m_mutex);
                if (m_schedulerTermId != schedulerTermId)
                {
                    oldExecutor = m_executor;

                    // TODO: check cycle reference in executor to avoid memory leak
                    EXECUTOR_LOG(DEBUG) << LOG_BADGE("Switch")
                                        << "ExecutorSwitch: Build new executor instance with "
                                        << LOG_KV("schedulerTermId", schedulerTermId);
                    m_executor = m_factory->build();

                    m_schedulerTermId = schedulerTermId;
                }
            }
            if (oldExecutor)
            {
                oldExecutor->stop();
            }
        }
    }

    bool hasStopped()
    {
        ReadGuard l(m_mutex);
        return m_schedulerTermId == STOPPED_TERM_ID;
    }

    bool hasNextBlockHeaderDone()
    {
        ReadGuard l(m_mutex);
        return m_schedulerTermId != INIT_SCHEDULER_TERM_ID;
    }

    void nextBlockHeader(int64_t schedulerTermId,
        const bcos::protocol::BlockHeader::ConstPtr& blockHeader,
        std::function<void(bcos::Error::UniquePtr)> callback) override
    {
        if (hasStopped())
        {
            std::string message = "nextBlockHeader: executor has been stopped";
            EXECUTOR_LOG(DEBUG) << message;
            callback(BCOS_ERROR_UNIQUE_PTR(bcos::executor::ExecuteError::STOPPED, message));
            return;
        }

        if (schedulerTermId < m_schedulerTermId)
        {
            // Call from an outdated scheduler instance
            // Just return without callback, because run callback may trigger a new switch, thus
            // some message will be outdated and trigger switch again and again.
            EXECUTOR_LOG(INFO)
                << LOG_DESC("nextBlockHeader: not refreshExecutor for invalid schedulerTermId")
                << LOG_KV("termId", schedulerTermId) << LOG_KV("currentTermId", m_schedulerTermId);
            callback(BCOS_ERROR_UNIQUE_PTR(
                bcos::executor::ExecuteError::STOPPED, "old executor has been stopped"));
            return;
        }

        refreshExecutor(schedulerTermId);

        m_pool.enqueue([executor = m_executor, schedulerTermId,
                           blockHeader = std::move(blockHeader), callback = std::move(callback)]() {
            // create a holder
            auto _holdExecutorCallback = [executorHolder = executor, callback =
                                                                         std::move(callback)](
                                             bcos::Error::UniquePtr error) {
                EXECUTOR_LOG(TRACE)
                    << "Release executor holder" << LOG_KV("ptr count", executorHolder.use_count());
                callback(std::move(error));
            };

            // execute the function
            executor->nextBlockHeader(
                schedulerTermId, blockHeader, std::move(_holdExecutorCallback));
        });
    }

    void executeTransaction(bcos::protocol::ExecutionMessage::UniquePtr input,
        std::function<void(bcos::Error::UniquePtr, bcos::protocol::ExecutionMessage::UniquePtr)>
            callback) override
    {
        if (hasStopped())
        {
            std::string message = "executeTransaction: executor has been stopped";
            EXECUTOR_LOG(DEBUG) << message;
            callback(
                BCOS_ERROR_UNIQUE_PTR(bcos::executor::ExecuteError::STOPPED, message), nullptr);
            return;
        }

        if (!hasNextBlockHeaderDone())
        {
            callback(BCOS_ERROR_UNIQUE_PTR(bcos::executor::ExecuteError::SCHEDULER_TERM_ID_ERROR,
                         "Executor has just inited, need to switch"),
                nullptr);
            return;
        }

        m_pool.enqueue(
            [executor = m_executor, inputRaw = input.release(), callback = std::move(callback)] {
                // create a holder
                auto _holdExecutorCallback =
                    [executorHolder = executor, callback = std::move(callback)](
                        bcos::Error::UniquePtr error,
                        bcos::protocol::ExecutionMessage::UniquePtr output) {
                        EXECUTOR_LOG(TRACE) << "Release executor holder"
                                            << LOG_KV("ptr count", executorHolder.use_count());
                        callback(std::move(error), std::move(output));
                    };

                // execute the function
                executor->executeTransaction(bcos::protocol::ExecutionMessage::UniquePtr(inputRaw),
                    std::move(_holdExecutorCallback));
            });
    }

    void call(bcos::protocol::ExecutionMessage::UniquePtr input,
        std::function<void(bcos::Error::UniquePtr, bcos::protocol::ExecutionMessage::UniquePtr)>
            callback) override
    {
        if (hasStopped())
        {
            std::string message = "call: executor has been stopped";
            EXECUTOR_LOG(DEBUG) << message;
            callback(
                BCOS_ERROR_UNIQUE_PTR(bcos::executor::ExecuteError::STOPPED, message), nullptr);
            return;
        }

        auto currentExecutor = getAndNewExecutorIfNotExists();

        // create a holder
        auto _holdExecutorCallback =
            [executorHolder = currentExecutor, callback = std::move(callback)](
                bcos::Error::UniquePtr error, bcos::protocol::ExecutionMessage::UniquePtr output) {
                EXECUTOR_LOG(TRACE)
                    << "Release executor holder" << LOG_KV("ptr count", executorHolder.use_count());
                callback(std::move(error), std::move(output));
            };

        // execute the function
        currentExecutor->call(bcos::protocol::ExecutionMessage::UniquePtr(input.release()),
            std::move(_holdExecutorCallback));
    }

    void executeTransactions(std::string contractAddress,
        gsl::span<bcos::protocol::ExecutionMessage::UniquePtr> inputs,
        std::function<void(
            bcos::Error::UniquePtr, std::vector<bcos::protocol::ExecutionMessage::UniquePtr>)>
            callback) override
    {
        if (hasStopped())
        {
            std::string message = "executeTransactions: executor has been stopped";
            EXECUTOR_LOG(DEBUG) << message;
            callback(BCOS_ERROR_UNIQUE_PTR(bcos::executor::ExecuteError::STOPPED, message), {});
            return;
        }

        // Note: copy the inputs here in case of inputs has been released
        if (!hasNextBlockHeaderDone())
        {
            callback(BCOS_ERROR_UNIQUE_PTR(bcos::executor::ExecuteError::SCHEDULER_TERM_ID_ERROR,
                         "Executor has just inited, need to switch"),
                {});
            return;
        }

        auto inputsVec =
            std::make_shared<std::vector<bcos::protocol::ExecutionMessage::UniquePtr>>();
        for (auto i = 0u; i < inputs.size(); i++)
        {
            inputsVec->emplace_back(std::move(inputs[i]));
        }
        m_pool.enqueue([executor = m_executor, contractAddress = std::move(contractAddress),
                           inputsVec, callback = std::move(callback)] {
            // create a holder
            auto _holdExecutorCallback =
                [executorHolder = executor, callback = std::move(callback)](
                    bcos::Error::UniquePtr error,
                    std::vector<bcos::protocol::ExecutionMessage::UniquePtr> outputs) {
                    EXECUTOR_LOG(TRACE) << "Release executor holder"
                                        << LOG_KV("ptr count", executorHolder.use_count());
                    callback(std::move(error), std::move(outputs));
                };

            // execute the function
            executor->executeTransactions(
                contractAddress, *inputsVec, std::move(_holdExecutorCallback));
        });
    }

    void dmcExecuteTransactions(std::string contractAddress,
        gsl::span<bcos::protocol::ExecutionMessage::UniquePtr> inputs,
        std::function<void(
            bcos::Error::UniquePtr, std::vector<bcos::protocol::ExecutionMessage::UniquePtr>)>
            callback) override
    {
        if (hasStopped())
        {
            std::string message = "dmcExecuteTransactions: executor has been stopped";
            EXECUTOR_LOG(DEBUG) << message;
            callback(BCOS_ERROR_UNIQUE_PTR(bcos::executor::ExecuteError::STOPPED, message), {});
            return;
        }

        if (!hasNextBlockHeaderDone())
        {
            callback(BCOS_ERROR_UNIQUE_PTR(bcos::executor::ExecuteError::SCHEDULER_TERM_ID_ERROR,
                         "Executor has just inited, need to switch"),
                {});
            return;
        }

        // Note: copy the inputs here in case of inputs has been released
        auto inputsVec =
            std::make_shared<std::vector<bcos::protocol::ExecutionMessage::UniquePtr>>();
        for (auto i = 0u; i < inputs.size(); i++)
        {
            inputsVec->emplace_back(std::move(inputs[i]));
        }
        m_pool.enqueue([executor = m_executor, contractAddress = std::move(contractAddress),
                           inputsVec, callback = std::move(callback)] {
            // create a holder
            auto _holdExecutorCallback =
                [executorHolder = executor, callback = std::move(callback)](
                    bcos::Error::UniquePtr error,
                    std::vector<bcos::protocol::ExecutionMessage::UniquePtr> outputs) {
                    EXECUTOR_LOG(TRACE) << "Release executor holder"
                                        << LOG_KV("ptr count", executorHolder.use_count());
                    callback(std::move(error), std::move(outputs));
                };

            // execute the function
            executor->dmcExecuteTransactions(
                contractAddress, *inputsVec, std::move(_holdExecutorCallback));
        });
    }

    void dagExecuteTransactions(gsl::span<bcos::protocol::ExecutionMessage::UniquePtr> inputs,
        std::function<void(
            bcos::Error::UniquePtr, std::vector<bcos::protocol::ExecutionMessage::UniquePtr>)>
            callback) override
    {
        if (hasStopped())
        {
            std::string message = "dagExecuteTransactions: executor has been stopped";
            EXECUTOR_LOG(DEBUG) << message;
            callback(BCOS_ERROR_UNIQUE_PTR(bcos::executor::ExecuteError::STOPPED, message), {});
            return;
        }

        if (!hasNextBlockHeaderDone())
        {
            callback(BCOS_ERROR_UNIQUE_PTR(bcos::executor::ExecuteError::SCHEDULER_TERM_ID_ERROR,
                         "Executor has just inited, need to switch"),
                {});
            return;
        }

        auto inputsVec =
            std::make_shared<std::vector<bcos::protocol::ExecutionMessage::UniquePtr>>();
        for (auto i = 0u; i < inputs.size(); i++)
        {
            inputsVec->emplace_back(std::move(inputs[i]));
        }
        m_pool.enqueue([executor = m_executor, inputsVec, callback = std::move(callback)] {
            // create a holder
            auto _holdExecutorCallback =
                [executorHolder = executor, callback = std::move(callback)](
                    bcos::Error::UniquePtr error,
                    std::vector<bcos::protocol::ExecutionMessage::UniquePtr> outputs) {
                    EXECUTOR_LOG(TRACE) << "Release executor holder"
                                        << LOG_KV("ptr count", executorHolder.use_count());
                    callback(std::move(error), std::move(outputs));
                };

            // execute the function
            executor->dagExecuteTransactions(*inputsVec, std::move(_holdExecutorCallback));
        });
    }

    void dmcCall(bcos::protocol::ExecutionMessage::UniquePtr input,
        std::function<void(bcos::Error::UniquePtr, bcos::protocol::ExecutionMessage::UniquePtr)>
            callback) override
    {
        if (hasStopped())
        {
            std::string message = "dmcCall: executor has been stopped";
            EXECUTOR_LOG(DEBUG) << message;
            callback(
                BCOS_ERROR_UNIQUE_PTR(bcos::executor::ExecuteError::STOPPED, message), nullptr);
            return;
        }

        auto currentExecutor = getAndNewExecutorIfNotExists();

        // create a holder
        auto _holdExecutorCallback =
            [executorHolder = currentExecutor, callback = std::move(callback)](
                bcos::Error::UniquePtr error, bcos::protocol::ExecutionMessage::UniquePtr output) {
                EXECUTOR_LOG(TRACE)
                    << "Release executor holder" << LOG_KV("ptr count", executorHolder.use_count());
                callback(std::move(error), std::move(output));
            };

        // execute the function
        currentExecutor->dmcCall(bcos::protocol::ExecutionMessage::UniquePtr(input.release()),
            std::move(_holdExecutorCallback));
    }

    void getHash(bcos::protocol::BlockNumber number,
        std::function<void(bcos::Error::UniquePtr, crypto::HashType)> callback) override
    {
        if (hasStopped())
        {
            std::string message = "getHash: executor has been stopped";
            EXECUTOR_LOG(DEBUG) << message;
            callback(BCOS_ERROR_UNIQUE_PTR(bcos::executor::ExecuteError::STOPPED, message), {});
            return;
        }

        auto currentExecutor = getAndNewExecutorIfNotExists();

        m_pool.enqueue([executor = currentExecutor, number, callback = std::move(callback)] {
            // create a holder
            auto _holdExecutorCallback =
                [executorHolder = executor, callback = std::move(callback)](
                    bcos::Error::UniquePtr error, crypto::HashType hashType) {
                    EXECUTOR_LOG(TRACE) << "Release executor holder"
                                        << LOG_KV("ptr count", executorHolder.use_count());
                    callback(std::move(error), std::move(hashType));
                };

            // execute the function
            executor->getHash(number, std::move(_holdExecutorCallback));
        });
    }

    /* ----- XA Transaction interface Start ----- */

    // Write data to storage uncommitted
    void prepare(const bcos::protocol::TwoPCParams& params,
        std::function<void(bcos::Error::Ptr)> callback) override
    {
        if (hasStopped())
        {
            std::string message = "prepare: executor has been stopped";
            EXECUTOR_LOG(DEBUG) << message;
            callback(BCOS_ERROR_UNIQUE_PTR(bcos::executor::ExecuteError::STOPPED, message));
            return;
        }

        if (!hasNextBlockHeaderDone())
        {
            callback(BCOS_ERROR_UNIQUE_PTR(bcos::executor::ExecuteError::SCHEDULER_TERM_ID_ERROR,
                "Executor has just inited, need to switch"));
            return;
        }

        m_pool.enqueue([executor = m_executor, params = bcos::protocol::TwoPCParams(params),
                           callback = std::move(callback)] {
            // create a holder
            auto _holdExecutorCallback = [executorHolder = executor, callback =
                                                                         std::move(callback)](
                                             bcos::Error::Ptr error) {
                EXECUTOR_LOG(TRACE)
                    << "Release executor holder" << LOG_KV("ptr count", executorHolder.use_count());
                callback(std::move(error));
            };

            // execute the function
            executor->prepare(params, std::move(_holdExecutorCallback));
        });
    }

    // Commit uncommitted data
    void commit(const bcos::protocol::TwoPCParams& params,
        std::function<void(bcos::Error::Ptr)> callback) override
    {
        if (hasStopped())
        {
            std::string message = "commit: executor has been stopped";
            EXECUTOR_LOG(DEBUG) << message;
            callback(BCOS_ERROR_UNIQUE_PTR(bcos::executor::ExecuteError::STOPPED, message));
            return;
        }

        if (!hasNextBlockHeaderDone())
        {
            callback(BCOS_ERROR_UNIQUE_PTR(bcos::executor::ExecuteError::SCHEDULER_TERM_ID_ERROR,
                "Executor has just inited, need to switch"));
            return;
        }

        m_pool.enqueue([executor = m_executor, params = bcos::protocol::TwoPCParams(params),
                           callback = std::move(callback)] {
            // create a holder
            auto _holdExecutorCallback = [executorHolder = executor, callback =
                                                                         std::move(callback)](
                                             bcos::Error::Ptr error) {
                EXECUTOR_LOG(TRACE)
                    << "Release executor holder" << LOG_KV("ptr count", executorHolder.use_count());
                callback(std::move(error));
            };

            // execute the function
            executor->commit(params, std::move(_holdExecutorCallback));
        });
    }

    // Rollback the changes
    void rollback(const bcos::protocol::TwoPCParams& params,
        std::function<void(bcos::Error::Ptr)> callback) override
    {
        if (hasStopped())
        {
            std::string message = "rollback: executor has been stopped";
            EXECUTOR_LOG(DEBUG) << message;
            callback(BCOS_ERROR_UNIQUE_PTR(bcos::executor::ExecuteError::STOPPED, message));
            return;
        }

        if (!hasNextBlockHeaderDone())
        {
            callback(BCOS_ERROR_UNIQUE_PTR(bcos::executor::ExecuteError::SCHEDULER_TERM_ID_ERROR,
                "Executor has just inited, need to switch"));
            return;
        }

        m_pool.enqueue([executor = m_executor, params = bcos::protocol::TwoPCParams(params),
                           callback = std::move(callback)] {
            // create a holder
            auto _holdExecutorCallback = [executorHolder = executor, callback =
                                                                         std::move(callback)](
                                             bcos::Error::Ptr error) {
                EXECUTOR_LOG(TRACE)
                    << "Release executor holder" << LOG_KV("ptr count", executorHolder.use_count());
                callback(std::move(error));
            };

            // execute the function
            executor->rollback(params, std::move(_holdExecutorCallback));
        });
    }

    /* ----- XA Transaction interface End ----- */

    // drop all status
    void reset(std::function<void(bcos::Error::Ptr)> callback) override
    {
        if (hasStopped())
        {
            std::string message = "reset: executor has been stopped";
            EXECUTOR_LOG(DEBUG) << message;
            callback(BCOS_ERROR_UNIQUE_PTR(bcos::executor::ExecuteError::STOPPED, message));
            return;
        }

        auto currentExecutor = getAndNewExecutorIfNotExists();

        m_pool.enqueue([executor = currentExecutor, callback = std::move(callback)] {
            // create a holder
            auto _holdExecutorCallback = [executorHolder = executor, callback =
                                                                         std::move(callback)](
                                             bcos::Error::Ptr error) {
                EXECUTOR_LOG(TRACE)
                    << "Release executor holder" << LOG_KV("ptr count", executorHolder.use_count());
                callback(std::move(error));
            };

            // execute the function
            executor->reset(std::move(_holdExecutorCallback));
        });
    }
    void getCode(std::string_view contract,
        std::function<void(bcos::Error::Ptr, bcos::bytes)> callback) override
    {
        if (hasStopped())
        {
            std::string message = "getCode: executor has been stopped";
            EXECUTOR_LOG(DEBUG) << message;
            callback(BCOS_ERROR_UNIQUE_PTR(bcos::executor::ExecuteError::STOPPED, message), {});
            return;
        }

        auto currentExecutor = getAndNewExecutorIfNotExists();

        m_pool.enqueue([executor = currentExecutor, contract = std::string(contract),
                           callback = std::move(callback)] {
            // create a holder
            auto _holdExecutorCallback = [executorHolder = executor, callback =
                                                                         std::move(callback)](
                                             bcos::Error::Ptr error, bcos::bytes bytes) {
                EXECUTOR_LOG(TRACE)
                    << "Release executor holder" << LOG_KV("ptr count", executorHolder.use_count());
                callback(std::move(error), std::move(bytes));
            };

            // execute the function
            executor->getCode(contract, std::move(_holdExecutorCallback));
        });
    }

    void getABI(std::string_view contract,
        std::function<void(bcos::Error::Ptr, std::string)> callback) override
    {
        if (hasStopped())
        {
            std::string message = "getABI: executor has been stopped";
            EXECUTOR_LOG(DEBUG) << message;
            callback(BCOS_ERROR_UNIQUE_PTR(bcos::executor::ExecuteError::STOPPED, message), {});
            return;
        }

        auto currentExecutor = getAndNewExecutorIfNotExists();

        m_pool.enqueue([executor = currentExecutor, contract = std::string(contract),
                           callback = std::move(callback)] {
            // create a holder
            auto _holdExecutorCallback = [executorHolder = executor, callback =
                                                                         std::move(callback)](
                                             bcos::Error::Ptr error, std::string str) {
                EXECUTOR_LOG(TRACE)
                    << "Release executor holder" << LOG_KV("ptr count", executorHolder.use_count());
                callback(std::move(error), std::move(str));
            };

            // execute the function
            executor->getABI(contract, std::move(_holdExecutorCallback));
        });
    }

    void stop() override
    {
        EXECUTOR_LOG(INFO) << "Try to stop SwitchExecutorManager";
        auto executorUseCount = 0;
        {
            WriteGuard l(m_mutex);
            if (m_executor)
            {
                m_executor->stop();
            }
            executorUseCount = m_executor.use_count();
        }

        // waiting for stopped
        while (executorUseCount > 1)
        {
            auto executor = getCurrentExecutor();
            if (executor != nullptr)
            {
                executorUseCount = executor.use_count();
            }
            EXECUTOR_LOG(DEBUG) << "Executor is stopping.. "
                                << LOG_KV("unfinishedTaskNum", executorUseCount - 1);
            std::this_thread::sleep_for(std::chrono::milliseconds(250));
        }
        EXECUTOR_LOG(INFO) << "Executor has stopped.";

        m_executor = nullptr;

        m_schedulerTermId = STOPPED_TERM_ID;
    }

    bcos::executor::TransactionExecutor::Ptr getCurrentExecutor()
    {
        ReadGuard l(m_mutex);
        auto executor = m_executor;
        return executor;
    }

    bcos::executor::TransactionExecutor::Ptr getAndNewExecutorIfNotExists()
    {
        WriteGuard l(m_mutex);
        if (!m_executor)
        {
            m_executor = m_factory->build();
        }
        auto executor = m_executor;
        return executor;
    }

private:
    bcos::ThreadPool m_pool;
    bcos::executor::TransactionExecutor::Ptr m_executor;
    int64_t m_schedulerTermId = STOPPED_TERM_ID;

    mutable bcos::SharedMutex m_mutex;

    bcos::executor::TransactionExecutorFactory::Ptr m_factory;
};
}  // namespace bcos::executor