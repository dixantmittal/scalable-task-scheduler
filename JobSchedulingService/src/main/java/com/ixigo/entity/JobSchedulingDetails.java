package com.ixigo.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by dixant on 28/03/17.
 */

@Getter
@Setter
public class JobSchedulingDetails extends JobDetails {
    public static final int MAX_PRIORITY = 10;
    public static final int MEDIUM_PRIORITY = 5;
    public static final int MIN_PRIORITY = 1;

    private int priority;

    public JobSchedulingDetails() {
        super();
        priority = MAX_PRIORITY;
    }
}
