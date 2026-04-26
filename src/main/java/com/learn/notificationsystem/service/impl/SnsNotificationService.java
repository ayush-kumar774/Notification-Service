package com.learn.notificationsystem.service.impl;

import com.learn.notificationsystem.config.AwsProperties;
import com.learn.notificationsystem.model.NotificationEvent;
import com.learn.notificationsystem.service.NotificationService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.SnsException;

import static com.learn.notificationsystem.utils.Utils.isPermanentFailure;
import static com.learn.notificationsystem.utils.Utils.isValidEmail;

@Service
@RequiredArgsConstructor
@Slf4j
public class SnsNotificationService implements NotificationService {

    private final SnsClient snsClient;
    private final AwsProperties awsProperties;

    @Override
    @CircuitBreaker(name = "snsService", fallbackMethod = "fallback")
    public void send(NotificationEvent event) {

        String email = event.getUserId();

        if (!isValidEmail(email)) {
            log.error("Invalid email: {}", email);
            return;
        }

        try{
            PublishRequest request = PublishRequest.builder()
                    .topicArn(awsProperties.getSns().getTopicArn())
                    .message(buildMessage(event))
                    .subject(buildSubject(event))
                    .build();

            snsClient.publish(request);
            log.info("SNS notification sent successfully for txnId={}", event.getTransactionId());
        } catch (SnsException e) {
            log.error("SNS error: {}", e.awsErrorDetails().errorMessage());
            if (isPermanentFailure(e)) {
                log.error("Permanent failure. Skipping retry.");
                return;
            }
            throw e;
        }

    }

    public void fallback(NotificationEvent event, Throwable t) {
        log.error("Circuit breaker triggered for txnId={}", event.getTransactionId(), t);
        throw new RuntimeException("SNS unavailable", t);
    }

    private String buildMessage(NotificationEvent event) {
        return String.format(
                "UserId: %s\nChannel: %s\nMessage: %s\nTransactionId: %s",
                event.getUserId(),
                event.getChannel(),
                event.getMessage(),
                event.getTransactionId()
        );
    }

    private String buildSubject(NotificationEvent event) {
        return "Notification | txnId=" + event.getTransactionId();
    }
}