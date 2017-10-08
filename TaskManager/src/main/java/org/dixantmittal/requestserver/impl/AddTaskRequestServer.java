package org.dixantmittal.requestserver.impl;

import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.dixantmittal.entity.Task;
import org.dixantmittal.requestserver.IRequestServer;
import org.dixantmittal.service.ITaskManagerRequestService;
import org.dixantmittal.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by dixant on 28/04/17.
 */
@Component
@Slf4j
public class AddTaskRequestServer implements IRequestServer {
    @Autowired
    ITaskManagerRequestService service;

    @Override
    public void serve(Task task) {
        log.info("Task to Add: {}", task);
        service.addTask(task);
    }
}
