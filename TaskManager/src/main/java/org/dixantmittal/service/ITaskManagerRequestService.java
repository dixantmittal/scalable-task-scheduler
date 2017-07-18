package org.dixantmittal.service;

import org.dixantmittal.request.taskmanager.AddTaskRequest;
import org.dixantmittal.request.taskmanager.DeleteTaskRequest;
import org.dixantmittal.request.taskmanager.SchedulerRequest;
import org.dixantmittal.response.GenericResponse;
import org.dixantmittal.response.taskmanager.AddTaskResponse;

/**
 * Created by dixant on 27/03/17.
 */
public interface ITaskManagerRequestService {

    AddTaskResponse addTask(AddTaskRequest request);

    GenericResponse deleteTask(DeleteTaskRequest request);

    GenericResponse startScheduler();

    GenericResponse stopScheduler(SchedulerRequest request);
}
