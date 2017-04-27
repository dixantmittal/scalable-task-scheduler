package com.ixigo.request.jobschedulingservice;

import com.ixigo.exception.constants.RequestValidationExceptionConstants;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by dixant on 30/03/17.
 */
@Data
public class DeleteTaskRequest {
    @NotBlank(message = RequestValidationExceptionConstants.JOB_ID_IS_BLANK)
    private String jobId;
}
