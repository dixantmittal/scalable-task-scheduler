package com.ixigo.exception;

/**
 * Created by dixant on 04/04/17.
 */
public class RequestValidationException extends GenericException {
    public RequestValidationException(String errCode, String errMsg) {
        super(errCode, errMsg);
    }
}
