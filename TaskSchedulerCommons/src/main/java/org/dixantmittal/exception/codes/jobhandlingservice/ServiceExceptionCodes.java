package org.dixantmittal.exception.codes.jobhandlingservice;

/**
 * Created by dixant on 04/04/17.
 */
public enum ServiceExceptionCodes {

    JOB_SCHEDULING_SERVICE_UNAVAILABLE("ER-3101", "Job Scheduling Service is unavailable");

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
