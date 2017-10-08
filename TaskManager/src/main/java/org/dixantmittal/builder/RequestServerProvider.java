package org.dixantmittal.builder;

import org.dixantmittal.constants.taskmanagerproxy.ServiceConstants;
import org.dixantmittal.requestserver.IRequestServer;
import org.dixantmittal.requestserver.impl.AddTaskRequestServer;
import org.dixantmittal.requestserver.impl.DeleteTaskRequestServer;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by dixant on 28/04/17.
 */
public class RequestServerProvider {

    private static RequestServerProvider _INSTANCE = new RequestServerProvider();

    @Autowired
    private DeleteTaskRequestServer deleteTaskRequestServer;
    @Autowired
    private AddTaskRequestServer addTaskRequestServer;

    public static RequestServerProvider getInstance() {
        return _INSTANCE;
    }

    public IRequestServer getRequestServer(String requestType) {
        switch (requestType) {
            case ServiceConstants.ADD_TASK:
                return addTaskRequestServer;
            case ServiceConstants.DELETE_TASK:
                return deleteTaskRequestServer;
            default:
                return null;
        }
    }
}
