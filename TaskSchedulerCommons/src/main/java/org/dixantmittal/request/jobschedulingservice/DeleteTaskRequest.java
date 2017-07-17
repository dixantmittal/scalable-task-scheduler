package org.dixantmittal.request.jobschedulingservice;

import org.dixantmittal.exception.constants.RequestValidationExceptionConstants;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by dixant on 30/03/17.
 */
@Data
public class DeleteTaskRequest {
    @NotBlank(message = RequestValidationExceptionConstants.JOB_ID_IS_BLANK)
    private String jobId;
    private Boolean canRetry;
}
