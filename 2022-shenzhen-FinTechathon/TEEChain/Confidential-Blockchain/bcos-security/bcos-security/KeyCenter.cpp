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
 */
/**
 * @brief : keycenter for disk encrytion
 * @author: jimmyshi
 * @date: 2018-12-03
 */

#include "KeyCenter.h"
#include <bcos-crypto/hash/Keccak256.h>
#include <bcos-crypto/hash/SM3.h>
#include <bcos-utilities/DataConvertUtility.h>
#include <bcos-utilities/Exceptions.h>
#include <json/json.h>
#include <iostream>
#include <string>

using namespace std;
using namespace bcos;
using namespace crypto;
using namespace security;

namespace beast = boost::beast;  // from <boost/beast.hpp>
namespace http = beast::http;    // from <boost/beast/http.hpp>
namespace net = boost::asio;     // from <boost/asio.hpp>
using tcp = net::ip::tcp;        // from <boost/asio/ip/tcp.hpp>

KeyCenterHttpClient::KeyCenterHttpClient(const string& _ip, int _port)
  : KeyCenterHttpClientInterface(), m_ip(_ip), m_port(_port), m_ioc(), m_socket(m_ioc)
{}

KeyCenterHttpClient::~KeyCenterHttpClient()
{
    close();
}

void KeyCenterHttpClient::connect()
{
    WriteGuard l(x_clinetSocket);
    try
    {
        if (m_socket.is_open())
            return;
        // These objects perform our I/O
        tcp::resolver resolver{m_ioc};
        // Look up the domain name
        auto const results = resolver.resolve(m_ip, to_string(m_port).c_str());

        // Make the connection on the IP address we get from a lookup
        // TODO Add timeout in connect and read write
        net::connect(m_socket, results.begin(), results.end());
    }
    catch (exception& e)
    {
        KC_LOG(DEBUG) << LOG_DESC("Init key manager failed.") << LOG_KV("reason", e.what()) << endl;
        BOOST_THROW_EXCEPTION(KeyCenterInitError());
    }
}

void KeyCenterHttpClient::close()
{
    WriteGuard l(x_clinetSocket);
    if (!m_socket.is_open())
        return;
    // Gracefully close the socket
    beast::error_code ec;
    m_socket.shutdown(tcp::socket::shutdown_both, ec);

    if (ec && ec != beast::errc::not_connected)
    {
        KC_LOG(DEBUG) << LOG_DESC("Close key manager failed.") << LOG_KV("error_code", ec) << endl;
        BOOST_THROW_EXCEPTION(KeyCenterCloseError());
    }
}

Json::Value KeyCenterHttpClient::callMethod(const string& _method, Json::Value _params)
{
    if (!m_socket.is_open())
        connect();  // Jump out immediately if has connected

    Json::Value res;
    try
    {
        /*
        query is:
            {"jsonrpc":"2.0","method":"encDataKey","params":["123456"],"id":83}
        */

        Json::Value queryJson;
        queryJson["id"] = 83;
        queryJson["jsonrpc"] = "2.0";
        queryJson["method"] = _method;
        queryJson["params"] = _params;

        Json::FastWriter fastWriter;
        std::string queryJsonStr = fastWriter.write(queryJson);
        std::string url = m_ip + ":" + to_string(m_port);
        // std::cout << queryJsonStr << " length: " << queryJsonStr.length() << std::endl;

        http::request<http::string_body> req{http::verb::post, "/", 11};
        req.set(http::field::host, url.c_str());
        req.set(http::field::accept, "*/*");
        req.set(http::field::content_type, "application/json");
        req.set(http::field::accept_charset, "utf-8");

        req.body() = queryJsonStr.c_str();
        req.prepare_payload();

        // Send the HTTP request to the remote host
        http::write(m_socket, req);

        // This buffer is used for reading and must be persisted
        beast::flat_buffer buffer;

        // Declare a container to hold the response
        http::response<http::string_body> rsp;

        // Receive the HTTP response
        http::read(m_socket, buffer, rsp);

        KC_LOG(DEBUG) << LOG_BADGE("callMethod") << LOG_DESC("key manager respond")
                      << LOG_KV("code", rsp.result_int());

        if (rsp.result_int() != 200)
        {
            KC_LOG(DEBUG) << LOG_BADGE("callMethod") << LOG_DESC("http error")
                          << LOG_KV("reason", rsp.result_int());
            throw;
        }

        Json::Reader reader;
        bool parsingSuccessful = reader.parse(rsp.body().c_str(), res);
        if (!parsingSuccessful)
        {
            KC_LOG(DEBUG) << LOG_BADGE("callMethod") << LOG_DESC("respond json error")
                          << LOG_KV("code", rsp.result_int()) << LOG_KV("string", rsp.body());
            throw;
        }

        return res["result"];
    }
    catch (exception& e)
    {
        KC_LOG(DEBUG) << LOG_DESC("CallMethod error") << LOG_KV("reason", e.what());
        BOOST_THROW_EXCEPTION(KeyCenterConnectionError());
    }

    return res;
}

