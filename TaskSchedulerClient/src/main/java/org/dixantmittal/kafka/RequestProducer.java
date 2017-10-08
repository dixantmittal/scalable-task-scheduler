package org.dixantmittal.kafka;

import lombok.extern.log4j.Log4j2;
import org.apache.kafka.common.errors.InterruptException;
import org.apache.kafka.common.errors.SerializationException;
import org.dixantmittal.builder.ProducerBuilder;
import org.dixantmittal.entity.Status;
import org.dixantmittal.entity.Task;
import org.dixantmittal.exception.InternalServerException;
import org.dixantmittal.producer.Producer;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by dixant on 26/04/17.
 */
@Log4j2
public class RequestProducer {

    private String topic;
    private Producer<String, Task> kafkaProducer;

    public RequestProducer(String topic) {
        Properties properties = new Properties();
        try {
            this.topic = topic;
            log.info("Loading PROPERTIES for KAFKA PRODUCER...");
            properties.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            properties.setProperty("value.serializer", "org.dixantmittal.serializer.TaskSerializer");
            properties.setProperty("metrics.recording.level", "INFO");
            properties.load(RequestProducer.class.getClassLoader().getResourceAsStream("kafka-producer.properties"));
            log.info("Loading properties SUCCESSFUL!");
            log.info("Creating KAFKA PRODUCER...");
            kafkaProducer = ProducerBuilder.<String, Task>newProducer()
                    .withProperties(properties)
                    .getProducer();
            log.info("KAFKA PRODUCER created SUCCESSFULLY!");

        } catch (IOException e) {
            log.error("Could not find kafka-producer.properties file in the classpath.");
            throw new InternalServerException();
        }
    }

    public Status send(String key, Task data) {
        try {
            log.debug("Publishing Request data to Kafka. Topic:{}, Key: {}, value: {}", topic, key, data);
            kafkaProducer.send(topic, key, data).get(); // send data right now.
            log.info("Publishing SUCCESSFUL");
        } catch (InterruptException ie) {
            log.error("Publisher thread interrupted. Exception: {}. Value: {}", ie, data);
            return Status.FAILURE;
        } catch (SerializationException se) {
            log.error("Supplied object could not be published due to serialization issues. Exception: {}", se);
            return Status.FAILURE;
        } catch (Exception e) {
            log.error("Error occurred while publishing task on Kafka. Exception: {}. Key: {}. Value{}", e, key, data);
            return Status.FAILURE;
        }
        return Status.SUCCESS;
    }
}
