#include "../../Common/TarsUtils.h"
#include "../GatewayInitializer.h"
#include "../GatewayServiceServer.h"
#include "libinitializer/CommandHelper.h"
#include <bcos-gateway/GatewayConfig.h>
#include <bcos-utilities/BoostLogInitializer.h>
#include <servant/Application.h>

using namespace bcostars;

class GatewayServiceApp : public tars::Application
{
public:
    GatewayServiceApp() {}
    ~GatewayServiceApp() override{};

    void destroyApp() override
    {
        if (m_gatewayInitializer)
        {
            m_gatewayInitializer->stop();
        }
    }
    void initialize() override
    {
        // Note: since tars Application catch the exception and output the error message with
        // e.what() which unable to explicitly display specific error messages, we actively catch
        // and output exception information here
        try
        {
            m_iniConfigPath = tars::ServerConfig::BasePath + "/config.ini";
            addConfig("config.ini");
            initService(m_iniConfigPath);
            GatewayServiceParam param;
            param.gatewayInitializer = m_gatewayInitializer;
            addServantWithParams<GatewayServiceServer, GatewayServiceParam>(
                getProxyDesc(bcos::protocol::GATEWAY_SERVANT_NAME), param);
        }
        catch (std::exception const& e)
        {
            std::cout << "init GatewayService failed, error: " << boost::diagnostic_information(e)
                      << std::endl;
            throw e;
        }
    }

protected:
    virtual void initService(std::string const& _configPath)
    {
        boost::property_tree::ptree pt;
        boost::property_tree::read_ini(_configPath, pt);

        // init the log
        m_logInitializer = std::make_shared<bcos::BoostLogInitializer>();
        m_logInitializer->setLogPath(getLogPath());
        m_logInitializer->initLog(pt);

        // init gateway config
        auto gatewayConfig = std::make_shared<bcos::gateway::GatewayConfig>();
        gatewayConfig->initP2PConfig(pt, true);
        gatewayConfig->initRatelimitConfig(pt);
        gatewayConfig->setCertPath(tars::ServerConfig::BasePath);
        gatewayConfig->setNodePath(tars::ServerConfig::BasePath);
        if (gatewayConfig->smSSL())
        {
            addConfig("sm_ca.crt");
            addConfig("sm_ssl.crt");
            addConfig("sm_enssl.crt");
            addConfig("sm_ssl.key");
            addConfig("sm_enssl.key");
            gatewayConfig->initSMCertConfig(pt);
        }
        else
        {
            addConfig("ca.crt");
            addConfig("ssl.key");
            addConfig("ssl.crt");
            gatewayConfig->initCertConfig(pt);
        }

        // nodes.json
        addConfig("nodes.json");
        gatewayConfig->loadP2pConnectedNodes();
        // init rpc
        m_gatewayInitializer = std::make_shared<GatewayInitializer>(_configPath, gatewayConfig);
        bcos::initializer::showNodeVersionMetric();
        m_gatewayInitializer->start();
    }

private:
    std::string m_iniConfigPath;
    bcos::BoostLogInitializer::Ptr m_logInitializer;
    GatewayInitializer::Ptr m_gatewayInitializer;
};

int main(int argc, char* argv[])
{
    try
    {
        bcos::initializer::initCommandLine(argc, argv);
        GatewayServiceApp app;
        app.main(argc, argv);
        app.waitForShutdown();

        return 0;
    }
    catch (std::exception& e)
    {
        cerr << "GatewayService std::exception:" << e.what() << std::endl;
    }
    catch (...)
    {
        cerr << "GatewayService unknown exception." << std::endl;
    }
    return -1;
}