package org.dixantmittal.service;

import com.google.gson.JsonSyntaxException;
import org.dixantmittal.constants.jobhandlingservice.ServiceConstants;
import org.dixantmittal.constants.jobschedulingservice.RestURIConstants;
import org.dixantmittal.entity.IxigoQueuePublisher;
import org.dixantmittal.enums.Status;
import org.dixantmittal.exception.ExceptionResponse;
import org.dixantmittal.exception.GenericException;
import org.dixantmittal.exception.ServiceException;
import org.dixantmittal.httpclient.HttpMethod;
import org.dixantmittal.httpclient.HttpMode;
import org.dixantmittal.httpclient.HttpUtils;
import org.dixantmittal.request.jobschedulingservice.AddTaskRequest;
import org.dixantmittal.request.jobschedulingservice.AddTaskWithTaskIdRequest;
import org.dixantmittal.request.jobschedulingservice.DeleteTaskRequest;
import org.dixantmittal.response.jobschedulingservice.AddTaskResponse;
import org.dixantmittal.response.jobschedulingservice.DeleteTaskResponse;
import org.dixantmittal.utils.IDGenerationUtils;
import org.dixantmittal.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by dixant on 26/04/17.
 */
@Slf4j
@Service
public class TaskSchedulerRequestService {
    @Autowired
    private IxigoQueuePublisher queuePublisher;

    @Value("${jobscheduler.server.address}")
    private String jobschedulerAddress;

    public AddTaskResponse addTask(AddTaskWithTaskIdRequest request) {
        log.info("Add Task request received. Request: {}", request);
        Status status = queuePublisher.sendToQueue(ServiceConstants.ADD_TASK, JsonUtils.toJson(request)) ? Status.SUCCESS : Status.FAILURE;
        log.info("Status received after publishing request: {}", status);
        return (new AddTaskResponse(status, request.getTaskId()));
    }

    public AddTaskResponse addTask(AddTaskRequest addTaskRequest) {
        AddTaskWithTaskIdRequest request = new AddTaskWithTaskIdRequest(addTaskRequest);
        request.setTaskId(IDGenerationUtils.generateRandomUUID(ServiceConstants.TASK_IDENTIFIER));
        log.info("TaskId generated: {}", request.getTaskId());
        return addTask(request);
    }

    public DeleteTaskResponse deleteTask(DeleteTaskRequest request) {
        log.info("Trying to connect to scheduling service to delete task...");
        String url = HttpUtils.URLBuilder.newURL()
                .withHttpMode(HttpMode.HTTP)
                .withServerIp(jobschedulerAddress)
                .withURI(RestURIConstants.TASK_SCHEDULER_BASE_URI + RestURIConstants.TASK)
                .build();

        try {
            log.info("Accessing URL: {}", url);
            return HttpUtils.processHttpRequest(url,
                    DeleteTaskResponse.class,
                    request,
                    HttpMethod.DELETE);
        } catch (GenericException se) {
            log.info("Task deletion request failed. Retry available?: {}", request.getCanRetry());
            if (request.getCanRetry()) {
                log.info("Retry mechanism is being applied.");
                Status status = queuePublisher.sendToQueue(ServiceConstants.DELETE_TASK, JsonUtils.toJson(request)) ? Status.SUCCESS : Status.FAILURE;
                return new DeleteTaskResponse(status);
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
}
