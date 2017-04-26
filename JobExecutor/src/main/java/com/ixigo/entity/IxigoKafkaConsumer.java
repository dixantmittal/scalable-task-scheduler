package com.ixigo.entity;

import com.ixigo.constants.ConfigurationConstants;
import com.ixigo.exception.ServiceException;
import com.ixigo.exception.codes.ServiceExceptionCodes;
import com.ixigo.factory.TaskExecutorFactory;
import com.ixigo.taskexecutors.ITaskExecutor;
import com.ixigo.utils.Configuration;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

/**
 * Created by dixant on 22/03/17.
 */
@Getter
@Slf4j
public class IxigoKafkaConsumer extends Thread {
    private KafkaConsumer<String, KafkaTaskDetails> consumer;
    private KafkaTopic topic;
    private String groupId;
    private volatile boolean shutdown;

    public IxigoKafkaConsumer(Properties properties, KafkaTopic topic) {
        consumer = new KafkaConsumer<String, KafkaTaskDetails>(properties);
        groupId = properties.get(ConsumerConfig.GROUP_ID_CONFIG).toString();
        this.topic = topic;
        subscribe(Arrays.asList(topic.topic()));
        shutdown = false;
    }

    @Override
    public void run() {
        // TODO where to set offset for a consumer.


        final int pollingTime = Integer.parseInt(
                Configuration.getGlobalProperty(ConfigurationConstants.KAFKA_MAX_POLLING_TIME));
        final int threadSleepTime = Integer.parseInt(
                Configuration.getGlobalProperty(ConfigurationConstants.KAFKA_CONSUMER_SLEEP_TIME));

        // keep on polling and executing tasks until shutdown for this thread is called.
        while (!shutdown) {
            ConsumerRecords<String, KafkaTaskDetails> tasks = poll(pollingTime);

            // if polling gave no tasks, then sleep this thread for n seconds.
            if (tasks.isEmpty()) {
                try {
                    sleep(threadSleepTime);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            // for each task, execute its business logic
            for (ConsumerRecord<String, KafkaTaskDetails> task : tasks) {
                if (task.key() == null) {
                    log.error("Wrong task encountered. Task meta: {}", task);
                    continue;
                }
                ITaskExecutor taskExecutor = TaskExecutorFactory.getTaskExecutor(task.value().getTaskType());
                if (taskExecutor == null) {
                    log.error("Task executor not found for taskType: {}", task.value().getTaskType());
                    throw new ServiceException(ServiceExceptionCodes.EXECUTOR_NOT_EXISTS.code(),
                            ServiceExceptionCodes.EXECUTOR_NOT_EXISTS.message());
                }
                taskExecutor.execute(task.value());
            }
            consumer.commitSync();
        }
        // when the shutdown is called, close the consumer connection.
        consumer.close();
    }

    public void subscribe(Collection<String> topics) {
        consumer.subscribe(topics);
    }

    public ConsumerRecords<String, KafkaTaskDetails> poll(long timeout) {
        return consumer.poll(timeout);
    }

    public void close() {
        shutdown = true;
    }
}