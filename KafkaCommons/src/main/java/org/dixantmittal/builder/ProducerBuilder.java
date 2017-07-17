package org.dixantmittal.builder;

import org.dixantmittal.producer.Producer;

import java.util.Properties;

/**
 * Created by dixant on 22/03/17.
 */
public class ProducerBuilder<K, V> {

    Properties properties;

    private ProducerBuilder() {
        properties = new Properties();
    }

    public static ProducerBuilder newProducer() {
        return new ProducerBuilder();
    }

    public ProducerBuilder addProperty(String key, String value) {
        properties.put(key, value);
        return this;
    }

    public ProducerBuilder loadDefaultProperties() {
        properties.put("bootstrap.servers", "10.31.33.244:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return this;
    }

    public Producer<K, V> getProducer() {
        return new Producer<>(properties);
    }
}
