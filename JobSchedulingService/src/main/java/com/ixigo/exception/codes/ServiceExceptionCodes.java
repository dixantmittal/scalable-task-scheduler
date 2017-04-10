package com.ixigo.exception.codes;

/**
 * Created by dixant on 04/04/17.
 */
public enum ServiceExceptionCodes {

    DATE_FORMAT_EXCEPTION("ER-3102", "Date format is incorrect"),
    KAFKA_PUSH_RETRIES_EXCEEDED("ER-3101", "Number of retries to push task to Kafka exceeded.");

    String code;
    String message;

    ServiceExceptionCodes(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ServiceExceptionCodes forName(String enumName) {
        for (ServiceExceptionCodes val : ServiceExceptionCodes.values()) {
            if (val.name().equals(enumName)) {
                return val;
            }
        }
        return null;
    }

    public String code() {
        return code;
    }

    public String message() {
        return message;
    }
}
