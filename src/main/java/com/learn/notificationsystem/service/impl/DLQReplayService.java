package com.learn.notificationsystem.service.impl;

import com.learn.notificationsystem.model.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static com.learn.notificationsystem.constants.Constants.NOTIFICATION_TOPIC;
import static com.learn.notificationsystem.constants.Constants.RETRY_COUNT;

@Service
@RequiredArgsConstructor
public class DLQReplayService {

    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;


    public void replay(NotificationEvent event, int retryCount) {
        kafkaTemplate.send(
                MessageBuilder.withPayload(event)
                        .setHeader(KafkaHeaders.TOPIC, NOTIFICATION_TOPIC)
                        .setHeader(RETRY_COUNT, retryCount + 1)
                        .build()
        );
    }
}
