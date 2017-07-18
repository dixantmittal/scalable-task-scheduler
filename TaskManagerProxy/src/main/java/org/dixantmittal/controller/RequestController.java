package org.dixantmittal.controller;

import lombok.extern.slf4j.Slf4j;
import org.dixantmittal.constants.taskmanager.RestURIConstants;
import org.dixantmittal.exception.RequestValidationException;
import org.dixantmittal.exception.codes.taskmanager.RequestValidationExceptionCodes;
import org.dixantmittal.request.taskmanager.AddTaskRequest;
import org.dixantmittal.request.taskmanager.DeleteTaskRequest;
import org.dixantmittal.response.GenericResponse;
import org.dixantmittal.response.taskmanager.AddTaskResponse;
import org.dixantmittal.service.RequestService;
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
@RequestMapping(value = RestURIConstants.TASK_MANAGER_BASE_URI)
public class RequestController {

    @Autowired
    Validator validator;
    @Autowired
    RequestService service;

    @RequestMapping(
            value = RestURIConstants.TASK,
            method = RequestMethod.POST,
            produces = RestURIConstants.APPLICATION_JSON)
    @ResponseBody
    AddTaskResponse addTask(@RequestBody AddTaskRequest request) {
        return service.addTask(validateRequest(request));
    }

    @RequestMapping(
            value = RestURIConstants.TASK + RestURIConstants.TASK_ID,
            method = RequestMethod.POST,
            produces = RestURIConstants.APPLICATION_JSON)
    @ResponseBody
    AddTaskResponse addTaskWithJobId(@RequestBody AddTaskRequest request) {
        return service.addTask(validateRequest(request));
    }

    @RequestMapping(
            value = RestURIConstants.TASK,
            method = RequestMethod.DELETE,
            produces = RestURIConstants.APPLICATION_JSON)
    @ResponseBody
    GenericResponse deleteTask(@RequestParam(value = "taskId", required = false) String taskId, @RequestParam(value = "canRetry", required = false) String canRetry) {
        DeleteTaskRequest request = new DeleteTaskRequest(taskId, Boolean.valueOf(canRetry));
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