package com.ixigo.cache;

import com.ixigo.cache.impl.ConfigCache;
import com.ixigo.cache.impl.ConsumerPropertiesCache;
import com.ixigo.cache.impl.ConsumersCache;
import com.ixigo.cache.impl.TaskExecutorCache;
import com.ixigo.cache.service.ICacheBuilder;
import com.ixigo.constants.ServiceConstants;
import com.ixigo.dao.impl.ConfigurationDaoImpl;
import com.ixigo.dbmapper.entity.ConfigDetails;
import com.ixigo.exception.InternalServerException;
import com.ixigo.taskexecutors.ITaskExecutor;
import com.ixigo.utils.Configuration;
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
        buildConfigCache();
        buildTaskExecutorCache();
        buildConsumerCache();
        buildConsumerPropertiesCache();
    }

    private void buildConsumerCache() {
        final ConsumersCache consumersCache = new ConsumersCache();
        CacheManager.getInstance().putCache(consumersCache);
    }

    private void buildConfigCache() {
        final ConfigCache configCache = new ConfigCache();
        for (ConfigDetails details : configurationDaoImpl.getAllConfigs()) {
            if (configCache.get(details.getConfigType()) == null) {
                configCache.put(details.getConfigType(), new HashMap<String, String>());
            }
            configCache.get(details.getConfigType()).put(details.getConfigKey(), details.getConfigValue());
        }
        CacheManager.getInstance().putCache(configCache);
    }

    private void buildTaskExecutorCache() {
        final TaskExecutorCache cache = new TaskExecutorCache();
        Map<String, String> configMap = Configuration.getConfigMap(ServiceConstants.TASK_EXECUTOR_CACHE);
        for (Map.Entry<String, String> entry : configMap.entrySet()) {
            ITaskExecutor taskExecutor = getTaskExecutorBean(entry.getValue());
            cache.put(entry.getKey(), taskExecutor);
        }
        CacheManager.getInstance().putCache(cache);
    }

    private void buildConsumerPropertiesCache() {
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
