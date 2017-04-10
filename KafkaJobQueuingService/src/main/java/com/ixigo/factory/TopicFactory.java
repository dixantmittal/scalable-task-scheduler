package com.ixigo.factory;

import com.ixigo.cache.CacheManager;
import com.ixigo.cache.TopicNameCache;
import com.ixigo.entity.KafkaTopic;
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
