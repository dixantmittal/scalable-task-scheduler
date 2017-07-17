package org.dixantmittal.request.jobschedulingservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created by dixant on 03/04/17.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class AddTaskWithTaskIdRequest extends AddTaskRequest {
    private String taskId;

    public AddTaskWithTaskIdRequest(AddTaskRequest ob) {
        super(ob);
    }
}
