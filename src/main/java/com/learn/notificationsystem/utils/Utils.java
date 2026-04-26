package com.learn.notificationsystem.utils;

import org.springframework.util.StringUtils;
import software.amazon.awssdk.services.sns.model.SnsException;

import static com.learn.notificationsystem.constants.Constants.INVALID_PARAMETER;
import static com.learn.notificationsystem.constants.Constants.INVALID_PARAMETER_VALUE;
import static com.learn.notificationsystem.constants.Constants.NOT_FOUND;


public class Utils {
    public static boolean isValidEmail(String email) {
        return StringUtils.hasText(email)  && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public static boolean isPermanentFailure(SnsException e) {
        String errorCode = e.awsErrorDetails().errorCode();

        return INVALID_PARAMETER.equals(errorCode)
                || NOT_FOUND.equals(errorCode)
                || INVALID_PARAMETER_VALUE.equals(errorCode);
    }
}
