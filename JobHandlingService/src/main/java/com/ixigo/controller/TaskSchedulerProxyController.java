package com.ixigo.controller;

import com.ixigo.constants.jobschedulingservice.RestURIConstants;
import com.ixigo.exception.RequestValidationException;
import com.ixigo.exception.codes.jobschedulingservice.RequestValidationExceptionCodes;
import com.ixigo.request.jobschedulingservice.AddTaskRequest;
import com.ixigo.request.jobschedulingservice.AddTaskWithTaskIdRequest;
import com.ixigo.request.jobschedulingservice.DeleteTaskRequest;
import com.ixigo.response.jobschedulingservice.AddTaskResponse;
import com.ixigo.response.jobschedulingservice.DeleteTaskResponse;
import com.ixigo.service.TaskSchedulerRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

/**
 * Created by dixant on 27/03/17.
 */
@Controller
@Slf4j
@RequestMapping(value = RestURIConstants.TASK_SCHEDULER_BASE_URI)
public class TaskSchedulerProxyController {

    @Autowired
    Validator validator;
    @Autowired
    TaskSchedulerRequestService service;

    @RequestMapping(
            value = RestURIConstants.TASK,
            method = RequestMethod.POST,
            produces = RestURIConstants.APPLICATION_JSON)
    @ResponseBody
    AddTaskResponse addTask(@RequestBody AddTaskRequest request) {
        return service.addTask(validateRequest(request));
    }

    @RequestMapping(
            value = RestURIConstants.TASK + RestURIConstants.JOB_ID,
            method = RequestMethod.POST,
            produces = RestURIConstants.APPLICATION_JSON)
    @ResponseBody
    AddTaskResponse addTask(@RequestBody AddTaskWithTaskIdRequest request) {
        return service.addTask(validateRequest(request));
    }

    @RequestMapping(
            value = RestURIConstants.TASK,
            method = RequestMethod.DELETE,
            produces = RestURIConstants.APPLICATION_JSON)
    @ResponseBody
    DeleteTaskResponse deleteTask(@RequestParam(value = "job-id", required = false) String jobId, @RequestParam(value = "can-retry", required = false) String canRetry) {
        DeleteTaskRequest request = new DeleteTaskRequest();
        request.setJobId(jobId);
        request.setCanRetry(Boolean.valueOf(canRetry));
        return service.deleteTask(validateRequest(request));
    }

    <T> T validateRequest(T request) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(request);
        if (constraintViolations != null && constraintViolations.size() > 0) {
            log.error("Error occurred while validating request. Request: {}", request.getClass().getName());
            RequestValidationExceptionCodes error = RequestValidationExceptionCodes
                    .forName(constraintViolations.iterator().next().getMessage());
            throw new RequestValidationException(error.code(), error.message());
        }
        return request;
    }
}