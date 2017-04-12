package com.ixigo.publisher;

import com.ixigo.client.IxigoKafkaClient;
import com.ixigo.client.entity.IxigoKafkaProducer;
import com.ixigo.entity.KafkaTaskDetails;
import com.ixigo.entity.KafkaTopic;
import com.ixigo.factory.KafkaProducerFactory;
import com.ixigo.factory.TopicFactory;
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
        IxigoKafkaProducer producer = kafkaProducerFactory.getKafkaProducer(topic);
        return ixigoKafkaClient.publish(producer, taskDetails.getJobId(), taskDetails);
    }
}
