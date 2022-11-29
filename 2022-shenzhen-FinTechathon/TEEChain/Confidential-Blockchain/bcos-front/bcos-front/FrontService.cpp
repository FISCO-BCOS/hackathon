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
 * @file FrontService.cpp
 * @author: octopus
 * @date 2021-04-19
 */
#include <bcos-framework/protocol/CommonError.h>
#include <bcos-framework/protocol/GlobalConfig.h>
#include <bcos-front/Common.h>
#include <bcos-front/FrontMessage.h>
#include <bcos-front/FrontService.h>
#include <bcos-utilities/Common.h>
#include <bcos-utilities/Exceptions.h>
#include <boost/uuid/random_generator.hpp>
#include <boost/uuid/uuid_io.hpp>
#include <random>
#include <thread>

using namespace bcos;
using namespace front;
using namespace protocol;

FrontService::FrontService()
{
    m_localProtocol = g_BCOSConfig.protocolInfo(ProtocolModuleID::NodeService);
    FRONT_LOG(INFO) << LOG_DESC("FrontService") << LOG_KV("this", this)
                    << LOG_KV("minVersion", m_localProtocol->minVersion())
                    << LOG_KV("maxVersion", m_localProtocol->maxVersion());
}

FrontService::~FrontService()
{
    stop();
    FRONT_LOG(INFO) << LOG_DESC("~FrontService") << LOG_KV("this", this);
}

// check the startup parameters, exception will be thrown if the required
// parameters are not set properly
void FrontService::checkParams()
{
    if (m_groupID.empty())
    {
        BOOST_THROW_EXCEPTION(
            InvalidParameter() << errinfo_comment(" FrontService groupID is uninitialized"));
    }

    if (!m_nodeID)
    {
        BOOST_THROW_EXCEPTION(
            InvalidParameter() << errinfo_comment(" FrontService nodeID is uninitialized"));
    }

    if (!m_gatewayInterface)
    {
        BOOST_THROW_EXCEPTION(InvalidParameter() << errinfo_comment(
                                  " FrontService gatewayInterface is uninitialized"));
    }

    if (!m_messageFactory)
    {
        BOOST_THROW_EXCEPTION(
            InvalidParameter() << errinfo_comment(" FrontService messageFactory is uninitialized"));
    }

    if (!m_ioService)
    {
        BOOST_THROW_EXCEPTION(
            InvalidParameter() << errinfo_comment(" FrontService ioService is uninitialized"));
    }

    return;
}

