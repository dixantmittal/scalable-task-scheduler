package org.dixantmittal.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.dixantmittal.builder.ProducerBuilder;
import org.dixantmittal.cache.CacheManager;
import org.dixantmittal.cache.TopicNameCache;
import org.dixantmittal.constants.taskmanager.ServiceConstants;
import org.dixantmittal.entity.Task;
import org.dixantmittal.producer.Producer;
import org.dixantmittal.service.ITaskQueuingService;
import org.dixantmittal.utils.JsonUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by dixant on 28/03/17.
 */
@Slf4j
@Service
public class KafkaTaskQueuingService implements ITaskQueuingService {

    private Producer<String, Task> producer;

    public KafkaTaskQueuingService() {
        producer = ProducerBuilder.<String, Task>newProducer()
                .loadDefaultProperties()
                .addProperty("value.serializer", "org.dixantmittal.serializer.TaskSerializer")
                .getProducer();
    }

    @Override
    public Boolean addTaskToExecutionQueue(Map<String, Object> jobDataMap) {
        String taskJson = jobDataMap.get(ServiceConstants.TASK_DETAILS).toString();
        Task task = JsonUtils.fromJson(taskJson, Task.class);
        task.setTaskId(jobDataMap.get(ServiceConstants.TASK_ID).toString());
        log.info("Task: {}", task);
        producer.send(CacheManager.getInstance().getCache(TopicNameCache.class).get(task.getTaskType()), task.getTaskType(), task);
        return true;
    }
}
