package com.ixigo.exception.codes;

/**
 * Created by dixant on 04/04/17.
 */
public enum RequestValidationExceptionCodes {
    COUNT_IS_BLANK("ER-2101", "Count can not be blank."),
    TOPIC_NAME_IS_BLANK("ER-2102", "Topic name can not be blank.");

    String code;
    String message;

    RequestValidationExceptionCodes(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static RequestValidationExceptionCodes forName(String enumName) {
        for (RequestValidationExceptionCodes val : RequestValidationExceptionCodes.values()) {
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
