package org.dixantmittal.producer;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.Future;

/**
 * Created by dixant on 22/03/17.
 */
public class Producer<K, V> {
    KafkaProducer<K, V> producer;

    public Producer(Properties properties) {
        producer = new KafkaProducer<>(properties);
    }

    public Future<RecordMetadata> send(String topic, K key, V value) {
        return producer.send(buildRecord(topic, key, value));
    }

    public Future<RecordMetadata> send(String topic, K key, V value, Callback callback) {
        return producer.send(buildRecord(topic, key, value), callback);
    }

    private ProducerRecord<K, V> buildRecord(String topic, K key, V value) {
        return new ProducerRecord<>(topic, key, value);
    }

    public void close() {
        producer.close();
    }
}
