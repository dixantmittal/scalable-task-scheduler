package com.ixigo.taskexecutors;

import com.ixigo.entity.KafkaTaskDetails;

/**
 * Created by dixant on 29/03/17.
 */
public interface ITaskExecutor {
    void execute(KafkaTaskDetails taskMeta);
}
