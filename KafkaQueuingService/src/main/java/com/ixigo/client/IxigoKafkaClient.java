package com.ixigo.client;

import com.ixigo.client.entity.IxigoKafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.InterruptException;
import org.apache.kafka.common.errors.SerializationException;
import org.springframework.stereotype.Component;

/**
 * Created by dixant on 20/03/17.
 */
@Slf4j
@Component
public class IxigoKafkaClient {

    public boolean publish(IxigoKafkaProducer producer, Object key, Object value) {
        if (producer == null || key == null || value == null) {
            log.error("Either producer, key or value is null. Can not proceed further. Producer: {}, Key: {}, Value: {}", producer, key, value);
            return false;
        }
        try {
            log.debug("publishing record to kafka. key: {}, value: {}", key, value);
            ProducerRecord<Object, Object> record = new ProducerRecord<Object, Object>(producer.getTopic().topic(), key, value);
            producer.send(record);
            producer.flush();
            log.debug("Record publishing complete.");
        } catch (InterruptException ie) {
            log.error("Publisher thread interrupted. Exception: {}. Value: {}", ie, value);
            return false;
        } catch (SerializationException se) {
            log.error("Supplied object could not be published due to serialization issues. Exception: {}", se);
            return false;
        } catch (Exception e) {
            log.error("Error occurred while publishing task on Kafka. Exception: {}. Key: {}. Value{}", e, key, value);
            return false;
        }
        return true;
    }
}
