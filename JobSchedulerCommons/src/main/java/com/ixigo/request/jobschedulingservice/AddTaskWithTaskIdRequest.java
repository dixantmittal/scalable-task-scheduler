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
public class AddTaskWithTaskIdRequest extends AddTaskRequest {
    private String taskId;

    public AddTaskWithTaskIdRequest(AddTaskRequest ob) {
        super(ob);
    }
}
