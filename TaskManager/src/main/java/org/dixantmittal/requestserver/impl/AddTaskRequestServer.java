package org.dixantmittal.requestserver.impl;

import com.google.gson.JsonSyntaxException;
import org.dixantmittal.request.jobschedulingservice.AddTaskWithTaskIdRequest;
import org.dixantmittal.requestserver.IRequestServer;
import org.dixantmittal.service.ITaskSchedulerRequestService;
import org.dixantmittal.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by dixant on 28/04/17.
 */
@Component
@Slf4j
public class AddTaskRequestServer implements IRequestServer {
    @Autowired
    ITaskSchedulerRequestService service;

    @Override
    public void serve(String requestJson) {
        try {
            log.info("creating addTask request from JSON.");
            AddTaskWithTaskIdRequest request = JsonUtils.fromJson(requestJson, AddTaskWithTaskIdRequest.class);
            service.addTask(request);
            log.info("request created: {}", request);
        } catch (JsonSyntaxException jse) {
            log.error("Json could not be deserialize in to request object. [JSON]: {}", requestJson);
        }
    }
}
