package com.ixigo.cache;

import com.ixigo.cache.impl.ConfigCache;
import com.ixigo.cache.service.ICacheBuilder;
import com.ixigo.constants.ServiceConstants;
import com.ixigo.dao.IConfigurationDao;
import com.ixigo.dbmapper.entity.ConfigDetails;
import com.ixigo.exception.InternalServerException;
import com.ixigo.utils.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

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
        buildConfigCache();
        buildTopicNameCache();
        buildProducerPropertiesCache();
    }

    private void buildProducerPropertiesCache() {
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
        final ConfigCache configCache = new ConfigCache();
        for (ConfigDetails details : configurationDaoImpl.getAllConfigs()) {
            if (configCache.get(details.getConfigType()) == null) {
                configCache.put(details.getConfigType(), new HashMap<String, String>());
            }
            configCache.get(details.getConfigType())
                    .put(details.getConfigKey(), details.getConfigValue());
        }
        CacheManager.getInstance().putCache(configCache);
    }
}
