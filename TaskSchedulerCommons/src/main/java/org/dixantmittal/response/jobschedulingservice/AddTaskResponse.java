package org.dixantmittal.response.jobschedulingservice;

import org.dixantmittal.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by dixant on 27/03/17.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddTaskResponse {
    private Status status;
    private String jobId;
}
