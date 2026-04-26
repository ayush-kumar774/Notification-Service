package com.learn.notificationsystem.service.impl;

import com.learn.notificationsystem.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.learn.notificationsystem.constants.Constants.EMAIL;

@Service
@RequiredArgsConstructor
public class NotificationServiceFactory {

    private final SnsNotificationService snsService;

    public NotificationService getService(String channel) {
        if (EMAIL.equalsIgnoreCase(channel)) {
            return snsService;
        }
        throw new IllegalArgumentException("Unsupported channel: " + channel);
    }
}
