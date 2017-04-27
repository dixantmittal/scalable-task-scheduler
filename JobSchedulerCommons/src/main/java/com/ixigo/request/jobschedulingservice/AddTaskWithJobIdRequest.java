package com.ixigo.request.jobschedulingservice;

import com.ixigo.validation.jobschedulingservice.AddTaskWithJobIdRequestValidation;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by dixant on 03/04/17.
 */
@Data
@EqualsAndHashCode (callSuper = true)
@AddTaskWithJobIdRequestValidation
public class AddTaskWithJobIdRequest extends AddTaskRequest {
    private String jobId;
}