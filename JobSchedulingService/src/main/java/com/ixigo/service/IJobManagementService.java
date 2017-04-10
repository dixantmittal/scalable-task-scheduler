package com.ixigo.service;

import com.ixigo.entity.JobSchedulingDetails;

/**
 * Created by dixant on 27/03/17.
 */
public interface IJobManagementService {
    /*
    create a new job and
    @return jobId
     */
    String createJob(JobSchedulingDetails requestData);

    /*
    create a new job with provided job id
    @return jobId
     */
    String createJobWithJobId(JobSchedulingDetails schedulingDetails, String jobId);

}
