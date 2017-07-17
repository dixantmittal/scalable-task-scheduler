package org.dixantmittal.request.taskmanager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dixantmittal.entity.RetryTask;

import java.sql.Timestamp;

/**
 * Created by dixant on 27/03/17.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddTaskRequest {
    private String taskId;
    private String taskType;
    private String taskMetadata;
    private Timestamp scheduledTime;
    private Integer priority;
    private RetryTask retryTask;
    private Boolean canRetry;
}
