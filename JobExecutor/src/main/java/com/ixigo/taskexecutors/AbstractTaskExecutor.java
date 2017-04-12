package com.ixigo.taskexecutors;

import com.google.gson.JsonSyntaxException;
import com.ixigo.dao.ITaskDao;
import com.ixigo.dbmapper.entity.TaskHistoryEntity;
import com.ixigo.entity.KafkaTaskDetails;
import com.ixigo.entity.RetryJobDetails;
import com.ixigo.enums.Status;
import com.ixigo.utils.IxigoDateUtils;
import com.ixigo.utils.ObjectAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * Created by dixant on 10/04/17.
 */
@Slf4j
public abstract class AbstractTaskExecutor implements ITaskExecutor {

    @Autowired
    ITaskDao taskDao;

    public abstract Boolean process(String meta) throws JsonSyntaxException;

    @Override
    public void execute(KafkaTaskDetails taskDetails) {
        log.debug("Job received by Task Executor. [JOB-ID]: {}", taskDetails.getJobId());
        // create a task history object
        TaskHistoryEntity taskHistory = ObjectAdapter.adapt(taskDetails);
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

        //TODO call scheduling service to reschedule

        // add new time to task history
        taskHistory.setNewScheduledTime(IxigoDateUtils.dateToString(newScheduledTime));
        log.debug("Job rescheduled. [JOB-ID]: {}. [NEW TIME]: {}", metadata.getJobId(), taskHistory.getNewScheduledTime());

        taskHistory.setRemarks("Retrying task");
    }
}
