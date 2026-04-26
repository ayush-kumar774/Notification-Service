package com.learn.notificationsystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEvent implements Serializable {
    private String transactionId; // For Idempotency
    private String userId;
    private String message;
    private String channel;          // e.g., EMAIL, SMS
}