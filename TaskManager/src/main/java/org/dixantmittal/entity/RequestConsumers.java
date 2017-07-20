package org.dixantmittal.entity;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.dixantmittal.builder.ConsumerBuilder;
import org.dixantmittal.builder.RequestServerProvider;
import org.dixantmittal.cache.CacheManager;
import org.dixantmittal.cache.ConsumerPropertiesCache;
import org.dixantmittal.constants.ConfigurationConstants;
import org.dixantmittal.consumer.Consumer;
import org.dixantmittal.exception.ServiceException;
import org.dixantmittal.requestserver.IRequestServer;
import org.dixantmittal.utils.Configuration;

import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by dixant on 28/04/17.
 */
@Slf4j
public class RequestConsumers {

    private static volatile Set<Consumer<String, Task>> _THREADPOOL = new HashSet<>();
    private static volatile Lock _LOCK = new ReentrantLock();
    private static RequestServerProvider provider = RequestServerProvider.getInstance();

    public static void startNewThreads() {
        // acquire lock
        _LOCK.lock();
        try {
            // get threads count
            int threadCount = Integer.parseInt(Configuration.getGlobalProperty(ConfigurationConstants.REQUEST_CONSUMER_THREAD_COUNT));
            log.info("Total threads allowed: {}.", threadCount);
            while (threadCount-- > 0) {
                // create a new thread and start it
                Consumer<String, Task> consumer = ConsumerBuilder.<String, Task>newConsumer()
                        .withProperties(getConsumerConfig())
                        .withTopics(Configuration.getGlobalProperty(ConfigurationConstants.REQUEST_CONSUMER_TOPIC_NAME))
                        .withProcessor(getProcessor())
                        .withGroupId("request")
                        .getConsumer();
                consumer.start();
                // add thread to the thread pool
                log.info("New consumer started: {}", consumer);
                _THREADPOOL.add(consumer);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            // release lock
            _LOCK.unlock();
        }
    }

    // Get lock to reduce threads.
    public static void closeThreads() {
        // acquire lock
        _LOCK.lock();
        // making sure thread releases lock
        try {
            log.info("Trying to close threads one by one...");
            _THREADPOOL.stream().forEach(consumer -> consumer.close());
//            Iterator<RequestConsumers> itr = _THREADPOOL.iterator();
//            while (itr.hasNext()) {
//                // call threads close method
//                RequestConsumers thread = itr.next();
//                thread.close();
//                // remove thread from set so that GC can collect it.
//                itr.remove();
//                log.info("Consumer thread removed.");
//            }
        } catch (Exception e) {
            throw e;
        } finally {
            // release lock
            _LOCK.unlock();
        }
    }

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

    public static Consumer.Processor<String, Task> getProcessor() {
        return new Consumer.Processor<String, Task>() {
            @Override
            protected Boolean process(ConsumerRecords<String, Task> records) {
                for (ConsumerRecord<String, Task> record : records) {
                    if (record.key() == null) {
                        log.error("Wrong task encountered. Task meta: {}", record);
                        continue;
                    }
                    IRequestServer requestServer = provider.getRequestServer(record.key());
                    if (requestServer == null) {
                        log.error("Request Server not found for request type: {}", record.key());
                        continue;
                    }
                    log.info("Request server found: {}", requestServer.getClass());
                    try {
                        requestServer.serve(record.value());
                    } catch (ServiceException se) {
                        log.error("Service Exception occurred while serving request. Error: ", se);
                        continue;
                    }
                }
                return true;
            }
        };
    }
}
