package org.dixantmittal.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.dixantmittal.constants.taskmanager.ServiceConstants;
import org.dixantmittal.entity.RequestConsumers;
import org.dixantmittal.entity.Status;
import org.dixantmittal.entity.Task;
import org.dixantmittal.exception.InternalServerException;
import org.dixantmittal.exception.ServiceException;
import org.dixantmittal.exception.codes.taskmanager.ServiceExceptionCodes;
import org.dixantmittal.request.taskmanager.SchedulerRequest;
import org.dixantmittal.response.GenericResponse;
import org.dixantmittal.response.taskmanager.AddTaskResponse;
import org.dixantmittal.service.IJobManagementService;
import org.dixantmittal.service.ITaskManagerRequestService;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.quartz.JobKey.jobKey;

/**
 * Created by dixant on 27/03/17.
 */
@Service
@Slf4j
public class TaskManagerRequestService implements ITaskManagerRequestService {

    @Autowired
    private Scheduler scheduler;
    @Autowired
    private IJobManagementService jobManagementService;
    private volatile Lock _LOCK = new ReentrantLock();

    public AddTaskResponse addTask(Task task) {
        String jobId = jobManagementService.createTask(task);
        log.info("Job added to Job Scheduler. [JOB-ID]: {}", jobId);
        return new AddTaskResponse(Status.SUCCESS, jobId);
    }

    @Override
    public GenericResponse deleteTask(String taskId) {
        try {
            if (scheduler.checkExists(jobKey(taskId, ServiceConstants.DEFAULT_GROUP_ID))) {
                scheduler.deleteJob(jobKey(taskId, ServiceConstants.DEFAULT_GROUP_ID));
            } else {
                log.error("TASK ID does not exist. [TASK-ID]: {}", taskId);
                throw new ServiceException(
                        ServiceExceptionCodes.TASK_ID_DOES_NOT_EXIST.code(),
                        ServiceExceptionCodes.TASK_ID_DOES_NOT_EXIST.message()
                );
            }
            log.info("Job removed from Job Scheduler. [JOB-ID]: {}", taskId);
        } catch (SchedulerException e) {
            log.error("Scheduler exception occurred. Exception: {}", e);
            throw new InternalServerException();
        }
        return new GenericResponse(Status.SUCCESS);
    }

    @Override
    public GenericResponse startScheduler() {
        // acquire lock
        _LOCK.lock();
        try {
            // check if shutdown has not been called.
            if (scheduler.isShutdown()) {
                throw new ServiceException(ServiceExceptionCodes.SCHEDULER_HAS_BEEN_SHUTDOWN.code(),
                        ServiceExceptionCodes.SCHEDULER_HAS_BEEN_SHUTDOWN.message());
            }
            // only start the consumer if it is in stand by mode.
            if (!scheduler.isStarted() || scheduler.isInStandbyMode()) {
                log.info("Starting new consumer threads to poll requests.");
                RequestConsumers.startNewThreads();
                log.info("Starting quartz scheduler.");
                scheduler.start();
            }
        } catch (SchedulerException e) {
            log.error("Error occurred while starting scheduler. Error: " + e);
            throw new InternalServerException();
        } finally {
            // release lock
            _LOCK.unlock();
        }
        return new GenericResponse(Status.SUCCESS);
    }

    @Override
    public GenericResponse stopScheduler(SchedulerRequest request) {
        // acquire lock
        _LOCK.lock();
        try {
            // check if scheduler has ever started or not been shutdown
            if (scheduler.isStarted() && !scheduler.isShutdown()) {
                // close consumer threads
                log.info("Trying to close scheduler...");
                RequestConsumers.closeThreads();
                switch (request.getMode()) {
                    case STANDBY:
                        // if scheduler is not in stand by mode
                        if (!scheduler.isInStandbyMode()) {
                            scheduler.standby();
                            log.info("scheduler has been moved to stand by mode...");
                        }
                        break;
                    case SHUTDOWN:
                        // shutdown the scheduler
                        scheduler.shutdown(true);
                        log.info("scheduler has been shutdown");
                        break;
                }
            }
        } catch (SchedulerException e) {
            log.error("Error occurred while stopping scheduler. Error: " + e);
            throw new InternalServerException();
        } finally {
            // release lock
            _LOCK.unlock();
        }
        return new GenericResponse(Status.SUCCESS);
    }
}
