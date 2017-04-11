package com.ixigo.utils;

import com.ixigo.dbmapper.entity.TaskHistoryEntity;
import com.ixigo.entity.KafkaTaskDetails;

/**
 * Created by dixant on 03/04/17.
 */
public class ObjectAdapter {
    public static TaskHistoryEntity adapt(KafkaTaskDetails kafkaTaskDetails) {
        TaskHistoryEntity entity = new TaskHistoryEntity();
        entity.setJobId(kafkaTaskDetails.getJobId());
        entity.setTaskMetadata(kafkaTaskDetails.getTaskMetadata());
        entity.setTaskType(kafkaTaskDetails.getTaskType());
        entity.setOldScheduledTime(IxigoDateUtils.dateToString(kafkaTaskDetails.getScheduledTime()));
        return entity;
    }
}
