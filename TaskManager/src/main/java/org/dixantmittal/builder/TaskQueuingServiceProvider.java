package org.dixantmittal.builder;

import org.dixantmittal.service.ITaskQueuingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Created by dixant on 28/03/17.
 */
public class TaskQueuingServiceProvider {

    private static TaskQueuingServiceProvider _INSTANCE = new TaskQueuingServiceProvider();

    @Autowired
    @Qualifier("kafkaTaskQueuingService")
    ITaskQueuingService kafkaTaskQueuingService;

    public static TaskQueuingServiceProvider getInstance() {
        return _INSTANCE;
    }

    public ITaskQueuingService getTaskQueuingService() {
        return kafkaTaskQueuingService;
    }
}
