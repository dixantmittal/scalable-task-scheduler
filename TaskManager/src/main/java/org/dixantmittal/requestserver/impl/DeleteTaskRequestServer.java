package org.dixantmittal.requestserver.impl;

import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.dixantmittal.request.taskmanager.DeleteTaskRequest;
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
public class DeleteTaskRequestServer implements IRequestServer {
    @Autowired
    ITaskManagerRequestService service;

    @Override
    public void serve(String requestJson) {
        try {
            log.info("creating deleteTask request from JSON.");
            DeleteTaskRequest request = JsonUtils.fromJson(requestJson, DeleteTaskRequest.class);
            service.deleteTask(request);
            log.info("delete task created: {}", request);
        } catch (JsonSyntaxException jse) {
            log.error("Json could not be deserialize in to request object. [JSON]: {}", requestJson);
        }
    }
}
