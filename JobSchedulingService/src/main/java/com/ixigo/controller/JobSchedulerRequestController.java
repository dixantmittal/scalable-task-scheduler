package com.ixigo.controller;

import com.ixigo.cache.service.ICacheBuilder;
import com.ixigo.constants.jobschedulingservice.RestURIConstants;
import com.ixigo.enums.SchedulerMode;
import com.ixigo.enums.Status;
import com.ixigo.exception.RequestValidationException;
import com.ixigo.exception.codes.jobschedulingservice.RequestValidationExceptionCodes;
import com.ixigo.request.jobschedulingservice.*;
import com.ixigo.response.ReloadCacheResponse;
import com.ixigo.response.jobschedulingservice.*;
import com.ixigo.service.IJobSchedulerRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import java.util.Set;

/**
 * Created by dixant on 27/03/17.
 */
@Controller
@Slf4j
@RequestMapping(value = RestURIConstants.JOB_SCHEDULER_BASE_URI)
public class JobSchedulerRequestController {

    @Autowired
    Validator validator;

    @Autowired
    private IJobSchedulerRequestService requestService;
    @Autowired
    private ICacheBuilder cacheBuilder;

    @RequestMapping(
            value = RestURIConstants.TASK,
            method = RequestMethod.POST,
            produces = RestURIConstants.APPLICATION_JSON)
    @ResponseBody
    AddTaskResponse addTask(@RequestBody @Valid AddTaskRequest request, BindingResult results) {
        if (results.hasErrors()) {
            RequestValidationExceptionCodes error = RequestValidationExceptionCodes.forName(results.getAllErrors().get(0).getDefaultMessage());
            log.error("Error occurred while adding task. Request: " + request + "\nError: " + error);
            throw new RequestValidationException(error.code(), error.message());
        }
        return requestService.addTask(request);
    }

    @RequestMapping(
            value = RestURIConstants.TASK + RestURIConstants.JOB_ID,
            method = RequestMethod.POST,
            produces = RestURIConstants.APPLICATION_JSON)
    @ResponseBody
    AddTaskResponse addTask(@RequestBody @Valid AddTaskWithJobIdRequest request, BindingResult results) {
        if (results.hasErrors()) {
            RequestValidationExceptionCodes error = RequestValidationExceptionCodes.forName(results.getAllErrors().get(0).getDefaultMessage());
            log.error("Error occurred while adding task. Request: " + request + "\nError: " + error);
            throw new RequestValidationException(error.code(), error.message());
        }
        return requestService.addTask(request);
    }

    @RequestMapping(
            value = RestURIConstants.TASK,
            method = RequestMethod.DELETE,
            produces = RestURIConstants.APPLICATION_JSON)
    @ResponseBody
    DeleteTaskResponse deleteTask(@RequestParam("job-id") String jobId) {
        DeleteTaskRequest request = new DeleteTaskRequest();
        request.setJobId(jobId);
        validateRequest(request);
        return requestService.deleteTask(request);
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
    StopSchedulerResponse stopScheduler(@RequestParam("mode") String mode) {
        StopSchedulerRequest request = new StopSchedulerRequest(SchedulerMode.forName(mode));
        validateRequest(request);
        return requestService.stopScheduler(request);
    }

    @RequestMapping(value = RestURIConstants.CACHE_RELOAD, method = RequestMethod.POST, produces = RestURIConstants.APPLICATION_JSON)
    @ResponseBody
    public ReloadCacheResponse reloadCache() {
        cacheBuilder.buildCaches();
        ReloadCacheResponse response = new ReloadCacheResponse();
        response.setStatus(Status.SUCCESS);
        return response;
    }

    <T> void validateRequest(T request) {
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
    }
}
