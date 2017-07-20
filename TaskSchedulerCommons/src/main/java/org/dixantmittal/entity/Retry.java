package org.dixantmittal.entity;

import lombok.Data;

/**
 * Created by dixant on 11/04/17.
 */
@Data
public class Retry {
    private int count;
    private int maxRetries;
    private int base;
    private int delay;
}
