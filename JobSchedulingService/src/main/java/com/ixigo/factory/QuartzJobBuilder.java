package com.ixigo.factory;

import com.ixigo.constants.jobschedulingservice.ServiceConstants;
import com.ixigo.entity.AddToExecutionQueueJob;
import com.ixigo.entity.TaskSchedulingDetails;
import com.ixigo.utils.IDGenerationUtils;
import com.ixigo.utils.JsonUtils;
import org.quartz.*;

import java.util.Date;
import java.time.ZoneId;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

/**
 * Created by dixant on 27/03/17.
 */
public class QuartzJobBuilder {
    public static JobDetail buildJob(TaskSchedulingDetails jobDetails) {
        return buildJobWithJobId(jobDetails, null);
    }

    public static JobDetail buildJobWithJobId(TaskSchedulingDetails jobDetails, String jobId) {

        jobId = (jobId == null) ? IDGenerationUtils.generateRandomUUID(ServiceConstants.JOB_IDENTIFIER) : jobId;
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(ServiceConstants.JOB_DETAILS, JsonUtils.toJson(jobDetails));
        jobDataMap.put(ServiceConstants.JOB_ID, jobId);
        JobDetail jobDetail = JobBuilder.newJob(AddToExecutionQueueJob.class)
                .withIdentity(jobId, ServiceConstants.DEFAULT_GROUP_ID)
                .usingJobData(jobDataMap)
                .build();
        return jobDetail;
    }

    public static Trigger buildTrigger(TaskSchedulingDetails jobDetails) {
        int priority = jobDetails.getPriority();
        Trigger jobTrigger = TriggerBuilder.newTrigger()
                .withIdentity(IDGenerationUtils.generateRandomUUID(ServiceConstants.TRIGGER_IDENTIFIER), ServiceConstants.DEFAULT_GROUP_ID)
                .startAt(Date.from(jobDetails.getScheduledTime().atZone(ZoneId.systemDefault()).toInstant()))
                .withSchedule(simpleSchedule().withMisfireHandlingInstructionFireNow())
                .withPriority(priority)
                .build();

        return jobTrigger;
    }

}
