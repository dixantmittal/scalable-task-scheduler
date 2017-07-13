package com.ixigo.taskexecutors;

import com.google.gson.JsonSyntaxException;
import com.ixigo.constants.ConfigurationConstants;
import com.ixigo.constants.jobschedulingservice.RestURIConstants;
import com.ixigo.dao.ITaskDao;
import com.ixigo.dbmapper.entity.TaskHistoryEntity;
import com.ixigo.entity.KafkaTaskDetails;
import com.ixigo.entity.RetryTaskDetails;
import com.ixigo.enums.Status;
import com.ixigo.exception.GenericException;
import com.ixigo.httpclient.HttpMethod;
import com.ixigo.httpclient.HttpMode;
import com.ixigo.httpclient.HttpUtils;
import com.ixigo.response.jobschedulingservice.AddTaskResponse;
import com.ixigo.utils.Configuration;
import com.ixigo.utils.IxigoDateUtils;
import com.ixigo.utils.adapter.AddTaskWithTaskIdRequestAdapter;
import com.ixigo.utils.adapter.TaskHistoryEntityAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * Created by dixant on 10/04/17.
 */
@Slf4j
public abstract class AbstractTaskExecutor implements ITaskExecutor {

    @Autowired
    private ITaskDao taskDao;

    public abstract Boolean process(String meta) throws JsonSyntaxException;

    @Override
    public void execute(KafkaTaskDetails taskDetails) {
        log.info("Task received by Task Executor. [JOB-ID]: {}", taskDetails.getTaskId());
        // create a task history object
        TaskHistoryEntity taskHistory = TaskHistoryEntityAdapter.adapt(taskDetails);
        try {

            // perform the business logic of the task
            Boolean response = process(taskDetails.getTaskMetadata());

            taskHistory.setExecutionStatus((response ? Status.SUCCESS : Status.FAILURE).toString());
            if (!response) {
                retryTask(taskDetails, taskHistory);
            }
            log.info("Task execution success: {}. [TASK-ID]: {}", response, taskDetails.getTaskId());
        } catch (JsonSyntaxException jse) {
            log.error("Wrong task meta passed to TaskMeta: " + taskDetails.getTaskMetadata());
            taskHistory.setExecutionStatus(Status.FAILURE.toString());
            taskHistory.setRemarks("Wrong task meta passed to TaskMeta");
        } catch (Exception e) {
            log.error("Error occurred while performing task. TaskId: {}. Exception: ", taskDetails.getTaskId(), e);
            retryTask(taskDetails, taskHistory);
            taskHistory.setExecutionStatus(Status.FAILURE.toString());
        }
        taskDao.addTaskHistory(taskHistory);
    }

    private void retryTask(KafkaTaskDetails metadata, TaskHistoryEntity taskHistory) {

        if (metadata.getRetryTaskDetails() == null) {
            taskHistory.setRemarks("Retry details not found");
            return;
        }

        // get retry logic
        RetryTaskDetails retryDetails = metadata.getRetryTaskDetails();
        int retryCount = retryDetails.getRetriesCount() + 1;
        int maxRetriesAllowed = retryDetails.getMaxRetriesAllowed();
        if (retryCount > maxRetriesAllowed) {
            taskHistory.setRemarks("Retries exceeded");
            return;
        }
        int retryBase = retryDetails.getRetryBase();
        int delayInSeconds = retryDetails.getDelayInSeconds();
        int totalDelay = delayInSeconds * (int) Math.pow(retryCount, retryBase);

        // find the new time
        LocalDateTime newScheduledTime = metadata.getScheduledTime().plusSeconds(totalDelay);

        // increase retry count
        retryDetails.setRetriesCount(retryCount);

        // set new scheduled time
        metadata.setScheduledTime(newScheduledTime);

        // add new time to task history
        taskHistory.setNewScheduledTime(IxigoDateUtils.dateToString(newScheduledTime));
        log.info("Trying to reschedule Task. [TASK-ID]: {}. [NEW TIME]: {}", metadata.getTaskId(), taskHistory.getNewScheduledTime());

        taskHistory.setRemarks("Retrying task");

        try {
            String serverIp = Configuration.getGlobalProperty(ConfigurationConstants.JOB_SCHEDULING_SERVICE_SERVER_IP);
            String serverPort = Configuration.getGlobalProperty(ConfigurationConstants.JOB_SCHEDULING_SERVICE_SERVER_PORT);

            HttpUtils.processHttpRequest(
                    HttpUtils.URLBuilder.newURL()
                            .withHttpMode(HttpMode.HTTP)
                            .withServerIp(serverIp)
                            .withServerPort(serverPort)
                            .withURI(RestURIConstants.TASK_SCHEDULER_BASE_URI + RestURIConstants.TASK + RestURIConstants.JOB_ID)
                            .build(),
                    AddTaskResponse.class,
                    AddTaskWithTaskIdRequestAdapter.adapt(metadata),
                    HttpMethod.POST
            );
            log.info("Task rescheduled. [TASK-ID]: {}. [NEW TIME]: {}", metadata.getTaskId(), taskHistory.getNewScheduledTime());
        } catch (GenericException e) {
            log.error("Could not add task to task scheduler for retry. [TASK-ID]: {}. Exception: {}", taskHistory.getTaskId(), e);
            taskHistory.setRemarks("Could not add task to task scheduler for retry. " +
                    "Reason: " + e.getMessage());
        }
    }
}
