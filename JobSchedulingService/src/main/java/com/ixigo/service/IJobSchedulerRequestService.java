package com.ixigo.service;

import com.ixigo.request.jobschedulingservice.AddTaskRequest;
import com.ixigo.request.jobschedulingservice.AddTaskWithJobIdRequest;
import com.ixigo.request.jobschedulingservice.DeleteTaskRequest;
import com.ixigo.request.jobschedulingservice.StopSchedulerRequest;
import com.ixigo.response.jobschedulingservice.AddTaskResponse;
import com.ixigo.response.jobschedulingservice.DeleteTaskResponse;
import com.ixigo.response.jobschedulingservice.StartSchedulerResponse;
import com.ixigo.response.jobschedulingservice.StopSchedulerResponse;

/**
 * Created by dixant on 27/03/17.
 */
public interface IJobSchedulerRequestService {
    AddTaskResponse addTask (AddTaskRequest request);

    AddTaskResponse addTask (AddTaskWithJobIdRequest request);

    DeleteTaskResponse deleteTask (DeleteTaskRequest request);

    StartSchedulerResponse startScheduler ();

    StopSchedulerResponse stopScheduler (StopSchedulerRequest request);
}
