package org.dixantmittal.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dixantmittal.constants.taskmanager.RestURIConstants;
import org.dixantmittal.exception.InternalServerException;
import org.dixantmittal.response.GenericResponse;
import org.dixantmittal.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by dixant on 18/07/17.
 */
@Controller
@Slf4j
@RequestMapping(value = RestURIConstants.TASK_MANAGER_BASE_URI + RestURIConstants.UTILS)
public class UtilsController {

    @Autowired
    RequestService service;

    @RequestMapping(
            value = RestURIConstants.TOPIC,
            method = RequestMethod.POST,
            produces = RestURIConstants.APPLICATION_JSON)
    @ResponseBody
    GenericResponse changeTopic(@RequestBody String topic) {
        if (StringUtils.isEmpty(topic)) {
            throw new InternalServerException();
        }
        return service.changeTopic(topic);
    }
}
