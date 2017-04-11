package com.ixigo.dbmapper.entity;

import lombok.Data;

/**
 * Created by dixant on 03/04/17.
 */
@Data
public class TaskHistoryEntity {
    private String jobId;
    private String taskType;
    private String taskMetadata;
    private String executionStatus;
    private String oldScheduledTime;
    private String newScheduledTime;
    private String remarks;
}