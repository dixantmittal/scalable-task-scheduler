package com.ixigo.client.entity;

import com.ixigo.entity.KafkaTopic;
import lombok.Getter;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * Created by dixant on 22/03/17.
 */
@Getter
public class IxigoKafkaConsumer {
    private KafkaConsumer<Object, Object> consumer;
    private KafkaTopic topic;
    private String groupId;

    public IxigoKafkaConsumer(Properties properties, KafkaTopic topic) {
        consumer = new KafkaConsumer<Object, Object>(properties);
        groupId = properties.get(ConsumerConfig.GROUP_ID_CONFIG).toString();
        this.topic = topic;
    }

    public void subscribe(Collection<String> topics) {

    }

    public void subscribe(Collection<String> topics, ConsumerRebalanceListener callback) {

    }

    public void assign(Collection<TopicPartition> partitions) {

    }

    public void subscribe(Pattern pattern, ConsumerRebalanceListener callback) {

    }

    public void unsubscribe() {

    }

    public ConsumerRecords<Object, Object> poll(long timeout) {
        return null;
    }

    public void commitSync() {

    }

    public void commitSync(Map<TopicPartition, OffsetAndMetadata> offsets) {

    }

    public void commitAsync() {

    }

    public void commitAsync(OffsetCommitCallback callback) {

    }

    public void commitAsync(Map<TopicPartition, OffsetAndMetadata> offsets, OffsetCommitCallback callback) {

    }

    public void seek(TopicPartition partition, long offset) {

    }

    public void seekToBeginning(Collection<TopicPartition> partitions) {

    }

    public void seekToEnd(Collection<TopicPartition> partitions) {

    }

    public long position(TopicPartition partition) {
        return 0;
    }

    public void pause(Collection<TopicPartition> partitions) {

    }

    public void resume(Collection<TopicPartition> partitions) {

    }

    public void close() {

    }
}
