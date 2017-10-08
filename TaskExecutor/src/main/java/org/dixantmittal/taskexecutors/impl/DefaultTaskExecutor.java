package org.dixantmittal.taskexecutors.impl;

import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.dixantmittal.taskexecutors.AbstractTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * Created by dixant on 17/07/17.
 */
@Component
@Slf4j
public class DefaultTaskExecutor extends AbstractTaskExecutor {
    @Override
    public Boolean process(String meta) throws JsonSyntaxException {
        log.error("TASK EXECUTOR NOT FOUND");
        return true;
    }
}
