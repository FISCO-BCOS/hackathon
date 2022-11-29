/** @file GatewayConfig.cpp
 *  @author octopus
 *  @date 2021-05-19
 */

#include "bcos-gateway/Common.h"
#include "bcos-utilities/BoostLog.h"
#include <bcos-framework/protocol/Protocol.h>
#include <bcos-gateway/GatewayConfig.h>
#include <bcos-security/bcos-security/KeyCenter.h>
#include <bcos-utilities/DataConvertUtility.h>
#include <bcos-utilities/FileUtility.h>
#include <json/json.h>
#include <boost/throw_exception.hpp>
#include <algorithm>

using namespace bcos;
using namespace security;
using namespace gateway;

bool GatewayConfig::isValidPort(int port)
{
    if (port <= 1024 || port > 65535)
        return false;
    return true;
}

// check if the ip valid
bool GatewayConfig::isValidIP(const std::string& _ip)
{
    boost::system::error_code ec;
    boost::asio::ip::address ip_address = boost::asio::ip::make_address(_ip, ec);
    std::ignore = ip_address;
    return ec.value() == 0;
}

// MB to bit
int64_t GatewayConfig::doubleMBToBit(double _d)
{
    _d *= (1024 * 1024 / 8);

    return (int64_t)_d;
}

void GatewayConfig::hostAndPort2Endpoint(const std::string& _host, NodeIPEndpoint& _endpoint)
{
    std::string ip;
    uint16_t port;

    std::vector<std::string> s;
    boost::split(s, _host, boost::is_any_of("]"), boost::token_compress_on);
    if (s.size() == 2)
    {  // ipv6
        ip = s[0].data() + 1;
        port = boost::lexical_cast<int>(s[1].data() + 1);
    }
    else if (s.size() == 1)
    {  // ipv4
        std::vector<std::string> v;
        boost::split(v, _host, boost::is_any_of(":"), boost::token_compress_on);
        if (v.size() < 2)
        {
            BOOST_THROW_EXCEPTION(InvalidParameter() << errinfo_comment(
                                      "GatewayConfig: invalid host , host=" + _host));
        }
        ip = v[0];
        port = boost::lexical_cast<int>(v[1]);
    }
    else
    {
        GATEWAY_CONFIG_LOG(ERROR) << LOG_DESC("not valid host value") << LOG_KV("host", _host);
        BOOST_THROW_EXCEPTION(InvalidParameter() << errinfo_comment(
                                  "GatewayConfig: the host is invalid, host=" + _host));
    }

    if (!isValidPort(port))
    {
        GATEWAY_CONFIG_LOG(ERROR) << LOG_DESC("the port is not valid") << LOG_KV("port", port);
        BOOST_THROW_EXCEPTION(
            InvalidParameter() << errinfo_comment(
                "GatewayConfig: the port is invalid, port=" + std::to_string(port)));
    }

    boost::system::error_code ec;
    boost::asio::ip::address ip_address = boost::asio::ip::make_address(ip, ec);
    if (ec.value() != 0)
    {
        GATEWAY_CONFIG_LOG(ERROR) << LOG_DESC("the host is invalid, make_address error")
                                  << LOG_KV("host", _host);
        BOOST_THROW_EXCEPTION(
            InvalidParameter() << errinfo_comment(
                "GatewayConfig: the host is invalid make_address error, host=" + _host));
    }

    _endpoint = NodeIPEndpoint{ip_address, port};
}

void GatewayConfig::parseConnectedJson(
    const std::string& _json, std::set<NodeIPEndpoint>& _nodeIPEndpointSet)
{
    /*
    {"nodes":["127.0.0.1:30355","127.0.0.1:30356"}]}
    */
    Json::Value root;
    Json::Reader jsonReader;

    try
    {
        if (!jsonReader.parse(_json, root))
        {
            GATEWAY_CONFIG_LOG(ERROR)
                << "unable to parse connected nodes json" << LOG_KV("json:", _json);
            BOOST_THROW_EXCEPTION(
                InvalidParameter() << errinfo_comment("GatewayConfig: unable to parse p2p "
                                                      "connected nodes json"));
        }

        std::set<NodeIPEndpoint> nodeIPEndpointSet;
        Json::Value jNodes = root["nodes"];
        if (jNodes.isArray())
        {
            unsigned int jNodesSize = jNodes.size();
            for (unsigned int i = 0; i < jNodesSize; i++)
            {
                std::string host = jNodes[i].asString();

                NodeIPEndpoint endpoint;
                hostAndPort2Endpoint(host, endpoint);
                _nodeIPEndpointSet.insert(endpoint);

                GATEWAY_CONFIG_LOG(INFO)
                    << LOG_DESC("add one connected node") << LOG_KV("host", host);
            }
        }
    }
    catch (const std::exception& e)
    {
        GATEWAY_CONFIG_LOG(ERROR) << LOG_KV(
            "parseConnectedJson error: ", boost::diagnostic_information(e));
        BOOST_THROW_EXCEPTION(e);
    }
}

