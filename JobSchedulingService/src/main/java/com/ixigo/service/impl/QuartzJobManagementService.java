package com.ixigo.service.impl;

import com.ixigo.entity.TaskSchedulingDetails;
import com.ixigo.exception.InternalServerException;
import com.ixigo.exception.ServiceException;
import com.ixigo.exception.codes.jobschedulingservice.ServiceExceptionCodes;
import com.ixigo.factory.QuartzJobBuilder;
import com.ixigo.service.IJobManagementService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
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
    public String createJob(TaskSchedulingDetails schedulingDetails) {
        return createJobWithJobId(schedulingDetails, null);
    }

    @Override
    public String createJobWithJobId(TaskSchedulingDetails schedulingDetails, String jobId) {
        JobDetail jobDetails = QuartzJobBuilder.buildJobWithJobId(schedulingDetails, jobId);
        Trigger jobTrigger = QuartzJobBuilder.buildTrigger(schedulingDetails);
        try {
            log.info("Adding job with taskId: {}", jobDetails.getKey());
            scheduler.scheduleJob(jobDetails, jobTrigger);
        } catch (ObjectAlreadyExistsException oaee) {
            log.error("Job ID already exists. [JOB-ID]: {}", jobDetails.getKey());
            throw new ServiceException(
                    ServiceExceptionCodes.JOB_ID_ALREADY_PRESENT.code(),
                    ServiceExceptionCodes.JOB_ID_ALREADY_PRESENT.message());
        } catch (SchedulerException e) {
            log.error("Scheduler exception occurred. Exception: {}", e);
            throw new InternalServerException();
        }
        return jobDetails.getKey().getName();
    }
}
