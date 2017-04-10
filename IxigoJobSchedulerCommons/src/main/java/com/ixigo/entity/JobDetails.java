package com.ixigo.entity;

import lombok.Data;

import java.util.Calendar;

/**
 * Created by dixant on 28/03/17.
 */
@Data
public abstract class JobDetails {
    protected String taskType;
    protected String taskMetadata;
    protected Calendar scheduledTime;
}
