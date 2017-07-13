package com.ixigo.utils.adapter;

import com.ixigo.entity.KafkaTaskDetails;
import com.ixigo.request.jobschedulingservice.AddTaskWithTaskIdRequest;

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
