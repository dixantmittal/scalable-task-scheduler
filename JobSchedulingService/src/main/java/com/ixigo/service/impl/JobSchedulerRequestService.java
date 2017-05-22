package com.ixigo.service.impl;

import com.ixigo.constants.ConfigurationConstants;
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
import com.ixigo.utils.Configuration;
import com.ixigo.utils.adapter.JobSchedulingDetailsAdapter;
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
public class JobSchedulerRequestService implements IJobSchedulerRequestService {

    private volatile Set<RequestConsumer> _THREADPOOL = new HashSet<>();
    @Autowired
    private Scheduler scheduler;
    @Autowired
    private IJobManagementService jobManagementService;
    private volatile Lock _LOCK = new ReentrantLock();

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
                startNewThreads();
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
            RequestConsumer newThread;
            while (threadCount-- > 0) {
                // create a new thread and start it
                (newThread = new RequestConsumer()).start();
                // add thread to the thread pool
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
            Iterator<RequestConsumer> itr = _THREADPOOL.iterator();
            while (itr.hasNext()) {
                // call threads close method
                RequestConsumer thread = itr.next();
                thread.close();
                // remove thread from set so that GC can collect it.
                itr.remove();
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
                closeThreads();
                switch (request.getMode()) {
                    case STANDBY:
                        // if scheduler is not in stand by mode
                        if (!scheduler.isInStandbyMode())
                            scheduler.standby();
                        break;
                    case SHUTDOWN:
                        // shutdown the scheduler
                        scheduler.shutdown(true);
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
