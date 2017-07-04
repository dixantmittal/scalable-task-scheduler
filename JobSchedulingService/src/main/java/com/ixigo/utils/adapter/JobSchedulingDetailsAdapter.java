package com.ixigo.utils.adapter;

import com.ixigo.entity.JobSchedulingDetails;
import com.ixigo.request.jobschedulingservice.AddTaskRequest;

/**
 * Created by dixant on 27/03/17.
 */
public class JobSchedulingDetailsAdapter {
    public static JobSchedulingDetails adapt(AddTaskRequest request) {
        JobSchedulingDetails jobDetails = new JobSchedulingDetails();
        jobDetails.setTaskType(request.getTaskType());
        jobDetails.setTaskMetadata(request.getTaskMetadata());
        jobDetails.setRetryJobDetails(request.getRetryJobDetails());
        jobDetails.setScheduledTime(request.getScheduledTime().toLocalDateTime());
        if (request.getPriority() != null) {
            jobDetails.setPriority(request.getPriority());
        }
        return jobDetails;
    }
}
