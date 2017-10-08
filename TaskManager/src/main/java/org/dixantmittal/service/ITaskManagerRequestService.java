package org.dixantmittal.service;

import org.dixantmittal.entity.Task;
import org.dixantmittal.request.taskmanager.SchedulerRequest;
import org.dixantmittal.response.GenericResponse;
import org.dixantmittal.response.taskmanager.AddTaskResponse;

/**
 * Created by dixant on 27/03/17.
 */
public interface ITaskManagerRequestService {

    AddTaskResponse addTask(Task task);

    GenericResponse deleteTask(String taskId);

    GenericResponse startScheduler();

    GenericResponse stopScheduler(SchedulerRequest request);
}
