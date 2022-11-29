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
 * @brief implementation for SM2KeyPair
 * @file SM2KeyPair.h
 * @date 2021.03.10
 * @author yujiechen
 */
#pragma once
#include <bcos-crypto/interfaces/crypto/Signature.h>
#include <bcos-crypto/signature/key/KeyPair.h>
#include <wedpr-crypto/WedprCrypto.h>

namespace bcos
{
namespace crypto
{
const int SM2_PRIVATE_KEY_LEN = 32;
const int SM2_PUBLIC_KEY_LEN = 64;
PublicPtr sm2PriToPub(SecretPtr _secret);
class SM2KeyPair : public KeyPair
{
public:
    SM2KeyPair() : KeyPair(SM2_PUBLIC_KEY_LEN, SM2_PRIVATE_KEY_LEN, KeyPairType::SM2)
    {
        m_publicKeyDeriver = wedpr_sm2_derive_public_key;
    }
    explicit SM2KeyPair(SecretPtr _secretKey);
    ~SM2KeyPair() override {}
    virtual PublicPtr priToPub(SecretPtr _secretKey);

protected:
    std::function<int8_t(const CInputBuffer* private_key, COutputBuffer* output_public_key)>
        m_publicKeyDeriver;
};
}  // namespace crypto
}  // namespace bcos