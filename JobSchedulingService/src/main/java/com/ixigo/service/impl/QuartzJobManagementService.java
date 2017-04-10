package com.ixigo.service.impl;

import com.ixigo.entity.JobSchedulingDetails;
import com.ixigo.exception.InternalServerException;
import com.ixigo.factory.QuartzJobBuilder;
import com.ixigo.service.IJobManagementService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by dixant on 27/03/17.
 */
@Service
@Slf4j
public class QuartzJobManagementService implements IJobManagementService {

    @Autowired
    Scheduler scheduler;

    @Override
    public String createJob(JobSchedulingDetails schedulingDetails) {
        return createJobWithJobId(schedulingDetails, null);
    }

    @Override
    public String createJobWithJobId(JobSchedulingDetails schedulingDetails, String jobId) {
        JobDetail jobDetails = QuartzJobBuilder.buildJobWithJobId(schedulingDetails, jobId);
        Trigger jobTrigger = QuartzJobBuilder.buildTrigger(schedulingDetails);
        try {
            scheduler.scheduleJob(jobDetails, jobTrigger);
        } catch (SchedulerException e) {
            log.error("Scheduler exception occurred. Exception: {}", e);
            throw new InternalServerException();
        }
        return jobDetails.getKey().getName();
    }
}
