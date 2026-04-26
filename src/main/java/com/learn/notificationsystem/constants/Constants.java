package com.learn.notificationsystem.constants;

public class Constants {
    public static final String INVALID_PARAMETER = "InvalidParameter";
    public static final String INVALID_PARAMETER_VALUE = "InvalidParameterValue";
    public static final String NOT_FOUND = "NotFound";
    public static final String EMAIL = "EMAIL";
    public static final String FAILED = "FAILED";
    public static final String COMPLETED = "COMPLETED";
    public static final String PROCESSING = "PROCESSING";
    public static final String NOTIFICATION_TOPIC = "notification-topic";
    public static final String NOTIFICATION_GROUP = "notification-group";
    public static final String IDEMPOTENCY_KEY_PREFIX = "notif_lock:";
    public static final String RETRY_COUNT = "x-retry-count";
    public static final String NOTIFICATION_TOPIC_DLQ = "notification-topic-dlq";
    public static final String NOTIFICATION_DLQ_GROUP = "notification-dlq-group";
    public static final int MAX_RETRY_COUNT = 3;
}
