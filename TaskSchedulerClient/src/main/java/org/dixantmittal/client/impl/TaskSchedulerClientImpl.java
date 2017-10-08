package org.dixantmittal.client.impl;

import com.google.gson.JsonSyntaxException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.dixantmittal.client.TaskSchedulerClient;
import org.dixantmittal.constants.taskmanager.RestURIConstants;
import org.dixantmittal.constants.taskmanagerproxy.ServiceConstants;
import org.dixantmittal.entity.SchedulingType;
import org.dixantmittal.entity.Status;
import org.dixantmittal.entity.Task;
import org.dixantmittal.exception.ExceptionResponse;
import org.dixantmittal.exception.GenericException;
import org.dixantmittal.exception.ServiceException;
import org.dixantmittal.httpclient.HttpMethod;
import org.dixantmittal.httpclient.HttpMode;
import org.dixantmittal.httpclient.HttpUtils;
import org.dixantmittal.kafka.RequestProducer;
import org.dixantmittal.response.GenericResponse;
import org.dixantmittal.utils.IDGenerationUtils;
import org.dixantmittal.utils.JsonUtils;

/**
 * Created by dixant on 24/03/17.
 */
@Log4j2
public class TaskSchedulerClientImpl implements TaskSchedulerClient {

    private String topic;
    private String taskSchedulerAddress;
    private RequestProducer producer;

    public TaskSchedulerClientImpl(String topic, String taskSchedulerAddress) {
        this.topic = topic;
        this.taskSchedulerAddress = taskSchedulerAddress;
        producer = new RequestProducer(this.topic);
    }

    @Override
    public String addTask(Task task) {
        if (StringUtils.isBlank(task.getTaskId())) {
            task.setTaskId(IDGenerationUtils.generateRandomUUID(ServiceConstants.TASK_IDENTIFIER));
            log.debug("TASK-ID created: {}", task.getTaskId());
        }
        log.debug("Add Task request received. Request: {}", task);
        Status status = producer.send(ServiceConstants.ADD_TASK, task);
        log.debug("Status received after publishing request: {}", status);
        return status == Status.SUCCESS ? task.getTaskId() : Status.FAILURE.toString();
    }

    @Override
    public String deleteTask(String taskId, Boolean retryable) {
        Task task = new Task(SchedulingType.ONE_TIME);
        task.setTaskId(taskId);

        log.info("Trying to connect to scheduling service to delete task...");
        String url = HttpUtils.URLBuilder.newURL()
                .withHttpMode(HttpMode.HTTP)
                .withServerIp(taskSchedulerAddress)
                .withURI(RestURIConstants.TASK_MANAGER_BASE_URI + RestURIConstants.TASK)
                .build();

        try {
            log.debug("Accessing URL: {}", url);
            return HttpUtils.processHttpRequest(url,
                    GenericResponse.class,
                    task,
                    HttpMethod.DELETE).toString();
        } catch (GenericException se) {
            log.info("Task deletion request failed. Retry available?: {}", retryable);
            if (retryable) {
                log.info("Retry mechanism is being applied.");
                Status status = producer.send(ServiceConstants.DELETE_TASK, task);
                return status.toString();
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