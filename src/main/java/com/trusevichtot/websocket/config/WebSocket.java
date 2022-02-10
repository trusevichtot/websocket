package com.trusevichtot.websocket.config;

import com.trusevichtot.websocket.domain.constant.Constant.Url;
import com.trusevichtot.websocket.domain.handler.websocket.ConnectionManager;
import com.trusevichtot.websocket.domain.handler.websocket.HandshakeHandlerDecorator;
import com.trusevichtot.websocket.domain.handler.websocket.TrackConnectionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import javax.annotation.Resource;

@Configuration
@EnableWebSocket
public class WebSocket implements WebSocketConfigurer {

    @Resource
    private ConnectionManager connectionManager;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new TrackConnectionHandler(connectionManager), Url.STATE_CHANGE_TRACKING)
                .setHandshakeHandler(new HandshakeHandlerDecorator(new DefaultHandshakeHandler()))
                .setAllowedOrigins("*");
    }
}
