package com.learn.notificationsystem.service.impl;

import com.learn.notificationsystem.model.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.learn.notificationsystem.constants.Constants.NOTIFICATION_TOPIC;

@Service
@RequiredArgsConstructor
public class NotificationProducer {

    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    public void sendEvent(NotificationEvent event) {
        // Using transactionId as the key ensures related messages
        // land in the same partition for ordering.
        kafkaTemplate.send(NOTIFICATION_TOPIC, event.getTransactionId(), event);
    }
}
