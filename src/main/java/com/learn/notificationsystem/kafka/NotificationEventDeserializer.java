package com.learn.notificationsystem.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.notificationsystem.model.NotificationEvent;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Objects;

public class NotificationEventDeserializer implements Deserializer<NotificationEvent> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public NotificationEvent deserialize(String topic, byte[] data) {
        if (Objects.isNull(data)) return null;

        try {
            return objectMapper.readValue(data, NotificationEvent.class);
        } catch (Exception e) {
            throw new SerializationException("Error deserializing NotificationEvent", e);
        }
    }
}