package com.ixigo.service;

import com.google.gson.JsonSyntaxException;
import com.ixigo.constants.jobhandlingservice.ServiceConstants;
import com.ixigo.constants.jobschedulingservice.RestURIConstants;
import com.ixigo.entity.IxigoQueuePublisher;
import com.ixigo.enums.Status;
import com.ixigo.exception.ExceptionResponse;
import com.ixigo.exception.GenericException;
import com.ixigo.exception.ServiceException;
import com.ixigo.httpclient.HttpMethod;
import com.ixigo.httpclient.HttpMode;
import com.ixigo.httpclient.HttpUtils;
import com.ixigo.request.jobschedulingservice.AddTaskRequest;
import com.ixigo.request.jobschedulingservice.AddTaskWithJobIdRequest;
import com.ixigo.request.jobschedulingservice.DeleteTaskRequest;
import com.ixigo.response.jobschedulingservice.AddTaskResponse;
import com.ixigo.response.jobschedulingservice.DeleteTaskResponse;
import com.ixigo.utils.IDGenerationUtils;
import com.ixigo.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by dixant on 26/04/17.
 */
@Slf4j
@Service
public class JobHandlerRequestService {
    @Autowired
    private IxigoQueuePublisher queuePublisher;

    @Value("${jobscheduler.server.address}")
    private String jobschedulerAddress;

    public AddTaskResponse addTask(AddTaskWithJobIdRequest request) {
        log.debug("Add Task request received. Request: {}", request);
        Status status = queuePublisher.sendToQueue(ServiceConstants.ADD_TASK, JsonUtils.toJson(request)) ? Status.SUCCESS : Status.FAILURE;
        log.debug("Status received after publishing request: {}", status);
        return (new AddTaskResponse(status, request.getJobId()));
    }

    public AddTaskResponse addTask(AddTaskRequest addTaskRequest) {
        AddTaskWithJobIdRequest request = new AddTaskWithJobIdRequest(addTaskRequest);
        request.setJobId(IDGenerationUtils.generateRandomUUID(com.ixigo.constants.jobschedulingservice.ServiceConstants.JOB_IDENTIFIER));
        log.debug("JobId generated: {}", request.getJobId());
        return addTask(request);
    }

    public DeleteTaskResponse deleteTask(DeleteTaskRequest request) {
        log.debug("Trying to connect to scheduling service to delete job...");
        String url = HttpUtils.URLBuilder.newURL()
                .withHttpMode(HttpMode.HTTP)
                .withServerIp(jobschedulerAddress)
                .withURI(RestURIConstants.JOB_SCHEDULER_BASE_URI + RestURIConstants.TASK)
                .build();

        try {
            log.debug("Accessing URL: {}", url);
            return HttpUtils.processHttpRequest(url,
                    DeleteTaskResponse.class,
                    request,
                    HttpMethod.DELETE);
        } catch (ServiceException se) {
            try {
                log.debug("Service Exception occurred. Could not delete job");
                ExceptionResponse exception = JsonUtils.fromJson(se.getErrMessage(), ExceptionResponse.class);
                throw new ServiceException(exception.getCode(), exception.getMessage());
            } catch (JsonSyntaxException jse) {
                throw se;
            }

        } catch (GenericException ge) {
            log.debug("Scheduling Service was not available.");
            if (request.getCanRetry()) {
                log.debug("Retry mechanism is being applied.");
                Status status = queuePublisher.sendToQueue(ServiceConstants.DELETE_TASK, JsonUtils.toJson(request)) ? Status.SUCCESS : Status.FAILURE;
                return new DeleteTaskResponse(status);
            }
            throw ge;
        }
    }
}
