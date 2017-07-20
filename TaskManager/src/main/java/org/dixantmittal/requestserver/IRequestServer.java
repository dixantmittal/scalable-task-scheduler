package org.dixantmittal.requestserver;

import org.dixantmittal.entity.Task;

/**
 * Created by dixant on 28/04/17.
 */
public interface IRequestServer {
    void serve(Task task);
}
