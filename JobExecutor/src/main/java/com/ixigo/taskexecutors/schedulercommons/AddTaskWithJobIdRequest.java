package com.ixigo.taskexecutors.schedulercommons;

import com.ixigo.entity.KafkaTaskDetails;
import com.ixigo.entity.RetryJobDetails;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by dixant on 03/04/17.
 */
@Data
@EqualsAndHashCode
public class AddTaskWithJobIdRequest {
    private String taskType;
    private String taskMetadata;
    private String scheduledTime;
    private RetryJobDetails retryJobDetails;
    private String jobId;
}
