package com.ixigo.request;

import lombok.Data;

import java.util.Calendar;

/**
 * Created by dixant on 27/03/17.
 */
@Data
public class AddTaskRequest {
    private String taskType;
    private String taskMetadata;
    private String scheduledTime;
    private String priority;
}
