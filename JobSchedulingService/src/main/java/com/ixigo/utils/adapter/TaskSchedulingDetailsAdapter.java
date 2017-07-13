package com.ixigo.utils.adapter;

import com.ixigo.entity.TaskSchedulingDetails;
import com.ixigo.request.jobschedulingservice.AddTaskRequest;

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
