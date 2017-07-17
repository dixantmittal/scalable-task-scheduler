package org.dixantmittal.utils.adapter;

import org.dixantmittal.entity.TaskSchedulingDetails;
import org.dixantmittal.request.jobschedulingservice.AddTaskRequest;

/**
 * Created by dixant on 27/03/17.
 */
public class TaskSchedulingDetailsAdapter {
    public static TaskSchedulingDetails adapt(AddTaskRequest request) {
        TaskSchedulingDetails jobDetails = new TaskSchedulingDetails();
        jobDetails.setTaskType(request.getTaskType());
        jobDetails.setTaskMetadata(request.getTaskMetadata());
        jobDetails.setRetryTaskDetails(request.getRetryTaskDetails());
        jobDetails.setScheduledTime(request.getScheduledTime().toLocalDateTime());
        if (request.getPriority() != null) {
            jobDetails.setPriority(request.getPriority());
        }
        return jobDetails;
    }
}
