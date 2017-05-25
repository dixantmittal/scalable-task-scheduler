package com.ixigo.entity;

import com.ixigo.exception.InternalServerException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.InterruptException;
import org.apache.kafka.common.errors.SerializationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by dixant on 26/04/17.
 */
@Component
@Slf4j
public class IxigoQueuePublisher {

    @Value("${kafka.topic.name}")
    private String topicName;

    private KafkaProducer<String, String> kafkaProducer;

    public IxigoQueuePublisher() {
        Properties properties = new Properties();
        try {
            log.debug("Loading properties for REQUEST kafka topic.");
            properties.load(IxigoQueuePublisher.class.getClassLoader().getResourceAsStream("kafka-producer.properties"));
            kafkaProducer = new KafkaProducer<String, String>(properties);
            log.debug("Loading properties SUCCESSFUL");
        } catch (IOException e) {
            log.error("Could not find kafka kafkaProducer properties.");
            throw new InternalServerException();
        }
    }

    public Boolean sendToQueue(String key, String data) {
        if (key == null || data == null) {
            log.error("Either key or value is null. Can not proceed further. Key: {}, Value: {}", key, data);
            return false;
        }
        try {
            log.debug("Publishing data to Kafka. Key: {}, value: {}", key, data);
            ProducerRecord<String, String> record = new ProducerRecord<String, String>(topicName, key, data);
            kafkaProducer.send(record);
            kafkaProducer.flush();
            log.debug("Publishing SUCCESSFUL");
        } catch (InterruptException ie) {
            log.error("Publisher thread interrupted. Exception: {}. Value: {}", ie, data);
            return false;
        } catch (SerializationException se) {
            log.error("Supplied object could not be published due to serialization issues. Exception: {}", se);
            return false;
        } catch (Exception e) {
            log.error("Error occurred while publishing task on Kafka. Exception: {}. Key: {}. Value{}", e, key, data);
            return false;
        }
        return true;
    }
}
