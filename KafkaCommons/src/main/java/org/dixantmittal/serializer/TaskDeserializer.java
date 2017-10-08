package org.dixantmittal.serializer;

import com.google.gson.JsonSyntaxException;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.dixantmittal.entity.Task;
import org.dixantmittal.utils.JsonUtils;

import java.util.Map;

/**
 * Created by dixant on 29/03/17.
 */
public class TaskDeserializer implements Deserializer<Task> {
    private StringDeserializer stringDeserializer;

    public TaskDeserializer() {
        stringDeserializer = new StringDeserializer();
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        stringDeserializer.configure(configs, isKey);
    }

    @Override
    public Task deserialize(String topic, byte[] data) {
        String json = this.stringDeserializer.deserialize(topic, data);
        try {
            return JsonUtils.fromJson(json, Task.class);
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
