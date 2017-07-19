package org.dixantmittal.request.taskmanager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dixantmittal.entity.Task;
import org.dixantmittal.exception.constants.RequestValidationExceptionConstants;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by dixant on 30/03/17.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteTaskRequest {
    @NotBlank(message = RequestValidationExceptionConstants.JOB_ID_IS_BLANK)
    private String taskId;

    public static class Adapter {
        public static DeleteTaskRequest adapt(Task task) {
            DeleteTaskRequest request = new DeleteTaskRequest();
            request.taskId = task.getTaskId();
            return request;
        }
    }
}
