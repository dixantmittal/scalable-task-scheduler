package com.ixigo.request;

import com.ixigo.entity.RetryJobDetails;
import lombok.Data;

/**
 * Created by dixant on 27/03/17.
 */
@Data
public class AddTaskRequest {
    private String taskType;
    private String taskMetadata;
    private String scheduledTime;
    private String priority;
    private RetryJobDetails retryJobDetails;
}
