package com.trusevichtot.websocket.domain.handler.kafka;

import com.trusevichtot.websocket.domain.handler.websocket.ConnectionManager;
import com.trusevichtot.websocket.domain.model.event.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static java.util.Optional.ofNullable;

@Component
@Slf4j
public class EventReceiver {

    @Resource
    private ConnectionManager connectionManager;

    @KafkaListener(topics = "ots-modification-state-event")
    public void consume(List<Event> events) {
        //todo: parallel processing adapter with custom executor
        events.stream().parallel()
                .filter(Objects::nonNull)
                .filter(Event::hasCommunicationId)
                .forEach(event ->
                        ofNullable(connectionManager.getConnection(event.getCommunicationId()))
                                .filter(WebSocketSession::isOpen)
                                .ifPresent(session -> {
                                    try {
                                        session.sendMessage(new TextMessage(event.getPayload()));
                                        log.info("Event was delivered - {}", event);
                                    } catch (IOException e) {
                                        log.error(e.getMessage());
                                    }
                                })
        );
    }
}
