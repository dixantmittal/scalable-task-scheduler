package com.ixigo.exception.codes;

/**
 * Created by dixant on 04/04/17.
 */
public enum CommonExceptionCodes {
    INTERNAL_SERVER_EXCEPTION("ER-1100", "Internal Server Exception"),
    REQUEST_VALIDATION_EXCEPTION("ER-2100", "Request Validation Exception"),
    SERVICE_EXCEPTION("ER-3100", "Service Exception"),
    CONFIG_CACHE_NOT_FOUND("ER-4100", "Config cache not found.");


    String code;
    String message;

    CommonExceptionCodes(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String code() {
        return code;
    }

    public String message() {
        return message;
    }

    public CommonExceptionCodes forName(String enumName) {
        for (CommonExceptionCodes val : CommonExceptionCodes.values()) {
            if (val.name().equals(enumName)) {
                return val;
            }
        }
        return null;
    }
}
