package com.ixigo.entity;

import com.ixigo.cache.CacheManager;
import com.ixigo.cache.ConsumerPropertiesCache;
import com.ixigo.constants.ConfigurationConstants;
import com.ixigo.exception.ServiceException;
import com.ixigo.factory.RequestServerProvider;
import com.ixigo.requestserver.IRequestServer;
import com.ixigo.utils.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

/**
 * Created by dixant on 28/04/17.
 */
@Slf4j
public class RequestConsumer extends Thread {

    RequestServerProvider provider = RequestServerProvider.getInstance();

    private volatile boolean shutdown;

    private static Properties getConsumerConfig() {
        log.info("Looking for Request Consumer properties in cache");
        Properties properties = new Properties();
        ConsumerPropertiesCache cache = CacheManager.getInstance().getCache(ConsumerPropertiesCache.class);
        for (Map.Entry<String, String> property : cache.entrySet()) {
            properties.put(property.getKey(), property.getValue());
        }
        log.info("Properties map size: {}", properties.size());
        return properties;
    }

    @Override
    public void run() {
        Properties consumerConfig = getConsumerConfig();
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(consumerConfig);
        String topic = Configuration.getGlobalProperty(ConfigurationConstants.REQUEST_CONSUMER_TOPIC_NAME);
        log.info("Subscribing to REQUEST topic: {}", topic);
        consumer.subscribe(Arrays.asList(topic));
        shutdown = false;

        // keep on polling and serving requests until shutdown for this thread is called.
        poll(consumer);

        // when the shutdown is called, close the consumer connection.
        log.info("Consumer {} is being closed down.", this.getThreadGroup());
        consumer.close();
    }

    private void poll(KafkaConsumer<String, String> consumer) {
        final int pollingTime = Integer.parseInt(Configuration.getGlobalProperty(ConfigurationConstants.REQUEST_CONSUMER_POLL_TIME));
        final int threadSleepTime = Integer.parseInt(Configuration.getGlobalProperty(ConfigurationConstants.REQUEST_CONSUMER_THREAD_SLEEP_TIME));

        log.info("Polling params: Poll time: {}, Sleep time: {}", pollingTime, threadSleepTime);

        while (!shutdown) {
            ConsumerRecords<String, String> tasks = consumer.poll(pollingTime);

            // if polling gave no tasks, then sleep this thread for n seconds.
            if (tasks.isEmpty()) {
                try {
                    log.debug("No record found, sending current thread to sleep....");
                    Thread.sleep(threadSleepTime);
                    continue;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            // for each task, execute its business logic
            for (ConsumerRecord<String, String> task : tasks) {
                if (task.key() == null) {
                    log.error("Wrong task encountered. Task meta: {}", task);
                    continue;
                }
                IRequestServer requestServer = provider.getRequestServer(task.key());
                if (requestServer == null) {
                    log.error("Request Server not found for request type: {}", task.key());
                    continue;
                }
                log.info("Request server found: {}", requestServer.getClass());
                try {
                    requestServer.serve(task.value());
                } catch (ServiceException se) {
                    log.error("Service Exception occurred while serving request. Error: ", se);
                    continue;
                }
            }
            consumer.commitSync();
        }
    }

    public void close() {
        shutdown = true;
    }
}