const bytes KeyCenter::getDataKey(const std::string& _cipherDataKey, const bool isSMCrypto)
{
    if (_cipherDataKey.empty())
    {
        KC_LOG(DEBUG) << LOG_DESC("Get datakey exception. cipherDataKey is empty");
        BOOST_THROW_EXCEPTION(KeyCenterDataKeyError());
    }

    if (m_lastQueryCipherDataKey == _cipherDataKey)
    {
        return m_lastRcvDataKey;
    }

    string dataKeyBytesStr;
    try
    {
        // Create
        KeyCenterHttpClientInterface::Ptr kcclient;
        if (m_kcclient == nullptr)
        {
            kcclient = make_shared<KeyCenterHttpClient>(m_ip, m_port);
        }
        else
        {
            kcclient = m_kcclient;
        }

        // connect
        kcclient->connect();

        // send and receive
        Json::Value params(Json::arrayValue);
        params.append(_cipherDataKey);
        Json::Value rsp = kcclient->callMethod("decDataKey", params);

        // parse respond
        int error = rsp["error"].asInt();
        dataKeyBytesStr = rsp["dataKey"].asString();
        string info = rsp["info"].asString();
        if (error)
        {
            KC_LOG(DEBUG) << LOG_DESC("Get datakey exception") << LOG_KV("keycentr info", info);
            BOOST_THROW_EXCEPTION(KeyCenterConnectionError() << errinfo_comment(info));
        }

        // update query cache
        m_lastQueryCipherDataKey = _cipherDataKey;
        bytes readableDataKey = fromHex(dataKeyBytesStr);
        m_lastRcvDataKey = uniformDataKey(readableDataKey, isSMCrypto);

        // close
        kcclient->close();
    }
    catch (exception& e)
    {
        clearCache();
        KC_LOG(DEBUG) << LOG_DESC("Get datakey exception") << LOG_KV("reason", e.what());
        BOOST_THROW_EXCEPTION(KeyCenterConnectionError() << errinfo_comment(e.what()));
    }

    return m_lastRcvDataKey;
}

void KeyCenter::setIpPort(const std::string& _ip, int _port)
{
    m_ip = _ip;
    m_port = _port;
    m_url = m_ip + ":" + std::to_string(m_port);
    KC_LOG(DEBUG) << LOG_DESC("Set instance url") << LOG_KV("IP", m_ip) << LOG_KV("port", m_port);
}

bytes KeyCenter::uniformDataKey(const bytes& _readableDataKey, const bool isSMCrypto)
{
    if (true == isSMCrypto)
    {
        bytes oneTurn = sm3Hash(ref(_readableDataKey)).asBytes();
        return oneTurn + oneTurn + oneTurn + oneTurn;
    }
    else
    {
        bytes oneTurn = keccak256Hash(ref(_readableDataKey)).asBytes();
        return oneTurn;
    }
}