package org.dixantmittal.taskexecutors;

import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.dixantmittal.constants.ConfigurationConstants;
import org.dixantmittal.constants.taskmanager.RestURIConstants;
import org.dixantmittal.entity.Retry;
import org.dixantmittal.entity.Task;
import org.dixantmittal.exception.GenericException;
import org.dixantmittal.httpclient.HttpMethod;
import org.dixantmittal.httpclient.HttpMode;
import org.dixantmittal.httpclient.HttpUtils;
import org.dixantmittal.response.taskmanager.AddTaskResponse;
import org.dixantmittal.utils.Configuration;

import java.time.LocalDateTime;

/**
 * Created by dixant on 10/04/17.
 */
@Slf4j
public abstract class AbstractTaskExecutor implements ITaskExecutor {

    public abstract Boolean process(String meta) throws JsonSyntaxException;

    @Override
    public void execute(Task task) {
        log.info("Task received by Task Executor. [TASK-ID]: {}", task.getTaskId());
        try {
            // perform the business logic of the task
            Boolean response = process(task.getTaskMetadata());
            if (!response) {
                retryTask(task);
            }
            log.info("Task execution success: {}. [TASK-ID]: {}", response, task.getTaskId());
        } catch (JsonSyntaxException jse) {
            log.error("Wrong task meta passed to TaskMeta: " + task.getTaskMetadata());
        } catch (Exception e) {
            log.error("Error occurred while performing task. TaskId: {}. Exception: ", task.getTaskId(), e);
            retryTask(task);
        }
    }

    private void retryTask(Task task) {

        if (task.getRetry() == null) {
            log.info("Retry details not found.");
            return;
        }

        // get retry logic
        Retry retryDetails = task.getRetry();
        int retryCount = retryDetails.getCount() + 1;
        int maxRetriesAllowed = retryDetails.getMaxRetries();
        if (retryCount > maxRetriesAllowed) {
            log.error("RETRIES EXCEEDED.");
            return;
        }
        int retryBase = retryDetails.getBase();
        int delayInSeconds = retryDetails.getDelay();
        int totalDelay = delayInSeconds * (int) Math.pow(retryCount, retryBase);

        // find the new time
        LocalDateTime newScheduledTime = task.getExecutionTime().plusSeconds(totalDelay);

        // increase retry count
        retryDetails.setCount(retryCount);

        // set new scheduled time
        task.setExecutionTime(newScheduledTime);

        log.info("Trying to reschedule Task. [TASK-ID]: {}. [NEW TIME]: {}", task.getTaskId(), task.getExecutionTime());

        try {
            String serverIp = Configuration.getGlobalProperty(ConfigurationConstants.TASK_MANAGER_SERVER_IP);
            String serverPort = Configuration.getGlobalProperty(ConfigurationConstants.TASK_MANAGER_SERVER_PORT);

            HttpUtils.processHttpRequest(
                    HttpUtils.URLBuilder.newURL()
                            .withHttpMode(HttpMode.HTTP)
                            .withServerIp(serverIp)
                            .withServerPort(serverPort)
                            .withURI(RestURIConstants.TASK_MANAGER_BASE_URI + RestURIConstants.TASK + RestURIConstants.TASK_ID)
                            .build(),
                    AddTaskResponse.class,
                    task,
                    HttpMethod.POST
            );
            log.info("TASK RESCHEDULED. [TASK-ID]: {}", task.getTaskId());
        } catch (GenericException e) {
            log.error("COULD NOT ADD TASK to TASK SCHEDULER for RETRY. [TASK-ID]: {}. Exception: ", task.getTaskId(), e);
        }
    }
}
