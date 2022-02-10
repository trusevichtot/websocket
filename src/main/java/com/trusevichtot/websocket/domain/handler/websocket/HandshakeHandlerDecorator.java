package com.trusevichtot.websocket.domain.handler.websocket;

import com.trusevichtot.websocket.domain.constant.Constant.Header;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeHandler;

import java.io.IOException;
import java.util.Map;

import static java.nio.charset.StandardCharsets.*;

public class HandshakeHandlerDecorator implements HandshakeHandler {

    private final HandshakeHandler handshakeHandler;

    public HandshakeHandlerDecorator(HandshakeHandler handshakeHandler) {
        this.handshakeHandler = handshakeHandler;
    }

    @SneakyThrows
    @Override
    public boolean doHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        if (!request.getHeaders().containsKey(Header.COMMUNICATION_ID)) {
            handleInvalidCommunicationHeader(request, response);
            return false;
        }
        return handshakeHandler.doHandshake(request, response, wsHandler, attributes);
    }

    protected void handleInvalidCommunicationHeader(ServerHttpRequest request, ServerHttpResponse response) throws IOException {
        response.setStatusCode(HttpStatus.BAD_REQUEST);
        response.getBody().write((Header.COMMUNICATION_ID + " header is required").getBytes(UTF_8));
    }
}
