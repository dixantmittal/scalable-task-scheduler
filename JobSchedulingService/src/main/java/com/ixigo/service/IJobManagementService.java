package com.ixigo.service;

import com.ixigo.entity.TaskSchedulingDetails;

/**
 * Created by dixant on 27/03/17.
 */
public interface IJobManagementService {
    /*
    create a new job and
    @return taskId
     */
    String createJob(TaskSchedulingDetails requestData);

    /*
    create a new job with provided job id
    @return taskId
     */
    String createJobWithJobId(TaskSchedulingDetails schedulingDetails, String jobId);

}
