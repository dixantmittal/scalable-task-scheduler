package org.dixantmittal.controller;


import org.dixantmittal.exception.*;
import org.dixantmittal.exception.*;
import org.dixantmittal.exception.codes.CommonExceptionCodes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by dixant on 04/04/17.
 */

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({IOException.class})
    @ResponseBody
    public ExceptionResponse handleIOException(IOException ex, HttpServletRequest request,
                                               HttpServletResponse httpResponse) {
        log.error("IO Exception occurred :", ex);

        ExceptionResponse exception = new ExceptionResponse();
        exception.setCode(CommonExceptionCodes.INTERNAL_SERVER_EXCEPTION.code());
        exception.setMessage(CommonExceptionCodes.INTERNAL_SERVER_EXCEPTION.message());

        httpResponse.setStatus(500);
        return exception;
    }

    @ExceptionHandler({GenericException.class})
    @ResponseBody
    public ExceptionResponse handleAuthorizationException(GenericException ex, HttpServletRequest request,
                                                          HttpServletResponse httpResponse) {
        log.error("Generic exception occurred :", ex);
        ExceptionResponse exception = new ExceptionResponse();
        exception.setCode(ex.getErrCode());
        exception.setMessage(ex.getErrMessage());
        httpResponse.setStatus(401);
        return exception;
    }

    @ExceptionHandler({RequestValidationException.class})
    @ResponseBody
    public ExceptionResponse handleRequestValidationException(GenericException ex, HttpServletRequest request,
                                                              HttpServletResponse httpResponse) {
        log.error("Request validation exception occurred :", ex);
        ExceptionResponse exception = new ExceptionResponse();
        exception.setCode(ex.getErrCode());
        exception.setMessage(ex.getErrMessage());
        httpResponse.setStatus(400);
        return exception;
    }

    @ExceptionHandler({ServiceException.class})
    @ResponseBody
    public ExceptionResponse handleServiceException(GenericException ex, HttpServletRequest request,
                                                    HttpServletResponse httpResponse) {
        log.error("Service exception occurred :", ex);
        ExceptionResponse exception = new ExceptionResponse();
        exception.setCode(ex.getErrCode());
        exception.setMessage(ex.getErrMessage());
        httpResponse.setStatus(503);
        return exception;
    }

    @ExceptionHandler({InternalServerException.class})
    @ResponseBody
    public ExceptionResponse handleInternalServerException(InternalServerException ex, HttpServletRequest request,
                                                           HttpServletResponse httpResponse) {
        log.error("Internal Server Exception occurred :", ex);
        ExceptionResponse exception = new ExceptionResponse();
        exception.setCode(ex.getErrCode());
        exception.setMessage(ex.getErrMessage());
        httpResponse.setStatus(500);
        return exception;
    }

    @ExceptionHandler({RuntimeException.class})
    @ResponseBody
    public ExceptionResponse handleRuntimeException(RuntimeException ex, HttpServletRequest request,
                                                    HttpServletResponse httpResponse) {
        log.error("Runtime exception occurred :", ex);
        ExceptionResponse exception = new ExceptionResponse();
        exception.setCode(CommonExceptionCodes.INTERNAL_SERVER_EXCEPTION.code());
        exception.setMessage(CommonExceptionCodes.INTERNAL_SERVER_EXCEPTION.message());
        httpResponse.setStatus(500);
        return exception;
    }

    @ExceptionHandler({Exception.class})
    @ResponseBody
    public ExceptionResponse handleException(Exception ex, HttpServletRequest request,
                                             HttpServletResponse httpResponse) {
        log.error("Exception occurred :", ex);
        ExceptionResponse exception = new ExceptionResponse();
        exception.setCode(CommonExceptionCodes.INTERNAL_SERVER_EXCEPTION.code());
        exception.setMessage(CommonExceptionCodes.INTERNAL_SERVER_EXCEPTION.message());
        httpResponse.setStatus(500);
        return exception;
    }

    @ExceptionHandler({SQLException.class})
    @ResponseBody
    public ExceptionResponse handleSQLException(Exception ex, HttpServletRequest request,
                                                HttpServletResponse httpResponse) {
        log.error("Exception occurred :", ex);
        ExceptionResponse exception = new ExceptionResponse();
        exception.setCode(CommonExceptionCodes.INTERNAL_SERVER_EXCEPTION.code());
        exception.setMessage(CommonExceptionCodes.INTERNAL_SERVER_EXCEPTION.message());
        exception.setMessage(CommonExceptionCodes.INTERNAL_SERVER_EXCEPTION.message());
        httpResponse.setStatus(500);
        return exception;
    }
}
