package org.dixantmittal.exception.codes.jobschedulingservice;

/**
 * Created by dixant on 04/04/17.
 */
public enum RequestValidationExceptionCodes {
    JOB_ID_IS_BLANK("ER-2106", "Job Id can not be blank."),
    SCHEDULED_TIME_IS_BLANK("ER-2105", "Scheduled time can not be blank."),
    TASK_TYPE_IS_BLANK("ER-2104", "Task type can not be blank"),
    INVALID_SCHEDULER_MODE("ER-2103", "Invalid scheduler mode passed."),
    COUNT_IS_BLANK("ER-2101", "Count can not be blank."),
    TOPIC_NAME_IS_BLANK("ER-2102", "Topic name can not be blank.");

    final private String code;
    final private String message;

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
