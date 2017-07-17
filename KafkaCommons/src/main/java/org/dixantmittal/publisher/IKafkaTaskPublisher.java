package org.dixantmittal.publisher;

import org.dixantmittal.entity.KafkaTaskDetails;

/**
 * Created by dixant on 22/03/17.
 */
public interface IKafkaTaskPublisher {
    boolean publishTask(KafkaTaskDetails taskRequest);
}
