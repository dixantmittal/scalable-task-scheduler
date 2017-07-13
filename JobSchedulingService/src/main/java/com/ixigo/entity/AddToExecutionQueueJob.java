package com.ixigo.entity;

import com.ixigo.constants.ConfigurationConstants;
import com.ixigo.factory.TaskQueuingServiceProvider;
import com.ixigo.service.ITaskQueuingService;
import com.ixigo.utils.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by dixant on 24/03/17.
 */

// Gets task meta from Quartz and create a Kafka Task and put it into relevant Kafka Queue.
@Slf4j
public class AddToExecutionQueueJob implements Job {

    private ITaskQueuingService taskQueuingService = TaskQueuingServiceProvider.getInstance().getTaskQueuingService();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        int maxRefireLimit = Integer.parseInt(Configuration.getGlobalProperty(ConfigurationConstants.MAX_REFIRE_LIMIT));
        log.info("Adding task to execution queue. Try count #{}", context.getRefireCount());
        if (context.getRefireCount() < maxRefireLimit) {
            JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
            log.info("Trying to add...");
            Boolean success = taskQueuingService.addJobToExecutionQueue(jobDataMap);
            if (!success) {
                log.error("Could not push task to queue. Trying again. [Task-ID]: {}", context.getJobDetail().getKey());
                JobExecutionException jee = new JobExecutionException("Could not push task to queue. Trying again.");
                jee.setRefireImmediately(true);
                throw jee;
            }
            log.info("Task published on Kafka queue. [TASK-ID]: {}");
        } else {
            log.error("Retries exceeded. [Task-ID]: {}", context.getJobDetail().getKey());
            JobExecutionException jee = new JobExecutionException("Retries exceeded");
            jee.setUnscheduleAllTriggers(true);
            throw jee;
        }
    }
}