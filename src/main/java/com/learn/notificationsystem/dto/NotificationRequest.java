package com.learn.notificationsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationRequest {
    private String id;        // Unique UUID for Idempotency
    private String userId;
    private String channel;      // EMAIL, SMS, PUSH
    private String recipient;
    private String message;
    private String transactionId;
}
