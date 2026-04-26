package com.learn.notificationsystem.service;

import com.learn.notificationsystem.model.NotificationEvent;

public interface NotificationService {
    void send(NotificationEvent event);
}
