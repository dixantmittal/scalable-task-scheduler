package org.dixantmittal.publisher;

import org.dixantmittal.client.IxigoKafkaClient;
import org.dixantmittal.client.entity.IxigoKafkaProducer;
import org.dixantmittal.entity.KafkaTaskDetails;
import org.dixantmittal.entity.KafkaTopic;
import org.dixantmittal.factory.KafkaProducerFactory;
import org.dixantmittal.factory.TopicFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by dixant on 22/03/17.
 */
@Component
@Slf4j
public class KafkaTaskPublisher implements IKafkaTaskPublisher {

    @Autowired
    private IxigoKafkaClient ixigoKafkaClient;

    @Autowired
    private TopicFactory topicFactory;

    @Autowired
    private KafkaProducerFactory kafkaProducerFactory;

    public boolean publishTask(KafkaTaskDetails taskDetails) {
        KafkaTopic topic = topicFactory.getTopicForTaskType(taskDetails.getTaskType());
        if (topic == null) {
            log.error("Topic not found for task type: {}. Can not proceed further.", taskDetails.getTaskType());
            return false;
        }
        log.info("Topic to use: {}", topic.topic());
        IxigoKafkaProducer producer = kafkaProducerFactory.getKafkaProducer(topic);
        log.info("Producer fetched from factory.");
        return ixigoKafkaClient.publish(producer, taskDetails.getTaskId(), taskDetails);
    }
}
