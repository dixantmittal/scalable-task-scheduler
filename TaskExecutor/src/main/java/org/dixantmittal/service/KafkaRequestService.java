package org.dixantmittal.service;

import org.dixantmittal.cache.CacheManager;
import org.dixantmittal.cache.impl.ConsumersCache;
import org.dixantmittal.constants.ConfigurationConstants;
import org.dixantmittal.entity.IxigoKafkaConsumer;
import org.dixantmittal.entity.KafkaTopic;
import org.dixantmittal.enums.Status;
import org.dixantmittal.exception.ServiceException;
import org.dixantmittal.exception.codes.jobexecutor.ServiceExceptionCodes;
import org.dixantmittal.factory.IxigoKafkaConsumerBuilder;
import org.dixantmittal.request.jobexecutor.AddConsumersRequest;
import org.dixantmittal.request.jobexecutor.RemoveConsumersRequest;
import org.dixantmittal.request.jobexecutor.StopAllConsumersRequest;
import org.dixantmittal.response.jobexecutor.AddConsumersResponse;
import org.dixantmittal.response.jobexecutor.RemoveConsumersResponse;
import org.dixantmittal.response.jobexecutor.StopConsumersResponse;
import org.dixantmittal.utils.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by dixant on 29/03/17.
 */
@Service
@Slf4j
public class KafkaRequestService {

    // total running threads in the system.
    private volatile int totalThreads = 0;

    private volatile Lock _LOCK = new ReentrantLock();

    // To increase the number of consumers in this system. new consumer is assigned a Topic from the request.
    public AddConsumersResponse addConsumers(AddConsumersRequest request) {
        _LOCK.lock();
        try {
            int count = request.getCount();
            int maxThreadPoolSize = Integer.parseInt(
                    Configuration.getGlobalProperty(ConfigurationConstants.MAX_THREAD_POOL_SIZE));

            log.info("Adding new consumers. Requested counts: {}, Max ThreadPool size: {}, Currently running: {}", count, maxThreadPoolSize, totalThreads);
            if (totalThreads + count > maxThreadPoolSize) {
                log.error("Requested consumer count is more than allowed. Requested: {}. Allowed: {}", count, maxThreadPoolSize - totalThreads);
                throw new ServiceException(ServiceExceptionCodes.CONSUMER_COUNT_OUT_OF_BOUNDS.code(),
                        ServiceExceptionCodes.CONSUMER_COUNT_OUT_OF_BOUNDS.message());
            }
            totalThreads = totalThreads + count;

            while (count-- > 0) {
                IxigoKafkaConsumer consumer = IxigoKafkaConsumerBuilder.buildNewKafkaConsumerWithTopic(new KafkaTopic(request.getTopic()));
                consumer.start();
                CacheManager.getInstance().getCache(ConsumersCache.class)
                        .computeIfAbsent(request.getTopic(), k -> new HashSet<IxigoKafkaConsumer>())
                        .add(consumer);
            }
            return new AddConsumersResponse(Status.SUCCESS);
        } catch (Exception e) {
            throw e;
        } finally {
            _LOCK.unlock();
        }
    }

    public RemoveConsumersResponse decreaseConsumers(RemoveConsumersRequest request) {
        _LOCK.lock();
        try {
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
        } catch (Exception e) {
            throw e;
        } finally {
            _LOCK.unlock();
        }
    }

    public StopConsumersResponse stopAllConsumers(StopAllConsumersRequest request) {
        _LOCK.lock();
        try {
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
        } catch (Exception e) {
            throw e;
        } finally {
            _LOCK.unlock();
        }
    }
}