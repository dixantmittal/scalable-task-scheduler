package org.dixantmittal.builder;

import org.dixantmittal.constants.taskmanager.ServiceConstants;
import org.dixantmittal.entity.AddToExecutionQueueJob;
import org.dixantmittal.entity.Task;
import org.dixantmittal.utils.IDGenerationUtils;
import org.dixantmittal.utils.JsonUtils;
import org.quartz.*;

import java.time.ZoneId;
import java.util.Date;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

/**
 * Created by dixant on 27/03/17.
 */
public class QuartzJobBuilder {

    public static JobDetail buildJob(Task task) {

        String jobId = task.getTaskId();
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(ServiceConstants.TASK_JSON, JsonUtils.toJson(task));
        jobDataMap.put(ServiceConstants.TASK_ID, jobId);
        JobDetail jobDetail = JobBuilder.newJob(AddToExecutionQueueJob.class)
                .withIdentity(jobId, ServiceConstants.DEFAULT_GROUP_ID)
                .usingJobData(jobDataMap)
                .build();
        return jobDetail;
    }

    public static Trigger buildTrigger(Task task) {
        Trigger jobTrigger = TriggerBuilder.newTrigger()
                .withIdentity(IDGenerationUtils.generateRandomUUID(ServiceConstants.TRIGGER_IDENTIFIER), ServiceConstants.DEFAULT_GROUP_ID)
                .startAt(getStartTime(task))
                .withSchedule(getScheduleBuilder(task))
                .withPriority(task.getPriority())
                .build();

        return jobTrigger;
    }

    private static Date getStartTime(Task task) {
        switch (task.getSchedulingType()) {
            case CRON:
            default:
                return new Date();
            case ONE_TIME:
                return Date.from(task.getExecutionTime().atZone(ZoneId.systemDefault()).toInstant());
        }
    }

    private static ScheduleBuilder getScheduleBuilder(Task task) {
        switch (task.getSchedulingType()) {
            case CRON:
                return cronSchedule(task.getCronExp());
            case ONE_TIME:
            default:
                return simpleSchedule().withMisfireHandlingInstructionFireNow();
        }
    }

}
