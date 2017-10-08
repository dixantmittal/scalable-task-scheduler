package org.dixantmittal.controller;

import lombok.extern.slf4j.Slf4j;
import org.dixantmittal.constants.taskmanager.RestURIConstants;
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
 * Created by dixant on 27/03/17.
 */
@Controller
@Slf4j
@RequestMapping(value = RestURIConstants.TASK_MANAGER_BASE_URI)
public class TaskManagerRequestController {

    @Autowired
    RequestValidator validator;

    @Autowired
    private ITaskManagerRequestService requestService;

    @RequestMapping(
            value = RestURIConstants.TASK,
            method = RequestMethod.DELETE,
            produces = RestURIConstants.APPLICATION_JSON)
    @ResponseBody
    GenericResponse deleteTask(@RequestParam(value = "taskId", required = false) String taskId) {
        return requestService.deleteTask(taskId);
    }
}
