package org.dixantmittal.entity;

import org.dixantmittal.constants.ConfigurationConstants;
import org.dixantmittal.exception.ServiceException;
import org.dixantmittal.exception.codes.jobexecutor.ServiceExceptionCodes;
import org.dixantmittal.factory.TaskExecutorFactory;
import org.dixantmittal.taskexecutors.ITaskExecutor;
import org.dixantmittal.utils.Configuration;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
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
        consumer.subscribe(Arrays.asList(topic.topic()));
        shutdown = false;
    }

    @Override
    public void run() {

        final int pollingTime = Integer.parseInt(
                Configuration.getGlobalProperty(ConfigurationConstants.KAFKA_MAX_POLLING_TIME));
        final int threadSleepTime = Integer.parseInt(
                Configuration.getGlobalProperty(ConfigurationConstants.KAFKA_CONSUMER_SLEEP_TIME));

        // keep on polling and executing tasks until shutdown for this thread is called.
        while (!shutdown) {
            ConsumerRecords<String, KafkaTaskDetails> tasks = consumer.poll(pollingTime);

            // if polling gave no tasks, then sleep this thread for n seconds.
            if (tasks.isEmpty()) {
                try {
                    log.debug("no tasks fetched from queue. Putting current thread to sleep.");
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
                log.info("task executor fetched: {}", taskExecutor.getClass());
                taskExecutor.execute(task.value());
            }
            consumer.commitSync();
        }
        // when the shutdown is called, close the consumer connection.
        consumer.close();
    }

    public void close() {
        shutdown = true;
    }
}