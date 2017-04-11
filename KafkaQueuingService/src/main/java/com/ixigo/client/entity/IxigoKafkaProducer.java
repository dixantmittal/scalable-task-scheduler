package com.ixigo.client.entity;

import com.ixigo.entity.KafkaTopic;
import lombok.Getter;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.Future;

/**
 * Created by dixant on 22/03/17.
 */
@Getter
public class IxigoKafkaProducer {
    KafkaTopic topic;
    KafkaProducer<Object, Object> producer;

    public IxigoKafkaProducer(Properties properties, KafkaTopic topic) {
        producer = new KafkaProducer<Object, Object>(properties);
        this.topic = topic;
    }

    public Future<RecordMetadata> send(ProducerRecord<Object, Object> record) {
        return producer.send(record);
    }

    public Future<RecordMetadata> send(ProducerRecord<Object, Object> record, Callback callback) {
        return producer.send(record, callback);
    }

    public void flush() {
        producer.flush();
    }

    public void close() {
        producer.close();
    }
}
