package br.com.estudos.chat.socket;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@ConditionalOnWebApplication
@Configuration
public class WebSocketConfigurator {

    @Bean
    public ServerEndPoint serverEndpoint() {
        return new ServerEndPoint();
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
    
    @Bean
    public CustomSpringConfigurator customSpringConfigurator() {
        return new CustomSpringConfigurator();
    }
}
