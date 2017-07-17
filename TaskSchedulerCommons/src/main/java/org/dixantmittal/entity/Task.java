package org.dixantmittal.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Created by dixant on 28/03/17.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    protected String taskId;
    protected String taskType;
    protected String taskMetadata;
    protected LocalDateTime scheduledTime;
    protected RetryTask retryTask;
}
