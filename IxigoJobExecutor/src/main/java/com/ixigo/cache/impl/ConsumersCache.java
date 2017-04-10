package com.ixigo.cache.impl;

import com.ixigo.cache.AbstractCache;
import com.ixigo.entity.IxigoKafkaConsumer;

import java.util.Set;

/**
 * Created by dixant on 30/03/17.
 */
public class ConsumersCache extends AbstractCache<String, Set<IxigoKafkaConsumer>> {
    public void remove(String topic) {
        map.remove(topic);
    }
    public Set<String> keySet() {
        return map.keySet();
    }
}
