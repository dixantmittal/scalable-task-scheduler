package org.dixantmittal.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created by dixant on 28/03/17.
 */
@Data
public abstract class TaskDetails {
    protected String taskType;
    protected String taskMetadata;
    protected LocalDateTime scheduledTime;
    protected RetryTaskDetails retryTaskDetails;
}
