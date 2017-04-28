package com.ixigo.request.jobschedulingservice;

import com.ixigo.validation.jobschedulingservice.AddTaskWithJobIdRequestValidation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created by dixant on 03/04/17.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AddTaskWithJobIdRequestValidation
@NoArgsConstructor
@AllArgsConstructor
public class AddTaskWithJobIdRequest extends AddTaskRequest {
    private String jobId;

    public AddTaskWithJobIdRequest(AddTaskRequest ob) {
        super(ob);
    }
}
