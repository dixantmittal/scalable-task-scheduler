package org.dixantmittal.taskexecutors;

import org.dixantmittal.entity.KafkaTaskDetails;

/**
 * Created by dixant on 29/03/17.
 */
public interface ITaskExecutor {
    void execute(KafkaTaskDetails taskDetails);
}
