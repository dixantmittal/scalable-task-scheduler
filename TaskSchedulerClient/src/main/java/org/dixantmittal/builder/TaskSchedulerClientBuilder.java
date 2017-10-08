package org.dixantmittal.builder;

import lombok.extern.log4j.Log4j2;
import org.dixantmittal.client.TaskSchedulerClient;
import org.dixantmittal.client.impl.TaskSchedulerClientImpl;

/**
 * Created by dixant on 19/07/17.
 */
@Log4j2
public class TaskSchedulerClientBuilder {

    private String topic;
    private String taskSchedulerAddress;

    public static TaskSchedulerClientBuilder newBuilder() {
        return new TaskSchedulerClientBuilder();
    }

    public TaskSchedulerClientBuilder topic(String topic) {
        this.topic = topic;
        return this;
    }

    public TaskSchedulerClientBuilder jobSchedulerAddress(String jobSchedulerAddress) {
        this.taskSchedulerAddress = jobSchedulerAddress;
        return this;
    }

    public TaskSchedulerClient getClient() {
        log.debug("Creating TaskSchedulerClient with topic: {}, Task Scheduler Address: {}", topic, taskSchedulerAddress);
        return new TaskSchedulerClientImpl(topic, taskSchedulerAddress);
    }
}
