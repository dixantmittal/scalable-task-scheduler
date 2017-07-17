package org.dixantmittal.taskexecutors.impl;

import com.google.gson.JsonSyntaxException;
import org.dixantmittal.taskexecutors.AbstractTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * Created by dixant on 17/07/17.
 */
@Component
public class TrainExecutor extends AbstractTaskExecutor {
    @Override
    public Boolean process(String meta) throws JsonSyntaxException {
        System.out.println(meta);
        return true;
    }
}
