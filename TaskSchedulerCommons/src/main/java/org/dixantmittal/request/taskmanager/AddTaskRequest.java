package org.dixantmittal.request.taskmanager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dixantmittal.entity.RetryTask;
import org.dixantmittal.entity.Task;

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

    public static class Adapter {
        public static AddTaskRequest adapt(Task task) {
            AddTaskRequest request = new AddTaskRequest();
            request.taskId = task.getTaskId();
            request.taskType = task.getTaskType();
            request.taskMetadata = task.getTaskMetadata();
            request.scheduledTime = Timestamp.valueOf(task.getScheduledTime());
            request.retryTask = task.getRetryTask();
            return request;
        }
    }
}
