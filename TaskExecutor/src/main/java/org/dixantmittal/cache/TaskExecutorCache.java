package org.dixantmittal.cache;

import lombok.extern.slf4j.Slf4j;
import org.dixantmittal.taskexecutors.ITaskExecutor;
import org.dixantmittal.taskexecutors.impl.DefaultTaskExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by dixant on 17/07/17.
 */
@Component
@Slf4j
public class TaskExecutorCache {

    @Autowired
    private ApplicationContext context;

    private Properties properties = new Properties();

    private Map<String, ITaskExecutor> cache;

    @Autowired
    private DefaultTaskExecutor defaultExecutor;

    public TaskExecutorCache() {
        try {
            log.info("Loading properties for building TASK EXECUTOR CACHE.");
            properties.load(TaskExecutorCache.class.getClassLoader().getResourceAsStream("taskexecutors.properties"));
            log.info("Loading SUCCESSFUL");
        } catch (IOException e) {
            log.error("Could not find taskexecutors.properties.");
        }
    }

    @PostConstruct
    private void init() {
        cache = new HashMap<>();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            cache.put(entry.getKey().toString(), getTaskExecutorBean(entry.getValue().toString()));
        }
    }

    public ITaskExecutor getTaskExecutor(String taskType) {
        return cache.getOrDefault(taskType, getDefaultExecutor());
    }

    private ITaskExecutor getTaskExecutorBean(String bean) {
        try {
            return (ITaskExecutor) context.getBean(Class.forName(bean));
        } catch (ClassNotFoundException e) {
            log.error("Could not find TaskExecutor for Class: {}", bean);
            return null;
        }
    }

    public ITaskExecutor getDefaultExecutor() {
        return defaultExecutor;
    }
}

