package com.learn.notificationsystem.controller;

import com.learn.notificationsystem.dto.NotificationRequest;
import com.learn.notificationsystem.model.NotificationEvent;
import com.learn.notificationsystem.service.impl.NotificationProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationProducer producer;

    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@RequestBody NotificationRequest request) {
        NotificationEvent event = NotificationEvent.builder()
                .channel(request.getChannel())
                .transactionId(request.getTransactionId())
                .userId(request.getUserId())
                .message(request.getMessage())
                .build();

        producer.sendEvent(event);
        return ResponseEntity.ok("Notification queued successfully!");
    }
}