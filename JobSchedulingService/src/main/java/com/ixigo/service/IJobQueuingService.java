package com.ixigo.service;

import java.util.Map;

/**
 * Created by dixant on 27/03/17.
 */
public interface IJobQueuingService {
    Boolean addJobToExecutionQueue(Map<String, Object> jobDataMap);
}
