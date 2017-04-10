package com.ixigo.taskexecutors.impl;

import com.google.gson.JsonSyntaxException;
import com.ixigo.dao.ITaskDao;
import com.ixigo.dbmapper.entity.TaskHistoryEntity;
import com.ixigo.entity.KafkaTaskDetails;
import com.ixigo.enums.Status;
import com.ixigo.taskexecutors.ITaskExecutor;
import com.ixigo.taskmeta.TaskMeta;
import com.ixigo.taskmeta.YatraCheckStatusTaskMeta;
import com.ixigo.utils.JsonUtils;
import com.ixigo.utils.ObjectAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by dixant on 29/03/17.
 */
@Slf4j
@Component
public class YatraCheckStatusTaskExecutor implements ITaskExecutor {

    @Autowired
    ITaskDao taskDao;

    @Override
    public void execute(KafkaTaskDetails taskDetails) {
        TaskHistoryEntity taskHistory = ObjectAdapter.adapt(taskDetails);
        try {
            TaskMeta yatraTaskMeta = JsonUtils
                    .fromJson(taskDetails.getTaskMetadata(), YatraCheckStatusTaskMeta.class);

            // TODO write business logic for Yatra check status
            System.out.println(Thread.currentThread().getName() + "  " + taskDetails);
            taskHistory.setExecutionStatus(Status.SUCCESS.toString());

        } catch (JsonSyntaxException jse) {
            log.error("Wrong task meta passed to YatraCheckStatusTaskExecutor. TaskMeta: " + taskDetails.getTaskMetadata());
            taskHistory.setExecutionStatus(Status.FAILURE.toString());
            // TODO log error in db
        } catch (Exception e) {
            log.error("Error occurred while performing task. Exception: " + e);
            retryTask(taskDetails, taskHistory);
            taskHistory.setExecutionStatus(Status.FAILURE.toString());
        }
        taskDao.addTaskHistory(taskHistory);
    }


}
