package com.ixigo.validation.jobschedulingservice.validator;

import com.ixigo.exception.codes.jobschedulingservice.RequestValidationExceptionCodes;
import com.ixigo.request.jobschedulingservice.AddTaskWithTaskIdRequest;
import com.ixigo.validation.jobschedulingservice.AddTaskWithJobIdRequestValidation;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by dixant on 04/04/17.
 */
public class AddTaskWithJobIdRequestValidator implements ConstraintValidator<AddTaskWithJobIdRequestValidation, AddTaskWithTaskIdRequest> {

    private void addConstraintViolation(ConstraintValidatorContext context,
                                        String requestExceptionCodes) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(requestExceptionCodes)
                .addConstraintViolation();
    }

    @Override
    public void initialize(AddTaskWithJobIdRequestValidation constraintAnnotation) {
    }

    @Override
    public boolean isValid(AddTaskWithTaskIdRequest request, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(request.getTaskId())) {
            addConstraintViolation(context, RequestValidationExceptionCodes.JOB_ID_IS_BLANK.name());
            return false;
        }
        if (StringUtils.isBlank(request.getTaskType())) {
            addConstraintViolation(context, RequestValidationExceptionCodes.TASK_TYPE_IS_BLANK.name());
            return false;
        }
        if (request.getScheduledTime() == null) {
            addConstraintViolation(context, RequestValidationExceptionCodes.SCHEDULED_TIME_IS_BLANK.name());
            return false;
        }
        return true;
    }
}
