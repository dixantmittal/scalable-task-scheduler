package org.dixantmittal.factory;

import org.dixantmittal.constants.jobhandlingservice.ServiceConstants;
import org.dixantmittal.requestserver.impl.AddTaskRequestServer;
import org.dixantmittal.requestserver.impl.DeleteTaskRequestServer;
import org.dixantmittal.requestserver.IRequestServer;
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
        if (requestType.equals(ServiceConstants.ADD_TASK))
            return addTaskRequestServer;
        else if (requestType.equals(ServiceConstants.DELETE_TASK))
            return deleteTaskRequestServer;
        else
            return null;
    }
}