void FrontService::start()
{
    if (m_run)
    {
        FRONT_LOG(INFO) << LOG_BADGE("start") << LOG_DESC("front service is running")
                        << LOG_KV("nodeID", m_nodeID->hex()) << LOG_KV("groupID", m_groupID);
        return;
    }

    checkParams();

    m_run = true;

    // try to getNodeIDs from gateway
    auto self = std::weak_ptr<FrontService>(shared_from_this());
    m_gatewayInterface->asyncGetGroupNodeInfo(
        m_groupID, [self](Error::Ptr _error, bcos::gateway::GroupNodeInfo::Ptr _groupNodeInfo) {
            if (_error)
            {
                FRONT_LOG(ERROR) << LOG_BADGE("start") << LOG_DESC("asyncGetGroupNodeInfo error")
                                 << LOG_KV("errorCode", _error->errorCode())
                                 << LOG_KV("errorMessage", _error->errorMessage());
                return;
            }
            FRONT_LOG(INFO) << LOG_BADGE("start") << LOG_DESC("asyncGetGroupNodeInfo callback")
                            << LOG_KV("node size",
                                   _groupNodeInfo ? _groupNodeInfo->nodeIDList().size() : 0);
            auto frontService = self.lock();
            if (frontService)
            {
                frontService->onReceiveGroupNodeInfo(
                    frontService->groupID(), _groupNodeInfo, nullptr);
            }
        });

    m_frontServiceThread = std::make_shared<std::thread>([=, this]() {
        while (m_run)
        {
            try
            {
                m_ioService->run();
            }
            catch (std::exception& e)
            {
                FRONT_LOG(WARNING)
                    << LOG_DESC("IOService") << LOG_KV("error", boost::diagnostic_information(e));
            }

            std::this_thread::sleep_for(std::chrono::milliseconds(100));

            if (m_run && m_ioService->stopped())
            {
                m_ioService->restart();
            }
        }
    });

    FRONT_LOG(INFO) << LOG_DESC("start") << LOG_KV("nodeID", m_nodeID->hex())
                    << LOG_KV("groupID", m_groupID);

    FRONT_LOG(INFO) << LOG_DESC("register module")
                    << LOG_KV("count", m_moduleID2MessageDispatcher.size());
    for (const auto& module : m_moduleID2MessageDispatcher)
    {
        FRONT_LOG(INFO) << LOG_DESC("register module") << LOG_KV("moduleID", module.first);
    }

    return;
}
void FrontService::stop()
{
    if (!m_run)
    {
        return;
    }

    m_run = false;

    try
    {
        {
            RecursiveGuard l(x_callback);
            for (auto& callback : m_callback)
            {
                FRONT_LOG(INFO) << LOG_DESC("FrontService stopped, erase the callback")
                                << LOG_KV("uuid", callback.first);
                // cancel the timer
                if (callback.second->timeoutHandler)
                {
                    callback.second->timeoutHandler->cancel();
                }
            }
            // clear the callback
            m_callback.clear();
        }

        if (m_ioService)
        {
            m_ioService->stop();
        }

        if (m_threadPool)
        {
            m_threadPool->stop();
        }

        if (m_frontServiceThread && m_frontServiceThread->joinable())
        {
            m_frontServiceThread->join();
        }
    }
    catch (const std::exception& e)
    {
        FRONT_LOG(ERROR) << LOG_DESC("FrontService stop")
                         << LOG_KV("error", boost::diagnostic_information(e));
    }

    FRONT_LOG(INFO) << LOG_DESC("FrontService stop")
                    << LOG_KV("nodeID", (m_nodeID ? m_nodeID->hex() : ""))
                    << LOG_KV("groupID", m_groupID);

    return;
}

/**
 * @brief: get nodeIDs from frontservice
 * @param _onGetGroupNodeInfo: response callback
 * @return void
 */
void FrontService::asyncGetGroupNodeInfo(GetGroupNodeInfoFunc _onGetGroupNodeInfo)
{
    bcos::gateway::GroupNodeInfo::Ptr groupNodeInfo;
    {
        Guard l(x_groupNodeInfo);
        groupNodeInfo = m_groupNodeInfo;
    }

    if (_onGetGroupNodeInfo)
    {
        if (m_threadPool)
        {
            m_threadPool->enqueue([_onGetGroupNodeInfo, groupNodeInfo]() {
                _onGetGroupNodeInfo(nullptr, groupNodeInfo);
            });
        }
        else
        {
            _onGetGroupNodeInfo(nullptr, groupNodeInfo);
        }
    }

    FRONT_LOG(INFO) << LOG_DESC("asyncGetGroupNodeInfo")
                    << LOG_KV("nodeIDs.size()",
                           (groupNodeInfo ? groupNodeInfo->nodeIDList().size() : 0));

    return;
}

/**
 * @brief: send message
 * @param _moduleID: moduleID
 * @param _nodeID: the receiver nodeID
 * @param _data: send message data
 * @param _timeout: timeout, in milliseconds.
 * @param _callbackFunc: callback
 * @return void
 */
