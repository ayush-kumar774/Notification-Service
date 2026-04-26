package com.learn.notificationsystem.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.notificationsystem.model.NotificationEvent;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Objects;

public class NotificationEventSerializer implements Serializer<NotificationEvent> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, NotificationEvent data) {
        if (Objects.isNull(data)) return null;

        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new SerializationException("Error serializing NotificationEvent", e);
        }
    }
}