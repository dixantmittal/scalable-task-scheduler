package com.ixigo.controller;

import com.ixigo.cache.service.ICacheBuilder;
import com.ixigo.constants.RestURIConstants;
import com.ixigo.enums.Status;
import com.ixigo.exception.RequestValidationException;
import com.ixigo.exception.codes.RequestValidationExceptionCodes;
import com.ixigo.request.AddConsumersRequest;
import com.ixigo.request.RemoveConsumersRequest;
import com.ixigo.request.StopAllConsumersRequest;
import com.ixigo.response.AddConsumersResponse;
import com.ixigo.response.ReloadCacheResponse;
import com.ixigo.response.RemoveConsumersResponse;
import com.ixigo.response.StopConsumersResponse;
import com.ixigo.service.KafkaRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by dixant on 29/03/17.
 */
@Controller
public class KafkaRequestController {

    @Autowired
    private KafkaRequestService service;

    @Autowired
    private ICacheBuilder cacheBuilder;

    @RequestMapping(value = RestURIConstants.CONSUMER, method = RequestMethod.POST, produces = RestURIConstants.APPLICATION_JSON)
    @ResponseBody
    public AddConsumersResponse addConsumers(@RequestBody @Valid AddConsumersRequest request, BindingResult results) {
        if (results.hasErrors()) {
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
        // TODO Validate request
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
}
