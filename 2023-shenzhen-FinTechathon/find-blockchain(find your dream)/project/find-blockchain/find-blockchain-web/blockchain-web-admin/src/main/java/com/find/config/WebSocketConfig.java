package com.find.config;


import com.sun.prism.impl.paint.PaintUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class WebSocketConfig {

    /**
     * ServerEndpointExporter 作用
     * 这个Bean会自动注册使用@ServerEndpoint注解声明的websocket endpoint
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }


}
