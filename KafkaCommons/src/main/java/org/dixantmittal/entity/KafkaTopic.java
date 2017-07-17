package org.dixantmittal.entity;

import org.apache.kafka.common.TopicPartition;

/**
 * Created by dixant on 22/03/17.
 */
public class KafkaTopic {

    private TopicPartition topicDetails;

    public KafkaTopic(String topic, int partition) {
        topicDetails = new TopicPartition(topic, partition);
    }

    public KafkaTopic(String topic) {
        topicDetails = new TopicPartition(topic, 0);
    }


    public int partition() {
        return topicDetails.partition();
    }

    public String topic() {
        return topicDetails.topic();
    }

    @Override
    public int hashCode() {
        return topicDetails.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return topicDetails.equals(other);
    }
}
