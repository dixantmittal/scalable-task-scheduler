package com.ixigo.request.jobexecutor;

import com.ixigo.exception.constants.RequestValidationExceptionConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;

/**
 * Created by dixant on 30/03/17.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoveConsumersRequest {
    @NotBlank(message = RequestValidationExceptionConstants.TOPIC_NAME_IS_BLANK)
    private String topic;
    @Min(value = 1, message = RequestValidationExceptionConstants.INVALID_COUNT)
    private int count;
}
