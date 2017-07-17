package org.dixantmittal.request.jobschedulingservice;

import org.dixantmittal.enums.SchedulerMode;
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
public class StopSchedulerRequest {
    @NotNull(message = RequestValidationExceptionConstants.INVALID_SCHEDULER_MODE)
    private SchedulerMode mode;
}
