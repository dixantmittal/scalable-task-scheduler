package org.dixantmittal.utils.adapter;

import org.dixantmittal.entity.TaskSchedulingDetails;
import org.dixantmittal.request.taskmanager.AddTaskRequest;

/**
 * Created by dixant on 27/03/17.
 */
public class TaskSchedulingDetailsAdapter {
    public static TaskSchedulingDetails adapt(AddTaskRequest request) {
        TaskSchedulingDetails task = new TaskSchedulingDetails();
        task.setTaskType(request.getTaskType());
        task.setTaskMetadata(request.getTaskMetadata());
        task.setRetryTask(request.getRetryTask());
        task.setScheduledTime(request.getScheduledTime().toLocalDateTime());
        if (request.getPriority() != null) {
            task.setPriority(request.getPriority());
        }
        return task;
    }
}
