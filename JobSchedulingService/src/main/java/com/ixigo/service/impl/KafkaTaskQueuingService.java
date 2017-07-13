package com.ixigo.service.impl;

import com.ixigo.constants.jobschedulingservice.ServiceConstants;
import com.ixigo.entity.KafkaTaskDetails;
import com.ixigo.publisher.IKafkaTaskPublisher;
import com.ixigo.service.ITaskQueuingService;
import com.ixigo.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by dixant on 28/03/17.
 */
@Slf4j
@Service
public class KafkaTaskQueuingService implements ITaskQueuingService {

    @Autowired
    private IKafkaTaskPublisher kafkaTaskPublisher;

    @Override
    public Boolean addJobToExecutionQueue(Map<String, Object> jobDataMap) {
        String jobDetails = jobDataMap.get(ServiceConstants.JOB_DETAILS).toString();
        KafkaTaskDetails taskDetails = JsonUtils.fromJson(jobDetails, KafkaTaskDetails.class);
        taskDetails.setTaskId(jobDataMap.get(ServiceConstants.JOB_ID).toString());
        log.info("Kafka Task Details constructed: {}", taskDetails);
        return kafkaTaskPublisher.publishTask(taskDetails);
    }
}