void FrontService::asyncSendMessageByNodeID(int _moduleID, bcos::crypto::NodeIDPtr _nodeID,
    bytesConstRef _data, uint32_t _timeout, CallbackFunc _callbackFunc)
{
    try
    {
        static thread_local auto uuid_gen =
            boost::uuids::basic_random_generator<std::random_device>();
        std::string uuid = boost::uuids::to_string(uuid_gen());
        if (_callbackFunc)
        {
            auto callback = std::make_shared<Callback>();
            callback->callbackFunc = _callbackFunc;

            if (_timeout > 0)
            {
                // create new timer to handle timeout
                auto timeoutHandler = std::make_shared<boost::asio::deadline_timer>(
                    *m_ioService, boost::posix_time::milliseconds(_timeout));

                callback->timeoutHandler = timeoutHandler;
                auto frontServiceWeakPtr = std::weak_ptr<FrontService>(shared_from_this());
                // callback->startTime = utcSteadyTime();
                timeoutHandler->async_wait(
                    [frontServiceWeakPtr, _nodeID, uuid](const boost::system::error_code& e) {
                        auto frontService = frontServiceWeakPtr.lock();
                        if (frontService)
                        {
                            frontService->onMessageTimeout(e, _nodeID, uuid);
                        }
                    });
            }

            addCallback(uuid, callback);

            FRONT_LOG(DEBUG) << LOG_DESC("asyncSendMessageByNodeID") << LOG_KV("groupID", m_groupID)
                             << LOG_KV("moduleID", _moduleID) << LOG_KV("uuid", uuid)
                             << LOG_KV("nodeID", _nodeID->hex())
                             << LOG_KV("data.size()", _data.size()) << LOG_KV("timeout", _timeout);
        }  // if (_callback)

        sendMessage(_moduleID, _nodeID, uuid, _data, false,
            [this, _moduleID, _nodeID, uuid](Error::Ptr _error) {
                if (_error && (_error->errorCode() != CommonError::SUCCESS))
                {
                    /*
                    FRONT_LOG(ERROR) << LOG_BADGE("sendMessage callback") << LOG_KV("uuid", uuid)
                                     << LOG_KV("errorCode", _error->errorCode())
                                     << LOG_KV("errorMessage", _error->errorMessage());
                */
                    handleCallback(_error, bytesConstRef(), uuid, _moduleID, _nodeID);
                }
            });
    }
    catch (std::exception& e)
    {
        FRONT_LOG(ERROR) << LOG_BADGE("asyncSendMessageByNodeID")
                         << LOG_KV("error", boost::diagnostic_information(e));
    }
}

/**
 * @brief: send response
 * @param _id: the request uuid
 * @param _data: message
 * @return void
 */
void FrontService::asyncSendResponse(const std::string& _id, int _moduleID,
    bcos::crypto::NodeIDPtr _nodeID, bytesConstRef _data, ReceiveMsgFunc _receiveMsgCallback)
{
    sendMessage(_moduleID, _nodeID, _id, _data, true, _receiveMsgCallback);
}

/**
 * @brief: send message to multiple nodes
 * @param _moduleID: moduleID
 * @param _nodeIDs: the receiver nodeIDs
 * @param _data: send message data
 * @return void
 */
void FrontService::asyncSendMessageByNodeIDs(
    int _moduleID, const crypto::NodeIDs& _nodeIDs, bytesConstRef _data)
{
    for (const auto& _nodeID : _nodeIDs)
    {
        asyncSendMessageByNodeID(_moduleID, _nodeID, _data, 0, CallbackFunc());
    }
}

/**
 * @brief: send broadcast message
 * @param _moduleID: moduleID
 * @param _data: send message data
 * @return void
 */
void FrontService::asyncSendBroadcastMessage(uint16_t _type, int _moduleID, bytesConstRef _data)
{
    auto message = messageFactory()->buildMessage();
    message->setModuleID(_moduleID);
    message->setPayload(_data);

    auto buffer = std::make_shared<bytes>();
    message->encode(*buffer.get());

    m_gatewayInterface->asyncSendBroadcastMessage(
        _type, m_groupID, _moduleID, m_nodeID, bytesConstRef(buffer->data(), buffer->size()));
}

/**
 * @brief: receive nodeIDs from gateway
 * @param _groupID: groupID
 * @param _groupNodeInfo: nodeIDs pushed by gateway
 * @param _receiveMsgCallback: response callback
 * @return void
 */
