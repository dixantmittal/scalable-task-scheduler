package com.ixigo.validation.validator;

import com.ixigo.exception.codes.RequestValidationExceptionCodes;
import com.ixigo.request.AddTaskWithJobIdRequest;
import com.ixigo.validation.AddTaskRequestValidation;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by dixant on 04/04/17.
 */
public class AddTaskWithJobIdRequestValidator implements ConstraintValidator<AddTaskRequestValidation, AddTaskWithJobIdRequest> {

    private void addConstraintViolation (ConstraintValidatorContext context,
                                         String requestExceptionCodes) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(requestExceptionCodes)
                .addConstraintViolation();
    }

    @Override
    public void initialize (AddTaskRequestValidation constraintAnnotation) {
    }

    @Override
    public boolean isValid (AddTaskWithJobIdRequest request, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(request.getJobId())) {
            addConstraintViolation(context, RequestValidationExceptionCodes.JOB_ID_IS_BLANK.name());
            return false;
        }
        if (StringUtils.isBlank(request.getTaskType())) {
            addConstraintViolation(context, RequestValidationExceptionCodes.TASK_TYPE_IS_BLANK.name());
            return false;
        }
        if (StringUtils.isBlank(request.getScheduledTime())) {
            addConstraintViolation(context, RequestValidationExceptionCodes.SCHEDULED_TIME_IS_BLANK.name());
            return false;
        }
        return true;
    }
}
