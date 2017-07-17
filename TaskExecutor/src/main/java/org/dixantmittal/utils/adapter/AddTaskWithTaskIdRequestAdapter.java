package org.dixantmittal.utils.adapter;

import org.dixantmittal.entity.KafkaTaskDetails;
import org.dixantmittal.request.jobschedulingservice.AddTaskWithTaskIdRequest;

import java.sql.Timestamp;

/**
 * Created by dixant on 20/04/17.
 */
public class AddTaskWithTaskIdRequestAdapter {
    public static AddTaskWithTaskIdRequest adapt(KafkaTaskDetails kafkaTaskDetails) {
        AddTaskWithTaskIdRequest request = new AddTaskWithTaskIdRequest();
        request.setTaskId(kafkaTaskDetails.getTaskId());
        request.setRetryTaskDetails(kafkaTaskDetails.getRetryTaskDetails());
        request.setTaskType(kafkaTaskDetails.getTaskType());
        request.setTaskMetadata(kafkaTaskDetails.getTaskMetadata());
        request.setScheduledTime(Timestamp.valueOf(kafkaTaskDetails.getScheduledTime()));
        return request;
    }
}