/**
 * @brief: loads configuration items from the config.ini
 * @param _configPath: config.ini path
 * @return void
 */
void GatewayConfig::initConfig(std::string const& _configPath, bool _uuidRequired)
{
    try
    {
        boost::property_tree::ptree pt;
        boost::property_tree::ini_parser::read_ini(_configPath, pt);
        initP2PConfig(pt, _uuidRequired);
        initRatelimitConfig(pt);
        if (m_smSSL)
        {
            initSMCertConfig(pt);
        }
        else
        {
            initCertConfig(pt);
        }
    }
    catch (const std::exception& e)
    {
        boost::filesystem::path full_path(boost::filesystem::current_path());

        GATEWAY_CONFIG_LOG(ERROR) << LOG_KV("configPath", _configPath)
                                  << LOG_KV("currentPath", full_path.string())
                                  << LOG_KV("initConfig error: ", boost::diagnostic_information(e));

        BOOST_THROW_EXCEPTION(
            InvalidParameter() << errinfo_comment("initConfig: currentPath:" + full_path.string() +
                                                  " ,error:" + boost::diagnostic_information(e)));
    }

    GATEWAY_CONFIG_LOG(INFO) << LOG_DESC("initConfig ok!") << LOG_KV("configPath", _configPath)
                             << LOG_KV("listenIP", m_listenIP) << LOG_KV("listenPort", m_listenPort)
                             << LOG_KV("smSSL", m_smSSL)
                             << LOG_KV("peers", m_connectedNodes.size());
}

/// loads p2p configuration items from the configuration file
void GatewayConfig::initP2PConfig(const boost::property_tree::ptree& _pt, bool _uuidRequired)
{
    /*
    [p2p]
      ; uuid
      uuid =
      ; ssl or sm ssl
      sm_ssl=true
      listen_ip=0.0.0.0
      listen_port=30300
      nodes_path=./
      nodes_file=nodes.json
      */
    m_uuid = _pt.get<std::string>("p2p.uuid", "");
    if (_uuidRequired && m_uuid.size() == 0)
    {
        BOOST_THROW_EXCEPTION(InvalidParameter() << errinfo_comment(
                                  "initP2PConfig: invalid uuid! Must be non-empty!"));
    }
    bool smSSL = _pt.get<bool>("p2p.sm_ssl", false);
    std::string listenIP = _pt.get<std::string>("p2p.listen_ip", "0.0.0.0");
    int listenPort = _pt.get<int>("p2p.listen_port", 30300);
    if (!isValidPort(listenPort))
    {
        BOOST_THROW_EXCEPTION(
            InvalidParameter() << errinfo_comment(
                "initP2PConfig: invalid listen port, port=" + std::to_string(listenPort)));
    }

    // not set the nodePath, load from the config
    if (m_nodePath.size() == 0)
    {
        m_nodePath = _pt.get<std::string>("p2p.nodes_path", "./");
    }

    m_nodeFileName = _pt.get<std::string>("p2p.nodes_file", "nodes.json");

    m_smSSL = smSSL;
    m_listenIP = listenIP;
    m_listenPort = (uint16_t)listenPort;

    GATEWAY_CONFIG_LOG(INFO) << LOG_DESC("initP2PConfig ok!") << LOG_KV("listenIP", listenIP)
                             << LOG_KV("listenPort", listenPort) << LOG_KV("smSSL", smSSL)
                             << LOG_KV("nodePath", m_nodePath)
                             << LOG_KV("nodeFileName", m_nodeFileName);
}

