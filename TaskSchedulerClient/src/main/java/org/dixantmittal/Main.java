package org.dixantmittal;

import org.dixantmittal.builder.TaskSchedulerClientBuilder;
import org.dixantmittal.entity.SchedulingType;
import org.dixantmittal.entity.Task;

import java.time.LocalDateTime;

/**
 * Created by dixant on 19/07/17.
 */
public class Main {
    public static void main(String[] args) {
        Task task = new Task(SchedulingType.CRON);
        task.setCronExp("0 * 15 * * ?");
        task.setTaskType("AutoWebCheckin");

        TaskSchedulerClientBuilder
                .newBuilder()
                .topic("REQUEST_QUEUE")
                .jobSchedulerAddress("localhost:9090")
                .getClient()
                .addTask(task);
    }
}