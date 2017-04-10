package com.ixigo.publisher;

import com.ixigo.entity.KafkaTaskDetails;

/**
 * Created by dixant on 22/03/17.
 */
public interface IKafkaTaskPublisher {
    boolean publishTask(KafkaTaskDetails taskRequest);
}
