package org.dixantmittal.utils.adapter;

import org.dixantmittal.entity.Task;
import org.dixantmittal.request.taskmanager.AddTaskRequest;

import java.sql.Timestamp;

/**
 * Created by dixant on 20/04/17.
 */
public class AddTaskRequestAdapter {
    public static AddTaskRequest adapt(Task task) {
        AddTaskRequest request = new AddTaskRequest();
        request.setTaskId(task.getTaskId());
        request.setRetryTask(task.getRetryTask());
        request.setTaskType(task.getTaskType());
        request.setTaskMetadata(task.getTaskMetadata());
        request.setScheduledTime(Timestamp.valueOf(task.getScheduledTime()));
        return request;
    }
}
