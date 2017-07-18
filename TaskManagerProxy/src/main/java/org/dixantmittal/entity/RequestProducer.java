package org.dixantmittal.entity;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.InterruptException;
import org.apache.kafka.common.errors.SerializationException;
import org.dixantmittal.builder.ProducerBuilder;
import org.dixantmittal.exception.InternalServerException;
import org.dixantmittal.producer.Producer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by dixant on 26/04/17.
 */
@Component
@Slf4j
@Setter
public class RequestProducer {

    private String topic;
    private Producer<String, String> kafkaProducer;

    public RequestProducer() {
        Properties properties = new Properties();
        try {
            log.info("Loading PROPERTIES for KAFKA PRODUCER...");
            properties.load(RequestProducer.class.getClassLoader().getResourceAsStream("kafka-producer.properties"));
            log.info("Loading properties SUCCESSFUL!");
            log.info("Creating KAFKA PRODUCER...");
            kafkaProducer = ProducerBuilder.<String, String>newProducer()
                    .withProperties(properties)
                    .getProducer();
            log.info("KAFKA PRODUCER created SUCCESSFULLY!");

        } catch (IOException e) {
            log.error("Could not find kafka kafkaProducer properties.");
            throw new InternalServerException();
        }
    }

    public Boolean send(String key, String data) {
        try {
            log.info("Publishing Request data to Kafka. Topic:{}, Key: {}, value: {}", topic, key, data);
            kafkaProducer.send(topic, key, data).get(); // send data right now.
            log.info("Publishing SUCCESSFUL");
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
