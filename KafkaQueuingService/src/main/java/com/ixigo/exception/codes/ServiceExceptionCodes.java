package com.ixigo.exception.codes;

/**
 * Created by dixant on 04/04/17.
 */
public enum ServiceExceptionCodes {

    TOPIC_NOT_FOUND("ER-3101", "Topic not found.");

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
