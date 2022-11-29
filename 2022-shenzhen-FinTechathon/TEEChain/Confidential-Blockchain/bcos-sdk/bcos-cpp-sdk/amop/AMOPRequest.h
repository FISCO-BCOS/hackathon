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
 * @file AMOPRequest.h
 * @author: octopus
 * @date 2021-08-23
 */
#pragma once
#include <bcos-utilities/Common.h>

namespace bcos
{
namespace protocol
{
class AMOPRequest
{
public:
    using Ptr = std::shared_ptr<AMOPRequest>;
    AMOPRequest() = default;
    AMOPRequest(bcos::bytesConstRef _data) { decode(_data); }
    virtual ~AMOPRequest() {}

    // topic field length
    const static size_t TOPIC_MAX_LENGTH = 65535;
    const static size_t MESSAGE_MIN_LENGTH = 2;


    std::string topic() const { return m_topic; }
    void setTopic(const std::string& _topic) { m_topic = _topic; }
    void setData(bcos::bytesConstRef _data) { m_data = _data; }
    bcos::bytesConstRef data() const { return m_data; }

    virtual bool encode(bcos::bytes& _buffer);
    virtual int64_t decode(bcos::bytesConstRef _data);

    virtual uint32_t version() const { return m_version; }
    virtual void setVersion(uint32_t _version) { m_version = _version; }

private:
    std::string m_topic;
    bcos::bytesConstRef m_data = bcos::bytesConstRef();
    uint32_t m_version = 0;
};

class AMOPRequestFactory
{
public:
    using Ptr = std::shared_ptr<AMOPRequestFactory>;
    AMOPRequestFactory() = default;
    virtual ~AMOPRequestFactory() {}

    std::shared_ptr<AMOPRequest> buildRequest() { return std::make_shared<AMOPRequest>(); }
    std::shared_ptr<AMOPRequest> buildRequest(bcos::bytesConstRef _data)
    {
        return std::make_shared<AMOPRequest>(_data);
    }
};

}  // namespace protocol
}  // namespace bcos
