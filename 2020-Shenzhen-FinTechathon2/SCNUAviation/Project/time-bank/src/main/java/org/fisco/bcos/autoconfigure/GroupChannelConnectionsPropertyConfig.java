package org.fisco.bcos.autoconfigure;

import java.util.ArrayList;
import java.util.List;
import org.fisco.bcos.channel.handler.ChannelConnections;
import org.fisco.bcos.channel.handler.GroupChannelConnectionsConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@ConfigurationProperties(prefix = "group-channel-connections-config")
public class GroupChannelConnectionsPropertyConfig {

    List<ChannelConnections> allChannelConnections = new ArrayList<>();
    private Resource caCert;
    private Resource sslCert;
    private Resource sslKey;

    @Bean
    public GroupChannelConnectionsConfig getGroupChannelConnections() {
        GroupChannelConnectionsConfig groupChannelConnectionsConfig =
                new GroupChannelConnectionsConfig();
        groupChannelConnectionsConfig.setCaCert(caCert);
        groupChannelConnectionsConfig.setSslCert(sslCert);
        groupChannelConnectionsConfig.setSslKey(sslKey);
        groupChannelConnectionsConfig.setAllChannelConnections(allChannelConnections);
        return groupChannelConnectionsConfig;
    }

    /**
     * @return the caCert
     */
    public Resource getCaCert() {
        return caCert;
    }

    /**
     * @param caCert the caCert to set
     */
    public void setCaCert(Resource caCert) {
        this.caCert = caCert;
    }

    /**
     * @return the sslCert
     */
    public Resource getSslCert() {
        return sslCert;
    }

    /**
     * @param sslCert the sslCert to set
     */
    public void setSslCert(Resource sslCert) {
        this.sslCert = sslCert;
    }

    /**
     * @return the sslKey
     */
    public Resource getSslKey() {
        return sslKey;
    }

    /**
     * @param sslKey the sslKey to set
     */
    public void setSslKey(Resource sslKey) {
        this.sslKey = sslKey;
    }

    /**
     * @return the allChannelConnections
     */
    public List<ChannelConnections> getAllChannelConnections() {
        return allChannelConnections;
    }

    /**
     * @param allChannelConnections the allChannelConnections to set
     */
    public void setAllChannelConnections(List<ChannelConnections> allChannelConnections) {
        this.allChannelConnections = allChannelConnections;
    }
    
}
