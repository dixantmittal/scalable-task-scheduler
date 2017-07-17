package org.dixantmittal.service;

import org.dixantmittal.request.jobschedulingservice.AddTaskWithTaskIdRequest;
import org.dixantmittal.request.jobschedulingservice.DeleteTaskRequest;
import org.dixantmittal.request.jobschedulingservice.StopSchedulerRequest;
import org.dixantmittal.response.jobschedulingservice.AddTaskResponse;
import org.dixantmittal.response.jobschedulingservice.DeleteTaskResponse;
import org.dixantmittal.response.jobschedulingservice.StartSchedulerResponse;
import org.dixantmittal.response.jobschedulingservice.StopSchedulerResponse;

/**
 * Created by dixant on 27/03/17.
 */
public interface ITaskSchedulerRequestService {

    AddTaskResponse addTask(AddTaskWithTaskIdRequest request);

    DeleteTaskResponse deleteTask (DeleteTaskRequest request);

    StartSchedulerResponse startScheduler ();

    StopSchedulerResponse stopScheduler (StopSchedulerRequest request);
}
