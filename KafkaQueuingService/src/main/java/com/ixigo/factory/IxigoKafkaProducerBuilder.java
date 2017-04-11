package com.ixigo.factory;

import com.ixigo.cache.CacheManager;
import com.ixigo.cache.ProducerPropertiesCache;
import com.ixigo.client.entity.IxigoKafkaProducer;
import com.ixigo.entity.KafkaTopic;

import java.util.Map;
import java.util.Properties;

/**
 * Created by dixant on 22/03/17.
 */
public class IxigoKafkaProducerBuilder {

    public static IxigoKafkaProducer buildNewKafkaProducerWithTopic(KafkaTopic topic) {
        IxigoKafkaProducer producer = new IxigoKafkaProducer(getDefaultPropertiesForProducer(), topic);
        return producer;
    }

    private static Properties getDefaultPropertiesForProducer() {
        Properties properties = new Properties();
        ProducerPropertiesCache cache = CacheManager.getInstance().getCache(ProducerPropertiesCache.class);
        for (Map.Entry<String, String> property : cache.entrySet()) {
            properties.put(property.getKey(), property.getValue());
        }
        return properties;
    }
}
