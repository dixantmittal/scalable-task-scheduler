package org.dixantmittal.validation;

import lombok.extern.slf4j.Slf4j;
import org.dixantmittal.exception.RequestValidationException;
import org.dixantmittal.exception.codes.taskmanager.RequestValidationExceptionCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

/**
 * Created by dixant on 18/07/17.
 */
@Slf4j
@Component
public class RequestValidator {

    @Autowired
    Validator validator;

    public <T> T validate(T request) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(request);
        if (constraintViolations != null && constraintViolations.size() > 0) {
            log.error("ERROR occurred while VALIDATING REQUEST. <RequestClass>: {}", request.getClass().getName());
            RequestValidationExceptionCodes error = RequestValidationExceptionCodes
                    .forName(constraintViolations.iterator().next().getMessage());
            throw new RequestValidationException(error.code(), error.message());
        }
        return request;
    }
}
