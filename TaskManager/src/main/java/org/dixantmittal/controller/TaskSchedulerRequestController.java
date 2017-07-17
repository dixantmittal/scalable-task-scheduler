package org.dixantmittal.controller;

import org.dixantmittal.cache.service.ICacheBuilder;
import org.dixantmittal.constants.jobschedulingservice.RestURIConstants;
import org.dixantmittal.enums.SchedulerMode;
import org.dixantmittal.enums.Status;
import org.dixantmittal.exception.RequestValidationException;
import org.dixantmittal.exception.codes.jobschedulingservice.RequestValidationExceptionCodes;
import org.dixantmittal.request.jobschedulingservice.DeleteTaskRequest;
import org.dixantmittal.request.jobschedulingservice.StopSchedulerRequest;
import org.dixantmittal.response.ReloadCacheResponse;
import org.dixantmittal.response.jobschedulingservice.DeleteTaskResponse;
import org.dixantmittal.response.jobschedulingservice.StartSchedulerResponse;
import org.dixantmittal.response.jobschedulingservice.StopSchedulerResponse;
import org.dixantmittal.service.ITaskSchedulerRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

/**
 * Created by dixant on 27/03/17.
 */
@Controller
@Slf4j
@RequestMapping(value = RestURIConstants.TASK_SCHEDULER_BASE_URI)
public class TaskSchedulerRequestController {

    @Autowired
    Validator validator;

    @Autowired
    private ITaskSchedulerRequestService requestService;
    @Autowired
    private ICacheBuilder cacheBuilder;

    @RequestMapping(
            value = RestURIConstants.TASK,
            method = RequestMethod.DELETE,
            produces = RestURIConstants.APPLICATION_JSON)
    @ResponseBody
    DeleteTaskResponse deleteTask(@RequestParam(value = "taskId", required = false) String jobId) {
        DeleteTaskRequest request = new DeleteTaskRequest();
        request.setJobId(jobId);
        return requestService.deleteTask(validateRequest(request));
    }

    @RequestMapping(
            value = RestURIConstants.START_SCHEDULER,
            method = RequestMethod.POST,
            produces = RestURIConstants.APPLICATION_JSON)
    @ResponseBody
    StartSchedulerResponse startScheduler() {
        return requestService.startScheduler();
    }

    @RequestMapping(
            value = RestURIConstants.STOP_SCHEDULER,
            method = RequestMethod.DELETE,
            produces = RestURIConstants.APPLICATION_JSON)
    @ResponseBody
    StopSchedulerResponse stopScheduler(@RequestParam(value = "mode", required = false) String mode) {
        StopSchedulerRequest request = new StopSchedulerRequest(SchedulerMode.forName(mode));
        return requestService.stopScheduler(validateRequest(request));
    }

    @RequestMapping(value = RestURIConstants.CACHE_RELOAD, method = RequestMethod.POST, produces = RestURIConstants.APPLICATION_JSON)
    @ResponseBody
    public ReloadCacheResponse reloadCache() {
        cacheBuilder.buildCaches();
        return new ReloadCacheResponse(Status.SUCCESS);
    }

    <T> T validateRequest(T request) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(request);
        if (constraintViolations != null && constraintViolations.size() > 0) {
            log.error("Error occurred while validating request. Request: {}", request.getClass().getName());
            RequestValidationExceptionCodes error = RequestValidationExceptionCodes
                    .forName(constraintViolations.iterator().next().getMessage());
            throw new RequestValidationException(
                    error.code(),
                    error.message()
            );
        }
        return request;
    }
}
