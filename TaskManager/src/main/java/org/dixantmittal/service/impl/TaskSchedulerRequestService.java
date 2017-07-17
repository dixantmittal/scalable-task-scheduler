package org.dixantmittal.service.impl;

import org.dixantmittal.constants.ConfigurationConstants;
import org.dixantmittal.constants.jobschedulingservice.ServiceConstants;
import org.dixantmittal.entity.RequestConsumer;
import org.dixantmittal.entity.TaskSchedulingDetails;
import org.dixantmittal.enums.Status;
import org.dixantmittal.exception.InternalServerException;
import org.dixantmittal.exception.ServiceException;
import org.dixantmittal.exception.codes.jobschedulingservice.ServiceExceptionCodes;
import org.dixantmittal.request.jobschedulingservice.AddTaskWithTaskIdRequest;
import org.dixantmittal.request.jobschedulingservice.DeleteTaskRequest;
import org.dixantmittal.request.jobschedulingservice.StopSchedulerRequest;
import org.dixantmittal.response.jobschedulingservice.AddTaskResponse;
import org.dixantmittal.response.jobschedulingservice.DeleteTaskResponse;
import org.dixantmittal.response.jobschedulingservice.StartSchedulerResponse;
import org.dixantmittal.response.jobschedulingservice.StopSchedulerResponse;
import org.dixantmittal.service.IJobManagementService;
import org.dixantmittal.service.ITaskSchedulerRequestService;
import org.dixantmittal.utils.Configuration;
import org.dixantmittal.utils.adapter.TaskSchedulingDetailsAdapter;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.quartz.JobKey.jobKey;

/**
 * Created by dixant on 27/03/17.
 */
@Service
@Slf4j
public class TaskSchedulerRequestService implements ITaskSchedulerRequestService {

    private volatile Set<RequestConsumer> _THREADPOOL = new HashSet<>();
    @Autowired
    private Scheduler scheduler;
    @Autowired
    private IJobManagementService jobManagementService;
    private volatile Lock _LOCK = new ReentrantLock();

    public AddTaskResponse addTask(AddTaskWithTaskIdRequest request) {
        TaskSchedulingDetails taskDetails = TaskSchedulingDetailsAdapter.adapt(request);
        String jobId = jobManagementService.createJobWithJobId(taskDetails, request.getTaskId());
        log.info("Job added to Job Scheduler. [JOB-ID]: {}", jobId);
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
            log.info("Job removed from Job Scheduler. [JOB-ID]: {}", request.getJobId());
        } catch (SchedulerException e) {
            log.error("Scheduler exception occurred. Exception: {}", e);
            throw new InternalServerException();
        }
        return new DeleteTaskResponse(Status.SUCCESS);
    }

    @Override
    public StartSchedulerResponse startScheduler() {
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
                startNewThreads();
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
        return new StartSchedulerResponse(Status.SUCCESS);
    }

    private void startNewThreads() {
        // acquire lock
        _LOCK.lock();
        try {
            // get threads count
            int threadCount = Integer.parseInt(Configuration.getGlobalProperty(ConfigurationConstants.REQUEST_CONSUMER_THREAD_COUNT));
            log.info("Total threads allowed: {}.", threadCount);
            RequestConsumer newThread;
            while (threadCount-- > 0) {
                // create a new thread and start it
                (newThread = new RequestConsumer()).start();
                // add thread to the thread pool
                log.info("New consumer started: {}", newThread);
                _THREADPOOL.add(newThread);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            // release lock
            _LOCK.unlock();
        }
    }

    // Get lock to reduce threads.
    private void closeThreads() {
        // acquire lock
        _LOCK.lock();
        // making sure thread releases lock
        try {
            log.info("Trying to close threads one by one...");
            Iterator<RequestConsumer> itr = _THREADPOOL.iterator();
            while (itr.hasNext()) {
                // call threads close method
                RequestConsumer thread = itr.next();
                thread.close();
                // remove thread from set so that GC can collect it.
                itr.remove();
                log.info("Consumer thread removed.");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            // release lock
            _LOCK.unlock();
        }
    }

    @Override
    public StopSchedulerResponse stopScheduler(StopSchedulerRequest request) {
        // acquire lock
        _LOCK.lock();
        try {
            // check if scheduler has ever started or not been shutdown
            if (scheduler.isStarted() && !scheduler.isShutdown()) {
                // close consumer threads
                log.info("Trying to close scheduler...");
                closeThreads();
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
        return new StopSchedulerResponse(Status.SUCCESS);
    }
}
