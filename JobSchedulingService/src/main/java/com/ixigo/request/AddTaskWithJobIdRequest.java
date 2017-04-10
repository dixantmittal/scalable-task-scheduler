package com.ixigo.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by dixant on 03/04/17.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AddTaskWithJobIdRequest extends AddTaskRequest {
    private String jobId;
}