// load p2p connected peers
void GatewayConfig::loadP2pConnectedNodes()
{
    std::string nodeFilePath = m_nodePath + "/" + m_nodeFileName;
    // load p2p connected nodes
    std::set<NodeIPEndpoint> nodes;
    auto jsonContent = readContentsToString(boost::filesystem::path(nodeFilePath));
    if (!jsonContent || jsonContent->empty())
    {
        BOOST_THROW_EXCEPTION(
            InvalidParameter() << errinfo_comment(
                "initP2PConfig: unable to read nodes json file, path=" + nodeFilePath));
    }

    parseConnectedJson(*jsonContent.get(), nodes);
    m_connectedNodes = nodes;

    GATEWAY_CONFIG_LOG(INFO) << LOG_DESC("loadP2pConnectedNodes ok!")
                             << LOG_KV("nodePath", m_nodePath)
                             << LOG_KV("nodeFileName", m_nodeFileName)
                             << LOG_KV("nodes", nodes.size());
}

/// loads ca configuration items from the configuration file
void GatewayConfig::initCertConfig(const boost::property_tree::ptree& _pt)
{
    /*
    [cert]
      ; directory the certificates located in
      ca_path=./
      ; the ca certificate file
      ca_cert=ca.crt
      ; the node private key file
      node_key=ssl.key
      ; the node certificate file
      node_cert=ssl.crt
    */
    if (m_certPath.size() == 0)
    {
        m_certPath = _pt.get<std::string>("cert.ca_path", "./");
    }
    std::string caCertFile = m_certPath + "/" + _pt.get<std::string>("cert.ca_cert", "ca.crt");
    std::string nodeCertFile = m_certPath + "/" + _pt.get<std::string>("cert.node_cert", "ssl.crt");
    std::string nodeKeyFile = m_certPath + "/" + _pt.get<std::string>("cert.node_key", "ssl.key");

    GATEWAY_CONFIG_LOG(INFO) << LOG_DESC("initCertConfig") << LOG_KV("ca_path", m_certPath)
                             << LOG_KV("ca_cert", caCertFile) << LOG_KV("node_cert", nodeCertFile)
                             << LOG_KV("node_key", nodeKeyFile);

    checkFileExist(caCertFile);
    checkFileExist(nodeCertFile);
    checkFileExist(nodeKeyFile);

    CertConfig certConfig;
    certConfig.caCert = caCertFile;
    certConfig.nodeCert = nodeCertFile;
    certConfig.nodeKey = nodeKeyFile;

    m_certConfig = certConfig;

    GATEWAY_CONFIG_LOG(INFO) << LOG_DESC("initCertConfig") << LOG_KV("ca", certConfig.caCert)
                             << LOG_KV("node_cert", certConfig.nodeCert)
                             << LOG_KV("node_key", certConfig.nodeKey);
}

// loads sm ca configuration items from the configuration file
void GatewayConfig::initSMCertConfig(const boost::property_tree::ptree& _pt)
{
    /*
    [cert]
    ; directory the certificates located in
    ca_path=./
    ; the ca certificate file
    sm_ca_cert=sm_ca.crt
    ; the node private key file
    sm_node_key=sm_ssl.key
    ; the node certificate file
    sm_node_cert=sm_ssl.crt
    ; the node private key file
    sm_ennode_key=sm_enssl.key
    ; the node certificate file
    sm_ennode_cert=sm_enssl.crt
    */
    // not set the certPath, load from the configuration
    if (m_certPath.size() == 0)
    {
        m_certPath = _pt.get<std::string>("cert.ca_path", "./");
    }
    std::string smCaCertFile =
        m_certPath + "/" + _pt.get<std::string>("cert.sm_ca_cert", "sm_ca.crt");
    std::string smNodeCertFile =
        m_certPath + "/" + _pt.get<std::string>("cert.sm_node_cert", "sm_ssl.crt");
    std::string smNodeKeyFile =
        m_certPath + "/" + _pt.get<std::string>("cert.sm_node_key", "sm_ssl.key");
    std::string smEnNodeCertFile =
        m_certPath + "/" + _pt.get<std::string>("cert.sm_ennode_cert", "sm_enssl.crt");
    std::string smEnNodeKeyFile =
        m_certPath + "/" + _pt.get<std::string>("cert.sm_ennode_key", "sm_enssl.key");

    checkFileExist(smCaCertFile);
    checkFileExist(smNodeCertFile);
    checkFileExist(smNodeKeyFile);
    checkFileExist(smEnNodeCertFile);
    checkFileExist(smEnNodeKeyFile);

    SMCertConfig smCertConfig;
    smCertConfig.caCert = smCaCertFile;
    smCertConfig.nodeCert = smNodeCertFile;
    smCertConfig.nodeKey = smNodeKeyFile;
    smCertConfig.enNodeCert = smEnNodeCertFile;
    smCertConfig.enNodeKey = smEnNodeKeyFile;

    m_smCertConfig = smCertConfig;

    GATEWAY_CONFIG_LOG(INFO) << LOG_DESC("initSMCertConfig") << LOG_KV("ca_path", m_certPath)
                             << LOG_KV("sm_ca_cert", smCertConfig.caCert)
                             << LOG_KV("sm_node_cert", smCertConfig.nodeCert)
                             << LOG_KV("sm_node_key", smCertConfig.nodeKey)
                             << LOG_KV("sm_ennode_cert", smCertConfig.enNodeCert)
                             << LOG_KV("sm_ennode_key", smCertConfig.enNodeKey);
}

