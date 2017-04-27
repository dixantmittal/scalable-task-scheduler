package com.ixigo.controller;

import com.ixigo.cache.service.ICacheBuilder;
import com.ixigo.constants.jobexecutor.RestURIConstants;
import com.ixigo.enums.Status;
import com.ixigo.exception.RequestValidationException;
import com.ixigo.exception.codes.jobschedulingservice.RequestValidationExceptionCodes;
import com.ixigo.request.jobexecutor.AddConsumersRequest;
import com.ixigo.request.jobexecutor.RemoveConsumersRequest;
import com.ixigo.request.jobexecutor.StopAllConsumersRequest;
import com.ixigo.response.jobexecutor.AddConsumersResponse;
import com.ixigo.response.ReloadCacheResponse;
import com.ixigo.response.jobexecutor.RemoveConsumersResponse;
import com.ixigo.response.jobexecutor.StopConsumersResponse;
import com.ixigo.service.KafkaRequestService;
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
 * Created by dixant on 29/03/17.
 */
@Controller
@Slf4j
@RequestMapping(value = RestURIConstants.KAFKA_JOB_EXECUTOR_BASE_URI)
public class KafkaRequestController {

    @Autowired
    Validator validator;

    @Autowired
    private KafkaRequestService service;

    @Autowired
    private ICacheBuilder cacheBuilder;

    @RequestMapping(value = RestURIConstants.CONSUMER, method = RequestMethod.POST, produces = RestURIConstants.APPLICATION_JSON)
    @ResponseBody
    public AddConsumersResponse addConsumers(@RequestBody @Valid AddConsumersRequest request, BindingResult results) {
        if (results.hasErrors()) {
            log.error("Invalid request exception occurred while adding consumers.");
            RequestValidationExceptionCodes error = RequestValidationExceptionCodes
                    .forName(results
                            .getAllErrors()
                            .get(0)
                            .getDefaultMessage());
            throw new RequestValidationException(error.code(), error.message());
        }
        return service.addConsumers(request);
    }

    @RequestMapping(value = RestURIConstants.CONSUMER, method = RequestMethod.DELETE, produces = RestURIConstants.APPLICATION_JSON)
    @ResponseBody
    public RemoveConsumersResponse removeConsumers(@RequestParam("topic") String topic, @RequestParam("count") Integer count) {
        RemoveConsumersRequest request = new RemoveConsumersRequest(topic, count);
        validateRequest(request);
        return service.decreaseConsumers(request);
    }

    @RequestMapping(value = RestURIConstants.STOP_CONSUMERS, method = RequestMethod.DELETE, produces = RestURIConstants.APPLICATION_JSON)
    @ResponseBody
    public StopConsumersResponse stopAllConsumers(@RequestParam("topic") String topic) {
        StopAllConsumersRequest request = new StopAllConsumersRequest();
        request.setTopic(topic);
        // TODO Validate request
        return service.stopAllConsumers(request);
    }

    @RequestMapping(value = RestURIConstants.CACHE_RELOAD, method = RequestMethod.POST, produces = RestURIConstants.APPLICATION_JSON)
    @ResponseBody
    public ReloadCacheResponse reloadCache() {
        cacheBuilder.buildCaches();
        ReloadCacheResponse response = new ReloadCacheResponse();
        response.setStatus(Status.SUCCESS);
        return response;
    }

    private <T> void validateRequest(T request) {
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
