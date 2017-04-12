package com.ixigo.service.impl;

import com.ixigo.adapter.ObjectAdapter;
import com.ixigo.constants.ServiceConstants;
import com.ixigo.entity.JobSchedulingDetails;
import com.ixigo.enums.Status;
import com.ixigo.exception.InternalServerException;
import com.ixigo.request.AddTaskRequest;
import com.ixigo.request.AddTaskWithJobIdRequest;
import com.ixigo.request.DeleteTaskRequest;
import com.ixigo.response.AddTaskResponse;
import com.ixigo.response.DeleteTaskResponse;
import com.ixigo.response.StartSchedulerResponse;
import com.ixigo.response.StopSchedulerResponse;
import com.ixigo.service.IJobManagementService;
import com.ixigo.service.IJobSchedulerRequestService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.quartz.JobKey.jobKey;

/**
 * Created by dixant on 27/03/17.
 */
@Service
@Slf4j
public class JobSchedulerRequestService implements IJobSchedulerRequestService {
    @Autowired
    Scheduler scheduler;

    @Autowired
    IJobManagementService jobManagementService;

    public AddTaskResponse addTask(AddTaskRequest request) {
        JobSchedulingDetails jobDetails = ObjectAdapter.adapt(request);
        String jobId = jobManagementService.createJob(jobDetails);
        log.debug("Job added to Job Scheduler. [JOB-ID]: {}", jobId);
        return new AddTaskResponse(Status.SUCCESS, jobId);
    }

    public AddTaskResponse addTask(AddTaskWithJobIdRequest request) {
        JobSchedulingDetails jobDetails = ObjectAdapter.adapt(request);
        String jobId = jobManagementService.createJobWithJobId(jobDetails, request.getJobId());
        log.debug("Job added to Job Scheduler. [JOB-ID]: {}", jobId);
        return new AddTaskResponse(Status.SUCCESS, jobId);
    }

    @Override
    public DeleteTaskResponse deleteTask(DeleteTaskRequest request) {
        try {
            scheduler.deleteJob(jobKey(request.getJobId(), ServiceConstants.DEFAULT_GROUP_ID));
            log.debug("Job removed from Job Scheduler. [JOB-ID]: {}", request.getJobId());
        } catch (SchedulerException e) {
            log.error("Scheduler exception occurred. Exception: {}", e);
            throw new InternalServerException();
        }
        return new DeleteTaskResponse(Status.SUCCESS);
    }

    @Override
    public StartSchedulerResponse startScheduler() {
        try {
            if (!scheduler.isStarted() || scheduler.isInStandbyMode()) {
                scheduler.start();
            }
        } catch (SchedulerException e) {
            log.error("Error occurred while starting scheduler. Error: " + e);
            throw new InternalServerException();
        }
        return new StartSchedulerResponse(Status.SUCCESS);
    }

    @Override
    public StopSchedulerResponse stopScheduler() {
        try {
            if (!scheduler.isShutdown() || scheduler.isStarted()) {
                scheduler.standby();
            }
        } catch (SchedulerException e) {
            log.error("Error occurred while stopping scheduler. Error: " + e);
            throw new InternalServerException();
        }
        return new StopSchedulerResponse(Status.SUCCESS);
    }
}
