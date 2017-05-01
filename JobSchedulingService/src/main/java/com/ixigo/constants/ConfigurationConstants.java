package com.ixigo.constants;

/**
 * Created by dixant on 29/03/17.
 */
public enum ConfigurationConstants implements IConfigurationConstants {

    REQUEST_CONSUMER_THREAD_COUNT("request.consumer.thread.count", "3"),
    REQUEST_CONSUMER_THREAD_SLEEP_TIME("request.consumer.thread.sleeptime", "1000"),
    REQUEST_CONSUMER_POLL_TIME("request.consumer.poll.time", "10"),
    REQUEST_CONSUMER_TOPIC_NAME("request.consumer.topic.name", "REQUEST_QUEUE"),
    MAX_REFIRE_LIMIT("quartz.refire.limit.max", "5");

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
