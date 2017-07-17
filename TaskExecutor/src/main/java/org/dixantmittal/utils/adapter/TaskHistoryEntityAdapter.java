package org.dixantmittal.utils.adapter;

import org.dixantmittal.dbmapper.entity.TaskHistoryEntity;
import org.dixantmittal.entity.KafkaTaskDetails;
import org.dixantmittal.utils.IxigoDateUtils;

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
