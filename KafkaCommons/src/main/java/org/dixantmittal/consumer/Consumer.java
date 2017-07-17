package org.dixantmittal.consumer;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

/**
 * Created by dixant on 22/03/17.
 */
@Getter
@Slf4j
public class Consumer<K, V> extends Thread {
    private KafkaConsumer<K, V> consumer;
    private String[] topic;
    private String groupId;
    private boolean shutdown;
    private Processor<K, V> processor;
    public int pollingTime;
    public int sleepTime;

    {
        pollingTime = 0;
        sleepTime = 1000;
        shutdown = false;
    }

    public Consumer(Properties properties, String[] topics, Processor<K, V> processor) {
        this.groupId = properties.get(ConsumerConfig.GROUP_ID_CONFIG).toString();
        this.topic = topics;
        this.consumer = new KafkaConsumer<>(properties);
        this.processor = processor;
    }

    @Override
    public void run() {
        // subscribe topics
        consumer.subscribe(Arrays.asList(topic));

        // poll and process records
        try {
            keepPolling();
        } catch (InterruptedException e) {
            log.error("THREAD INTERRUPTED.", e);
        }

        // when the shutdown is called, close the consumer connection.
        consumer.close();
    }

    private void keepPolling() throws InterruptedException {
        // keep on polling until shutdown for this thread is called.
        while (!shutdown) {
            ConsumerRecords<K, V> records = consumer.poll(pollingTime);

            // if polling gave no tasks, then sleep this thread for n seconds.
            if (records.isEmpty()) {
                log.debug("NO RECORDS fetched from queue. Putting current THREAD to SLEEP.");
                Thread.sleep(sleepTime);
                continue;
            }

            log.info("Executing a batch of tasks.");
            if (!processor.process(records)) {
                log.error("ERROR occurred while PROCESSING RECORDS.");
            }
        }
    }

    public static abstract class Processor<K, V> {
        protected abstract Boolean process(ConsumerRecords<K, V> record);
    }

    public void close() {
        shutdown = true;
    }
}