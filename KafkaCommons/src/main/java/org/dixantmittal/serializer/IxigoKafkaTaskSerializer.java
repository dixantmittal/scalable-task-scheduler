package org.dixantmittal.serializer;

import org.dixantmittal.utils.JsonUtils;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by dixant on 23/03/17.
 */
@Component
public class IxigoKafkaTaskSerializer implements Serializer<Object> {

    private StringSerializer stringSerializer;

    public IxigoKafkaTaskSerializer() {
        stringSerializer = new StringSerializer();
    }

    public void configure(Map<String, ?> configs, boolean isKey) {
        stringSerializer.configure(configs, isKey);
    }

    public byte[] serialize(String topic, Object data) {
        String json = JsonUtils.toJson(data);
        return stringSerializer.serialize(topic, json);
    }

    public void close() {
        stringSerializer.close();
    }
}
