package com.ixigo.factory;

import com.ixigo.client.entity.IxigoKafkaProducer;
import com.ixigo.entity.KafkaTopic;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dixant on 22/03/17.
 */
@Component
public class KafkaProducerFactory {

    private Map<KafkaTopic, IxigoKafkaProducer> _PRODUCER_CACHE = new HashMap<KafkaTopic, IxigoKafkaProducer>();

    public IxigoKafkaProducer getKafkaProducer(KafkaTopic topic) {
        if (_PRODUCER_CACHE.get(topic) == null) {
            synchronized (this.getClass()) {
                _PRODUCER_CACHE.putIfAbsent(topic, IxigoKafkaProducerBuilder.buildNewKafkaProducerWithTopic(topic));
            }
        }
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
