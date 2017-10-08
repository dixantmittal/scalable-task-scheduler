package org.dixantmittal.service;

import org.dixantmittal.entity.Task;

/**
 * Created by dixant on 27/03/17.
 */
public interface IJobManagementService {

    /*
    create a new job with provided job id
    @return taskId
     */
    String createTask(Task task);

}
