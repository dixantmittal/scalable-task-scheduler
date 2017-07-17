package org.dixantmittal.factory;

import org.dixantmittal.cache.CacheManager;
import org.dixantmittal.cache.impl.TaskExecutorCache;
import org.dixantmittal.taskexecutors.ITaskExecutor;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by dixant on 29/03/17.
 */
@Slf4j
public class TaskExecutorFactory {
    public static ITaskExecutor getTaskExecutor(String taskType) {
        log.info("Getting task executor for task type: {}", taskType);
        return CacheManager.getInstance().getCache(TaskExecutorCache.class).get(taskType);
    }
}
