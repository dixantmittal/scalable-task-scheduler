package org.dixantmittal.client;

import org.dixantmittal.entity.Task;

/**
 * Created by dixant on 19/07/17.
 */
public interface TaskSchedulerClient {

    /*
    Adds a task to the scheduler.
    @params Task object
    @return Job Id
     */
    String addTask(Task task);

    /*
    Deletes a task from the scheduler.
    @params Task object
    @return Job Id
    */
    String deleteTask(String taskId, Boolean retryable);
}
