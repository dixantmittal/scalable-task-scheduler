package com.ixigo.request.jobschedulingservice;

import com.ixigo.entity.RetryJobDetails;
import com.ixigo.validation.jobschedulingservice.AddTaskRequestValidation;
import lombok.Data;

/**
 * Created by dixant on 27/03/17.
 */
@Data
@AddTaskRequestValidation
public class AddTaskRequest {
    private String taskType;
    private String taskMetadata;
    private String scheduledTime;
    private String priority;
    private RetryJobDetails retryJobDetails;
    private Boolean canRetry;
}
