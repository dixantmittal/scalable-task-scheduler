package org.dixantmittal.service.impl;

import org.dixantmittal.constants.jobschedulingservice.ServiceConstants;
import org.dixantmittal.entity.KafkaTaskDetails;
import org.dixantmittal.publisher.IKafkaTaskPublisher;
import org.dixantmittal.service.ITaskQueuingService;
import org.dixantmittal.utils.JsonUtils;
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
