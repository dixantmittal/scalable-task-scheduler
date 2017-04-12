package com.ixigo.factory;

import com.ixigo.client.entity.IxigoKafkaProducer;
import com.ixigo.entity.KafkaTopic;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by dixant on 22/03/17.
 */
@Component
public class KafkaProducerFactory {

    private Map<KafkaTopic, IxigoKafkaProducer> _PRODUCER_CACHE = new ConcurrentHashMap<KafkaTopic, IxigoKafkaProducer>();

    public IxigoKafkaProducer getKafkaProducer(KafkaTopic topic) {
        _PRODUCER_CACHE.putIfAbsent(topic, IxigoKafkaProducerBuilder.buildNewKafkaProducerWithTopic(topic));
        return _PRODUCER_CACHE.get(topic);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        for (Map.Entry<KafkaTopic, IxigoKafkaProducer> entry : _PRODUCER_CACHE.entrySet()) {
            entry.getValue().close();
        }
    }
}
