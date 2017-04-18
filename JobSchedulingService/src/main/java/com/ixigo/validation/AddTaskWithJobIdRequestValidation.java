package com.ixigo.validation;

import com.ixigo.validation.validator.AddTaskRequestValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by dixant on 04/04/17.
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {AddTaskRequestValidator.class})
@Documented
public @interface AddTaskWithJobIdRequestValidation {
    String message () default
            "{com.ixigo.validation.AddTaskWithJobIdRequestValidation.message}";

    Class<?>[] groups () default {};

    Class<? extends Payload>[] payload () default {};
}