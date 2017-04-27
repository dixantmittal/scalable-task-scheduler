package com.ixigo.taskexecutors;

import com.google.gson.JsonSyntaxException;
import com.ixigo.constants.ConfigurationConstants;
import com.ixigo.constants.jobschedulingservice.RestURIConstants;
import com.ixigo.dao.ITaskDao;
import com.ixigo.dbmapper.entity.TaskHistoryEntity;
import com.ixigo.entity.KafkaTaskDetails;
import com.ixigo.entity.RetryJobDetails;
import com.ixigo.enums.Status;
import com.ixigo.exception.ServiceException;
import com.ixigo.httpclient.HttpMethod;
import com.ixigo.httpclient.HttpMode;
import com.ixigo.httpclient.HttpUtils;
import com.ixigo.response.jobschedulingservice.AddTaskResponse;
import com.ixigo.utils.Configuration;
import com.ixigo.utils.IxigoDateUtils;
import com.ixigo.utils.adapter.AddTaskWithJobIdRequestAdapter;
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
        log.debug("Job received by Task Executor. [JOB-ID]: {}", taskDetails.getJobId());
        // create a task history object
        TaskHistoryEntity taskHistory = TaskHistoryEntityAdapter.adapt(taskDetails);
        try {

            // perform the business logic of the task
            Boolean response = process(taskDetails.getTaskMetadata());

            taskHistory.setExecutionStatus((response ? Status.SUCCESS : Status.FAILURE).toString());
            if (!response) {
                retryTask(taskDetails, taskHistory);
            }
            log.debug("Job execution completed. [JOB-ID]: {}", taskDetails.getJobId());
        } catch (JsonSyntaxException jse) {
            log.error("Wrong task meta passed to TaskMeta: " + taskDetails.getTaskMetadata());
            taskHistory.setExecutionStatus(Status.FAILURE.toString());
            taskHistory.setRemarks("Wrong task meta passed to TaskMeta");
        } catch (Exception e) {
            log.error("Error occurred while performing task. JobId: {}. Exception: {}.", e, taskDetails.getJobId());
            retryTask(taskDetails, taskHistory);
            taskHistory.setExecutionStatus(Status.FAILURE.toString());
        }
        taskDao.addTaskHistory(taskHistory);
    }

    private void retryTask(KafkaTaskDetails metadata, TaskHistoryEntity taskHistory) {

        if (metadata.getRetryJobDetails() == null) {
            taskHistory.setRemarks("Retry details not found");
            return;
        }

        // get retry logic
        RetryJobDetails retryDetails = metadata.getRetryJobDetails();
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
        log.debug("Job rescheduled. [JOB-ID]: {}. [NEW TIME]: {}", metadata.getJobId(), taskHistory.getNewScheduledTime());

        taskHistory.setRemarks("Retrying task");

        try {
            String serverIp = Configuration.getGlobalProperty(ConfigurationConstants.JOB_SCHEDULING_SERVICE_SERVER_IP);
            String serverPort = Configuration.getGlobalProperty(ConfigurationConstants.JOB_SCHEDULING_SERVICE_SERVER_PORT);

            HttpUtils.processHttpRequest(
                    HttpUtils.URLBuilder.newURL()
                            .withHttpMode(HttpMode.HTTP)
                            .withServerIp(serverIp)
                            .withServerPort(serverPort)
                            .withURI(RestURIConstants.JOB_SCHEDULER_BASE_URI + RestURIConstants.TASK + RestURIConstants.JOB_ID)
                            .build(),
                    AddTaskResponse.class,
                    AddTaskWithJobIdRequestAdapter.adapt(metadata),
                    HttpMethod.POST
            );
        } catch (ServiceException e) {
            log.error("Could not add task to job scheduler for retry. Exception: ", e);
            taskHistory.setRemarks("Could not add task to job scheduler for retry.");
        }
    }
}
