package com.ixigo.request;

import com.ixigo.enums.SchedulerMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by dixant on 12/04/17.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StopSchedulerRequest {
    private SchedulerMode mode;
}
