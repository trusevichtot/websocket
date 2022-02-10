package com.trusevichtot.websocket.domain.handler.websocket;

import com.trusevichtot.websocket.domain.constant.Constant.Header;
import org.springframework.util.Assert;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.ofNullable;

public class ConnectionManager {

    private final Map<String, WebSocketSession> sessionHolder = new ConcurrentHashMap<>();
    private String connectionIdentifier = Header.COMMUNICATION_ID;
    private int sendTimeLimit = 10_000;
    private int bufferSizeLimit = 10_240;

    public void setConnectionIdentifier(String connectionIdentifier) {
        this.connectionIdentifier = connectionIdentifier;
    }

    public void setSendTimeLimit(int sendTimeLimit) {
        Assert.isTrue(sendTimeLimit > 0, "sendTimeLimit must be more than 0");
        this.sendTimeLimit = sendTimeLimit;
    }

    public void setBufferSizeLimit(int bufferSizeLimit) {
        Assert.isTrue(bufferSizeLimit > 0, "bufferSizeLimit must be more than 0");
        this.bufferSizeLimit = bufferSizeLimit;
    }

    public WebSocketSession getConnection(String communicationId) {
        return sessionHolder.get(communicationId);
    }

    void set(WebSocketSession session) {
        if (session.isOpen()) {
            ofNullable(getCommunicationIdHeader(session))
                    .ifPresent(h -> sessionHolder.put(h, new ConcurrentWebSocketSessionDecorator(session, sendTimeLimit, bufferSizeLimit)));
        }
    }

    void remove(WebSocketSession session) {
        ofNullable(getCommunicationIdHeader(session))
                .ifPresent(sessionHolder::remove);
    }

    private String getCommunicationIdHeader(WebSocketSession session) {
        return ofNullable(session.getHandshakeHeaders().get(connectionIdentifier))
                .map(h -> h.get(0))
                .orElse(null);
    }
}
