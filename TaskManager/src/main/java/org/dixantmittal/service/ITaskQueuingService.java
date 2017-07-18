package org.dixantmittal.service;

import java.util.Map;

/**
 * Created by dixant on 27/03/17.
 */
public interface ITaskQueuingService {
    Boolean addTaskToExecutionQueue(Map<String, Object> jobDataMap);
}