void FrontService::onReceiveGroupNodeInfo(const std::string& _groupID,
    bcos::gateway::GroupNodeInfo::Ptr _groupNodeInfo, ReceiveMsgFunc _receiveMsgCallback)
{
    {
        protocolNegotiate(_groupNodeInfo);
        Guard l(x_groupNodeInfo);
        m_groupNodeInfo = _groupNodeInfo;
    }
    // To be considered: How to ensure orderly notifications in the pro/max mode
    FRONT_LOG(INFO) << LOG_DESC("onReceiveGroupNodeInfo") << LOG_KV("groupID", _groupID)
                    << LOG_KV("nodeIDs.size()",
                           (_groupNodeInfo ? _groupNodeInfo->nodeIDList().size() : 0));

    if (m_threadPool)
    {
        auto self = std::weak_ptr<FrontService>(shared_from_this());
        m_threadPool->enqueue([self, _groupID, _groupNodeInfo]() {
            auto front = self.lock();
            if (!front)
            {
                return;
            }
            front->notifyGroupNodeInfo(_groupID, _groupNodeInfo);
        });
    }
    else
    {
        notifyGroupNodeInfo(_groupID, _groupNodeInfo);
    }

    if (_receiveMsgCallback)
    {
        _receiveMsgCallback(nullptr);
    }
}

void FrontService::protocolNegotiate(bcos::gateway::GroupNodeInfo::Ptr _groupNodeInfo)
{
    auto const& protocolList = _groupNodeInfo->nodeProtocolList();
    auto const& nodeIDList = _groupNodeInfo->nodeIDList();
    size_t i = 0;
    for (auto const& protocol : protocolList)
    {
        auto mutableProtocol = std::const_pointer_cast<ProtocolInfo>(protocol);
        // negotiate failed: can't happen unless the code has a bug
        if (mutableProtocol->minVersion() > m_localProtocol->maxVersion() ||
            mutableProtocol->maxVersion() < m_localProtocol->minVersion())
        {
            FRONT_LOG(ERROR) << LOG_DESC("protocolNegotiate failed")
                             << LOG_KV("nodeID", nodeIDList.at(i))
                             << LOG_KV("groupID", _groupNodeInfo->groupID())
                             << LOG_KV("minVersion", mutableProtocol->minVersion())
                             << LOG_KV("maxVersion", mutableProtocol->maxVersion())
                             << LOG_KV("supportedMinVersion", m_localProtocol->minVersion())
                             << LOG_KV("supportedMaxVersion", m_localProtocol->maxVersion());
            mutableProtocol->setVersion(ProtocolVersion::V0);
            i++;
            continue;
        }
        // set the negotiated version
        auto version = std::min(m_localProtocol->maxVersion(), mutableProtocol->maxVersion());
        mutableProtocol->setVersion((ProtocolVersion)version);
        FRONT_LOG(INFO) << LOG_DESC("protocolNegotiate success")
                        << LOG_KV("nodeID", nodeIDList.at(i))
                        << LOG_KV("groupID", _groupNodeInfo->groupID())
                        << LOG_KV("minVersion", mutableProtocol->minVersion())
                        << LOG_KV("maxVersion", mutableProtocol->maxVersion())
                        << LOG_KV("supportedMinVersion", m_localProtocol->minVersion())
                        << LOG_KV("supportedMaxVersion", m_localProtocol->maxVersion())
                        << LOG_KV("version", version);
        i++;
    }
}

void FrontService::notifyGroupNodeInfo(
    const std::string& _groupID, bcos::gateway::GroupNodeInfo::Ptr _groupNodeInfo)
{
    Guard l(x_notifierLock);
    for (const auto& entry : m_module2GroupNodeInfoNotifier)
    {
        auto moduleID = entry.first;
        entry.second(_groupNodeInfo, [_groupID, moduleID](Error::Ptr _error) {
            if (_error)
            {
                FRONT_LOG(ERROR) << LOG_DESC("onReceiveGroupNodeInfo dispather failed")
                                 << LOG_KV("groupID", _groupID) << LOG_KV("moduleID", moduleID);
            }
        });
    }
}

