#pragma once
#pragma GCC diagnostic ignored "-Wunused-variable"
#pragma GCC diagnostic ignored "-Wunused-parameter"

#include "RpcInitializer.h"
#include <bcos-tars-protocol/tars/RpcService.h>
namespace bcostars
{
struct RpcServiceParam
{
    RpcInitializer::Ptr rpcInitializer;
};
class RpcServiceServer : public bcostars::RpcService
{
public:
    RpcServiceServer(RpcServiceParam const& _param) : m_rpcInitializer(_param.rpcInitializer) {}
    virtual ~RpcServiceServer() {}

    void initialize() override {}
    void destroy() override {}

    bcostars::Error asyncNotifyBlockNumber(const std::string& _groupID,
        const std::string& _nodeName, tars::Int64 blockNumber,
        tars::TarsCurrentPtr current) override;
    bcostars::Error asyncNotifyGroupInfo(
        const bcostars::GroupInfo& groupInfo, tars::TarsCurrentPtr current) override;

    bcostars::Error asyncNotifyAMOPMessage(tars::Int32 _type, const std::string& _topic,
        const vector<tars::Char>& _requestData, vector<tars::Char>& _responseData,
        tars::TarsCurrentPtr current) override;
    bcostars::Error asyncNotifySubscribeTopic(
        std::string& _topicInfo, tars::TarsCurrentPtr current) override;

private:
    RpcInitializer::Ptr m_rpcInitializer;
};
}  // namespace bcostars