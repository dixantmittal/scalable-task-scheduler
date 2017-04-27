package com.ixigo.response.jobschedulingservice;

import com.ixigo.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by dixant on 28/03/17.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StartSchedulerResponse {
    private Status status;
}