void FrontService::handleCallback(bcos::Error::Ptr _error, bytesConstRef _payLoad,
    std::string const& _uuid, int _moduleID, bcos::crypto::NodeIDPtr _nodeID)
{
    // callback message
    auto callback = getAndRemoveCallback(_uuid);
    if (!callback)
    {
        return;
    }
    auto frontServiceWeakPtr = std::weak_ptr<FrontService>(shared_from_this());
    auto respFunc = [frontServiceWeakPtr, _moduleID, _nodeID, _uuid](bytesConstRef _data) {
        auto frontService = frontServiceWeakPtr.lock();
        if (frontService)
        {
            frontService->sendMessage(
                _moduleID, _nodeID, _uuid, _data, true, [_uuid](Error::Ptr _error) {
                    if (_error && (_error->errorCode() != CommonError::SUCCESS))
                    {
                        FRONT_LOG(ERROR)
                            << LOG_BADGE("onReceiveMessage sendMessage callback")
                            << LOG_KV("uuid", _uuid) << LOG_KV("errorCode", _error->errorCode())
                            << LOG_KV("errorMessage", _error->errorMessage());
                    }
                });
        }
    };
    // cancel the timer first
    if (callback->timeoutHandler)
    {
        callback->timeoutHandler->cancel();
    }

    if (m_threadPool)
    {
        // construct shared_ptr<bytes> from message->payload() first for
        // thead safe
        std::shared_ptr<bytes> buffer = std::make_shared<bytes>(_payLoad.begin(), _payLoad.end());
        m_threadPool->enqueue([_uuid, _error, callback, buffer, _nodeID, respFunc] {
            callback->callbackFunc(
                _error, _nodeID, bytesConstRef(buffer->data(), buffer->size()), _uuid, respFunc);
        });
    }
    else
    {
        callback->callbackFunc(_error, _nodeID, _payLoad, _uuid, respFunc);
    }
}
/**
 * @brief: receive message from gateway
 * @param _groupID: groupID
 * @param _nodeID: the node send the message
 * @param _data: received message data
 * @param _receiveMsgCallback: response callback
 * @return void
 */
void FrontService::onReceiveMessage(const std::string& _groupID, bcos::crypto::NodeIDPtr _nodeID,
    bytesConstRef _data, ReceiveMsgFunc _receiveMsgCallback)
{
    try
    {
        auto message = messageFactory()->buildMessage();
        auto ret = message->decode(_data);
        if (MessageDecodeStatus::MESSAGE_COMPLETE != ret)
        {
            FRONT_LOG(ERROR) << LOG_DESC("onReceiveMessage") << LOG_DESC("illegal message")
                             << LOG_KV("length", _data.size()) << LOG_KV("nodeID", m_nodeID->hex());
            BOOST_THROW_EXCEPTION(InvalidParameter() << errinfo_comment("illegal message"));
        }

        int moduleID = message->moduleID();
        int ext = message->ext();
        std::string uuid = std::string(message->uuid()->begin(), message->uuid()->end());

        FRONT_LOG(TRACE) << LOG_BADGE("onReceiveMessage") << LOG_KV("moduleID", moduleID)
                         << LOG_KV("uuid", uuid) << LOG_KV("ext", ext)
                         << LOG_KV("groupID", _groupID) << LOG_KV("nodeID", _nodeID->hex())
                         << LOG_KV("length", _data.size());

        if (message->isResponse())
        {
            handleCallback(nullptr, message->payload(), uuid, moduleID, _nodeID);
        }
        else
        {
            auto it = m_moduleID2MessageDispatcher.find(moduleID);
            if (it != m_moduleID2MessageDispatcher.end())
            {
                if (m_threadPool)
                {
                    auto callback = it->second;
                    // construct shared_ptr<bytes> from message->payload() first for
                    // thead safe
                    std::shared_ptr<bytes> buffer = std::make_shared<bytes>(
                        message->payload().begin(), message->payload().end());
                    m_threadPool->enqueue([uuid, callback, buffer, message, _nodeID] {
                        callback(_nodeID, uuid, bytesConstRef(buffer->data(), buffer->size()));
                    });
                }
                else
                {
                    it->second(_nodeID, uuid, message->payload());
                }
            }
            else
            {
                FRONT_LOG(WARNING) << LOG_DESC("unable find the register module message dispather")
                                   << LOG_KV("moduleID", moduleID) << LOG_KV("uuid", uuid);
            }
        }
    }
    catch (const std::exception& e)
    {
        FRONT_LOG(ERROR) << "onReceiveMessage" << LOG_KV("error", boost::diagnostic_information(e));
    }

    if (_receiveMsgCallback)
    {
        if (m_threadPool)
        {
            m_threadPool->enqueue([_receiveMsgCallback]() { _receiveMsgCallback(nullptr); });
        }
        else
        {
            _receiveMsgCallback(nullptr);
        }
    }
}

