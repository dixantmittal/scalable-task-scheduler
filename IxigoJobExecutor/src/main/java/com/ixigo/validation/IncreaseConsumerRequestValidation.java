package com.ixigo.validation;

import com.ixigo.validation.validator.IncreaseConsumerRequestValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by dixant on 04/04/17.
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {IncreaseConsumerRequestValidator.class})
@Documented
public @interface IncreaseConsumerRequestValidation {
    String message() default
            "{com.ixigo.validation.IncreaseConsumerRequestValidation.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}