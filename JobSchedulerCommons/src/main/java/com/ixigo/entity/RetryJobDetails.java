package com.ixigo.entity;

import lombok.Data;

/**
 * Created by dixant on 11/04/17.
 */
@Data
public class RetryJobDetails {
    private int retriesCount;
    private int maxRetriesAllowed;
    private int retryBase;
    private int delayInSeconds;
}
