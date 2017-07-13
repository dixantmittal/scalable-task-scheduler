package com.ixigo.requestserver.impl;

import com.google.gson.JsonSyntaxException;
import com.ixigo.request.jobschedulingservice.AddTaskWithTaskIdRequest;
import com.ixigo.requestserver.IRequestServer;
import com.ixigo.service.ITaskSchedulerRequestService;
import com.ixigo.utils.JsonUtils;
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
