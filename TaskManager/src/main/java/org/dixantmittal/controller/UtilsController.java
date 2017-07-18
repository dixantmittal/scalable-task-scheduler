package org.dixantmittal.controller;

import lombok.extern.slf4j.Slf4j;
import org.dixantmittal.cache.service.ICacheBuilder;
import org.dixantmittal.constants.taskmanager.RestURIConstants;
import org.dixantmittal.entity.SchedulerMode;
import org.dixantmittal.entity.Status;
import org.dixantmittal.request.taskmanager.SchedulerRequest;
import org.dixantmittal.response.GenericResponse;
import org.dixantmittal.service.ITaskManagerRequestService;
import org.dixantmittal.validation.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by dixant on 18/07/17.
 */
@Controller
@Slf4j
@RequestMapping(value = RestURIConstants.TASK_MANAGER_BASE_URI + RestURIConstants.UTILS)
public class UtilsController {

    @Autowired
    RequestValidator validator;

    @Autowired
    private ICacheBuilder cacheBuilder;

    @Autowired
    private ITaskManagerRequestService requestService;

    @RequestMapping(
            value = RestURIConstants.START_SCHEDULER,
            method = RequestMethod.POST,
            produces = RestURIConstants.APPLICATION_JSON)
    @ResponseBody
    GenericResponse startScheduler() {
        return requestService.startScheduler();
    }

    @RequestMapping(
            value = RestURIConstants.STOP_SCHEDULER,
            method = RequestMethod.DELETE,
            produces = RestURIConstants.APPLICATION_JSON)
    @ResponseBody
    GenericResponse stopScheduler(@RequestParam(value = "mode", required = false) String mode) {
        SchedulerRequest request = new SchedulerRequest(SchedulerMode.forName(mode));
        return requestService.stopScheduler(validator.validate(request));
    }

    @RequestMapping(value = RestURIConstants.CACHE_RELOAD, method = RequestMethod.POST, produces = RestURIConstants.APPLICATION_JSON)
    @ResponseBody
    public GenericResponse reloadCache() {
        cacheBuilder.buildCaches();
        return new GenericResponse(Status.SUCCESS);
    }
}
