package com.ixigo.validation.jobexecutor.validator;

import com.ixigo.exception.codes.jobexecutor.RequestValidationExceptionCodes;
import com.ixigo.request.jobexecutor.AddConsumersRequest;
import com.ixigo.validation.jobexecutor.AddConsumersRequestValidation;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by dixant on 04/04/17.
 */
public class AddConsumersRequestValidator implements ConstraintValidator<AddConsumersRequestValidation, AddConsumersRequest> {

    private void addConstraintViolation(ConstraintValidatorContext context,
                                        String requestExceptionCodes) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(requestExceptionCodes)
                .addConstraintViolation();
    }

    @Override
    public void initialize(AddConsumersRequestValidation constraintAnnotation) {
    }

    @Override
    public boolean isValid(AddConsumersRequest request, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(request.getTopicName())) {
            addConstraintViolation(context, RequestValidationExceptionCodes.TOPIC_NAME_IS_BLANK.name());
            return false;
        }
        if (request.getCount() == 0) {
            addConstraintViolation(context, RequestValidationExceptionCodes.INVALID_COUNT.name());
            return false;
        }
        return true;
    }
}