// loads rate limit configuration items from the configuration file
void GatewayConfig::initRatelimitConfig(const boost::property_tree::ptree& _pt)
{
    /*
    [flow_control]
    ; the module that does not limit bandwidth
    ; list of all modules: raft,pbft,amop,block_sync,txs_sync,light_node,cons_txs_sync
    ;
    ; modules_without_bw_limit=raft,pbft

    ; restrict the outgoing bandwidth of the node
    ; both integer and decimal is support, unit: Mb
    ;
    ; total_outgoing_bw_limit=10

    ; restrict the outgoing bandwidth of the the connection
    ; both integer and decimal is support, unit: Mb
    ;
    ; conn_outgoing_bw_limit=2
    ;
    ; specify IP to limit bandwidth, format: ip_x.x.x.x=n
    ;   ip_192.108.0.1=3
    ;   ip_192.108.0.2=3
    ;   ip_192.108.0.3=3
    ;
    ; default bandwidth limit for the group
    ; group_outgoing_bw_limit=2
    ;
    ; specify group to limit bandwidth, group_groupName=n
    ;   group_group0=2
    ;   group_group1=2
    ;   group_group2=2
    */

    // modules_without_bw_limit=raft,pbft
    std::string strNoLimitModules =
        _pt.get<std::string>("flow_control.modules_without_bw_limit", "raft,pbft,cons_txs_sync");

    std::set<uint16_t> moduleIDs;
    std::vector<std::string> modules;

    if (!strNoLimitModules.empty())
    {
        boost::split(modules, strNoLimitModules, boost::is_any_of(","), boost::token_compress_on);

        for (auto module : modules)
        {
            boost::trim(module);
            boost::algorithm::to_lower(module);
            auto optModuleID = protocol::stringToModuleID(module);
            if (!optModuleID.has_value())
            {
                BOOST_THROW_EXCEPTION(InvalidParameter() << errinfo_comment(
                                          "unrecognized module: " + module +
                                          " ,list of available modules: "
                                          "raft,pbft,amop,block_sync,txs_sync,light_node"));
            }
            moduleIDs.insert(optModuleID.value());
        }
    }

    int64_t totalOutgoingBwLimit = -1;
    // total_outgoing_bw_limit
    std::string value = _pt.get<std::string>("flow_control.total_outgoing_bw_limit", "");
    if (value.empty())
    {
        GATEWAY_CONFIG_LOG(INFO) << LOG_DESC("the total_outgoing_bw_limit is not initialized");
    }
    else
    {
        double bw = boost::lexical_cast<double>(value);
        totalOutgoingBwLimit = doubleMBToBit(bw);

        GATEWAY_CONFIG_LOG(INFO) << LOG_DESC("the total_outgoing_bw_limit has been initialized")
                                 << LOG_KV("value", value) << LOG_KV("bw", bw)
                                 << LOG_KV("totalOutgoingBwLimit", totalOutgoingBwLimit);
    }

    int64_t connOutgoingBwLimit = -1;
    // conn_outgoing_bw_limit
    value = _pt.get<std::string>("flow_control.conn_outgoing_bw_limit", "");
    if (value.empty())
    {
        GATEWAY_CONFIG_LOG(INFO) << LOG_DESC("the conn_outgoing_bw_limit is not initialized");
    }
    else
    {
        double bw = boost::lexical_cast<double>(value);
        connOutgoingBwLimit = doubleMBToBit(bw);

        GATEWAY_CONFIG_LOG(INFO) << LOG_DESC("the conn_outgoing_bw_limit has been initialized")
                                 << LOG_KV("value", value) << LOG_KV("bw", bw)
                                 << LOG_KV("connOutgoingBwLimit", connOutgoingBwLimit);
    }

    int64_t groupOutgoingBwLimit = -1;
    // group_outgoing_bw_limit
    value = _pt.get<std::string>("flow_control.group_outgoing_bw_limit", "");
    if (value.empty())
    {
        GATEWAY_CONFIG_LOG(INFO) << LOG_DESC("the group_outgoing_bw_limit is not initialized");
    }
    else
    {
        double bw = boost::lexical_cast<double>(value);
        groupOutgoingBwLimit = doubleMBToBit(bw);

        GATEWAY_CONFIG_LOG(INFO) << LOG_DESC("the group_outgoing_bw_limit has been initialized")
                                 << LOG_KV("value", value) << LOG_KV("bw", bw)
                                 << LOG_KV("groupOutgoingBwLimit", groupOutgoingBwLimit);
    }

    // ip => bw && group => bw
    if (_pt.get_child_optional("flow_control"))
    {
        for (auto const& it : _pt.get_child("flow_control"))
        {
            auto key = it.first;
            auto value = it.second.data();

            boost::trim(key);
            boost::trim(value);

            if (boost::starts_with(key, "ip_"))
            {
                // ip_x.x.x.x =
                std::string ip = key.substr(3);
                if (!isValidIP(ip))
                {
                    BOOST_THROW_EXCEPTION(
                        InvalidParameter() << errinfo_comment(
                            "flow_control.ip_x.x.x.x config, invalid ip format, ip: " + ip));
                }
                double bw = boost::lexical_cast<double>(value);
                m_rateLimitConfig.ip2BwLimit[ip] = doubleMBToBit(bw);

                GATEWAY_CONFIG_LOG(INFO)
                    << LOG_BADGE("initRateLimitConfig") << LOG_DESC("add ip bandwidth limit")
                    << LOG_KV("ip", ip) << LOG_KV("bandwidth", bw);
            }
            else if (boost::starts_with(key, "group_") &&
                     !boost::starts_with(key, "group_outgoing"))
            {
                // group_xxxx =
                std::string group = key.substr(6);
                double bw = boost::lexical_cast<double>(value);
                m_rateLimitConfig.group2BwLimit[group] = doubleMBToBit(bw);

                GATEWAY_CONFIG_LOG(INFO)
                    << LOG_BADGE("initRateLimitConfig") << LOG_DESC("add group bandwidth limit")
                    << LOG_KV("group", group) << LOG_KV("bandwidth", bw);
            }
        }
    }

    m_rateLimitConfig.modulesWithNoBwLimit = moduleIDs;
    m_rateLimitConfig.totalOutgoingBwLimit = totalOutgoingBwLimit;
    m_rateLimitConfig.connOutgoingBwLimit = connOutgoingBwLimit;
    m_rateLimitConfig.groupOutgoingBwLimit = groupOutgoingBwLimit;

    if (totalOutgoingBwLimit > 0 && connOutgoingBwLimit > 0 &&
        connOutgoingBwLimit > totalOutgoingBwLimit)
    {
        BOOST_THROW_EXCEPTION(InvalidParameter() << errinfo_comment(
                                  "flow_control.conn_outgoing_bw_limit should not greater "
                                  "than flow_control.total_outgoing_bw_limit"));
    }

    if (totalOutgoingBwLimit > 0 && groupOutgoingBwLimit > 0 &&
        groupOutgoingBwLimit > totalOutgoingBwLimit)
    {
        BOOST_THROW_EXCEPTION(InvalidParameter() << errinfo_comment(
                                  "flow_control.group_outgoing_bw_limit should not greater "
                                  "than flow_control.total_outgoing_bw_limit"));
    }

    GATEWAY_CONFIG_LOG(INFO) << LOG_BADGE("initRateLimitConfig")
                             << LOG_KV("rateLimitConfigEffect", m_rateLimitConfig.isConfigEffect())
                             << LOG_KV("totalOutgoingBwLimit", totalOutgoingBwLimit)
                             << LOG_KV("connOutgoingBwLimit", connOutgoingBwLimit)
                             << LOG_KV("groupOutgoingBwLimit", groupOutgoingBwLimit)
                             << LOG_KV("moduleIDs", boost::join(modules, ","))
                             << LOG_KV("ips size", m_rateLimitConfig.ip2BwLimit.size())
                             << LOG_KV("groups size", m_rateLimitConfig.group2BwLimit.size());
}

void GatewayConfig::checkFileExist(const std::string& _path)
{
    auto fileContent = readContentsToString(boost::filesystem::path(_path));
    if (!fileContent || fileContent->empty())
    {
        BOOST_THROW_EXCEPTION(
            InvalidParameter() << errinfo_comment("checkFileExist: unable to load file content "
                                                  " maybe file not exist, path: " +
                                                  _path));
    }
}