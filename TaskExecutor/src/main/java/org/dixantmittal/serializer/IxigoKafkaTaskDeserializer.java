package org.dixantmittal.serializer;

import com.google.gson.JsonSyntaxException;
import org.dixantmittal.entity.KafkaTaskDetails;
import org.dixantmittal.utils.JsonUtils;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Map;

/**
 * Created by dixant on 29/03/17.
 */
public class IxigoKafkaTaskDeserializer implements Deserializer<KafkaTaskDetails> {
    private StringDeserializer stringDeserializer;

    public IxigoKafkaTaskDeserializer() {
        stringDeserializer = new StringDeserializer();
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        stringDeserializer.configure(configs, isKey);
    }

    @Override
    public KafkaTaskDetails deserialize(String topic, byte[] data) {
        String json = this.stringDeserializer.deserialize(topic, data);
        try {
            return JsonUtils.fromJson(json, KafkaTaskDetails.class);
        } catch (JsonSyntaxException jse) {
            System.out.println(json + Thread.currentThread().getName());
            return null;
        }
    }

    @Override
    public void close() {
        stringDeserializer.close();
    }
}
