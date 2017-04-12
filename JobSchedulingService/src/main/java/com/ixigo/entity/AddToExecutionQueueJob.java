package com.ixigo.entity;

import com.ixigo.cache.enums.ConfigurationConstants;
import com.ixigo.factory.JobQueuingServiceProvider;
import com.ixigo.service.IJobQueuingService;
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

    private IJobQueuingService jobQueuingService = JobQueuingServiceProvider.getInstance().getJobQueuingService();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        int maxRefireLimit = Integer.parseInt(Configuration.getGlobalProperty(ConfigurationConstants.MAX_REFIRE_LIMIT));
        if (context.getRefireCount() < maxRefireLimit) {
            JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();

            Boolean success = jobQueuingService.addJobToExecutionQueue(jobDataMap);
            if (!success) {
                log.error("Could not push task to queue. Trying again. [Job-ID]: {}", context.getJobDetail().getKey());
                JobExecutionException jee = new JobExecutionException("Could not push task to queue. Trying again.");
                jee.setRefireImmediately(true);
                throw jee;
            }
            log.debug("Job published on Kafka queue. [JOB-ID]: {}");
        } else {
            log.error("Retries exceeded. [Job-ID]: {}", context.getJobDetail().getKey());
            JobExecutionException jee = new JobExecutionException("Retries exceeded");
            jee.setUnscheduleAllTriggers(true);
            throw jee;
        }
    }
}