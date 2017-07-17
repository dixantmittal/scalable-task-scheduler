package org.dixantmittal.taskexecutors.impl;

import com.google.gson.JsonParseException;
import org.dixantmittal.taskexecutors.AbstractTaskExecutor;
import org.dixantmittal.taskmeta.YatraCheckStatusTaskMeta;
import org.dixantmittal.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created by dixant on 29/03/17.
 */
@Slf4j
@Component
public class YatraCheckStatusTaskExecutor extends AbstractTaskExecutor {

    @Override
    public Boolean process(String taskMeta) throws JsonParseException {

        YatraCheckStatusTaskMeta yatraTaskMeta = JsonUtils
                .fromJson(taskMeta, YatraCheckStatusTaskMeta.class);

        // TODO write business logic for Yatra check status
        System.out.println(Thread.currentThread().getName() + "  " + yatraTaskMeta);
        return false;
    }
}
