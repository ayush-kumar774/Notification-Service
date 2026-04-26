package com.learn.notificationsystem.service.impl;

import com.learn.notificationsystem.model.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import static com.learn.notificationsystem.constants.Constants.MAX_RETRY_COUNT;
import static com.learn.notificationsystem.constants.Constants.NOTIFICATION_DLQ_GROUP;
import static com.learn.notificationsystem.constants.Constants.NOTIFICATION_TOPIC_DLQ;
import static com.learn.notificationsystem.constants.Constants.RETRY_COUNT;

@Service
@RequiredArgsConstructor
@Slf4j
public class DLQConsumer {

    private final DLQReplayService replayService;
    private final FinalFailureService finalFailureService;

    @KafkaListener(topics = NOTIFICATION_TOPIC_DLQ, groupId = NOTIFICATION_DLQ_GROUP)
    public void consumeDLQ(
            NotificationEvent event,
            @Header(name = RETRY_COUNT, required = false) Integer retryCount
    ) {
        int count = retryCount == null ? 0 : retryCount;

        if (count >= MAX_RETRY_COUNT) {
            log.error("DLQ retry limit reached. Dropping event txnId={}", event.getTransactionId());
            finalFailureService.handle(event);
            return;
        }

        log.warn("Replaying event from DLQ, retryCount={}", count);
        replayService.replay(event, count);
    }
}
