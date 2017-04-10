package com.ixigo.taskmeta;

import lombok.Data;

/**
 * Created by dixant on 10/04/17.
 */
@Data
public class TaskMeta {
    private int retryCount;
    private int retryBase;
    private int waitTimeInSeconds;
}
