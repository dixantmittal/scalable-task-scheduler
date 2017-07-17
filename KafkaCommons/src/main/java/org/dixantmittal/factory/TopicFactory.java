package org.dixantmittal.factory;

import org.dixantmittal.cache.CacheManager;
import org.dixantmittal.cache.TopicNameCache;
import org.dixantmittal.entity.KafkaTopic;
import org.springframework.stereotype.Component;

/**
 * Created by dixant on 24/03/17.
 */
@Component
public class TopicFactory {

    public static KafkaTopic getTopicForTaskType(String taskType) {
        TopicNameCache topicNameCache = CacheManager.getInstance().getCache(TopicNameCache.class);
        return (topicNameCache.get(taskType) == null) ? null : new KafkaTopic(topicNameCache.get(taskType), 0);
    }
}
