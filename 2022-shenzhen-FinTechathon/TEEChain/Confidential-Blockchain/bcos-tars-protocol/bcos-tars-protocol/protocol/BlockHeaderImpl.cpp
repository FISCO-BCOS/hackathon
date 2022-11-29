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
 * @brief implementation for BlockHeader
 * @file BlockHeaderImpl.cpp
 * @author: ancelmo
 * @date 2021-04-20
 */

#include "../impl/TarsHashable.h"

#include "BlockHeaderImpl.h"
#include <bcos-concepts/Hash.h>
#include <bcos-utilities/Common.h>
#include <boost/endian/conversion.hpp>

using namespace bcostars;
using namespace bcostars::protocol;
void BlockHeaderImpl::decode(bcos::bytesConstRef _data)
{
    tars::TarsInputStream<tars::BufferReader> input;
    input.setBuffer((const char*)_data.data(), _data.size());

    m_inner()->readFrom(input);
}

void BlockHeaderImpl::encode(bcos::bytes& _encodeData) const
{
    tars::TarsOutputStream<bcostars::protocol::BufferWriterByteVector> output;

    m_inner()->writeTo(output);
    output.getByteBuffer().swap(_encodeData);
}


bcos::crypto::HashType BlockHeaderImpl::hash() const
{
    bcos::UpgradableGuard l(x_inner);
    if (!m_inner()->dataHash.empty())
    {
        return *(reinterpret_cast<const bcos::crypto::HashType*>(m_inner()->dataHash.data()));
    }
    auto hashImpl = m_cryptoSuite->hashImpl();
    auto anyHasher = hashImpl->hasher();

    bcos::crypto::HashType hashResult;
    std::visit(
        [this, &hashResult, &l](auto& hasher) {
            using Hasher = std::remove_cvref_t<decltype(hasher)>;
            bcos::concepts::hash::calculate<Hasher>(*m_inner(), hashResult);

            bcos::UpgradeGuard ul(l);
            m_inner()->dataHash.assign(hashResult.begin(), hashResult.end());
        },
        anyHasher);
    return hashResult;
}

void BlockHeaderImpl::clear()
{
    m_inner()->resetDefautlt();
    m_parentInfo.clear();
}

gsl::span<const bcos::protocol::ParentInfo> BlockHeaderImpl::parentInfo() const
{
    bcos::ReadGuard l(x_inner);
    if (m_parentInfo.empty())
    {
        for (auto const& it : m_inner()->data.parentInfo)
        {
            bcos::protocol::ParentInfo parentInfo;
            parentInfo.blockNumber = it.blockNumber;
            parentInfo.blockHash =
                *(reinterpret_cast<const bcos::crypto::HashType*>(it.blockHash.data()));
            m_parentInfo.emplace_back(parentInfo);
        }
    }

    return gsl::span(m_parentInfo.data(), m_parentInfo.size());
}

bcos::crypto::HashType BlockHeaderImpl::txsRoot() const
{
    if (!m_inner()->data.txsRoot.empty())
    {
        return *(reinterpret_cast<const bcos::crypto::HashType*>(m_inner()->data.txsRoot.data()));
    }
    return {};
}

bcos::crypto::HashType BlockHeaderImpl::stateRoot() const
{
    if (!m_inner()->data.stateRoot.empty())
    {
        return *(reinterpret_cast<const bcos::crypto::HashType*>(m_inner()->data.stateRoot.data()));
    }
    return {};
}

bcos::crypto::HashType BlockHeaderImpl::receiptsRoot() const
{
    if (!m_inner()->data.receiptRoot.empty())
    {
        return *(
            reinterpret_cast<const bcos::crypto::HashType*>(m_inner()->data.receiptRoot.data()));
    }
    return {};
}

bcos::u256 BlockHeaderImpl::gasUsed() const
{
    if (!m_inner()->data.gasUsed.empty())
    {
        return boost::lexical_cast<bcos::u256>(m_inner()->data.gasUsed);
    }
    return {};
}

void BlockHeaderImpl::setParentInfo(gsl::span<const bcos::protocol::ParentInfo> const& _parentInfo)
{
    bcos::WriteGuard l(x_inner);
    m_parentInfo.clear();
    m_inner()->data.parentInfo.clear();
    for (auto& it : _parentInfo)
    {
        ParentInfo parentInfo;
        parentInfo.blockNumber = it.blockNumber;
        parentInfo.blockHash.assign(it.blockHash.begin(), it.blockHash.end());
        m_inner()->data.parentInfo.emplace_back(parentInfo);
    }
    clearDataHash();
}

void BlockHeaderImpl::setSealerList(gsl::span<const bcos::bytes> const& _sealerList)
{
    m_inner()->data.sealerList.clear();
    for (auto const& it : _sealerList)
    {
        m_inner()->data.sealerList.push_back(std::vector<char>(it.begin(), it.end()));
    }
    clearDataHash();
}

void BlockHeaderImpl::setSignatureList(
    gsl::span<const bcos::protocol::Signature> const& _signatureList)
{
    bcos::WriteGuard l(x_inner);
    // Note: must clear the old signatureList when set the new signatureList
    // in case of the consensus module get the cached-sync-blockHeader with signatureList which will
    // cause redundant signature lists to be stored
    m_inner()->signatureList.clear();
    for (auto& it : _signatureList)
    {
        Signature signature;
        signature.sealerIndex = it.index;
        signature.signature.assign(it.signature.begin(), it.signature.end());
        m_inner()->signatureList.emplace_back(signature);
    }
}