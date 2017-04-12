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
    public static JobSchedulingDetails adapt(AddTaskRequest request) {
        JobSchedulingDetails jobDetails = new JobSchedulingDetails();
        jobDetails.setTaskType(request.getTaskType());
        jobDetails.setTaskMetadata(request.getTaskMetadata());
        jobDetails.setPriority(Integer.parseInt(request.getPriority()));
        jobDetails.setRetryJobDetails(request.getRetryJobDetails());

        try {
            jobDetails.setScheduledTime(IxigoDateUtils.parse(request.getScheduledTime()));
        } catch (Exception e) {
            throw new ServiceException(ServiceExceptionCodes.DATE_FORMAT_EXCEPTION.code(),
                    ServiceExceptionCodes.DATE_FORMAT_EXCEPTION.message());
        }
        return jobDetails;
    }
}
