package com.ixigo.adapter;

import com.ixigo.constants.CommonConstants;
import com.ixigo.entity.JobSchedulingDetails;
import com.ixigo.exception.ServiceException;
import com.ixigo.exception.codes.ServiceExceptionCodes;
import com.ixigo.request.AddTaskRequest;
import com.ixigo.utils.IxigoDateUtils;

import java.text.ParseException;

/**
 * Created by dixant on 27/03/17.
 */
public class ObjectAdapter {
    public static JobSchedulingDetails adapt(AddTaskRequest object) {
        JobSchedulingDetails jobDetails = new JobSchedulingDetails();
        jobDetails.setTaskType(object.getTaskType());
        jobDetails.setTaskMetadata(object.getTaskMetadata());
        jobDetails.setPriority(Integer.parseInt(object.getPriority()));

        try {
            jobDetails.setScheduledTime(IxigoDateUtils
                    .toCalendar(IxigoDateUtils.parseDate(object.getScheduledTime(), CommonConstants.DATE_PATTERN)));
        } catch (ParseException e) {
            throw new ServiceException(ServiceExceptionCodes.DATE_FORMAT_EXCEPTION.code(),
                    ServiceExceptionCodes.DATE_FORMAT_EXCEPTION.message());
        }
        return jobDetails;
    }
}
