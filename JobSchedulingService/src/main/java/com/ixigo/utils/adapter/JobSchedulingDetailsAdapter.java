package com.ixigo.utils.adapter;

import com.ixigo.entity.JobSchedulingDetails;
import com.ixigo.exception.ServiceException;
import com.ixigo.exception.codes.jobschedulingservice.ServiceExceptionCodes;
import com.ixigo.request.jobschedulingservice.AddTaskRequest;
import com.ixigo.utils.IxigoDateUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by dixant on 27/03/17.
 */
public class JobSchedulingDetailsAdapter {
    public static JobSchedulingDetails adapt(AddTaskRequest request) {
        JobSchedulingDetails jobDetails = new JobSchedulingDetails();
        jobDetails.setTaskType(request.getTaskType());
        jobDetails.setTaskMetadata(request.getTaskMetadata());
        jobDetails.setRetryJobDetails(request.getRetryJobDetails());

        try {
            jobDetails.setScheduledTime(IxigoDateUtils.parse(request.getScheduledTime()));
        } catch (Exception e) {
            throw new ServiceException(ServiceExceptionCodes.DATE_FORMAT_EXCEPTION.code(),
                    ServiceExceptionCodes.DATE_FORMAT_EXCEPTION.message());
        }
        if (StringUtils.isNotBlank(request.getPriority())) {
            jobDetails.setPriority(Integer.parseInt(request.getPriority()));
        }
        return jobDetails;
    }
}
