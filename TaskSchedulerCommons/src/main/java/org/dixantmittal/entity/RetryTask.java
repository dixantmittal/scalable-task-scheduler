package org.dixantmittal.entity;

import lombok.Data;

/**
 * Created by dixant on 11/04/17.
 */
@Data
public class RetryTask {
    private int retriesCount;
    private int maxRetriesAllowed;
    private int retryBase;
    private int delayInSeconds;
}