/**
 * @brief: receive broadcast message from gateway
 * @param _groupID: groupID
 * @param _nodeID: the node send the message
 * @param _data: received message data
 * @param _receiveMsgCallback: response callback
 * @return void
 */
void FrontService::onReceiveBroadcastMessage(const std::string& _groupID,
    bcos::crypto::NodeIDPtr _nodeID, bytesConstRef _data, ReceiveMsgFunc _receiveMsgCallback)
{
    onReceiveMessage(_groupID, _nodeID, _data, _receiveMsgCallback);
}

/**
 * @brief: send message
 * @param _moduleID: moduleID
 * @param _nodeID: the node the message sent to
 * @param _uuid: uuid identify this message
 * @param _data: send data payload
 * @param isResponse: if send response message
 * @param _receiveMsgCallback: response callback
 * @return void
 */
void FrontService::sendMessage(int _moduleID, bcos::crypto::NodeIDPtr _nodeID,
    const std::string& _uuid, bytesConstRef _data, bool isResponse,
    ReceiveMsgFunc _receiveMsgCallback)
{
    auto message = messageFactory()->buildMessage();
    message->setModuleID(_moduleID);
    message->setUuid(std::make_shared<bytes>(_uuid.begin(), _uuid.end()));
    message->setPayload(_data);
    if (isResponse)
    {
        message->setResponse();
    }

    auto buffer = std::make_shared<bytes>();
    message->encode(*buffer.get());

    // call gateway interface to send the message
    m_gatewayInterface->asyncSendMessageByNodeID(m_groupID, _moduleID, m_nodeID, _nodeID,
        bytesConstRef(buffer->data(), buffer->size()), [_receiveMsgCallback](Error::Ptr _error) {
            if (_receiveMsgCallback)
            {
                _receiveMsgCallback(_error);
            }
        });
}

/**
 * @brief: handle message timeout
 * @param _error: boost error code
 * @param _uuid: message uuid
 * @return void
 */
void FrontService::onMessageTimeout(const boost::system::error_code& _error,
    bcos::crypto::NodeIDPtr _nodeID, const std::string& _uuid)
{
    if (_error)
    {
        return;
    }

    try
    {
        Callback::Ptr callback = getAndRemoveCallback(_uuid);
        if (callback)
        {
            auto errorPtr = std::make_shared<Error>(CommonError::TIMEOUT, "timeout");
            if (m_threadPool)
            {
                m_threadPool->enqueue([_uuid, _nodeID, callback, errorPtr]() {
                    callback->callbackFunc(errorPtr, _nodeID, bytesConstRef(), _uuid,
                        std::function<void(bytesConstRef)>());
                });
            }
            else
            {
                callback->callbackFunc(errorPtr, _nodeID, bytesConstRef(), _uuid,
                    std::function<void(bytesConstRef)>());
            }
        }

        FRONT_LOG(WARNING) << LOG_BADGE("onMessageTimeout") << LOG_KV("uuid", _uuid);
    }
    catch (std::exception& e)
    {
        FRONT_LOG(ERROR) << "onMessageTimeout" << LOG_KV("uuid", _uuid)
                         << LOG_KV("error", boost::diagnostic_information(e));
    }
}
