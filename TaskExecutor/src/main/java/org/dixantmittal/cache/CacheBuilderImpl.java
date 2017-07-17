package org.dixantmittal.cache;

import org.dixantmittal.cache.impl.ConfigurationCache;
import org.dixantmittal.cache.impl.ConsumerPropertiesCache;
import org.dixantmittal.cache.impl.ConsumersCache;
import org.dixantmittal.cache.impl.TaskExecutorCache;
import org.dixantmittal.cache.service.ICacheBuilder;
import org.dixantmittal.constants.jobexecutor.ServiceConstants;
import org.dixantmittal.dao.impl.ConfigurationDaoImpl;
import org.dixantmittal.dbmapper.entity.ConfigDetails;
import org.dixantmittal.exception.InternalServerException;
import org.dixantmittal.taskexecutors.ITaskExecutor;
import org.dixantmittal.utils.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.beans.Introspector;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dixant on 29/03/17.
 */
@Service
@Slf4j
public class CacheBuilderImpl implements ICacheBuilder {

    @Autowired
    ApplicationContext context;

    @Autowired
    ConfigurationDaoImpl configurationDaoImpl;

    @PostConstruct
    @Override
    public void buildCaches() {
        log.info("Loading caches...");
        buildConfigCache();
        buildTaskExecutorCache();
        buildConsumerCache();
        buildConsumerPropertiesCache();
        log.info("Caches loaded successfully!...");
    }

    private void buildConsumerCache() {
        log.info("building consumer cache...");
        final ConsumersCache consumersCache = new ConsumersCache();
        CacheManager.getInstance().putCache(consumersCache);
    }

    private void buildConfigCache() {
        log.info("building Configuration cache...");
        final ConfigurationCache configCache = new ConfigurationCache();
        for (ConfigDetails details : configurationDaoImpl.getAllConfigs()) {
            if (configCache.get(details.getConfigType()) == null) {
                configCache.put(details.getConfigType(), new HashMap<String, String>());
            }
            configCache.get(details.getConfigType()).put(details.getConfigKey(), details.getConfigValue());
        }
        CacheManager.getInstance().putCache(configCache);
    }

    private void buildTaskExecutorCache() {
        log.info("building task executor cache...");
        final TaskExecutorCache cache = new TaskExecutorCache();
        Map<String, String> configMap = Configuration.getConfigMap(ServiceConstants.TASK_EXECUTOR_CACHE);
        for (Map.Entry<String, String> entry : configMap.entrySet()) {
            ITaskExecutor taskExecutor = getTaskExecutorBean(entry.getValue());
            cache.put(entry.getKey(), taskExecutor);
        }
        CacheManager.getInstance().putCache(cache);
    }

    private void buildConsumerPropertiesCache() {
        log.info("building consumer properties cache...");
        final ConsumerPropertiesCache cache = new ConsumerPropertiesCache();
        Map<String, String> configMap = Configuration.getConfigMap(ServiceConstants.CONSUMER_PROPERTIES_CACHE);
        if (configMap == null || configMap.size() == 0) {
            log.error("Consumer Properties not found in the db. Please contact admin.");
            throw new InternalServerException();
        }
        for (Map.Entry<String, String> entry : configMap.entrySet()) {
            cache.put(entry.getKey(), entry.getValue());
        }
        CacheManager.getInstance().putCache(cache);
    }

    private ITaskExecutor getTaskExecutorBean(String className) {
        className = Introspector.decapitalize(className);
        return (ITaskExecutor) context.getBean(className);
    }
}
