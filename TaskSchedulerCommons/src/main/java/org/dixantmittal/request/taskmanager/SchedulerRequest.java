package org.dixantmittal.request.taskmanager;

import org.dixantmittal.entity.SchedulerMode;
import org.dixantmittal.exception.constants.RequestValidationExceptionConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Created by dixant on 12/04/17.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchedulerRequest {
    @NotNull(message = RequestValidationExceptionConstants.INVALID_SCHEDULER_MODE)
    private SchedulerMode mode;
}
