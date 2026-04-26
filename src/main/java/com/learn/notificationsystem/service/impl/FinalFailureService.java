package com.learn.notificationsystem.service.impl;

import com.learn.notificationsystem.model.NotificationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FinalFailureService {
    public void handle(NotificationEvent event) {
        log.error("FINAL FAULURE txnId={}, user={}, message={}",
                event.getTransactionId(),
                event.getUserId(),
                event.getMessage());

        // save to db
        // alerting
        // send to monitoring
    }
}
