package org.dixantmittal.request.taskmanager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dixantmittal.exception.constants.RequestValidationExceptionConstants;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by dixant on 30/03/17.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteTaskRequest {
    @NotBlank(message = RequestValidationExceptionConstants.JOB_ID_IS_BLANK)
    private String taskId;
    private Boolean canRetry;
}
