package org.dixantmittal.response.taskmanager;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.dixantmittal.entity.Status;
import org.dixantmittal.response.GenericResponse;

/**
 * Created by dixant on 27/03/17.
 */
@Data
@NoArgsConstructor
public class AddTaskResponse extends GenericResponse {
    private String jobId;

    public AddTaskResponse(Status status, String jobId) {
        this.status = status;
        this.jobId = jobId;
    }
}
