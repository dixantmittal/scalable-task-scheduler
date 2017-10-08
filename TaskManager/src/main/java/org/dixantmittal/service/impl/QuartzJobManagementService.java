package org.dixantmittal.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.dixantmittal.builder.QuartzJobBuilder;
import org.dixantmittal.entity.Task;
import org.dixantmittal.exception.InternalServerException;
import org.dixantmittal.exception.ServiceException;
import org.dixantmittal.exception.codes.taskmanager.ServiceExceptionCodes;
import org.dixantmittal.service.IJobManagementService;
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
    public String createTask(Task task) {
        JobDetail jobDetails = QuartzJobBuilder.buildJob(task);
        Trigger jobTrigger = QuartzJobBuilder.buildTrigger(task);
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
