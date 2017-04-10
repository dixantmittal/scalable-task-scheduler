package com.ixigo.factory;

import com.ixigo.cache.CacheManager;
import com.ixigo.cache.impl.TaskExecutorCache;
import com.ixigo.taskexecutors.ITaskExecutor;

/**
 * Created by dixant on 29/03/17.
 */
public class TaskExecutorFactory {
    public static ITaskExecutor getTaskExecutor(String taskType) {
        return CacheManager.getInstance().getCache(TaskExecutorCache.class).get(taskType);
    }
}
