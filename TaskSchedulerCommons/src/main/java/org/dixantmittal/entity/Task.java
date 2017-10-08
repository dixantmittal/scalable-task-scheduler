package org.dixantmittal.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created by dixant on 28/03/17.
 */
@Data
@AllArgsConstructor
public class Task {
    private String taskId;
    private SchedulingType schedulingType;
    private String taskType;
    private String taskMetadata;
    private Retry retry;
    private int priority;
    private LocalDateTime executionTime;
    private String cronExp;

    public Task(SchedulingType type) {
        this.schedulingType = type;
    }
}
