package com.ixigo.service;

import com.ixigo.request.AddTaskRequest;
import com.ixigo.request.DeleteTaskRequest;
import com.ixigo.response.AddTaskResponse;
import com.ixigo.response.DeleteTaskResponse;
import com.ixigo.response.StartSchedulerResponse;
import com.ixigo.response.StopSchedulerResponse;

/**
 * Created by dixant on 27/03/17.
 */
public interface IJobSchedulerRequestService {
    AddTaskResponse addTask(AddTaskRequest request);

    DeleteTaskResponse deleteTask(DeleteTaskRequest request);

    StartSchedulerResponse startScheduler();

    StopSchedulerResponse stopScheduler();
}
