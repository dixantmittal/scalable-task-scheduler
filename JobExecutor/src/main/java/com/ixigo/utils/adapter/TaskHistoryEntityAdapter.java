package com.ixigo.utils.adapter;

import com.ixigo.dbmapper.entity.TaskHistoryEntity;
import com.ixigo.entity.KafkaTaskDetails;
import com.ixigo.utils.IxigoDateUtils;

/**
 * Created by dixant on 20/04/17.
 */
public class TaskHistoryEntityAdapter {
    public static TaskHistoryEntity adapt(KafkaTaskDetails kafkaTaskDetails) {
        TaskHistoryEntity entity = new TaskHistoryEntity();
        entity.setTaskId(kafkaTaskDetails.getTaskId());
        entity.setTaskMetadata(kafkaTaskDetails.getTaskMetadata());
        entity.setTaskType(kafkaTaskDetails.getTaskType());
        entity.setOldScheduledTime(IxigoDateUtils.dateToString(kafkaTaskDetails.getScheduledTime()));
        return entity;
    }
}
