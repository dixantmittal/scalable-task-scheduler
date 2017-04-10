package com.ixigo.taskexecutors;

import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.ixigo.dao.ITaskDao;
import com.ixigo.dbmapper.entity.TaskHistoryEntity;
import com.ixigo.entity.KafkaTaskDetails;
import com.ixigo.enums.Status;
import com.ixigo.taskmeta.TaskMeta;
import com.ixigo.utils.IxigoDateUtils;
import com.ixigo.utils.JsonUtils;
import com.ixigo.utils.ObjectAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;

/**
 * Created by dixant on 10/04/17.
 */
@Slf4j
public abstract class AbstractTaskExecutor implements ITaskExecutor {

    @Autowired
    ITaskDao taskDao;

    public abstract Boolean process(String meta) throws JsonParseException;

    @Override
    public void execute(KafkaTaskDetails taskDetails) {
        TaskHistoryEntity taskHistory = ObjectAdapter.adapt(taskDetails);
        try {

            Boolean response = process(taskDetails.getTaskMetadata());

            System.out.println(Thread.currentThread().getName() + "  " + taskDetails);
            Status status = response ? Status.SUCCESS : Status.FAILURE;
            taskHistory.setExecutionStatus(status.toString());
            if(!response) {
                retryTask(taskDetails, taskHistory);
            }

        } catch (JsonSyntaxException jse) {
            log.error("Wrong task meta passed to TaskMeta: " + taskDetails.getTaskMetadata());
            taskHistory.setExecutionStatus(Status.FAILURE.toString());
            // TODO log error in db
        } catch (Exception e) {
            log.error("Error occurred while performing task. Exception: " + e);
            retryTask(taskDetails, taskHistory);
            taskHistory.setExecutionStatus(Status.FAILURE.toString());
        }
        taskDao.addTaskHistory(taskHistory);
    }

    private void retryTask(KafkaTaskDetails taskDetails, TaskHistoryEntity entity) {

        // get retry logic
        TaskMeta meta = JsonUtils
                .fromJson(taskDetails.getTaskMetadata(), TaskMeta.class);

        // Calculate new date/time
        int retryCount = meta.getRetryCount();
        int retryBase = meta.getRetryBase();
        int waitTimeInSeconds = meta.getWaitTimeInSeconds();
        int delay = (int) Math.pow(waitTimeInSeconds * retryCount, retryBase);
        Calendar newScheduledTime = IxigoDateUtils.add(taskDetails.getScheduledTime(), Calendar.SECOND, delay);

        // increase retry count
        meta.setRetryCount(retryCount + 1);

        // create new task object
        KafkaTaskDetails newTaskDetails = new KafkaTaskDetails();
        newTaskDetails.setJobId(taskDetails.getJobId());
        newTaskDetails.setTaskMetadata(JsonUtils.toJson(meta));
        newTaskDetails.setTaskType(taskDetails.getTaskType());
        newTaskDetails.setScheduledTime(newScheduledTime);

        //TODO call scheduling service to reschedule

        // add new time to task history
        entity.setNewScheduledTime(IxigoDateUtils.dateToString(newScheduledTime));
    }
}
