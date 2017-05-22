package com.ixigo.factory;

import com.ixigo.cache.CacheManager;
import com.ixigo.cache.impl.TaskExecutorCache;
import com.ixigo.taskexecutors.ITaskExecutor;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by dixant on 29/03/17.
 */
@Slf4j
public class TaskExecutorFactory {
    public static ITaskExecutor getTaskExecutor(String taskType) {
        log.debug("Getting task executor for task type: {}", taskType);
        return CacheManager.getInstance().getCache(TaskExecutorCache.class).get(taskType);
    }
}
