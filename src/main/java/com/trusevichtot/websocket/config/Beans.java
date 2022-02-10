package com.trusevichtot.websocket.config;

import com.trusevichtot.websocket.domain.handler.websocket.ConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Beans {

    @Bean
    public ConnectionManager connectionManager() {
        return new ConnectionManager();
    }
}
