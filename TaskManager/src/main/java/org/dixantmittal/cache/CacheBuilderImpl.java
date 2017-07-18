package org.dixantmittal.cache;

import lombok.extern.slf4j.Slf4j;
import org.dixantmittal.cache.impl.ConfigurationCache;
import org.dixantmittal.cache.service.ICacheBuilder;
import org.dixantmittal.constants.taskmanager.ServiceConstants;
import org.dixantmittal.dao.IConfigurationDao;
import org.dixantmittal.dbmapper.entity.ConfigDetails;
import org.dixantmittal.exception.InternalServerException;
import org.dixantmittal.utils.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import static org.dixantmittal.constants.taskmanager.ServiceConstants.REQUEST_CONSUMER_PROPERTIES_CACHE;

/**
 * Created by dixant on 29/03/17.
 */
@Service
@Slf4j
public class CacheBuilderImpl implements ICacheBuilder {

    @Autowired
    private IConfigurationDao configurationDaoImpl;

    @PostConstruct
    @Override
    public void buildCaches() {
        log.info("Loading caches...");
        buildConfigCache();
        buildTopicNameCache();
        buildProducerPropertiesCache();
        buildConsumerPropertiesCache();
        log.info("Caches loaded successfully!...");
    }

    private void buildProducerPropertiesCache() {
        log.info("building producer properties cache...");
        final ProducerPropertiesCache cache = new ProducerPropertiesCache();
        Map<String, String> configMap = Configuration.getConfigMap(ServiceConstants.PRODUCER_PROPERTIES_CACHE);
        if (configMap == null) {
            log.error("Producer Properties not found in the db. Please contact admin.");
            throw new InternalServerException();
        }
        for (Map.Entry<String, String> entry : configMap.entrySet()) {
            cache.put(entry.getKey(), entry.getValue());
        }
        CacheManager.getInstance().putCache(cache);
    }

    private void buildTopicNameCache() {
        log.info("building topic name cache...");
        final TopicNameCache cache = new TopicNameCache();
        Map<String, String> configMap = Configuration.getConfigMap(ServiceConstants.TOPIC_NAME_CACHE_PROPERTY);
        if (configMap == null) {
            log.error("Topics Name not found in the db. Please contact admin.");
            throw new InternalServerException();
        }
        for (Map.Entry<String, String> entry : configMap.entrySet()) {
            cache.put(entry.getKey(), entry.getValue());
        }
        CacheManager.getInstance().putCache(cache);
    }

    private void buildConfigCache() {
        log.info("building Configuration cache...");
        final ConfigurationCache configCache = new ConfigurationCache();
        for (ConfigDetails details : configurationDaoImpl.getAllConfigs()) {
            if (configCache.get(details.getConfigType()) == null) {
                configCache.put(details.getConfigType(), new HashMap<String, String>());
            }
            configCache.get(details.getConfigType())
                    .put(details.getConfigKey(), details.getConfigValue());
        }
        CacheManager.getInstance().putCache(configCache);
    }

    private void buildConsumerPropertiesCache() {
        log.info("building consumer properties cache...");
        final ConsumerPropertiesCache cache = new ConsumerPropertiesCache();
        Map<String, String> configMap = Configuration.getConfigMap(REQUEST_CONSUMER_PROPERTIES_CACHE);
        if (configMap == null || configMap.size() == 0) {
            log.error("Consumer Properties not found in the db. Please contact admin.");
            throw new InternalServerException();
        }
        for (Map.Entry<String, String> entry : configMap.entrySet()) {
            cache.put(entry.getKey(), entry.getValue());
        }
        CacheManager.getInstance().putCache(cache);
    }
}
