package com.learn.notificationsystem.service.impl;

import com.learn.notificationsystem.model.NotificationEvent;
import com.learn.notificationsystem.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Objects;

import static com.learn.notificationsystem.constants.Constants.COMPLETED;
import static com.learn.notificationsystem.constants.Constants.FAILED;
import static com.learn.notificationsystem.constants.Constants.IDEMPOTENCY_KEY_PREFIX;
import static com.learn.notificationsystem.constants.Constants.NOTIFICATION_GROUP;
import static com.learn.notificationsystem.constants.Constants.NOTIFICATION_TOPIC;
import static com.learn.notificationsystem.constants.Constants.PROCESSING;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumer {

    private final StringRedisTemplate redisTemplate;
    private final NotificationServiceFactory factory;


    @KafkaListener(
            topics = NOTIFICATION_TOPIC,
            groupId = NOTIFICATION_GROUP,
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(NotificationEvent event) {

        if (Objects.isNull(event.getTransactionId())) {
            log.error("Transaction id is null");
            throw new IllegalArgumentException("TransactionId cannot be null");
        }

        String key = IDEMPOTENCY_KEY_PREFIX + event.getTransactionId();

        String existing = redisTemplate.opsForValue().get(key);

        if (COMPLETED.equals(existing)) {
            log.warn("Duplicate COMPLETED event txnId={}", event.getTransactionId());
            return;
        }

        Boolean isNewEntry = redisTemplate.opsForValue()
                .setIfAbsent(key, PROCESSING, Duration.ofMinutes(15));

        if (Boolean.FALSE.equals(isNewEntry)) {
            log.warn("Duplicate PROCESSING event txnId={}", event.getTransactionId());
            return;
        }

        try {
            String channel = event.getChannel();

            if (!StringUtils.hasText(channel)) {
                throw new IllegalArgumentException("Channel cannot be null");
            }

            log.info("EVENT_STATUS=PROCESSING txnId={} user={} channel={}",
                    event.getTransactionId(),
                    event.getUserId(),
                    channel);

            NotificationService service = factory.getService(channel);
            service.send(event);

            redisTemplate.opsForValue()
                    .set(key, COMPLETED, Duration.ofHours(24));

            log.info("EVENT_STATUS=SUCCESS txnId={} user={} channel={}",
                    event.getTransactionId(),
                    event.getUserId(),
                    channel);

        } catch (Exception e) {
            log.error("EVENT_STATUS=FAILED txnId={}", event.getTransactionId(), e);

            redisTemplate.opsForValue()
                    .set(key, FAILED, Duration.ofHours(24));

            throw e;
        }
    }
}