package org.dixantmittal.factory;

import org.dixantmittal.cache.CacheManager;
import org.dixantmittal.cache.impl.ConsumerPropertiesCache;
import org.dixantmittal.entity.IxigoKafkaConsumer;
import org.dixantmittal.entity.KafkaTopic;

import java.util.Map;
import java.util.Properties;

/**
 * Created by dixant on 27/03/17.
 */
public class IxigoKafkaConsumerBuilder {

    public static IxigoKafkaConsumer buildNewKafkaConsumerWithTopic(KafkaTopic topic) {
        IxigoKafkaConsumer consumer = new IxigoKafkaConsumer(getDefaultPropertiesForConsumer(), topic);
        return consumer;
    }

    private static Properties getDefaultPropertiesForConsumer() {
        Properties properties = new Properties();
        ConsumerPropertiesCache cache = CacheManager.getInstance().getCache(ConsumerPropertiesCache.class);
        for (Map.Entry<String, String> property : cache.entrySet()) {
            properties.put(property.getKey(), property.getValue());
        }
        return properties;
    }
}
