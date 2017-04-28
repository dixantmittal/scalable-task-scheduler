package com.ixigo.requestserver;

import com.google.gson.JsonSyntaxException;
import com.ixigo.request.jobschedulingservice.DeleteTaskRequest;
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
public class DeleteTaskRequestServer implements IRequestServer {
    @Autowired
    IJobSchedulerRequestService service;

    @Override
    public void serve(String requestJson) {
        try {
            DeleteTaskRequest request = JsonUtils.fromJson(requestJson, DeleteTaskRequest.class);
            service.deleteTask(request);
        } catch (JsonSyntaxException jse) {
            log.error("Json could not be deserialize in to request object. [JSON]: {}", requestJson);
        }
    }
}
