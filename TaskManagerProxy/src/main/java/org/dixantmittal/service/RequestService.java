package org.dixantmittal.service;

import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.dixantmittal.constants.taskmanager.RestURIConstants;
import org.dixantmittal.constants.taskmanagerproxy.ServiceConstants;
import org.dixantmittal.entity.RequestProducer;
import org.dixantmittal.entity.Status;
import org.dixantmittal.exception.ExceptionResponse;
import org.dixantmittal.exception.GenericException;
import org.dixantmittal.exception.ServiceException;
import org.dixantmittal.httpclient.HttpMethod;
import org.dixantmittal.httpclient.HttpMode;
import org.dixantmittal.httpclient.HttpUtils;
import org.dixantmittal.request.taskmanager.AddTaskRequest;
import org.dixantmittal.request.taskmanager.DeleteTaskRequest;
import org.dixantmittal.response.GenericResponse;
import org.dixantmittal.response.taskmanager.AddTaskResponse;
import org.dixantmittal.utils.IDGenerationUtils;
import org.dixantmittal.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by dixant on 26/04/17.
 */
@Slf4j
@Service
public class RequestService {
    @Autowired
    private RequestProducer queuePublisher;

    @Value("${taskscheduler.server.address}")
    private String taskschedulerAddress;

    public AddTaskResponse addTaskWithTaskId(AddTaskRequest request) {
        log.info("Add Task request received. Request: {}", request);
        Status status = queuePublisher.send(ServiceConstants.ADD_TASK, JsonUtils.toJson(request)) ? Status.SUCCESS : Status.FAILURE;
        log.info("Status received after publishing request: {}", status);
        return (new AddTaskResponse(status, request.getTaskId()));
    }

    public AddTaskResponse addTask(AddTaskRequest request) {
        request.setTaskId(IDGenerationUtils.generateRandomUUID(ServiceConstants.TASK_IDENTIFIER));
        log.info("TaskId generated: {}", request.getTaskId());
        return addTaskWithTaskId(request);
    }

    public GenericResponse deleteTask(DeleteTaskRequest request) {
        log.info("Trying to connect to scheduling service to delete task...");
        String url = HttpUtils.URLBuilder.newURL()
                .withHttpMode(HttpMode.HTTP)
                .withServerIp(taskschedulerAddress)
                .withURI(RestURIConstants.TASK_MANAGER_BASE_URI + RestURIConstants.TASK)
                .build();

        try {
            log.info("Accessing URL: {}", url);
            return HttpUtils.processHttpRequest(url,
                    GenericResponse.class,
                    request,
                    HttpMethod.DELETE);
        } catch (GenericException se) {
            log.info("Task deletion request failed. Retry available?: {}", request.getCanRetry());
            if (request.getCanRetry()) {
                log.info("Retry mechanism is being applied.");
                Status status = queuePublisher.send(ServiceConstants.DELETE_TASK, JsonUtils.toJson(request)) ? Status.SUCCESS : Status.FAILURE;
                return new GenericResponse(status);
            }
            log.error("Service Exception occurred. Could not delete task");
            try {
                ExceptionResponse exception = JsonUtils.fromJson(se.getErrMessage(), ExceptionResponse.class);
                throw new ServiceException(exception.getCode(), exception.getMessage());
            } catch (JsonSyntaxException jse) {
                throw se;
            }
        }
    }

    public GenericResponse changeTopic(String topic) {
        queuePublisher.setTopic(topic);
        return new GenericResponse(Status.SUCCESS);
    }
}
