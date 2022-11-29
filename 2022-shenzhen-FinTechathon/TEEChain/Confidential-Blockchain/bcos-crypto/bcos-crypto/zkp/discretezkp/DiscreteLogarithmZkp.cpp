/**
 *  Copyright (C) 2022 FISCO BCOS.
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
 * @brief implementation for discrete-logarithm-proof
 * @file DiscreteLogarithmZkp.cpp
 * @date 2022.01.17
 * @author yujiechen
 */
#include "DiscreteLogarithmZkp.h"
#include "bcos-crypto/zkp/Common.h"

using namespace bcos;
using namespace bcos::crypto;

// wedpr_verify_either_equality_relationship_proof
bool DiscreteLogarithmZkp::verifyEitherEqualityProof(bytes const& c1PointData,
    bytes const& c2PointData, bytes const& c3PointData, bytes const& equalityProofData,
    bytes const& basePointData, bytes const& blindingBasePointData)
{
    auto c1Point = bytesToInputBuffer(c1PointData, m_pointLen);
    auto c2Point = bytesToInputBuffer(c2PointData, m_pointLen);
    auto c3Point = bytesToInputBuffer(c3PointData, m_pointLen);
    CInputBuffer proof{(const char*)equalityProofData.data(), equalityProofData.size()};
    auto basePoint = bytesToInputBuffer(basePointData, m_pointLen);
    auto blindingBasePoint = bytesToInputBuffer(blindingBasePointData, m_pointLen);
    auto ret = wedpr_verify_either_equality_relationship_proof(
        &c1Point, &c2Point, &c3Point, &proof, &basePoint, &blindingBasePoint);
    if (ret == WEDPR_SUCCESS)
    {
        return true;
    }
    return false;
}

// wedpr_verify_knowledge_proof
bool DiscreteLogarithmZkp::verifyKnowledgeProof(bytes const& pointData,
    bytes const& knowledgeProofData, bytes const& basePointData, bytes const& blindingBasePointData)
{
    auto point = bytesToInputBuffer(pointData, m_pointLen);
    CInputBuffer proof{(const char*)knowledgeProofData.data(), knowledgeProofData.size()};
    auto basePoint = bytesToInputBuffer(basePointData, m_pointLen);
    auto blindingBasePoint = bytesToInputBuffer(blindingBasePointData, m_pointLen);

    auto ret = wedpr_verify_knowledge_proof(&point, &proof, &basePoint, &blindingBasePoint);
    if (ret == WEDPR_SUCCESS)
    {
        return true;
    }
    return false;
}

// wedpr_verify_format_proof
bool DiscreteLogarithmZkp::verifyFormatProof(bytes const& c1Point, bytes const& c2Point,
    bytes const& formatProofData, bytes const& c1BasePointData, bytes const& c2BasePointData,
    bytes const& blindingBasePointData)
{
    auto c1 = bytesToInputBuffer(c1Point, m_pointLen);
    auto c2 = bytesToInputBuffer(c2Point, m_pointLen);
    CInputBuffer proof{(const char*)formatProofData.data(), formatProofData.size()};
    auto c1BasePoint = bytesToInputBuffer(c1BasePointData, m_pointLen);
    auto c2BasePoint = bytesToInputBuffer(c2BasePointData, m_pointLen);
    auto blindingBasePoint = bytesToInputBuffer(blindingBasePointData, m_pointLen);
    auto ret =
        wedpr_verify_format_proof(&c1, &c2, &proof, &c1BasePoint, &c2BasePoint, &blindingBasePoint);
    if (ret == WEDPR_SUCCESS)
    {
        return true;
    }
    return false;
}

// wedpr_verify_sum_relationship
bool DiscreteLogarithmZkp::verifySumProof(bytes const& c1Point, bytes const& c2Point,
    bytes const& c3Point, bytes const& arithmeticProofData, bytes const& valueBasePointData,
    bytes const& blindingBasePointData)
{
    auto c1 = bytesToInputBuffer(c1Point, m_pointLen);
    auto c2 = bytesToInputBuffer(c2Point, m_pointLen);
    auto c3 = bytesToInputBuffer(c3Point, m_pointLen);
    CInputBuffer proof{(const char*)arithmeticProofData.data(), arithmeticProofData.size()};
    auto valueBasePoint = bytesToInputBuffer(valueBasePointData, m_pointLen);
    auto blindingBasePoint = bytesToInputBuffer(blindingBasePointData, m_pointLen);
    auto ret =
        wedpr_verify_sum_relationship(&c1, &c2, &c3, &proof, &valueBasePoint, &blindingBasePoint);
    if (ret == WEDPR_SUCCESS)
    {
        return true;
    }
    return false;
}

// wedpr_verify_product_relationship
bool DiscreteLogarithmZkp::verifyProductProof(bytes const& c1Point, bytes const& c2Point,
    bytes const& c3Point, bytes const& arithmeticProofData, bytes const& valueBasePointData,
    bytes const& blindingBasePointData)
{
    auto c1 = bytesToInputBuffer(c1Point, m_pointLen);
    auto c2 = bytesToInputBuffer(c2Point, m_pointLen);
    auto c3 = bytesToInputBuffer(c3Point, m_pointLen);
    CInputBuffer proof{(const char*)arithmeticProofData.data(), arithmeticProofData.size()};
    auto valueBasePoint = bytesToInputBuffer(valueBasePointData, m_pointLen);
    auto blindingBasePoint = bytesToInputBuffer(blindingBasePointData, m_pointLen);
    auto ret = wedpr_verify_product_relationship(
        &c1, &c2, &c3, &proof, &valueBasePoint, &blindingBasePoint);
    if (ret == WEDPR_SUCCESS)
    {
        return true;
    }
    return false;
}

// wedpr_verify_equality_relationship_proof
bool DiscreteLogarithmZkp::verifyEqualityProof(bytes const& c1Point, bytes const c2Point,
    bytes const& equalityProofData, bytes const& basePoint1Data, bytes const& basePoint2Data)
{
    auto c1 = bytesToInputBuffer(c1Point, m_pointLen);
    auto c2 = bytesToInputBuffer(c2Point, m_pointLen);
    CInputBuffer proof{(const char*)equalityProofData.data(), equalityProofData.size()};

    auto basePoint1 = bytesToInputBuffer(basePoint1Data, m_pointLen);
    auto basePoint2 = bytesToInputBuffer(basePoint2Data, m_pointLen);
    auto ret = wedpr_verify_equality_relationship_proof(&c1, &c2, &proof, &basePoint1, &basePoint2);
    if (ret == WEDPR_SUCCESS)
    {
        return true;
    }
    return false;
}

// wedpr_aggregate_ristretto_point
bytes DiscreteLogarithmZkp::aggregateRistrettoPoint(bytes const& pointSum, bytes const& pointShare)
{
    // with empty pointShare to aggregate
    if (pointShare.size() == 0)
    {
        return pointSum;
    }
    auto pointSumInput = bytesToInputBuffer(pointSum, m_pointLen);
    auto pointShareInput = bytesToInputBuffer(pointShare, m_pointLen);
    bytes aggregatedResult;
    aggregatedResult.resize(m_pointLen);
    COutputBuffer result{(char*)aggregatedResult.data(), m_pointLen};
    auto ret = wedpr_aggregate_ristretto_point(&pointSumInput, &pointShareInput, &result);
    if (ret == WEDPR_SUCCESS)
    {
        return aggregatedResult;
    }
    BOOST_THROW_EXCEPTION(ZkpExcetpion() << errinfo_comment("aggregateRistrettoPoint error"));
}
