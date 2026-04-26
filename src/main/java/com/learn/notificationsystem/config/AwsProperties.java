package com.learn.notificationsystem.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "aws")
@Data
public class AwsProperties {
    private String region;
    private Sns sns;

    @Data
    public static class Sns {
        private String topicArn;
    }
}