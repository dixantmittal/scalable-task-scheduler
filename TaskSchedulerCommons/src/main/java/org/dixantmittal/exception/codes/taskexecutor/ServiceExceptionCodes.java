package org.dixantmittal.exception.codes.taskexecutor;

/**
 * Created by dixant on 04/04/17.
 */
public enum ServiceExceptionCodes {

    EXECUTOR_NOT_EXISTS("ER-3106", "Executor does not exist"),
    CONSUMER_COUNT_OUT_OF_BOUNDS("ER-3105", "Requested count of consumers is more than allowed"),
    CONFIG_CACHE_NOT_FOUND("ER-3104", "Config cache not found."),
    TOPIC_DOES_NOT_EXIST("ER-3103", "Topic does not exist."),
    THREADPOOL_FULL("ER-3102", "ThreadPool is completely filled. Cannot create new consumers."),
    THREADPOOL_EMPTY("ER-3101", "ThreadPool is empty. Cannot remove consumers.");

    String code;
    String message;

    ServiceExceptionCodes(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String code() {
        return code;
    }

    public String message() {
        return message;
    }

    public ServiceExceptionCodes forName(String enumName) {
        for (ServiceExceptionCodes val : ServiceExceptionCodes.values()) {
            if (val.name().equals(enumName)) {
                return val;
            }
        }
        return null;
    }
}
