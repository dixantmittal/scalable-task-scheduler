package com.ixigo.service.impl;

import com.ixigo.constants.ServiceConstants;
import com.ixigo.entity.KafkaTaskDetails;
import com.ixigo.publisher.IKafkaTaskPublisher;
import com.ixigo.service.IJobQueuingService;
import com.ixigo.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by dixant on 28/03/17.
 */
@Service
public class KafkaJobQueuingService implements IJobQueuingService {

    @Autowired
    private IKafkaTaskPublisher kafkaTaskPublisher;

    @Override
    public Boolean addJobToExecutionQueue(Map<String, Object> jobDataMap) {
        String jobDetails = jobDataMap.get(ServiceConstants.JOB_DETAILS).toString();
        KafkaTaskDetails taskDetails = JsonUtils.fromJson(jobDetails, KafkaTaskDetails.class);
        taskDetails.setJobId(jobDataMap.get(ServiceConstants.JOB_ID).toString());
        return kafkaTaskPublisher.publishTask(taskDetails);
    }
}
