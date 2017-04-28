package com.ixigo.service.impl;

import com.ixigo.constants.jobschedulingservice.ServiceConstants;
import com.ixigo.entity.JobSchedulingDetails;
import com.ixigo.entity.RequestConsumer;
import com.ixigo.enums.Status;
import com.ixigo.exception.InternalServerException;
import com.ixigo.exception.ServiceException;
import com.ixigo.exception.codes.jobschedulingservice.ServiceExceptionCodes;
import com.ixigo.request.jobschedulingservice.AddTaskWithJobIdRequest;
import com.ixigo.request.jobschedulingservice.DeleteTaskRequest;
import com.ixigo.request.jobschedulingservice.StopSchedulerRequest;
import com.ixigo.response.jobschedulingservice.AddTaskResponse;
import com.ixigo.response.jobschedulingservice.DeleteTaskResponse;
import com.ixigo.response.jobschedulingservice.StartSchedulerResponse;
import com.ixigo.response.jobschedulingservice.StopSchedulerResponse;
import com.ixigo.service.IJobManagementService;
import com.ixigo.service.IJobSchedulerRequestService;
import com.ixigo.utils.adapter.JobSchedulingDetailsAdapter;
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
    private RequestConsumer requestConsumer;

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private IJobManagementService jobManagementService;

    private Thread consumerThread;

//    public AddTaskResponse addTask(AddTaskRequest request) {
//        JobSchedulingDetails jobDetails = JobSchedulingDetailsAdapter.adapt(request);
//        String jobId = jobManagementService.createJob(jobDetails);
//        log.debug("Job added to Job Scheduler. [JOB-ID]: {}", jobId);
//        return new AddTaskResponse(Status.SUCCESS, jobId);
//    }

    public AddTaskResponse addTask(AddTaskWithJobIdRequest request) {
        JobSchedulingDetails jobDetails = JobSchedulingDetailsAdapter.adapt(request);
        String jobId = jobManagementService.createJobWithJobId(jobDetails, request.getJobId());
        log.debug("Job added to Job Scheduler. [JOB-ID]: {}", jobId);
        return new AddTaskResponse(Status.SUCCESS, jobId);
    }

    @Override
    public DeleteTaskResponse deleteTask(DeleteTaskRequest request) {
        try {
            if (scheduler.checkExists(jobKey(request.getJobId(), ServiceConstants.DEFAULT_GROUP_ID))) {
                scheduler.deleteJob(jobKey(request.getJobId(), ServiceConstants.DEFAULT_GROUP_ID));
            } else {
                log.error("JOB ID does not exist. [JOB-ID]: {}", request.getJobId());
                throw new ServiceException(
                        ServiceExceptionCodes.JOB_ID_DOES_NOT_EXIST.code(),
                        ServiceExceptionCodes.JOB_ID_DOES_NOT_EXIST.message()
                );
            }
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
            if (scheduler.isShutdown()) {
                throw new ServiceException(ServiceExceptionCodes.SCHEDULER_HAS_BEEN_SHUTDOWN.code(),
                        ServiceExceptionCodes.SCHEDULER_HAS_BEEN_SHUTDOWN.message());
            }
            if (!scheduler.isStarted() || scheduler.isInStandbyMode()) {
                requestConsumer.close();
                (consumerThread = new Thread(requestConsumer)).start();
                scheduler.start();
            }
        } catch (SchedulerException e) {
            log.error("Error occurred while starting scheduler. Error: " + e);
            throw new InternalServerException();
        }
        return new StartSchedulerResponse(Status.SUCCESS);
    }

    @Override
    public StopSchedulerResponse stopScheduler(StopSchedulerRequest request) {
        try {
            if (!scheduler.isShutdown() || scheduler.isStarted()) {
                requestConsumer.close();
                switch (request.getMode()) {
                    case STANDBY:
                        scheduler.standby();
                        break;
                    case SHUTDOWN:
                        scheduler.shutdown();
                        break;
                }
            }
        } catch (SchedulerException e) {
            log.error("Error occurred while stopping scheduler. Error: " + e);
            throw new InternalServerException();
        }
        return new StopSchedulerResponse(Status.SUCCESS);
    }
}
