package org.dixantmittal.request.jobschedulingservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dixantmittal.entity.RetryTaskDetails;

import java.sql.Timestamp;

/**
 * Created by dixant on 27/03/17.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddTaskRequest {
    private String taskType;
    private String taskMetadata;
    private Timestamp scheduledTime;
    private Integer priority;
    private RetryTaskDetails retryTaskDetails;
    private Boolean canRetry;

    public AddTaskRequest(AddTaskRequest ob) {
        taskType = ob.taskType;
        taskMetadata = ob.taskMetadata;
        scheduledTime = ob.scheduledTime;
        priority = ob.priority;
        retryTaskDetails = ob.retryTaskDetails;
        canRetry = ob.canRetry;
    }
}
