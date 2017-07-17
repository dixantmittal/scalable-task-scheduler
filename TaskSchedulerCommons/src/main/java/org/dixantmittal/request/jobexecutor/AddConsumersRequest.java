package org.dixantmittal.request.jobexecutor;

import org.dixantmittal.exception.constants.RequestValidationExceptionConstants;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by dixant on 29/03/17.
 */
@Data
public class AddConsumersRequest {
    @NotBlank(message = RequestValidationExceptionConstants.TOPIC_NAME_IS_BLANK)
    private String topic;

    @Min(value = 1, message = RequestValidationExceptionConstants.INVALID_COUNT)
    @NotNull(message = RequestValidationExceptionConstants.INVALID_COUNT)
    private Integer count;
}
