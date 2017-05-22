package com.ixigo.requestserver;

import com.google.gson.JsonSyntaxException;
import com.ixigo.request.jobschedulingservice.AddTaskWithJobIdRequest;
import com.ixigo.service.IJobSchedulerRequestService;
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
    IJobSchedulerRequestService service;

    @Override
    public void serve(String requestJson) {
        try {
            log.debug("creating addTask request from JSON.");
            AddTaskWithJobIdRequest request = JsonUtils.fromJson(requestJson, AddTaskWithJobIdRequest.class);
            service.addTask(request);
            log.debug("request created: {}", request);
        } catch (JsonSyntaxException jse) {
            log.error("Json could not be deserialize in to request object. [JSON]: {}", requestJson);
        }
    }
}
