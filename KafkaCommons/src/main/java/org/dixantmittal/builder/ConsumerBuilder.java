package org.dixantmittal.builder;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.dixantmittal.consumer.Consumer;

import java.util.Properties;

/**
 * Created by dixant on 22/03/17.
 */
public class ConsumerBuilder<K, V> {

    private Properties properties;
    private Consumer.Processor<K, V> processor;
    private String[] topics;
    private String groupId;
    private int pollingTime;
    private int sleepTime;


    private ConsumerBuilder() {
        properties = new Properties();
    }

    public static <K, V> ConsumerBuilder<K, V> newConsumer() {
        return new ConsumerBuilder();
    }

    public ConsumerBuilder<K, V> addProperty(String key, Object value) {
        properties.put(key, value);
        return this;
    }

    public ConsumerBuilder<K, V> loadDefaultProperties() {
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        return this;
    }

    public ConsumerBuilder<K, V> withProcessor(Consumer.Processor<K, V> processor) {
        this.processor = processor;
        return this;
    }

    public ConsumerBuilder<K, V> withTopics(String... topics) {
        this.topics = topics;
        return this;
    }

    public ConsumerBuilder<K, V> withGroupId(String groupId) {
        this.groupId = groupId;
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        return this;
    }

    public ConsumerBuilder<K, V> withPollingTime(int pollingTime) {
        this.pollingTime = pollingTime;
        return this;
    }

    public ConsumerBuilder<K, V> withSleepingTime(int sleepTime) {
        this.sleepTime = sleepTime;
        return this;
    }

    public Consumer<K, V> getConsumer() {
        if (processor == null || topics == null || properties.size() == 0 || StringUtils.isEmpty(groupId)) {
            return null;
        }
        return new Consumer<K, V>(properties, topics, processor);
    }
}
