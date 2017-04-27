package com.ixigo.service;

import com.ixigo.cache.CacheManager;
import com.ixigo.cache.impl.ConsumersCache;
import com.ixigo.constants.ConfigurationConstants;
import com.ixigo.entity.IxigoKafkaConsumer;
import com.ixigo.entity.KafkaTopic;
import com.ixigo.enums.Status;
import com.ixigo.exception.ServiceException;
import com.ixigo.exception.codes.jobexecutor.ServiceExceptionCodes;
import com.ixigo.factory.IxigoKafkaConsumerBuilder;
import com.ixigo.request.jobexecutor.AddConsumersRequest;
import com.ixigo.request.jobexecutor.RemoveConsumersRequest;
import com.ixigo.request.jobexecutor.StopAllConsumersRequest;
import com.ixigo.response.jobexecutor.AddConsumersResponse;
import com.ixigo.response.jobexecutor.RemoveConsumersResponse;
import com.ixigo.response.jobexecutor.StopConsumersResponse;
import com.ixigo.utils.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by dixant on 29/03/17.
 */
@Service
@Slf4j
public class KafkaRequestService {

    // total running threads in the system.
    private volatile int totalThreads = 0;

    // To increase the number of consumers in this system. new consumer is assigned a Topic from the request.
    public synchronized AddConsumersResponse addConsumers(AddConsumersRequest request) {
        AddConsumersResponse response = new AddConsumersResponse();
        int count = request.getCount();
        int maxThreadPoolSize = Integer.parseInt(
                Configuration.getGlobalProperty(ConfigurationConstants.MAX_THREAD_POOL_SIZE));

        if (totalThreads + count > maxThreadPoolSize) {
            log.error("Requested consumer count is more than allowed. Requested: {}. Allowed: {}", count, maxThreadPoolSize - totalThreads);
            throw new ServiceException(ServiceExceptionCodes.CONSUMER_COUNT_OUT_OF_BOUNDS.code(),
                    ServiceExceptionCodes.CONSUMER_COUNT_OUT_OF_BOUNDS.message());
        }

        while (count-- > 0) {

            IxigoKafkaConsumer consumer = IxigoKafkaConsumerBuilder.buildNewKafkaConsumerWithTopic(
                    new KafkaTopic(request.getTopicName()));
            consumer.start();
            CacheManager.getInstance().getCache(ConsumersCache.class)
                    .computeIfAbsent(request.getTopicName(), k -> new HashSet<IxigoKafkaConsumer>())
                    .add(consumer);
        }
        response.setStatus(Status.SUCCESS);
        return response;
    }

    public synchronized RemoveConsumersResponse decreaseConsumers(RemoveConsumersRequest request) {
        RemoveConsumersResponse response = new RemoveConsumersResponse();
        if (totalThreads <= 0) {
            log.info("Thread pool is empty. Cannot decrease further.");
            throw new ServiceException(ServiceExceptionCodes.THREADPOOL_EMPTY.code(),
                    ServiceExceptionCodes.THREADPOOL_EMPTY.message());
        }
        int count = request.getCount();

        Set<IxigoKafkaConsumer> consumerSet = CacheManager.getInstance().getCache(ConsumersCache.class)
                .getOrDefault(request.getTopic(), new HashSet<>());

        Iterator<IxigoKafkaConsumer> iterator = consumerSet.iterator();
        if (consumerSet.size() == 0) {
            log.error("No consumer set exist with topic: {}.", request.getTopic());
            throw new ServiceException(ServiceExceptionCodes.TOPIC_DOES_NOT_EXIST.code(),
                    ServiceExceptionCodes.TOPIC_DOES_NOT_EXIST.message());
        }
        if (consumerSet.size() < count) {
            log.error("Requested consumer count is more than allowed. Requested: {}. Allowed: {}", count, consumerSet.size());
            throw new ServiceException(ServiceExceptionCodes.CONSUMER_COUNT_OUT_OF_BOUNDS.code(),
                    ServiceExceptionCodes.CONSUMER_COUNT_OUT_OF_BOUNDS.message());
        }

        while (count-- > 0 && iterator.hasNext()) {
            iterator.next().close();
            iterator.remove();
            totalThreads--;
        }
        response.setStatus(Status.SUCCESS);
        return response;
    }

    public synchronized StopConsumersResponse stopAllConsumers(StopAllConsumersRequest request) {
        if (totalThreads <= 0) {
            log.error("No Threads are running as thread pool is empty.");
            throw new ServiceException(ServiceExceptionCodes.THREADPOOL_EMPTY.code(),
                    ServiceExceptionCodes.THREADPOOL_EMPTY.message());
        }
        String topic = request.getTopic();
        log.info("Removing consumers for topic: {}", topic);

        ConsumersCache consumersCache = CacheManager.getInstance().getCache(ConsumersCache.class);
        Set<IxigoKafkaConsumer> consumers = consumersCache.getOrDefault(topic, new HashSet<>());
        RemoveConsumersResponse removeResponse =
                decreaseConsumers(new RemoveConsumersRequest(topic, consumers.size()));

        StopConsumersResponse response = new StopConsumersResponse();
        response.setStatus(removeResponse.getStatus());
        return response;
    }
}