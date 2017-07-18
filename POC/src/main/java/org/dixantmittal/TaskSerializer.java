package org.dixantmittal;

import com.google.gson.Gson;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.dixantmittal.entity.Task;

import java.util.Map;

/**
 * Created by dixant on 23/03/17.
 */
public class TaskSerializer implements Serializer<Task> {

    private StringSerializer stringSerializer;

    public TaskSerializer() {
        stringSerializer = new StringSerializer();
    }

    public void configure(Map<String, ?> configs, boolean isKey) {
        stringSerializer.configure(configs, isKey);
    }

    public byte[] serialize(String topic, Task data) {
        String json = new Gson().toJson(data);
        return stringSerializer.serialize(topic, json);
    }

    public void close() {
        stringSerializer.close();
    }
}
