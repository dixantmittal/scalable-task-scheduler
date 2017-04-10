package com.ixigo.constants;

/**
 * Created by dixant on 29/03/17.
 */
public enum ConfigurationConstants implements IConfigurationConstants {
    MAX_THREAD_POOL_SIZE("threadpool.size.max", "50"),
    KAFKA_MAX_POLLING_TIME("kafka.poll.time.max", "10"),
    KAFKA_CONSUMER_SLEEP_TIME("kafka.consumer.time.sleep", "1000");

    private final String key;
    private final String defaultValue;

    ConfigurationConstants(String key, String defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public String defaultValue() {
        return defaultValue;
    }

    public String key() {
        return key;
    }
}
