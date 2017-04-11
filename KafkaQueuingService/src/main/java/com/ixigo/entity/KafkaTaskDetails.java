package com.ixigo.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by dixant on 22/03/17.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class KafkaTaskDetails extends JobDetails {
    String jobId;
}