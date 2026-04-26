package com.learn.notificationsystem.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import static com.learn.notificationsystem.constants.Constants.NOTIFICATION_TOPIC;
import static com.learn.notificationsystem.constants.Constants.NOTIFICATION_TOPIC_DLQ;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic notificationTopic() {
        return TopicBuilder.name(NOTIFICATION_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic dlqTopic() {
        return TopicBuilder.name(NOTIFICATION_TOPIC_DLQ)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
