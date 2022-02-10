package com.trusevichtot.websocket.domain;

import com.trusevichtot.websocket.domain.model.notification.Notification;

import java.util.List;

public interface NotificationProducerService {

    void push(Notification notification);

    void push(List<Notification> notifications);

}
